package com.aone.recorder.views;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import com.aone.recorder.model.AudioData;
import com.aone.recorder.model.FFTData;
import com.aone.recorder.views.render.Renderer;


/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.views
 * @ClassName: VisualizerView
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/14 9:05
 * @Desc:
 */
public class VisualizerView extends View {
    private static final String TAG = VisualizerView.class.getSimpleName();

    private byte[] mBytes;
    private byte[] mFFTBytes;
    private Rect mRect = new Rect();
    private Visualizer mVisualizer;

    private Set<Renderer> mRenderers;

    private Paint mFlashPaint = new Paint();
    private Paint mFadePaint = new Paint();

    public VisualizerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public VisualizerView(Context context)
    {
        this(context, null, 0);
    }

    private void init() {
        mBytes = null;
        mFFTBytes = null;

        mFlashPaint.setColor(Color.argb(122, 255, 255, 255));
        mFadePaint.setColor(Color.argb(238, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
        mFadePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));

        mRenderers = new HashSet<Renderer>();
    }

    /**
     * Links the visualizer to a player
     * @param player - MediaPlayer instance to link to
     */
    public void link(MediaPlayer player)
    {
        if(player == null)
        {
            throw new NullPointerException("Cannot link to null MediaPlayer");
        }

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(player.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        // Pass through Visualizer data to VisualizerView
        Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
        {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate)
            {
                updateVisualizer(bytes);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                         int samplingRate)
            {
                updateVisualizerFFT(bytes);
            }
        };

        mVisualizer.setDataCaptureListener(captureListener,
                Visualizer.getMaxCaptureRate() / 2, true, true);

        // Enabled Visualizer and disable when we're done with the stream
        mVisualizer.setEnabled(true);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                mVisualizer.setEnabled(false);
            }
        });
    }

    public void addRenderer(Renderer renderer)
    {
        if(renderer != null)
        {
            mRenderers.add(renderer);
        }
    }

    public void clearRenderers()
    {
        mRenderers.clear();
    }

    /**
     * Call to release the resources used by VisualizerView. Like with the
     * MediaPlayer it is good practice to call this method
     */
    public void release()
    {
        mVisualizer.release();
    }

    /**
     * Pass data to the visualizer. Typically this will be obtained from the
     * Android Visualizer.OnDataCaptureListener call back. See
     * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture }
     * @param bytes
     */
    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    /**
     * Pass FFT data to the visualizer. Typically this will be obtained from the
     * Android Visualizer.OnDataCaptureListener call back. See
     * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
     * @param bytes
     */
    public void updateVisualizerFFT(byte[] bytes) {
        mFFTBytes = bytes;
        invalidate();
    }

    boolean mFlash = false;

    /**
     * Call this to make the visualizer flash. Useful for flashing at the start
     * of a song/loop etc...
     */
    public void flash() {
        mFlash = true;
        invalidate();
    }

    Bitmap mCanvasBitmap;
    Canvas mCanvas;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create canvas once we're ready to draw
        mRect.set(0, 0, getWidth(), getHeight());

        if(mCanvasBitmap == null)
        {
            mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
        }
        if(mCanvas == null)
        {
            mCanvas = new Canvas(mCanvasBitmap);
        }

        if (mBytes != null) {
            // Render all audio renderers
            AudioData audioData = new AudioData(mBytes);
            for(Renderer r : mRenderers)
            {
                r.render(mCanvas, audioData, mRect);
            }
        }

        if (mFFTBytes != null) {
            // Render all FFT renderers
            FFTData fftData = new FFTData(mFFTBytes);
            for(Renderer r : mRenderers)
            {
                r.render(mCanvas, fftData, mRect);
            }
        }

        // Fade out old contents
        mCanvas.drawPaint(mFadePaint);

        if(mFlash)
        {
            mFlash = false;
            mCanvas.drawPaint(mFlashPaint);
        }

        canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
    }
//    private static final int DN_W = 470;//view宽度与单个音频块占比 - 正常480 需微调
//    private static final int DN_H = 360;//view高度与单个音频块占比
//    private static final int DN_SL = 15;//单个音频块宽度
//    private static final int DN_SW = 5;//单个音频块高度
//
//    private int hgap = 0;
//    private int vgap = 0;
//    private int levelStep = 0;
//    private float strokeWidth = 0;
//    private float strokeLength = 0;
//
//    protected final static int MAX_LEVEL = 30;//音量柱·音频块 - 最大个数
//
//    protected final static int CYLINDER_NUM = 26;//音量柱 - 最大个数
//
//    protected Visualizer mVisualizer = null;//频谱器
//
//    protected Paint mPaint = null;//画笔
//
//    protected byte[] mData = new byte[CYLINDER_NUM];//音量柱 数组
//
//    boolean mDataEn = true;
//
//    //构造函数初始化画笔
//    public VisualizerView(Context context) {
//        super(context);
//
//        init();
//    }
//
//    public VisualizerView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//       init();
//    }
//
//    public VisualizerView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init();
//    }
//
//    private void init() {
//        mPaint = new Paint();//初始化画笔工具
//        mPaint.setAntiAlias(true);//抗锯齿
//        mPaint.setColor(Color.WHITE);//画笔颜色
//
//        mPaint.setStrokeJoin(Join.ROUND); //频块圆角
//        mPaint.setStrokeCap(Cap.ROUND); //频块圆角
//    }
//
//    //执行 Layout 操作
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//
//        float w, h, xr, yr;
//
//        w = right - left;
//        h = bottom - top;
//        xr = w / (float) DN_W;
//        yr = h / (float) DN_H;
//
//        strokeWidth = DN_SW * yr;
//        strokeLength = DN_SL * xr;
//        hgap = (int) ((w - strokeLength * CYLINDER_NUM) / (CYLINDER_NUM + 1));
//        vgap = (int) (h / (MAX_LEVEL + 2));//频谱块高度
//
//        mPaint.setStrokeWidth(strokeWidth); //设置频谱块宽度
//    }
//
//    //绘制频谱块和倒影
//    protected void drawCylinder(Canvas canvas, float x, byte value) {
//        if (value == 0) {value = 1;}//最少有一个频谱块
//        for (int i = 0; i < value; i++) { //每个能量柱绘制value个能量块
//            float y = (getHeight()/2 - i * vgap - vgap);//计算y轴坐标
//            float y1=(getHeight()/2+i * vgap + vgap);
//            //绘制频谱块
//            mPaint.setColor(Color.WHITE);//画笔颜色
//            canvas.drawLine(x, y, (x + strokeLength), y, mPaint);//绘制频谱块
//
//            //绘制音量柱倒影
//            if (i <= 6 && value > 0) {
//                mPaint.setColor(Color.WHITE);//画笔颜色
//                mPaint.setAlpha(100 - (100 / 6 * i));//倒影颜色
//                canvas.drawLine(x, y1, (x + strokeLength), y1, mPaint);//绘制频谱块
//            }
//        }
//    }
//
//    @Override
//    public void onDraw(Canvas canvas) {
//        int j=-4;
//        for (int i = 0; i < CYLINDER_NUM/2-4; i++) { //绘制25个能量柱
//
//            drawCylinder(canvas, strokeWidth / 2 + hgap + i * (hgap + strokeLength), mData[i]);
//        }
//        for(int i =CYLINDER_NUM; i>=CYLINDER_NUM/2-4; i--){
//            j++;
//            drawCylinder(canvas, strokeWidth / 2 + hgap + (CYLINDER_NUM / 2+j-1 )* (hgap + strokeLength), mData[i-1]);
//        }
//    }
//
//    /**
//     * It sets the visualizer of the view. DO set the viaulizer to null when exit the program.
//     *
//     * @parma visualizer It is the visualizer to set.
//     */
//    public void setVisualizer(Visualizer visualizer) {
//        if (visualizer != null) {
//            if (!visualizer.getEnabled()) {
//                visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
//            }
//            levelStep = 230 / MAX_LEVEL;
//            visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, false, true);
//        } else {
//
//            if (mVisualizer != null) {
//                mVisualizer.setEnabled(false);
//                mVisualizer.release();
//            }
//        }
//        mVisualizer = visualizer;
//    }
//
//    public byte[] updateVisualizer(byte[] fft) {
//        levelStep = 230 / MAX_LEVEL;
//        byte[] model = new byte[fft.length / 2 + 1];
//        if (mDataEn) {
//            model[0] = (byte) Math.abs(fft[1]);
//            int j = 1;
//            for (int i = 2; i < fft.length; ) {
//                model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
//                i += 2;
//                j++;
//            }
//        } else {
//            for (int i = 0; i < CYLINDER_NUM; i++) {
//                model[i] = 0;
//            }
//        }
//        for (int i = 0; i < CYLINDER_NUM; i++) {
//            final byte a = (byte) (Math.abs(model[CYLINDER_NUM - i]) / levelStep);
//
//            final byte b = mData[i];
//            if (a > b) {
//                mData[i] = a;
//            } else {
//                if (b > 0) {
//                    mData[i]--;
//                }
//            }
//        }
//        postInvalidate();
//        return mData;
//    }
//
//    //这个回调应该采集的是快速傅里叶变换有关的数据
//    @Override
//    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
//
//
//        byte[] model = new byte[fft.length / 2 + 1];
//        if (mDataEn) {
//            model[0] = (byte) Math.abs(fft[1]);
//            int j = 1;
//            for (int i = 2; i < fft.length; ) {
//                model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
//                i += 2;
//                j++;
//            }
//        } else {
//            for (int i = 0; i < CYLINDER_NUM; i++) {
//                model[i] = 0;
//            }
//        }
//        for (int i = 0; i < CYLINDER_NUM; i++) {
//            final byte a = (byte) (Math.abs(model[CYLINDER_NUM - i]) / levelStep);
//
//            final byte b = mData[i];
//            if (a > b) {
//                mData[i] = a;
//            } else {
//                if (b > 0) {
//                    mData[i]--;
//                }
//            }
//        }
//        postInvalidate();//刷新界面
////        invalidate();
//    }
//
//    //这个回调应该采集的是波形数据
//    @Override
//    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
//        // Do nothing...
//    }

}

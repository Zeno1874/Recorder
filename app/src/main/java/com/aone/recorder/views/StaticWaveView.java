package com.aone.recorder.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.views
 * @ClassName: FileWaveView
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/14 5:55
 * @Desc:
 */
public class StaticWaveView extends View {
    private static final String TAG = StaticWaveView.class.getSimpleName();

    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();

    private Paint mForePaint = new Paint();
    private int mSpectrumNum;

    public StaticWaveView(Context context) {
        super(context);
        init();
    }

    public StaticWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StaticWaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBytes = null;

        mForePaint.setStrokeWidth(4.5f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.WHITE);
    }

    public void updateVisualizer(byte[] fft) {
        mSpectrumNum = fft.length / 2;
        byte[] model = new byte[fft.length / 2];
        for (int i = 0, j = 0; j < mSpectrumNum; j++) {
            model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
            i += 2;
        }
        mBytes = model;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null) {
            return;
        }

        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }

        mRect.set(0, 0, getWidth(), getHeight());
        //绘制柱形频谱
        final int baseX = mRect.width() / mSpectrumNum;
        final int height = mRect.height();

        for (int i = 0; i < mSpectrumNum; i++) {
            if (mBytes[i] < 0) {
                mBytes[i] = 127;
            }
            final int xi = baseX * i + baseX / 2;

            mPoints[i * 4] = xi;
            mPoints[i * 4 + 1] = height;

            mPoints[i * 4 + 2] = xi;
            mPoints[i * 4 + 3] = height - mBytes[i];
        }
        canvas.drawLines(mPoints, mForePaint);
    }


}

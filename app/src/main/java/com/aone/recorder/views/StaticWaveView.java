package com.aone.recorder.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

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

    private List<Double> mData;
    //    private byte[] mBytes;
    private Rect mRect = new Rect();

    private Paint mPaint = new Paint();

    private double mSpace;
    private double mWidth;
    private double mHeight;

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
        mWidth = 2;

        mData = null;
        mPaint.setStrokeWidth(4.5f);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
    }

    public void setData(List<Double> data) {
        mData = data;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int Height = getHeight();
        int Width = getWidth();
        canvas.drawColor(Color.argb(0, 0, 0, 0));
        if (mData == null)
            return;
        mSpace = (Width - mWidth * mData.size()) / (mData.size() - 1);
        float left = 0;
        float top;
        float right;
        float bottom;
        for (double data : mData) {
            if (data != 0) {
                top = (float) (Height - Height * data);
//                top= 0;
                right = (float) (left + mWidth);
                bottom = (float) Height;
                @SuppressLint("DrawAllocation") RectF rect = new RectF(left, top, right, bottom);
                canvas.drawRect(rect, mPaint);
                left = (float) (right + mSpace);
            } else {

                float x = (float) (left + mWidth);
                float y = (float) Height;
                canvas.drawPoint(x, y, mPaint);
                left = (float) (x + mSpace);
            }
        }

//        if (mData == null) {
//            return;
//        }
//
//        if (mPoints == null || mPoints.length < mData.size() * 4) {
//            mPoints = new float[mBytes.length * 4];
//        }
//
//        mRect.set(0, 0, getWidth(), getHeight());
//        //绘制柱形频谱
//        final int baseX = mRect.width() / mSpectrumNum;
//        final int height = mRect.height();
//
//        for (int i = 0; i < mSpectrumNum; i++) {
//            if (mBytes[i] < 0) {
//                mBytes[i] = 127;
//            }
//            final int xi = baseX * i + baseX / 2;
//
//            mPoints[i * 4] = xi;
//            mPoints[i * 4 + 1] = height;
//
//            mPoints[i * 4 + 2] = xi;
//            mPoints[i * 4 + 3] = height - mBytes[i];
//        }
//        canvas.drawLines(mPoints, mForePaint);
    }


}

package com.aone.recorder.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private Paint mPaint = new Paint();

    private double mWidth;

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
        double space = (Width - mWidth * mData.size()) / (mData.size() - 1);
        float left = 0;
        float top;
        float right;
        float bottom;
        for (double data : mData) {
            if (data != 0) {
                top = (float) (Height - Height * data);
                right = (float) (left + mWidth);
                bottom = (float) Height;
                @SuppressLint("DrawAllocation") RectF rect = new RectF(left, top, right, bottom);
                canvas.drawRect(rect, mPaint);
                left = (float) (right + space);
            } else {
                float x = (float) (left + mWidth);
                float y = (float) Height;
                canvas.drawPoint(x, y, mPaint);
                left = (float) (x + space);
            }
        }
    }
}

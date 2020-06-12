package com.aone.recorder.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.aone.recorder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Recorder
 * @Package: com.aone.recorder.views
 * @ClassName: WaveView
 * @Author: Xeon
 * @University: Gansu Agricultural University
 * @Department: Information Science and Technology
 * @Major: Computer Science and Technology
 * @Date: 2020/6/13 2:06
 * @Desc:
 */
public class WaveView extends View {
    private static final String TAG = WaveView.class.getSimpleName();

    private static final int DEFAULT_COLOR = Color.parseColor("#3DB9A0");

    private static final int DEFAULT_NOTE_WIDTH = 1;

    private static final int DEFAULT_SPACE_WIDTH = 5;

    private static final int DEFAULT_LINE_SIZE = 0;

    /*
        音符列表
     */
    private List<Integer> notes = new ArrayList<>();
    /*
        音符画笔
     */
    private Paint paint;
    /*
        倒影画笔
     */
    private Paint reflectionPaint;
    /*
        音符颜色
     */
    private int color;
    /*
        倒影颜色
     */
    private int reflectionColor;
    /*
        音符宽度
     */
    private float noteWidth;
    /*
        空格宽度
     */
    private float space;
    /*
        中线高度
     */
    private float line;

    private boolean ViewState;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.WaveView, defStyleAttr, 0);
        color = typedArray.getColor(R.styleable.WaveView_rectColor, DEFAULT_COLOR);
        noteWidth = typedArray.getDimension(R.styleable.WaveView_rectWidth, DEFAULT_NOTE_WIDTH);
        space = typedArray.getDimension(R.styleable.WaveView_rectSpace, DEFAULT_SPACE_WIDTH);
        line = typedArray.getDimension(R.styleable.WaveView_middleLine, DEFAULT_LINE_SIZE);
        typedArray.recycle();

//        reflectionColor = color - 0xC0000000;
        reflectionColor = color;
        init();
    }

    private void init() {
        //初始化画笔
        paint = new Paint();
        reflectionPaint = new Paint();

        paint.setColor(color);
        reflectionPaint.setColor(reflectionColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        if (ViewState) {
//            画一条中线
            canvas.drawRect(0, height / 2 - line / 2, width, height / 2 + line / 2, paint);
//            循环向画布上画音频刻度
            for (int i = 0, size = notes.size(); i < size; i++) {
                int noteHeight = notes.get(i);
//                上半部分
                float left = width - (noteWidth * (1 + i) + space * i);
                float top = height / 2 - noteHeight - space - (line / 2);
                float right = width - (noteWidth * i + space * i);
                float bottom = height / 2;
                canvas.drawRect(left, top, right, bottom, paint);
//                下半部分
                float refLeft = width - (noteWidth * (1 + i) + space * i);
                float refTop = height / 2;
                float refRight = width - (noteWidth * i + space * i);
                float refBottom = height / 2 + noteHeight + space + line / 2;
                canvas.drawRect(refLeft, refTop, refRight, refBottom, reflectionPaint);
            }
        }
    }

    /**
     * 添加一个刻度
     * @param height 高度
     */
    public void addSpectrum(int height) {
        notes.add(0, height);
        ViewState = true;
        invalidate();
    }

    /**
     * 清空画布
     */
    public void cleanCanvas() {
        ViewState = !ViewState;
        notes.clear();
        invalidate();
    }
}

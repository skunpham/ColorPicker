package com.skunpham.colorpicker;

import static com.skunpham.colorpicker.Constants.SELECTOR_RADIUS_DP;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class ColorWheelSelector extends View {

    private Paint selectorPaint;
    private float selectorRadiusPx = SELECTOR_RADIUS_DP * 3;
    private PointF currentPoint = new PointF();

    private int color = Color.BLACK;

    public ColorWheelSelector(Context context) {
        this(context, null);
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setColor(color);
        selectorPaint.setStyle(Paint.Style.FILL);
        selectorPaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        selectorPaint.setColor(Color.WHITE);
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx, selectorPaint);
        selectorPaint.setColor(color);
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 0.7f, selectorPaint);
    }

    public void setSelectorRadiusPx(float selectorRadiusPx) {
        this.selectorRadiusPx = selectorRadiusPx * 1.5f;
    }

    public void setCurrentPoint(PointF currentPoint, int color) {
        this.currentPoint = currentPoint;
        this.color = color;
        invalidate();
    }
}

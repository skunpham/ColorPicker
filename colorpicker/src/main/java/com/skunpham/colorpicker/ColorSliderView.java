package com.skunpham.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class ColorSliderView extends View implements ColorObservable, Updatable {
    protected int baseColor = Color.WHITE;
    private Paint colorPaint;

    private Paint colorBorder;

    //private Paint borderPaint;
    private Paint selectorPaint;

    private Path currentSelectorPath = new Path();
    protected float selectorSize;
    protected float currentValue = 1f;
    private boolean onlyUpdateOnTouchEventUp;

    private int colorChoose = Color.WHITE;

    private ColorObservableEmitter emitter = new ColorObservableEmitter();
    private ThrottledTouchEventHandler handler = new ThrottledTouchEventHandler(this);

    public ColorSliderView(Context context) {
        this(context, null);
    }

    public ColorSliderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        colorBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        colorBorder.setStyle(Paint.Style.STROKE);
        colorBorder.setStrokeWidth(8);
        colorBorder.setColor(Color.WHITE);

        colorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        configurePaint(colorPaint);
        selectorSize = h * 0.5f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();

        canvas.drawRect(height/2, height * 0.35f, width - height/2, height * 0.65f, colorPaint);

        //selectorPath.offset(currentValue * (width - 2 * selectorSize), 0, currentSelectorPath);
        //canvas.drawPath(currentSelectorPath, selectorPaint);

        float tmpX = currentValue * (width - 2 * height/2) + height/2;
        float tmpY = height/2;

        /*selectorPaint.setColor(Color.WHITE);
        canvas.drawCircle(tmpX, tmpY, height/2, selectorPaint);*/

        selectorPaint.setColor(colorChoose);
        canvas.drawCircle(tmpX, tmpY, height*0.4f, selectorPaint);


        canvas.drawCircle(tmpX, tmpY, height*0.4f, colorBorder);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                handler.onTouchEvent(event);
                return true;
            case MotionEvent.ACTION_UP:
                update(event);
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void update(MotionEvent event) {
        updateValue(event.getX());
        boolean isTouchUpEvent = event.getActionMasked() == MotionEvent.ACTION_UP;
        if (!onlyUpdateOnTouchEventUp || isTouchUpEvent) {
            emitter.onColor(assembleColor(), true, isTouchUpEvent);
        }
        updateSelected();
    }

    void setBaseColor(int color, boolean fromUser, boolean shouldPropagate) {
        baseColor = color;
        configurePaint(colorPaint);
        int targetColor = color;
        if (!fromUser) {
            currentValue = resolveValue(color);
        } else {
            targetColor = assembleColor();
        }

        if (!onlyUpdateOnTouchEventUp) {
            emitter.onColor(targetColor, fromUser, shouldPropagate);
        } else if (shouldPropagate) {
            emitter.onColor(targetColor, fromUser, true);
        }
        updateSelected();
    }

    private void updateValue(float eventX) {
        float left = getHeight()/2f;
        float right = getWidth() - getHeight()/2f;
        if (eventX < left) eventX = left;
        if (eventX > right) eventX = right;
        currentValue = (eventX - left) / (right - left);
    }

    public void updateSelected() {
        colorChoose = getColor();
        invalidate();
    }

    protected abstract float resolveValue(int color);

    protected abstract void configurePaint(Paint colorPaint);

    protected abstract int assembleColor();

    @Override
    public void subscribe(ColorObserver observer) {
        emitter.subscribe(observer);
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        emitter.unsubscribe(observer);
    }

    @Override
    public int getColor() {
        return emitter.getColor();
    }

    public void setOnlyUpdateOnTouchEventUp(boolean onlyUpdateOnTouchEventUp) {
        this.onlyUpdateOnTouchEventUp = onlyUpdateOnTouchEventUp;
    }

    private ColorObserver bindObserver = new ColorObserver() {
        @Override
        public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
            setBaseColor(color, fromUser, shouldPropagate);
        }
    };

    private ColorObservable boundObservable;

    public void bind(ColorObservable colorObservable) {
        if (colorObservable != null) {
            colorObservable.subscribe(bindObserver);
            setBaseColor(colorObservable.getColor(), true, true);
        }
        boundObservable = colorObservable;
    }

    public void unbind() {
        if (boundObservable != null) {
            boundObservable.unsubscribe(bindObserver);
            boundObservable = null;
        }
    }
}

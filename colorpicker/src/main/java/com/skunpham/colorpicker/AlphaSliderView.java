package com.skunpham.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;


public class AlphaSliderView extends ColorSliderView {

    private Bitmap backgroundBitmap;
    private Canvas backgroundCanvas;

    public AlphaSliderView(Context context) {
        super(context);
    }

    public AlphaSliderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AlphaSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        backgroundBitmap = Bitmap.createBitmap((int) (w - 2 * selectorSize),
                (int) (h * 0.3f), Bitmap.Config.ARGB_8888);
        backgroundCanvas = new Canvas(backgroundBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        Drawable drawable = CheckerboardDrawable.create();
        drawable.setBounds((int) (selectorSize), (int) (height * 0.35f), (int) (width - selectorSize), (int) (height * 0.65f));
        drawable.draw(backgroundCanvas);
        canvas.drawBitmap(backgroundBitmap, selectorSize, (int) (height * 0.35f), null);
        super.onDraw(canvas);
    }

    @Override
    protected float resolveValue(int color) {
        return Color.alpha(color) / 255.f;
    }

    protected void configurePaint(Paint colorPaint) {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        int startColor = Color.HSVToColor(0, hsv);
        int endColor = Color.HSVToColor(255, hsv);
        Shader shader = new LinearGradient(0, 0, getWidth(), getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        colorPaint.setShader(shader);
    }

    protected int assembleColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(baseColor, hsv);
        int alpha = (int) (currentValue * 255);
        return Color.HSVToColor(alpha, hsv);
    }
}

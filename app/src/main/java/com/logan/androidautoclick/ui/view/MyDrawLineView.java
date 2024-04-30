package com.logan.androidautoclick.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.logan.androidautoclick.R;

public class MyDrawLineView extends View {

    private float x1 = 0f, y1 = 0f, x2 = 0f, y2 = 0f;
    public float x0 = 0f, y0 = 0f;

    public MyDrawLineView(Context context) {
        super(context);
    }

    public MyDrawLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawLineView(Context context, @Nullable AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCenterLineX(canvas, x0, getHeight(), getContext().getColor(R.color.blue));
        drawCenterLineY(canvas, y0, getWidth(), getContext().getColor(R.color.blue));

        drawCenterLineX(canvas, x1, getHeight(), Color.BLACK);
        drawCenterLineY(canvas, y1, getWidth(), Color.BLACK);
        drawCenterLineX(canvas, x2, getHeight(), Color.WHITE);
        drawCenterLineY(canvas, y2, getWidth(), Color.WHITE);


        drawSpot(canvas, x0, y0, 20, getContext().getColor(R.color.blue));
        drawSpot(canvas, x2, y2, 20, Color.WHITE);


    }

    private void drawSpot(Canvas canvas, float cx, float cy, int radius, int clolor) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(clolor);
        paint.setStrokeWidth(2);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //修改触屏坐标
        x0 = event.getX();
        y0 = event.getY();
        //通知当前控件更新
        invalidate();
        //true表示已处理该方法
        return true;
    }

    /**
     * 绘制x轴的中间竖线
     */
    private void drawCenterLineX(Canvas canvas, Float value, int wh, int clolor) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(clolor);
        paint.setStrokeWidth(2);
        canvas.drawLine(value, 0, value, wh, paint);
    }

    /**
     * 绘制y轴的中间横线
     */
    private void drawCenterLineY(Canvas canvas, Float value, int wh, int clolor) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(clolor);
        paint.setStrokeWidth(2);
        canvas.drawLine(0, value, wh, value, paint);
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }
}

package com.example.shape_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class ShapeView extends View {
    private String shapeType;
    private Paint paint;
    private int lastProgress=0;
    public int getLastProgress() {
        return lastProgress;
    }

    public void setLastProgress(int lastProgress) {
        this.lastProgress = lastProgress;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "ShapeView{" +
                "shapeType='" + shapeType + '\'' +
                ", paint=" + paint +
                ", size=" + size +
                '}';
    }

    private int size=100 ;

    public ShapeView(Context context, String shapeType) {
        super(context);
        this.shapeType = shapeType;
        init();
    }

    public ShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setSize(int newSize) {
        this.size = newSize;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        switch (shapeType) {
            case "circle":
                canvas.drawCircle(width / 2f, height / 2f, Math.min(width, height) / 2f, paint);
                break;
            case "square":
                canvas.drawRect(0, 0, width, height, paint);
                break;
            case "rectangle":
                canvas.drawRect(0, 0, width, height / 2f, paint);
                break;
            case "triangle":
                Path path = new Path();
                path.moveTo(width / 2f, 0);
                path.lineTo(0, height);
                path.lineTo(width, height);
                path.close();
                canvas.drawPath(path, paint);
                break;
        }
    }

    public void setShapeColor(int color) {
        paint.setColor(color);
        invalidate();
    }

}
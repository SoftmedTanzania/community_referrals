package com.softmed.ccm_chw.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

public class SmallDiagonalCutPathDrawable extends Drawable {

    Path mPath = new Path();
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SmallDiagonalCutPathDrawable(){
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(5);
    }

    @Override
    public void draw(Canvas canvas) {
        mPath.moveTo(10,10);
        mPath.lineTo(canvas.getWidth()-50, 10);
        mPath.lineTo(canvas.getWidth()-10, 50);
        mPath.lineTo(canvas.getWidth()-10, canvas.getHeight()-10);
        mPath.lineTo(10, canvas.getHeight()-10);


        mPath.close();
        canvas.drawPath(mPath, mPaint);

    }

    @Override
    public void setAlpha(int i) {}

    @Override
    public void setColorFilter(ColorFilter colorFilter) {}

    @Override
    public int getOpacity() {
        return 0;
    }


}
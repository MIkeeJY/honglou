package com.hyxsp.video.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * Created by hackest on 2018/3/12.
 */

public class ShadowView extends View {

    private Drawable mDrawable;

    public ShadowView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable == null) {
            int colors[] = {0x00B8C5D9, 0x12B8C5D9, 0x30B8C5D9};//分别为开始颜色，中间颜色，结束颜色
            mDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
        }
        mDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mDrawable.draw(canvas);
    }
}

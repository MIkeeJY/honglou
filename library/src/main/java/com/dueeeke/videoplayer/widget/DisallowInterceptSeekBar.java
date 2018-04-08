package com.dueeeke.videoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * Created by hackest on 2018/4/8.
 */

public class DisallowInterceptSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    public DisallowInterceptSeekBar(Context var1) {
        super(var1);
    }

    public DisallowInterceptSeekBar(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public DisallowInterceptSeekBar(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public boolean onTouchEvent(MotionEvent var1) {
        ViewParent var2 = this.getParent();
        if (var2 != null) {
            var2.requestDisallowInterceptTouchEvent(true);
        }

        return super.onTouchEvent(var1);
    }
}


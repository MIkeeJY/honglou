package com.hyxsp.video.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * ================================================
 * 作    者：linksus
 * 版    本：1.0
 * 创建日期：8/3 0003
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class StateBarUtils {
    /**
     * 设置状态栏透明
     * @param activity
     */
    public static void setTranslucentColor(Activity activity) {
        //最终方案
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 全透明实现
            //getWindow.setStatusBarColor(Color.TRANSPARENT)
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4 全透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void setStateBarColor(Activity activity, int resid) {
        setTranslucentColor(activity);
        // 设置状态栏颜色
        ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
        setupStatusBarView(activity, contentLayout, resid);
        // 设置Activity layout的fitsSystemWindows
        View contentChild = contentLayout.getChildAt(0);
        contentChild.setFitsSystemWindows(true);//等同于在根布局设置android:fitsSystemWindows="true"
    }

    private static void setupStatusBarView(Activity activity, ViewGroup contentLayout, int resid) {
        // View mStatusBarView = null;
        View statusBarView = new View(activity);
        statusBarView.setBackgroundResource(resid);
        int statusBarHeight = getStatusBarHeight(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        contentLayout.addView(statusBarView, lp);
        // mStatusBarView = statusBarView;
    }

    /**
     * 获得状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}

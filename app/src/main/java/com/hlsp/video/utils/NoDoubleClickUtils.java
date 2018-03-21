package com.hlsp.video.utils;

import android.view.View;

/**
 * 防止多次点击的工具类
 */
public class NoDoubleClickUtils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 1000;

    public static void initLastClickTime() {
        lastClickTime = 0;
    }

    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime >
                SPACE_TIME) {
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        lastClickTime = currentTime;
        return isClick2;
    }


    public static void setEnable(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {

                view.setEnabled(true);
            }
        });
    }
}

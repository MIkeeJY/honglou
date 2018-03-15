package com.hlsp.video.utils;

import android.widget.Toast;

import cn.share.jack.cyghttp.app.CygApplication;


/**
 * 吐司工具类 为了避免内存泄漏 使用application的context
 * Created by hackest on 2017/3/14.
 */

public class ToastUtil {

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(CygApplication.getInstance(), s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }


    public static void showToast(int resId) {
        showToast(CygApplication.getInstance().getString(resId));
    }
}

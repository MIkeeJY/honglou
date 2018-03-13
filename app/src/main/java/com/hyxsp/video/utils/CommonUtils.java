package com.hyxsp.video.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 通用方法工具类
 * Created by hackest on 2018/2/7.
 */

public class CommonUtils {

    /**
     * 获取渠道号
     */
    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            //
        }
        return null;
    }


    /**
     * @return 大于10万的返回以万为单位的数字, 如12.5万
     */
    public static String formatCount(long count) {
        if (count < 0)
            return "0";
        if (count < 100000) {
            return count + "";
        } else {
            return String.format("%.1f万", count / 10000.0f);
        }
    }

}

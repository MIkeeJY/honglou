package com.hlsp.video.statistics.util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.hlsp.video.App;

/**
 * 动态获取资源id
 */
public class ResourceUtil {
    /**
     * type   文件类型如： drawable, color, string, layout等
     * resName     资源名称 :night_xxxx 或 light_xxxx
     */
    public static String mode = "";

    public static int getResourceId(Context context, int type, String resName) {
        Resources res = context.getResources();
        if (mode == null || mode.length() == 0) {
            SharedPreferences sp = context.getSharedPreferences("config", Activity.MODE_PRIVATE);
            mode = sp.getString("mode", "light");
        }
        int id = 0;
        switch (type) {
            case Constants.DRAWABLE:
                id = res.getIdentifier(mode + resName, "drawable", context.getPackageName());
                break;
            case Constants.COLOR:
                id = res.getIdentifier(mode + resName, "color", context.getPackageName());
                break;
            case Constants.STYLE:
                id = res.getIdentifier(mode + resName, "style", context.getPackageName());
                break;
        }
        return id;
    }

    public static String getStringById(int id) {
        Resources res = App.getInstance().getResources();
        return res.getString(id);
    }
}

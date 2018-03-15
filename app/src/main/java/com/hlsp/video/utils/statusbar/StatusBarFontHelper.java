package com.hlsp.video.utils.statusbar;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.IntDef;

import com.hlsp.video.utils.statusbar.impl.AndroidMHelper;
import com.hlsp.video.utils.statusbar.impl.FlymeHelper;
import com.hlsp.video.utils.statusbar.impl.MIUIHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kzl on 2016/5/17
 */
public class StatusBarFontHelper {
    @IntDef({
            OTHER,
            MIUI,
            FLYME,
            ANDROID_M
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface SystemType {

    }

    public static final int OTHER = -1;
    public static final int MIUI = 1;
    public static final int FLYME = 2;
    public static final int ANDROID_M = 3;


    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return 1:MIUI 2:Flyme 3:android6.0
     */
    public static int setStatusBarMode(Activity activity, boolean isFontColorDark) {
        @SystemType int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (new MIUIHelper().setStatusBarLightMode(activity, isFontColorDark)) {
                result = MIUI;
            } else if (new FlymeHelper().setStatusBarLightMode(activity, isFontColorDark)) {
                result = FLYME;
            } else if (new AndroidMHelper().setStatusBarLightMode(activity, isFontColorDark)) {
                result = ANDROID_M;
            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUI6、Flyme和6.0以上版本其他Android
     *
     * @param type 1:MIUI 2:Flyme 3:android6.0
     */
    public static void setLightMode(Activity activity, @SystemType int type) {
        setStatusBarMode(activity, type, true);

    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void setDarkMode(Activity activity, @SystemType int type) {
        setStatusBarMode(activity, type, false);
    }

    private static void setStatusBarMode(Activity activity, @SystemType int type, boolean isFontColorDark) {
        if (type == MIUI) {
            new MIUIHelper().setStatusBarLightMode(activity, isFontColorDark);
        } else if (type == FLYME) {
            new FlymeHelper().setStatusBarLightMode(activity, isFontColorDark);
        } else if (type == ANDROID_M) {
            new AndroidMHelper().setStatusBarLightMode(activity, isFontColorDark);
        }
    }

}

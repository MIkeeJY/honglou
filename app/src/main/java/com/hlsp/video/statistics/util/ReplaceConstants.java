package com.hlsp.video.statistics.util;


import com.hlsp.video.R;

public class ReplaceConstants {

    public static ReplaceConstants replaceConstants = null;
    public String APP_PATH;
    public String APP_PATH_CACHE;

    private ReplaceConstants() {
        APP_PATH = Constants.SDCARD_PATH + ResourceUtil.getStringById(R.string.app_path);
        APP_PATH_CACHE = APP_PATH + ResourceUtil.getStringById(R.string.app_path_cache);

    }

    public static ReplaceConstants getReplaceConstants() {
        if (replaceConstants == null)
            replaceConstants = new ReplaceConstants();
        return replaceConstants;
    }
}

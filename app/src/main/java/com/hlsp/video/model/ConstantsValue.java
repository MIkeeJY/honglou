package com.hlsp.video.model;

import com.hlsp.video.App;

import java.io.File;

/**
 * Created by hackest on 2018/3/14.
 */

public class ConstantsValue {

    public static final String USER_SOFT_CHECK_UPDATE = "user_soft_check_update";// 存储下次是否检查更新

    public static final String BASE_NAME = "honglou";
    public static final String BASE_PATH = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + BASE_NAME;

    public static final String REAL_IP = "REALIP";

    public static final String USER_NAME = "USER_NAME";


    public static final String HISTORY_VIDEO = "history_video";

}

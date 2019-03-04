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

    /**
     * TODO　根据自己的账号去修改（只需要修改这几个即可）
     */
    public static final String IID = "26636470443"; //安装ID
    public static final String UUID = "864394010744615";
    public static final String OPEN_UDID = "7446a0820f251016";
    public static final String DEVICE_ID = "43826946118";

    /**
     * 固定不变
     */
    public static final String APP_NAME = "aweme";
    public static final String CHANNEL = "xiaomi";
    public static final String V_CODE = "159";
    public static final String V_NAME = "1.5.9";
    public static final String AID = "1128";

}

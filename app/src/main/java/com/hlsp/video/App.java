package com.hlsp.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.hlsp.video.model.HttpBaseUrl;
import com.hlsp.video.utils.CommonUtils;
import com.hlsp.video.utils.SpUtils;
import com.hlsp.video.utils.Utils;
import com.hlsp.video.utils.hookpms.ServiceManagerWraper;
import com.ss.android.common.applog.GlobalContext;
import com.ss.android.common.applog.UserInfo;

import cn.share.jack.cyghttp.app.CygApplication;
import cn.share.jack.cyghttp.app.HttpServletAddress;

/**
 * Created by jack on 2017/6/13
 */

public class App extends CygApplication {

    public static String IMEI;
    public static String PACKAGE_NAME;
    public static String VERSION_NAME;
    public static String CHANNEL_ID;
    public static String ANDROID_ID;
    public static String SERIAL_NO;

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceManagerWraper.hookPMS(this.getApplicationContext());

        SpUtils.init(this);
        //缓存起来防止每次网络请求都去拿
        PACKAGE_NAME = CommonUtils.getProcessName();
        VERSION_NAME = Utils.getHasDotVersion(App.getInstance());
        CHANNEL_ID = CommonUtils.getMetaData(App.getInstance(), "BaiduMobAd_CHANNEL");
        IMEI = Utils.getDeviceIMEI(App.getInstance());
        ANDROID_ID = Utils.getDeviceAndroidId(App.getInstance());
        SERIAL_NO = Utils.getSerialNo();


        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setDownsampleEnabled(true)   // 对图片进行自动缩放
                .setResizeAndRotateEnabledForNetwork(true)    // 对图片进行自动缩放
                .setBitmapsConfig(Bitmap.Config.RGB_565) //  //图片设置RGB_565，减小内存开销  fresco默认情况下是RGB_8888
                //other settings
                .build();
        Fresco.initialize(this, config);


        if (BuildConfig.DEBUG) {
            HttpServletAddress.getInstance().setOfflineAddress(HttpBaseUrl.BASE_TEXT_URL);
        } else {
            HttpServletAddress.getInstance().setOnlineAddress(HttpBaseUrl.BASE_URL);
        }

        LogUtils.getLogConfig()
                .configAllowLog(BuildConfig.DEBUG)
                .configTagPrefix(this.getPackageName())
                .configShowBorders(true)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                .configLevel(LogLevel.TYPE_VERBOSE);


        StatService.setDebugOn(BuildConfig.DEBUG);


        GlobalContext.setContext(getApplicationContext()); //Hook 抖音

        try {
            System.loadLibrary("userinfo");//抖音&火山
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserInfo.setAppId(2);
        int result = UserInfo.initUser("a3668f0afac72ca3f6c1697d29e0e1bb1fef4ab0285319b95ac39fa42c38d05f");

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //内存低的时候，清理Glide的缓存
        Glide.get(this).clearMemory();
    }
}

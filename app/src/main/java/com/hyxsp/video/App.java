package com.hyxsp.video;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mobstat.StatService;
import com.dycm_adsdk.PlatformSDK;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyxsp.video.utils.CommonUtils;
import com.hyxsp.video.utils.SpUtils;
import com.ss.android.common.applog.GlobalContext;
import com.ss.android.common.applog.UserInfo;

import cn.share.jack.cyghttp.app.CygApplication;

/**
 * Created by jack on 2017/6/13
 */

public class App extends CygApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        SpUtils.init(this);
        Fresco.initialize(this);

        PlatformSDK.app().onAppCreate(this);

//        HttpServletAddress.getInstance().setOfflineAddress("http://client.kuolie.me:56899/api/");
        LogUtils.getLogConfig()
                .configAllowLog(BuildConfig.DEBUG)
                .configTagPrefix(this.getPackageName())
                .configShowBorders(true)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                .configLevel(LogLevel.TYPE_VERBOSE);


        StatService.setDebugOn(BuildConfig.DEBUG);

        PlatformSDK.config().setChannel_code(CommonUtils.getMetaData(getApplicationContext(), "BaiduMobAd_CHANNEL"));
//        PlatformSDK.config().setAd_userid("UserID");
//        PlatformSDK.config().setCityCode("CityCode");
//        PlatformSDK.config().setCityName("CityName");
//        PlatformSDK.config().setLatitude("Latitude");
//        PlatformSDK.config().setLongitude("Longitude");

        GlobalContext.setContext(getApplicationContext()); //Hook 抖音

        try {
            System.loadLibrary("userinfo");//抖音&火山
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserInfo.setAppId(2);
        int result = UserInfo.initUser("a3668f0afac72ca3f6c1697d29e0e1bb1fef4ab0285319b95ac39fa42c38d05f");

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

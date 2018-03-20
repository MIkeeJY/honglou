package com.hlsp.video;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.baidu.mobstat.StatService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hlsp.video.utils.SpUtils;
import com.ss.android.common.applog.GlobalContext;
import com.ss.android.common.applog.UserInfo;

import cn.share.jack.cyghttp.app.CygApplication;
import cn.share.jack.cyghttp.app.HttpServletAddress;

/**
 * Created by jack on 2017/6/13
 */

public class App extends CygApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        ServiceManagerWraper.hookPMS(this.getApplicationContext());

        SpUtils.init(this);
        Fresco.initialize(this);

        HttpServletAddress.getInstance().setOfflineAddress("http://20.20.23.79:8888/v1/app/");
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

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

package com.hlsp.video.base;

import android.text.TextUtils;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.hlsp.video.App;
import com.hlsp.video.BuildConfig;
import com.hlsp.video.model.ConstantsValue;
import com.hlsp.video.model.CygApi;
import com.hlsp.video.utils.SpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.share.jack.cyghttp.BaseRetrofit;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jack on 2017/6/13
 */

public class BaseModel extends BaseRetrofit {

    private static final String TAG = "BaseModel";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected CygApi mServletApi;

    protected Map<String, String> mParams = new HashMap<>();

    public BaseModel() {
        super();
        mServletApi = mRetrofit.create(CygApi.class);
    }

    /**
     * 设置header的公共参数
     */
    @Override
    protected Interceptor getApiheader() {
        Interceptor apiheader = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
//                request = request.newBuilder()
//                        .addHeader("Token", String.valueOf(UserDao.getInstance().getToken()))
//                        .addHeader("Custom-Client-Type", "1")//iOS:0 , android:1
//                        .addHeader("Custom-Client-Version", Utils.getFormatBersion(CygApplication.getInstance()))
//                        .build();
                return chain.proceed(request);
            }
        };
        return apiheader;
    }

    /**
     * 设置拦截打印的json
     */
    @Override
    protected HttpLoggingInterceptor getlogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        if (TextUtils.isEmpty(message))
                            return;
                        String s = message.substring(0, 1);
                        //如果收到想响应是json才打印
//                        if ("{".equals(s) || "[".equals(s)) {
//                            LogUtils.json(message);
//                        }
                        if ("{".equals(s) || "[".equals(s)) {
                            LogUtils.json(message);
                        } else if (message.contains("http://")) {
                            LogUtils.d(message);
                        } else if (message.contains("Exception")) {
                            LogUtils.d(message);
                        }
                    }
                });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    /**
     * 设置是否是debug
     */
    @Override
    protected Boolean isDebug() {
        return BuildConfig.DEBUG;
    }


    /**
     * 设置header的公共参数
     */

    protected Map<String, String> getCommonMap() {
        Map<String, String> commonMap = new HashMap<>();
        commonMap.put("packageName", App.PACKAGE_NAME);
        commonMap.put("versionName", App.VERSION_NAME);
        commonMap.put("channelId", App.CHANNEL_ID);
        commonMap.put("IMEI", App.IMEI);
        commonMap.put("AndroidID", App.ANDROID_ID);
        commonMap.put("SerialNo", App.SERIAL_NO);
        commonMap.put("os", "android");
        String ip = SpUtils.getString(ConstantsValue.REAL_IP);
        if (ip.contains("\"")) {
            ip.replace("\"", "");
        }
        commonMap.put("ip", ip);

        return commonMap;
    }


    protected void addParams(String key, String value) {
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "the key is null");
            return;
        }
        mParams.put(key, value);
    }

    protected void addParams(Map<String, String> params) {
        if (null == params) {
            Log.e(TAG, "the map is null");
            return;
        }
        mParams.putAll(params);
    }
}

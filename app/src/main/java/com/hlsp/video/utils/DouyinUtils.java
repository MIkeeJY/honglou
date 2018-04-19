package com.hlsp.video.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.ss.android.common.applog.UserInfo;

import java.util.HashMap;
import java.util.Map.Entry;

public class DouyinUtils {

    public static String appVersionCode = "159";
    public static String appVersionName = "1.5.9";

    public static final String PKGNAME = "com.ss.android.ugc.aweme";

    private final static String GETDATA_JSON_URL = "https://api.amemv.com/aweme/v1/feed/";


    /**
     * 下拉数据规律：min_cursor=max_cursor=0
     * <p>
     * 上拉数据规律：
     * 第二次请求取第一次请求返回的json数据中的min_cursor字段，max_cursor不需要携带。
     * 第三次以及后面所有的请求都只带max_cursor字段，值为第一次请求返回的json数据中的max_cursor字段
     *
     * @return
     */
    public static String getEncryptUrl(Activity act, long minCursor, long maxCursor) {
        String url = null;
        int time = (int) (System.currentTimeMillis() / 1000);
        try {
            HashMap<String, String> paramsMap = getCommonParams(act);
            String[] paramsAry = new String[paramsMap.size() * 2];
            int i = 0;
            for (Entry<String, String> entry : paramsMap.entrySet()) {
                paramsAry[i] = entry.getKey();
                i++;
                paramsAry[i] = entry.getValue();
                i++;
            }

            paramsMap.put("count", "20");
            paramsMap.put("type", "0");
            paramsMap.put("retry_type", "no_retry");
            paramsMap.put("ts", "" + time);

            if (minCursor >= 0) {
                paramsMap.put("max_cursor", minCursor + "");
            }
            if (maxCursor >= 0) {
                paramsMap.put("min_cursor", maxCursor + "");
            }

            StringBuilder paramsSb = new StringBuilder();
            for (String key : paramsMap.keySet()) {
                paramsSb.append(key + "=" + paramsMap.get(key) + "&");
            }
            String urlStr = GETDATA_JSON_URL + "?" + paramsSb.toString();
            if (urlStr.endsWith("&")) {
                urlStr = urlStr.substring(0, urlStr.length() - 1);
            }

            String as_cp = UserInfo.getUserInfo(time, urlStr, paramsAry);

            String asStr = as_cp.substring(0, as_cp.length() / 2);
            String cpStr = as_cp.substring(as_cp.length() / 2, as_cp.length());

            url = urlStr + "&as=" + asStr + "&cp=" + cpStr;

        } catch (Exception e) {
            Log.i("jw", "get url err:" + Log.getStackTraceString(e));
        }
        return url;
    }

    /**
     * 公共参数
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static HashMap<String, String> getCommonParams(Activity act) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("iid", "16715863991");
        params.put("channel", "360");
        params.put("aid", "1128");
        params.put("uuid", Utils.getDeviceUUID(act));
        params.put("openudid", "b39d9675ee6af5b2");
        params.put("app_name", "aweme");
        params.put("version_code", appVersionCode);
        params.put("version_name", appVersionName);
        params.put("ssmix", "a");
        params.put("manifest_version_code", appVersionCode);
        params.put("device_type", Utils.getDeviceName());
        params.put("device_brand", Utils.getDeviceFactory());
        params.put("os_api", Utils.getOSSDK());
        params.put("os_version", Utils.getOSRelease());
        params.put("resolution", Utils.getDeviceWidth(act) + "*" + Utils.getDeviceHeight(act));
        params.put("dpi", Utils.getDeviceDpi(act) + "");
//        params.put("device_id", "34971691517");
//        params.put("device_id", "40545321430");
        LogUtils.e(Utils.getDeviceIMEI(act));
//		params.put("ac", NetworkUtil.getNetworkType(GlobalContext.getContext()).toLowerCase());
        params.put("ac", "wifi");
        params.put("device_platform", "android");
        params.put("update_version_code", "1592");
        params.put("app_type", "normal");
        return params;
    }

}

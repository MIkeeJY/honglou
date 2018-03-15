package com.hlsp.video.utils;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.ss.android.common.applog.GlobalContext;
import com.ss.android.common.applog.UserInfo;

import java.util.HashMap;
import java.util.Map.Entry;

public class HotsoonUtils {

    public static String appVersionCode = "159";
    public static String appVersionName = "1.5.9";

    public static final String PKGNAME = "com.ss.android.ugc.live";

    private final static String GETDATA_JSON_URL = "https://hotsoon.snssdk.com/hotsoon/feed/";

    /**
     * 下拉数据规律：min_time=0
     * <p>
     * 上拉数据规律：
     * 第二次请求取第一次请求返回的json数据中的min_time字段，max_time不需要携带。
     * 第三次以及后面所有的请求都只带max_time字段，值为第一次请求返回的json数据中的max_time字段
     *
     * @return
     */
    public static String getEncryptUrl(Activity act, long minTime, long maxTime) {
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
            paramsMap.put("type", "video");
            paramsMap.put("live_sdk_version", "272");
            paramsMap.put("req_from", "enter_auto");
            paramsMap.put("ts", "" + time);
            //这里需要注意这两个字段是进行分页请求功能，大致规则如下：
            /**
             * 第一次请求，这两个字段都是0
             * 第二次请求取第一次请求返回的json数据中的min_cursor字段，max_cursor不需要携带。
             * 第三次以及后面所有的请求都只带max_cursor字段，值为第一次请求返回的json数据中的max_cursor字段。
             */

            if (maxTime >= 0) {
                paramsMap.put("max_time", "0");
            }
            if (minTime >= 0) {
                paramsMap.put("min_time", "0");
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
        params.put("device_id", "40545321430");
        params.put("ac", NetworkUtil.getNetworkType(GlobalContext.getContext()).toLowerCase());
        params.put("channel", "360");
        params.put("aid", "1128");
        params.put("app_name", "aweme");
        params.put("version_code", appVersionCode);
        params.put("version_name", appVersionName);
        params.put("device_platform", "android");
        params.put("ssmix", "a");
        params.put("device_type", Utils.getDeviceName());
        params.put("device_brand", Utils.getDeviceFactory());
        params.put("os_api", Utils.getOSSDK());
        params.put("os_version", Utils.getOSRelease());
        params.put("uuid", "863970029764198");
        params.put("openudid", "b39d9675ee6af5b2");
        params.put("manifest_version_code", appVersionCode);
        params.put("resolution", Utils.getDeviceWidth(act) + "*" + Utils.getDeviceHeight(act));
        params.put("dpi", Utils.getDeviceDpi(act) + "");
        params.put("update_version_code", "1592");
        params.put("app_type", "normal");
        return params;
    }

}

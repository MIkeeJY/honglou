package com.hlsp.video.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import com.hlsp.video.model.ConstantsValue;
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
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("iid", ConstantsValue.IID);
        paramsMap.put("channel", ConstantsValue.CHANNEL);//下载渠道
        paramsMap.put("aid", ConstantsValue.AID);
        paramsMap.put("uuid", ConstantsValue.UUID); //设备唯一号 需要运行时权限
        paramsMap.put("openudid", ConstantsValue.OPEN_UDID); //更账户绑定
        paramsMap.put("app_name", ConstantsValue.APP_NAME); //应用名称
        paramsMap.put("version_code", ConstantsValue.V_CODE);//版本号
        paramsMap.put("version_name", ConstantsValue.V_NAME);//版本名称
        paramsMap.put("ssmix", "a");
        paramsMap.put("manifest_version_code", ConstantsValue.V_CODE);//版本号
        paramsMap.put("device_type", Utils.getDeviceName());//设备类型
        paramsMap.put("device_brand", Utils.getDeviceFactory());//手机品牌
        paramsMap.put("os_api", Utils.getOSSDK());//SDK 版本号
        paramsMap.put("os_version", Utils.getOSRelease());//手机系统版本号
        paramsMap.put("resolution", Utils.getDeviceWidth(act) + "*" + Utils.getDeviceHeight(act));//分辨率
        paramsMap.put("dpi", Utils.getDeviceDpi(act) + "");//屏幕密度
        paramsMap.put("device_id", ConstantsValue.DEVICE_ID);//设备ID
//		params.put("ac", NetworkUtil.getNetworkType(GlobalContext.getContext()).toLowerCase());
        paramsMap.put("ac", "wifi");//网络类型
        paramsMap.put("device_platform", "android");//平台
        paramsMap.put("update_version_code", "1592");//更新版本号
        paramsMap.put("app_type", "normal");//应用类型
        return paramsMap;
    }

}

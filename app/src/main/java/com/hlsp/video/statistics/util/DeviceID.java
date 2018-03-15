package com.hlsp.video.statistics.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hlsp.video.App;

import java.lang.reflect.Method;
import java.util.UUID;

public class DeviceID {

    // 用户唯一标志
    public final static String PREF_KEY = "openudid";
    public final static String TIME = "time";
    //deviceid更换位置存储, 原来的位置和key与udid冲突
    public static final String COMMON_PREFS = "deviceid_prefs";
    public static final String GENERATE_UDID = "generate_udid";
    private static String TAG = DeviceID.class.getSimpleName();
    private static String _openUdid;
    private static long _time;

    public static void syncContext(Context mContext) {
        if (_openUdid == null) {
            SharedPreferences mPreferences = mContext.getSharedPreferences(COMMON_PREFS, Context.MODE_PRIVATE);
            boolean isNewUdid = mPreferences.getBoolean(GENERATE_UDID, false);
            String _keyInPref = mPreferences.getString(PREF_KEY, null);
            long _keyTime = mPreferences.getLong(TIME, 0);
            if (!isNewUdid || _keyInPref == null || _keyTime == 0) {
                _openUdid = getUniqueId(mContext);
                _time = System.currentTimeMillis();
                Editor e = mPreferences.edit();
                e.putString(PREF_KEY, _openUdid);
                e.putLong(TIME, _time);
                e.putBoolean(GENERATE_UDID, true);
                e.apply();
            } else {
                _openUdid = _keyInPref;
                _time = _keyTime;
            }
        }
    }

    public static String getOpenUDIDInContext() {
        syncContext(App.getInstance());
        return _openUdid;
    }

    public static long getTime() {
        return _time;
    }

    private static String getUniqueId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = null, androidId, macAd, serialnum = null;
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                .checkPermission(Manifest.permission.READ_PHONE_STATE,
                        context.getPackageName())) {
            tmDevice = tm.getDeviceId();
        }
        androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        macAd = wifiMan.getConnectionInfo().getMacAddress();
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) get.invoke(c, "ro.serialno", "unknown");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tmDevice == null || TextUtils.isEmpty(tmDevice) && androidId == null || TextUtils.isEmpty(androidId)
                && macAd == null || TextUtils.isEmpty(macAd) && serialnum == null || TextUtils.isEmpty(serialnum)) {

            return UUID.randomUUID().toString();
        }
        tmDevice = "IEMI:" + tmDevice;
        androidId = "ANDROID_ID:" + androidId;
        macAd = "MAC:" + macAd;
        serialnum = "SERIAL:" + serialnum;
        UUID deviceUuid = new UUID(androidId.hashCode(), tmDevice.hashCode() | macAd.hashCode() | serialnum.hashCode());
        String uniqueId = deviceUuid.toString();

        return uniqueId;
    }
}

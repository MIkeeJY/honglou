package com.hlsp.video.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.util.regex.Pattern;

/**
 * 获取设备信息
 *
 * @author Evil
 */

public class DeviceInfoUtil {

    /**
     * 获取当前网络状态
     *
     * @return NetworkInfo
     */
    public static NetworkInfo getCurrentNetStatus(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }

    /**
     * 获取网络连接状态
     *
     * @param ctx
     * @return true:有网 false：没网
     */
    public static boolean isNetworkAvailable(Context ctx) {
        NetworkInfo nki = getCurrentNetStatus(ctx);
        if (nki != null) {
            return nki.isAvailable();
        } else
            return false;
    }

    /**
     * 获取当前程序版本名
     *
     * @param context
     * @return 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取客户端版本号
     *
     * @return 版本号
     */
    public static int getVersionCode(Context c) {
        PackageInfo pi = null;
        try {
            pi = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi.versionCode;
    }

    /**
     * 获取可用存储空间大小 若存在SD卡则返回SD卡剩余空间大小 否则返回手机内存剩余空间大小
     *
     * @return
     */
    public static long getAvailableStorageSpace() {
        long externalSpace = getExternalStorageSpace();
        if (externalSpace == -1L) {
            return getInternalStorageSpace();
        }

        return externalSpace;
    }

    /**
     * 获取SD卡可用空间
     *
     * @return availableSDCardSpace 可用空间(MB)。-1L:没有SD卡
     */
    public static long getExternalStorageSpace() {
        long availableSDCardSpace = -1L;
        // 存在SD卡
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = sf.getBlockSize();// 块大小,单位byte
            long availCount = sf.getAvailableBlocks();// 可用块数量
            availableSDCardSpace = availCount * blockSize / 1024 / 1024;// 可用SD卡空间，单位MB
        }

        return availableSDCardSpace;
    }

    /**
     * 获取机器内部可用空间
     *
     * @return availableSDCardSpace 可用空间(MB)。-1L:没有SD卡
     */
    public static long getInternalStorageSpace() {
        long availableInternalSpace = -1L;

        StatFs sf = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = sf.getBlockSize();// 块大小,单位byte
        long availCount = sf.getAvailableBlocks();// 可用块数量
        availableInternalSpace = availCount * blockSize / 1024 / 1024;// 可用SD卡空间，单位MB

        return availableInternalSpace;
    }

    /**
     * 获取SD卡总空间
     *
     * @return availableSDCardSpace 总空间(MB)。-1L:没有SD卡
     */
    public static long getExternalStorageTotalSpace() {
        long availableSDCardSpace = -1L;
        // 存在SD卡
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = sf.getBlockSize();// 块大小,单位byte
            long blockCount = sf.getBlockCount();// 块总数量
            availableSDCardSpace = blockCount * blockSize / 1024 / 1024;// 总SD卡空间，单位MB
        }

        return availableSDCardSpace;
    }

    /**
     * 获取mac 地址
     *
     * @return
     */
    public static String getLocalMacAddress(Context ctx) {
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获得设备型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 获得国际移动设备身份码
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获得国际移动用户识别码
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获得设备屏幕矩形区域范围
     *
     * @return
     */
    public static Rect getScreenRect(Activity activity) {
        // 取出平屏幕的宽和高
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return new Rect(0, 0, metric.widthPixels, metric.heightPixels);
    }

    /**
     * 获得设备屏幕密度
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        return metrics.density;
    }

    /**
     * 获得系统版本
     */
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    public static int getSDKVersionInt() {
        return Build.VERSION.SDK_INT;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    // Get the Location by GPS or WIFI
    public static Location getLocation(Context context) {
        try {
            LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            return location;
        } catch (Exception e) {
        }
        return null;
    }

    public static String getTelNumber(Context ctx) {
        return ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();

    }

    // 启动安装界面
//    public static void installFuction(final Context ctx, final Button confirmBtn) {
//        String setMkdir = FileUtil.setMkdir(ctx);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(new File(setMkdir)), "application/vnd.android.package-archive");
//        ctx.startActivity(intent);
//
//        confirmBtn.setText("安装");
//        confirmBtn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                DeviceInfoUtil.installFuction(ctx, confirmBtn);
//            }
//        });
//    }

    /**
     * 是否是手機格式
     *
     * @return boolean true为是
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null) {
            return false;
        }
        boolean flag = false;
        StringBuffer sb = new StringBuffer();
        sb.append("^(13[0-9]|170|168|176|177|178|101|15[0-9]|18[0-9]|14[0-9]|100)\\d{8}$");
        Pattern pt = Pattern.compile(sb.toString());
        if (pt.matcher(mobile).matches()) {
            flag = true;
        }
        return flag;
    }


}

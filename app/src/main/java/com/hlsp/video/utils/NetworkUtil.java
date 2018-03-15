package com.hlsp.video.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;


public class NetworkUtil {

    public static boolean isMobileNetworkAvailable(Context context) {
        if (isNetworkAvailable(context)) {
            if (isWifiNetworkAvailable(context))
                return false;
            return true;
        }
        return false;
    }

	@SuppressWarnings("deprecation")
	public static boolean isWifiNetworkAvailable(Context context) {
        ConnectivityManager conmgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conmgr == null) {
            return false;
        }
        NetworkInfo info = null;
        try {
            info = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        State wifi = info.getState(); // 显示wifi连接状态
        if (wifi == State.CONNECTED || wifi == State.CONNECTING)
            return true;

        return false;
    }

    /**
     * 是否有网络可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkUp(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    return true;
                }
            } else if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否有wifi可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiNetworkUp(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前移动网络可用
     *
     * @param context
     * @return
     */
    public static NetworkInfo getNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.isConnected()) {
                return ni;
            }
        }
        return null;
    }

    /**
     * 是否有移动网络可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileNetworkUp(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    //判断网络是否存在
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                try {
                    if (cm.getActiveNetworkInfo() != null) {
                        if (cm.getActiveNetworkInfo().isAvailable()) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }

            }
            return false;
        }
        return false;
    }

    /**
     * 判断网络是否可用
     *
     * @return true:可用 false:不可用
     */
    public static boolean isNetworkActive(Context context) {
        if (context == null)
            return false;
        boolean bReturn = false;
        ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = null;
        try {
            netInfo = conManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (netInfo != null && netInfo.isConnected()) {
            bReturn = true;
        }
        return bReturn;
    }
    
    public static String getNetworkType(Context ctx){
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                strNetworkType = "WIFI";
            }else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || 
                        		_strSubTypeName.equalsIgnoreCase("WCDMA") ||
                        		_strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        }else{
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                 }
            }
        }
        return strNetworkType;
    }

}

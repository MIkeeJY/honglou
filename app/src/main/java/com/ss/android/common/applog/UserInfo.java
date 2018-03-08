package com.ss.android.common.applog;

/**
 * 抖音，火山so对应的native类
 * @author jiangwei1-g
 *
 */
public class UserInfo {
	
    public static native void getPackage(String str);

    public static native String getUserInfo(int i, String str, String[] strArr);

    public static native int initUser(String str);

    public static native void setAppId(int i);
    
}

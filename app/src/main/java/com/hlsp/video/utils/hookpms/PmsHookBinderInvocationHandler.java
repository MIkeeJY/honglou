package com.hlsp.video.utils.hookpms;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by jiangwei1-g on 2016/9/7.
 */
public class PmsHookBinderInvocationHandler implements InvocationHandler {

    private Object base;

    private String SIGN;

    public PmsHookBinderInvocationHandler(Object base, String sign) {
        try {
            this.base = base;
            this.SIGN = sign;
        } catch (Exception e) {
            Log.d("jw", "error:" + Log.getStackTraceString(e));
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if ("getPackageInfo".equals(method.getName())) {
                String pkgName = (String) args[0];
                Integer flag = (Integer) args[1];
                if (flag == PackageManager.GET_SIGNATURES && "com.hlsp.video".equals(pkgName)) {
                    Signature sign = new Signature(SIGN);
                    PackageInfo info = (PackageInfo) method.invoke(base, args);
                    info.signatures[0] = sign;
                    return info;
                }
            }
        } catch (Exception e) {
        }
        return method.invoke(base, args);
    }

}


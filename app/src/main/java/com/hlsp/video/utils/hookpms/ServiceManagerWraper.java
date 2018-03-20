package com.hlsp.video.utils.hookpms;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by jiangwei1-g on 2016/9/7.
 */
public class ServiceManagerWraper {
	
    public static void hookPMS(Context context, String signed){
        try{
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod =
            		activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(
                    iPackageManagerInterface.getClassLoader(),
                    new Class<?>[] { iPackageManagerInterface },
                    new PmsHookBinderInvocationHandler(sPackageManager, signed));
            sPackageManagerField.set(currentActivityThread, proxy);
            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);
        }catch (Exception e){
            Log.d("jw", "hook pms error:"+ Log.getStackTraceString(e));
        }
    }
    
    public static void hookPMS(Context context){
    	String douyin_signStr = "308203563082023ea00302010202044efec96a300d06092a864886f70d0101050500306d310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e6731123010060355040a13094279746544616e636531123010060355040b13094279746544616e636531123010060355040313094d6963726f2043616f301e170d3131313233313038333535345a170d3339303531383038333535345a306d310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e6731123010060355040a13094279746544616e636531123010060355040b13094279746544616e636531123010060355040313094d6963726f2043616f30820122300d06092a864886f70d01010105000382010f003082010a0282010100a46d108be827bff2c1ac7ad986c463b8cda9f0e7ddc21295af55bd16f7bfabb36fa33b72a8e76f5a59b48b29cb6e34c38d065589636dd120f39346c37b3753830422cc0c84243fdf0e28d3e5970dcd641c70c9e2e3ec66ac14afd351abb59d6885370e16b64bbfb28fbb234dffe25f5cfb6680c84121770cf3a177bc8a28b78b7c86d30a61eb67b9fbfd92e0c8fc5eb8346a238ddfe08522f091c622789932d9debe6910b4b903d02e5f6ded69f5c13a5d1742dac21050dfbb5f4ea615028d7a8642e4a93e075cf8f0e33a4a654af11f4f9a4905d917f0bbb84e63a1a2e90b8997f936e5bf5a75ea6d19d1d93d2677886e59e95c0bb33505363c05e10a389d0b0203010001300d06092a864886f70d010105050003820101008704e53758907db6785bec65c5f51af050873c4b0a5e08f90191b901c59969ce537942dbc9307f8fcc23b1c281a66fe46136890564f89fb16839ac69f836a9ea074eb03da8578330ab50b185bd6916f195a67036060a0bbf2aed06990e72bc4dede895ae5e695371aa4ad26efcd44b65891bda9ce02d9e71548592c2951e2cb62ed4408eec7e828ce573ffba0458341aef25957b2a76403da091322eb845b6a9903fe6aed1434012d483f1c668e2468ce129815e18283baa5e1c4209691b36ffa86506ff6a4b83f24faa744383b75968046c69703d2c5df38bad6920d9122cb1f7c78e8bfe283870359c053115e2ba0a7a03c9656a2f5a2d81f6a6fad5db2cd7";
    	hookPMS(context, douyin_signStr);
    }
    
}

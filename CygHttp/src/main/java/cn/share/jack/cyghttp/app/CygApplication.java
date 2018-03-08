package cn.share.jack.cyghttp.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by jack on 2017/6/13
 * 全局唯一的Application实例，只要继承MyApplication就可以了，MyApplication会把this存到单实例引用
 */

public class CygApplication extends Application {

    private static CygApplication sInstance;
    /**
     * 主线程ID
     */
    public static int mMainThreadId = -1;
    /**
     * 主线程ID
     */
    public static Thread mMainThread;
    /**
     * 主线程Handler
     */
    public static Handler mMainThreadHandler;
    /**
     * 主线程Looper
     */
    public static Looper mMainLooper;


    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程的looper
     */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static CygApplication getInstance() {
        return sInstance;
    }

    protected CygApplication() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
    }
}
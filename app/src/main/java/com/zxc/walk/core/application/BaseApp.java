package com.zxc.walk.core.application;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

/**
 * Application基类，不包含任何业务相关代码
 *
 * @author wp
 */
public class BaseApp extends Application {

    private static BaseApp mApplication;
    private static Handler mMainThreadHandler;
    private static Looper mMainThreadLooper;
    private static Thread mMainThread;
    private static int mMainThreadId;


    /**
     * 应用内所有Activity的生命周期回调
     */
    private AppActivityLifecycleCallbacks mAppActivityLifecycleCallbacks;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppActivityLifecycleCallbacks = new AppActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks(mAppActivityLifecycleCallbacks);

        mApplication = this;
        mMainThreadHandler = new Handler();
        mMainThreadLooper = this.getMainLooper();
        mMainThread = Thread.currentThread();
        mMainThreadId = Process.myTid();
    }

    /**
     * 应用内所有Activity的生命周期回调
     *
     * @return
     */
    public AppActivityLifecycleCallbacks getAppActivityLifecycleCallbacks() {
        if (mAppActivityLifecycleCallbacks == null) {
            mAppActivityLifecycleCallbacks = new AppActivityLifecycleCallbacks();
            registerActivityLifecycleCallbacks(mAppActivityLifecycleCallbacks);
        }
        return mAppActivityLifecycleCallbacks;
    }

    public static BaseApp getApplication() {
        return mApplication;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }
}


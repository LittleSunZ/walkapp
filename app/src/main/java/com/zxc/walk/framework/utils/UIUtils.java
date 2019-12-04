package com.zxc.walk.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.zxc.walk.core.application.BaseApp;
import com.zxc.walk.ui.widget.CustomToast;

/**
 * @author xyu
 * @date 2018/11/27
 * UI方面常用的方法工具类
 */
public class UIUtils {
    private UIUtils() {
    }

    /**
     * 直接获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApp.getApplication();
    }

    /**
     * 获取前台显示的Activity
     *
     * @return
     */
    public static Activity getForegroundActivity() {
        return BaseApp.getApplication().getAppActivityLifecycleCallbacks().getForegroundActivity();
    }

    /*---------  Handler和主线程相关----------*/
    public static Thread getMainThread() {
        return BaseApp.getMainThread();
    }

    public static long getMainThreadId() {
        return (long) BaseApp.getMainThreadId();
    }

    public static Handler getHandler() {
        return BaseApp.getMainThreadHandler();
    }

    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public static boolean isRunInMainThread() {
        return (long) Process.myTid() == getMainThreadId();
    }
    /*---------  Handler和主线程相关   end----------*/

    public static View inflate(Context context, int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static int getIdentifier(String name, String defType) {
        return getResources().getIdentifier(name, defType, getContext().getPackageName());
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }
    /*---------  资源相关   end----------*/


    public static void toast(final CharSequence str) {
        if (isRunInMainThread()) {
            showToast(str);
        } else {
            post(() -> showToast(str));
        }
    }

    private static void showToast(CharSequence str) {
        //只有App处于前台才吐司
        if (BaseApp.getApplication().getAppActivityLifecycleCallbacks().isApplicationInForeground()
                && !TextUtils.isEmpty(str)) {
            CustomToast.showToast(getContext(), str, Toast.LENGTH_SHORT);
        }
    }
}

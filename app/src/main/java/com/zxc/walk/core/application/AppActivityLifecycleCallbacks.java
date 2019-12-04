package com.zxc.walk.core.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.zxc.walk.framework.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 应用内所有Activity的生命周期回调
 *
 * @author wp
 */

public class AppActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    /**
     * 目前处于已START状态Activity的数量
     */
    private int startCount;
    /**
     * 当前可交互的Activity(处于Resume状态）
     */
    private Activity foregroundActivity;
    /**
     * 当前最顶层的Activity(最近处于Resume状态）
     */
    private Activity topActivity;

    private List<Activity> activities = new ArrayList<>();


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        topActivity = activity;
        activities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (startCount <= 0) {
            onAppForeGround(activity);
        }
        ++startCount;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        topActivity = activity;
        foregroundActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        foregroundActivity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        --startCount;
        if (startCount <= 0) {
            onAppBackGround(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activities.remove(activity);
    }

    public boolean isApplicationInForeground() {
        return startCount > 0;
    }

    /**
     * 应用切入后台的监听
     *
     * @param activity
     */
    private void onAppBackGround(Activity activity) {
        notifyAppState(true);
    }

    /**
     * 应用回到前台的监听
     *
     * @param activity
     */
    private void onAppForeGround(Activity activity) {
        notifyAppState(false);
    }

    public Activity getForegroundActivity() {
        return foregroundActivity;
    }

    public Activity getTopActivity() {
        return topActivity;
    }

    private List<AppStateListener> mAppStateListeners;

    public void addAppStateListener(AppStateListener appStateListener) {
        if (mAppStateListeners == null) {
            mAppStateListeners = new LinkedList<>();
        }
        mAppStateListeners.add(appStateListener);
    }

    public void removeAppStateListener(AppStateListener appStateListener) {
        if (ArrayUtils.isEmpty(mAppStateListeners)) {
            return;
        }
        mAppStateListeners.remove(appStateListener);
    }

    /**
     * 广播事件
     *
     * @param background
     */
    public void notifyAppState(boolean background) {
        if (ArrayUtils.isEmpty(mAppStateListeners)) {
            return;
        }
        for (AppStateListener appStateListener : mAppStateListeners) {
            appStateListener.onAppBackGround(background);
        }
    }


    public interface AppStateListener {
        /**
         * @param background 是否切入后台
         *                   App处于前后台监听
         */
        void onAppBackGround(boolean background);
    }


}

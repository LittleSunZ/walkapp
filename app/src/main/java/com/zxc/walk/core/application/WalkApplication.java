package com.zxc.walk.core.application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class WalkApplication extends BaseApp {

    public static WalkApplication ourApplication;
    private ZlActivityLifecycleCallbacks mZlActivityLifecycleCallbacks;
    private String uploadToken;
    public static WalkApplication Instance() {
        if (ourApplication == null) {
            synchronized (WalkApplication.class) {
                if (ourApplication == null) {
                    ourApplication = new WalkApplication();
                }
            }
        }
        return ourApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourApplication = this;
        mZlActivityLifecycleCallbacks = new ZlActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks(mZlActivityLifecycleCallbacks);
        Fresco.initialize(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public ZlActivityLifecycleCallbacks getZlActivityLifecycleCallbacks() {
        return mZlActivityLifecycleCallbacks;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }
}

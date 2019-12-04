package com.zxc.walk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.zxc.walk.MainActivity;
import com.zxc.walk.R;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.ui.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        myCountDownTimer = new MyCountDownTimer(3000, 1000);
        myCountDownTimer.start();
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            SplashActivity.this.finish();
            if (!TextUtils.isEmpty(userInfo.getUserid())) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

        }
    }
}

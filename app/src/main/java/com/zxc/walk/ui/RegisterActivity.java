package com.zxc.walk.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zxc.walk.R;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.LogUtil;
import com.zxc.walk.framework.utils.PhoneInfoTools;
import com.zxc.walk.ui.base.TitleBarActivity;
import com.zxc.walk.ui.widget.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class RegisterActivity extends TitleBarActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_getcode)
    TextView tvGetCode;
    @BindView(R.id.et_invted)
    EditText etInvted;
    private MyCountDownTimer myCountDownTimer;
    private String smsCode = "";

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_register);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("注册");
    }

    @OnClick({R.id.tv_register, R.id.tv_getcode})
    public void onViewClicked(View view) {
        String phone = "";
        switch (view.getId()) {
            case R.id.tv_getcode:
                if (myCountDownTimer == null) {
                    myCountDownTimer = new MyCountDownTimer(60000, 1000);
                }
                phone = etPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    CustomToast.showToast(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (!PhoneInfoTools.isMobilePhone(phone)) {
                    CustomToast.showToast(RegisterActivity.this, "手机号格式错误", Toast.LENGTH_SHORT);
                    return;
                }
                getSmsCode(phone);
                break;
            case R.id.tv_register:
                phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                String code = etCode.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    CustomToast.showToast(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (!PhoneInfoTools.isMobilePhone(phone)) {
                    CustomToast.showToast(RegisterActivity.this, "手机号格式错误", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    CustomToast.showToast(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    CustomToast.showToast(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (password.length() < 6) {
                    CustomToast.showToast(RegisterActivity.this, "密码最少要6位数", Toast.LENGTH_SHORT);
                    return;
                }
                if (!code.equals(smsCode)) {
                    CustomToast.showToast(RegisterActivity.this, "验证码输入错误", Toast.LENGTH_SHORT);
                    return;
                }
                showProgressDialog("");
                Observable<Result<UserInfo>> dutyInfo = walkApiReq.regsiter(phone, password, etInvted.getText().toString());
                DataCallBack<UserInfo> dataCallBack = new DataCallBack<UserInfo>() {
                    @Override
                    protected void onSuccessData(UserInfo message) {
                        CustomToast.showToast(RegisterActivity.this, "注册成功!", Toast.LENGTH_SHORT);
                        finish();
                    }

                    @Override
                    protected void onEnd() {
                        super.onEnd();
                        dismissProgressDialog();
                    }
                };
                HttpUtils.invoke(this, dutyInfo, dataCallBack);
                break;
        }
    }

    private void getSmsCode(String phone) {
        showProgressDialog("");
        Observable<Result<String>> reqUserInfo = walkApiReq.getSmsCode(phone);
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
                smsCode = message;
                LogUtil.e("getSmsCode = " + message);
                //发送验证码成功 开始60S倒计时
                //开启倒计时
                myCountDownTimer.start();
            }

            @Override
            protected void onEnd() {
                super.onEnd();
                dismissProgressDialog();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            tvGetCode.setClickable(false);
            tvGetCode.setText(l / 1000 + "s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            tvGetCode.setText("重新获取");
            //设置可点击
            tvGetCode.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }
    }
}

package com.zxc.walk.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
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

public class UpdatePsdActivity extends TitleBarActivity {

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
    @BindView(R.id.et_password2)
    EditText etPassword2;
    private MyCountDownTimer myCountDownTimer;

    private int type;
    private String smsCode = "";

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_update_psd);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            setTvTitle("修改登录密码");
        } else if (type == 1) {
            setTvTitle("修改支付密码");
        } else if (type == 2) {
            setTvTitle("找回密码");
        }
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserid())) {
            etPhone.setText(PhoneInfoTools.phoneNumerParserFourStar(userInfo.getPhone()));
            etPhone.setInputType(InputType.TYPE_NULL);
        }
    }

    @OnClick({R.id.tv_register, R.id.tv_getcode})
    public void onViewClicked(View view) {
        String phone;
        switch (view.getId()) {
            case R.id.tv_getcode:
                if (myCountDownTimer == null) {
                    myCountDownTimer = new MyCountDownTimer(60000, 1000);
                }
                phone = etPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    CustomToast.showToast(UpdatePsdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (!PhoneInfoTools.isMobilePhone(phone)) {
                    CustomToast.showToast(UpdatePsdActivity.this, "手机号格式错误", Toast.LENGTH_SHORT);
                    return;
                }
                getSmsCode(phone);
                break;
            case R.id.tv_register:
                String password = etPassword.getText().toString();
                String password2 = etPassword2.getText().toString();
                String code = etCode.getText().toString();
                phone = etPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    CustomToast.showToast(UpdatePsdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (!PhoneInfoTools.isMobilePhone(phone)) {
                    CustomToast.showToast(UpdatePsdActivity.this, "手机号格式错误", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    CustomToast.showToast(UpdatePsdActivity.this, "密码不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (!password2.equals(password)) {
                    CustomToast.showToast(UpdatePsdActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    CustomToast.showToast(UpdatePsdActivity.this, "验证码不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (!code.equals(smsCode)) {
                    CustomToast.showToast(UpdatePsdActivity.this, "验证码输入错误", Toast.LENGTH_SHORT);
                    return;
                }
                showProgressDialog("");
                UserInfo nuserInfo = new UserInfo();
                if (type == 0 || type == 1) {
                    nuserInfo.setUserid(userInfo.getUserid());
                    if (type == 0) {
                        nuserInfo.setPassword(password);
                    } else {
                        nuserInfo.setPaypassword(password);
                    }
                } else if (type == 2) {
                    nuserInfo.setPhone(phone);
                    nuserInfo.setPassword(password);
                }
                Observable<Result<String>> userinfoReq = null;
                if (type == 0 || type == 1) {
                    userinfoReq = walkApiReq.updateUserInfo(nuserInfo);
                } else if (type == 2) {
                    userinfoReq = walkApiReq.updatePsdByPhone(nuserInfo);
                }
                DataCallBack<String> dataCallBack = new DataCallBack<String>() {
                    @Override
                    protected void onSuccessData(String message) {
                        CustomToast.showToast(UpdatePsdActivity.this, "修改成功!", Toast.LENGTH_SHORT);
                        finish();
                    }

                    @Override
                    protected void onEnd() {
                        super.onEnd();
                        dismissProgressDialog();
                    }
                };
                HttpUtils.invoke(this, userinfoReq, dataCallBack);
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

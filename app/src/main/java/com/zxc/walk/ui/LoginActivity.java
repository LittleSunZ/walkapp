package com.zxc.walk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxc.walk.MainActivity;
import com.zxc.walk.R;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.AtyContainer;
import com.zxc.walk.ui.base.BaseActivity;
import com.zxc.walk.ui.widget.CustomToast;

import butterknife.BindView;
import io.reactivex.Observable;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_psd)
    TextView tvForgetPsd;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_service)
    ImageView ivService;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setListener() {
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        ivService.setOnClickListener(this);
        tvForgetPsd.setOnClickListener(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    CustomToast.showToast(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    CustomToast.showToast(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                showProgressDialog("");
                Observable<Result<UserInfo>> dutyInfo = walkApiReq.login(phone, password);
                DataCallBack<UserInfo> dataCallBack = new DataCallBack<UserInfo>() {
                    @Override
                    protected void onSuccessData(UserInfo message) {
                        CustomToast.showToast(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT);
                        userSp.setUserInfo(message);
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    @Override
                    protected void onEnd() {
                        super.onEnd();
                        dismissProgressDialog();
                    }
                };
                HttpUtils.invoke(this, dutyInfo, dataCallBack);

                break;
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.iv_service:
                startActivity(new Intent(LoginActivity.this, ServiceActivity.class));
                break;
            case R.id.tv_forget_psd:
                Intent intent = new Intent(LoginActivity.this, UpdatePsdActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AtyContainer.getInstance().finishAllActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

package com.zxc.walk.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zxc.walk.R;

import androidx.annotation.NonNull;

public class PayDialog extends Dialog {

    private View tvCancel;
    private View tvSure;
    private EditText etPassword;
    private OnPayDialogClickListen onPayDialogClickListen;

    public PayDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_pay);
        setCanceledOnTouchOutside(false);
        tvCancel = findViewById(R.id.tv_cancel);
        tvSure = findViewById(R.id.tv_sure);
        etPassword = findViewById(R.id.et_password);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPayDialogClickListen != null) {
                    onPayDialogClickListen.onPayCancel();
                }
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    CustomToast.showToast(context, "请输入支付密码!", Toast.LENGTH_SHORT);
                    return;
                }
                if (onPayDialogClickListen != null) {
                    onPayDialogClickListen.onPay(etPassword.getText().toString());
                }
            }
        });
    }

    public void setOnPayDialogClickListen(OnPayDialogClickListen onPayDialogClickListen) {
        this.onPayDialogClickListen = onPayDialogClickListen;
    }

    public interface OnPayDialogClickListen {
        void onPay(String password);

        void onPayCancel();
    }

    public void setCancelHide() {
        tvCancel.setVisibility(View.GONE);
    }
}

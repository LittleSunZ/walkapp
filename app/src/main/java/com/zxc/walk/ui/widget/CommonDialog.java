package com.zxc.walk.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zxc.walk.R;


/**
 * 通用提醒dialog
 *
 * @author zxc
 */

public class CommonDialog extends Dialog {

    private TextView tvMsg;
    private View tvCancel;
    private View tvSure;
    private onCommonDialogClickListen onCommonDialogClickListen;

    public CommonDialog(@NonNull Context context, String msg) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_common);

        tvMsg = findViewById(R.id.tv_msg);
        tvCancel = findViewById(R.id.tv_cancel);
        tvSure = findViewById(R.id.tv_sure);
        if (msg != null) {
            tvMsg.setText(msg);
        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCommonDialogClickListen != null) {
                    onCommonDialogClickListen.onSure();
                }
            }
        });
    }

    public void setOnCommonDialogClickListen(CommonDialog.onCommonDialogClickListen onCommonDialogClickListen) {
        this.onCommonDialogClickListen = onCommonDialogClickListen;
    }

    public interface onCommonDialogClickListen {
        void onSure();
    }
    public void setCancelHide(){
        tvCancel.setVisibility(View.GONE);
    }
}

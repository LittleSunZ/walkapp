package com.zxc.walk.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zxc.walk.R;


public class CommonProgressDialog extends Dialog {


    private TextView mTextViewLoadingText;

    public CommonProgressDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialogfragment_common_progress);
        mTextViewLoadingText = findViewById(R.id.textview_dialogfragment_loadingtext);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(String msg){
        mTextViewLoadingText.setText(msg);
    }

}
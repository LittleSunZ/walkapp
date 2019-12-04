package com.zxc.walk.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zxc.walk.R;


/**
 * 选择图片dialog
 *
 * @author zxc
 */

public class SelectPhotoDialog extends Dialog {

    private TextView tvCamera;
    private TextView tvAlbum;
    private TextView cancelLayout;
    private Activity context;
    private OnSelectClickLisnter lisnter;
    public SelectPhotoDialog(@NonNull Context context) {
        super(context, R.style.BottomInAndOutStyle);
        setContentView(R.layout.dialog_selectphoto);
        this.context = (Activity) context;

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = dm.widthPixels;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.BottomInAndOutStyle;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);

        init();
    }

    public void init() {
        tvCamera = findViewById(R.id.tv_camera);
        tvAlbum = findViewById( R.id.tv_album);
        cancelLayout = findViewById(R.id.cancel_layout);

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lisnter!=null){
                    lisnter.onFromCamera();
                }
            }
        });
        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lisnter!=null){
                    lisnter.onFromGallery();
                }
            }
        });
    }

    public interface OnSelectClickLisnter {
        /***
         * 点击拍照
         */
        void onFromCamera();

        /***
         * 点击相册
         */
        void onFromGallery();
    }

    public void setLisnter(OnSelectClickLisnter lisnter) {
        this.lisnter = lisnter;
    }
}

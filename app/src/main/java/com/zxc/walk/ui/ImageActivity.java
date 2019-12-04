package com.zxc.walk.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zxc.walk.R;
import com.zxc.walk.framework.utils.AppUtils;
import com.zxc.walk.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends BaseActivity {
    @BindView(R.id.image)
    ImageView image;

    private String imageUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    protected void setListener() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        imageUrl = getIntent().getStringExtra("imageUrl");
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(ImageActivity.this).load(imageUrl).into(image);
        }
    }

}

package com.zxc.walk.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zxc.walk.R;
import com.zxc.walk.framework.utils.AppUtils;
import com.zxc.walk.request.Constants;
import com.zxc.walk.ui.base.BaseActivity;
import com.zxc.walk.ui.widget.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class InviteActivity extends BaseActivity {
    @BindView(R.id.image_head)
    CircleImageView imageHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_share)
    TextView tvShare;

    private String url = Constants.IMAGE_HOST + "share_code.png";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    protected void setListener() {
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder((InviteActivity.this)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dialog.setMessage("请截图保存并分享给好友");
                dialog.show();
            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Glide.with(this).load(url).into(ivCode);
        tvId.setText("推广码：" + userInfo.getUserid());
        tvNickname.setText("我是" + userInfo.getNickname());
        if (!TextUtils.isEmpty(userInfo.getHeadimage())) {
            Glide.with(InviteActivity.this).load(userInfo.getHeadimage()).skipMemoryCache(true).into(imageHead);
        }
    }
}

package com.zxc.walk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zxc.walk.R;
import com.zxc.walk.framework.utils.AppUtils;
import com.zxc.walk.ui.base.TitleBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends TitleBarActivity {
    @BindView(R.id.tv_inviteId)
    TextView tvInviteId;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_setting);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("设置");
        tvInviteId.setText("我的推荐人：" + (TextUtils.isEmpty(userInfo.getSuserid()) ? "无" : userInfo.getSuserid()));
        tvVersion.setText("版本号：v" + AppUtils.getLocalVersionName(this));
    }


    @OnClick({R.id.login_layout, R.id.tv_logout, R.id.deal_layout})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_logout:
                userSp.removeUserInfo();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                break;
            case R.id.login_layout:
                intent = new Intent(SettingActivity.this, UpdatePsdActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.deal_layout:
                intent = new Intent(SettingActivity.this, UpdatePsdActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
        }
    }
}

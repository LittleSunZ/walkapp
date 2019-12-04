package com.zxc.walk.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxc.walk.R;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.AppUtils;
import com.zxc.walk.ui.AddressActivity;
import com.zxc.walk.ui.BusinessActivity;
import com.zxc.walk.ui.DealActivity;
import com.zxc.walk.ui.InviteActivity;
import com.zxc.walk.ui.RuleActivity;
import com.zxc.walk.ui.ServiceActivity;
import com.zxc.walk.ui.SettingActivity;
import com.zxc.walk.ui.TeamActivity;
import com.zxc.walk.ui.UserInfoActivity;
import com.zxc.walk.ui.VipActivity;
import com.zxc.walk.ui.base.BaseFragment;

import butterknife.BindView;
import io.reactivex.Observable;

public class MyFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.deal_layout)
    LinearLayout dealLayout;
    @BindView(R.id.vip_layout)
    LinearLayout vipLayout;
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.address_layout)
    LinearLayout addressLayout;
    @BindView(R.id.business_layout)
    LinearLayout businessLayout;
    @BindView(R.id.tv_setting)
    ImageView tvSetting;
    @BindView(R.id.iv_service)
    ImageView ivService;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_userid)
    TextView tvUserid;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_huoyue)
    TextView tvHuoyue;
    @BindView(R.id.tv_yulu)
    TextView tvYulu;
    @BindView(R.id.team_layout)
    LinearLayout teamLayout;
    @BindView(R.id.layout_invite)
    LinearLayout layoutInvite;
    @BindView(R.id.userinfo_layout)
    FrameLayout userinfoLayout;
    @BindView(R.id.tv_rule)
    TextView tvRule;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void setListener() {
        dealLayout.setOnClickListener(this);
        vipLayout.setOnClickListener(this);
        userinfoLayout.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        businessLayout.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        ivService.setOnClickListener(this);
        teamLayout.setOnClickListener(this);
        layoutInvite.setOnClickListener(this);
        tvRule.setOnClickListener(this);
    }

    @Override
    protected void init() {
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    public void getUserInfo() {
        if (userInfo == null) {
            return;
        }
        Observable<Result<UserInfo>> reqUserInfo = walkApiReq.getUserInfoImage(userInfo.getUserid());
        DataCallBack<UserInfo> dataCallBack = new DataCallBack<UserInfo>() {
            @Override
            protected void onSuccessData(UserInfo message) {
                userInfo = message;
                if (message != null) {
                    userSp.setUserInfo(message);
                    tvPhone.setText(message.getNickname());
                    tvUserid.setText("用户ID：" + message.getUserid());
                    tvYulu.setText(message.getCandynumber() + "");
                    tvLevel.setText("LV " + message.getViplevel());
                    tvHuoyue.setText(message.getActivitylevel() + "");
                    if (!TextUtils.isEmpty(userInfo.getHeadimage())) {
                        Glide.with(getActivity()).load(message.getHeadimage()).into(imageHead);
                    }
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deal_layout:
                startActivity(new Intent(getActivity(), DealActivity.class));
                break;
            case R.id.vip_layout:
                startActivity(new Intent(getActivity(), VipActivity.class));
                break;
            case R.id.userinfo_layout:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.address_layout:
                startActivity(new Intent(getActivity(), AddressActivity.class));
                break;
            case R.id.business_layout:
                startActivity(new Intent(getActivity(), BusinessActivity.class));
                break;
            case R.id.tv_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.iv_service:
                startActivity(new Intent(getActivity(), ServiceActivity.class));
                break;
            case R.id.team_layout:
                startActivity(new Intent(getActivity(), TeamActivity.class));
                break;
            case R.id.tv_rule:
                startActivity(new Intent(getActivity(), RuleActivity.class));
                break;
            case R.id.layout_invite:
                startActivity(new Intent(getActivity(), InviteActivity.class));
                break;
        }
    }
}

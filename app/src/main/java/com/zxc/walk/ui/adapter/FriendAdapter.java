package com.zxc.walk.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxc.walk.R;
import com.zxc.walk.entity.Friend;

import androidx.annotation.NonNull;

public class FriendAdapter extends BaseQuickAdapter<Friend, BaseViewHolder> {

    int type;

    public FriendAdapter(int type) {
        super(R.layout.adapter_friend_item);
        this.type = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Friend item) {
        switch (type) {
            case 0:
                helper.setText(R.id.tv_phone, "好友ID：" + item.getFuserid());
                break;
            case 1:
                helper.setText(R.id.tv_phone, "好友ID：" + item.getSuserid());
                break;
            case 2:
                helper.setText(R.id.tv_phone, "好友ID：" + item.getTuserid());
                break;
            default:
                break;
        }

    }
}

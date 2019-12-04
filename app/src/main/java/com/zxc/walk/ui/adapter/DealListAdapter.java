package com.zxc.walk.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxc.walk.R;
import com.zxc.walk.entity.Transaction;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.utils.UserSp;

public class DealListAdapter extends BaseQuickAdapter<Transaction, BaseViewHolder> {
    UserInfo userInfo;
    OnDealClickListener onDealClickListener;

    public DealListAdapter(Context context) {
        super(R.layout.adapter_deal_item);
        UserSp userSp = new UserSp(context);
        userInfo = userSp.getUserInfo();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Transaction item) {
        View dealLayout = helper.getView(R.id.deal_layout);
        helper.setText(R.id.tv_id, "订单编号：" + item.getId());
        helper.setText(R.id.tv_number, "交易数量：" + item.getNumber() + " 枚");
        helper.setText(R.id.tv_price, "交易单价：￥" + item.getPrice());
        TextView button = helper.getView(R.id.tv_button);
        if (item.getStatus() == 0) {
            button.setText("取消");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDealClickListener != null) {
                        onDealClickListener.onCancel(item);
                    }
                }
            });
        } else if (item.getStatus() == 3) {
            button.setText("已完成");
        } else if (item.getStatus() == 4) {
            button.setText("已取消");
        } else {
            button.setText("查看");
            dealLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDealClickListener != null) {
                        onDealClickListener.onGoDetail(item);
                    }
                }
            });
        }

    }

    public interface OnDealClickListener {
        void onGoDetail(Transaction item);

        void onCancel(Transaction item);

        void onSure(Transaction item);
    }

    public void setOnDealClickListener(OnDealClickListener onDealClickListener) {
        this.onDealClickListener = onDealClickListener;
    }
}
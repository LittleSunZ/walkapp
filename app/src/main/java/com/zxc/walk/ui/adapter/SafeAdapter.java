package com.zxc.walk.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxc.walk.R;
import com.zxc.walk.entity.Transaction;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.utils.PhoneInfoTools;
import com.zxc.walk.framework.utils.UserSp;
import com.zxc.walk.ui.widget.CustomToast;

import java.text.DecimalFormat;

public class SafeAdapter extends BaseQuickAdapter<Transaction, BaseViewHolder> {

    private Context context;
    private UserInfo userInfo;
    OnDealClickListener onDealClickListener;
    UserSp userSp;
    public SafeAdapter(Context context) {
        super(R.layout.adapter_safe_item);
        this.context = context;
        userSp = new UserSp(context);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Transaction item) {
        DecimalFormat df = new DecimalFormat("0.00");
        helper.setText(R.id.tv_phone, PhoneInfoTools.phoneNumerParserFourStar(item.getPhone()));
        helper.setText(R.id.tv_price, "单价：￥" + df.format(item.getPrice()));
        helper.setText(R.id.tv_number, "数量：" + item.getNumber());

        helper.getView(R.id.tv_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo = userSp.getUserInfo();
//                if (userInfo.getViplevel() == 0) {
//                    CustomToast.showToast(context, "VIP0 不能购买!", Toast.LENGTH_SHORT);
//                    return;
//                }
                if (TextUtils.isEmpty(userInfo.getName())
                        || TextUtils.isEmpty(userInfo.getCardid())
                        || TextUtils.isEmpty(userInfo.getBankname())
                        || TextUtils.isEmpty(userInfo.getCardnum())) {
                    CustomToast.showToast(context, "个人信息未完善，请去完善后再来交易!", Toast.LENGTH_SHORT);
                    return;
                }
                AlertDialog dialog = new AlertDialog.Builder(context).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (userInfo.getUserid().equals(item.getUserid())) {
                            CustomToast.showToast(context, "不能购买自己的订单!!", Toast.LENGTH_SHORT);
                            return;
                        }
                        if (onDealClickListener != null) {
                            onDealClickListener.onBuy(item);
                        }
                    }
                }).create();
                dialog.setTitle("提示");
                dialog.setMessage("是否确定购买" + item.getNumber() + "个雨露，单价￥" + item.getPrice());
                dialog.show();
            }
        });
    }

    public interface OnDealClickListener {
        void onBuy(Transaction transaction);
    }

    public void setOnDealClickListener(OnDealClickListener onDealClickListener) {
        this.onDealClickListener = onDealClickListener;
    }
}

package com.zxc.walk.ui.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxc.walk.R;
import com.zxc.walk.entity.Order;
import com.zxc.walk.ui.widget.CustomToast;

import androidx.annotation.NonNull;

public class OrderListAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

    private OnOrderClickListener onOrderClickListener;
    private Context context;

    public OrderListAdapter(Context context) {
        super(R.layout.adapter_order_item);
        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Order item) {
        helper.setText(R.id.tv_id, "订单编号：" + item.getId());
        helper.setText(R.id.tv_name, item.getProductname());
        helper.setText(R.id.tv_price, "价格：" + item.getPrice() + " 雨露");
        TextView view = helper.getView(R.id.tv_button);
        TextView view2 = helper.getView(R.id.tv_button2);
        TextView kuaidiView = helper.getView(R.id.tv_kuaidi);
        if (item.getStatus().equals("0")) {
            view2.setVisibility(View.GONE);
            kuaidiView.setVisibility(View.GONE);
            view.setText("去支付");
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOrderClickListener != null) {
                        onOrderClickListener.onClick(helper.getAdapterPosition());
                    }
                }
            });
        } else if (item.getStatus().equals("1")) {
            view.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            kuaidiView.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.GONE);
            view2.setText("复制快递编号");
            view2.setVisibility(View.VISIBLE);
            kuaidiView.setVisibility(View.VISIBLE);
            kuaidiView.setText("物流：" + item.getThirdname() + "\n物流编号：" + item.getThirdno());
            view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(item.getThirdno());
                    CustomToast.showToast(context, "已复制", Toast.LENGTH_SHORT);
                }
            });
        }

    }

    public interface OnOrderClickListener {
        void onClick(int position);
    }

    public void setOnOrderClickListener(OnOrderClickListener onOrderClickListener) {
        this.onOrderClickListener = onOrderClickListener;
    }
}
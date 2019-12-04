package com.zxc.walk.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxc.walk.R;
import com.zxc.walk.entity.Address;
import com.zxc.walk.framework.utils.PhoneInfoTools;

import androidx.annotation.NonNull;

public class AddressAdapter extends BaseQuickAdapter<Address, BaseViewHolder> {

    OnAddressDeleteListener onAddressDeleteListener;

    public AddressAdapter() {
        super(R.layout.adapter_address_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Address item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_address, item.getAddress());
        helper.setText(R.id.tv_phone, PhoneInfoTools.phoneNumerParserFourStar(item.getPhone()));
        helper.getView(R.id.delete_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAddressDeleteListener != null) {
                    onAddressDeleteListener.onDelete(item.getId());
                }
            }
        });
    }

    public interface OnAddressDeleteListener {
        void onDelete(String id);
    }

    public void setOnAddressDeleteListener(OnAddressDeleteListener onAddressDeleteListener) {
        this.onAddressDeleteListener = onAddressDeleteListener;
    }
}
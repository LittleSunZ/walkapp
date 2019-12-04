package com.zxc.walk.ui.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zxc.walk.R;
import com.zxc.walk.entity.Shop;
import com.zxc.walk.framework.utils.AppUtils;

import androidx.annotation.NonNull;

public class ShopListAdapter extends BaseQuickAdapter<Shop, BaseViewHolder> {

    public ShopListAdapter() {
        super(R.layout.adapter_shop_list_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Shop item) {
        SimpleDraweeView companyImage = helper.getView(R.id.shop_image);
        if (!TextUtils.isEmpty(item.getImage0())) {
            companyImage.setImageURI(Uri.parse(item.getImage0()));
        }
        helper.setText(R.id.tv_shop_name, item.getName());
        helper.setText(R.id.tv_shop_price, "雨露：" + item.getYuluprice());
    }
}

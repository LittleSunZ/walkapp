package com.zxc.walk.ui.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zxc.walk.R;
import com.zxc.walk.entity.Company;
import com.zxc.walk.framework.utils.AppUtils;

public class BusinessAdapter extends BaseQuickAdapter<Company, BaseViewHolder> {

    public BusinessAdapter() {
        super(R.layout.adapter_business_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Company item) {
        SimpleDraweeView companyImage = helper.getView(R.id.company_image);
        if(!TextUtils.isEmpty(item.getImage())){
            companyImage.setImageURI(Uri.parse(item.getImage()));
        }
        helper.setText(R.id.tv_company_name, item.getName());
    }
}

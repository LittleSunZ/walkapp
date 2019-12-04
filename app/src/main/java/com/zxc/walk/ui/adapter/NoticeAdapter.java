package com.zxc.walk.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxc.walk.R;
import com.zxc.walk.entity.NoticeBean;

import androidx.annotation.NonNull;

public class NoticeAdapter extends BaseQuickAdapter<NoticeBean, BaseViewHolder> {
    public NoticeAdapter() {
        super(R.layout.adpate_notice_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NoticeBean item) {
        helper.setText(R.id.tv_content, item.getContent());
    }
}

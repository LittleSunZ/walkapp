package com.zxc.walk.ui.widget;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.zxc.walk.R;

public class EasyLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.brvah_wo_loadmore;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.rl_loading;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.ll_loadmore_end;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        super.convert(holder);
        convertByStatus(holder, getLoadMoreStatus());
    }

    protected void convertByStatus(BaseViewHolder holder, int loadMoreStatus) {
    }
}

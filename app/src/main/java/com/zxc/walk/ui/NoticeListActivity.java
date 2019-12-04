package com.zxc.walk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxc.walk.R;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.NoticeBean;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.adapter.NoticeAdapter;
import com.zxc.walk.ui.base.TitleBarActivity;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;

public class NoticeListActivity extends TitleBarActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 10;
    NoticeAdapter addressAdapter;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_notice_list);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("公告中心");
        initRecyclerView();
        showProgressDialog("");
        reqAddressList();
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        addressAdapter = new NoticeAdapter();
        addressAdapter.bindToRecyclerView(recyclerView);
        addressAdapter.setOnLoadMoreListener(this, recyclerView);
        addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NoticeBean noticeBean = (NoticeBean) adapter.getData().get(position);
                Intent intent = new Intent(NoticeListActivity.this, NoticeActivity.class);
                intent.putExtra("content", noticeBean.getContent());
                startActivity(intent);
            }
        });
    }

    private void reqAddressList() {
        Observable<Result<ListResult<List<NoticeBean>>>> reqUserInfo = walkApiReq.getNoticeList(pageNum, pageSize);
        DataCallBack<ListResult<List<NoticeBean>>> dataCallBack = new DataCallBack<ListResult<List<NoticeBean>>>() {
            @Override
            protected void onSuccessData(ListResult<List<NoticeBean>> message) {
                if (pageNum == 1) {
                    addressAdapter.setNewData(message.getData());
                } else {
                    addressAdapter.addData(message.getData());
                    addressAdapter.loadMoreComplete();
                }
                if (pageNum * pageSize >= message.getTotal()) {
                    addressAdapter.loadMoreEnd();
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
                dismissProgressDialog();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }


    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        reqAddressList();
    }

}

package com.zxc.walk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxc.walk.R;
import com.zxc.walk.entity.Company;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.Shop;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.adapter.BusinessAdapter;
import com.zxc.walk.ui.adapter.ShopListAdapter;
import com.zxc.walk.ui.base.TitleBarActivity;
import com.zxc.walk.ui.widget.EasyLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;

public class ShopListActivity extends TitleBarActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 10;
    String companyid;
    ShopListAdapter shopListAdapter;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_shop_list);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("商品列表");
        recyclerView = findViewById(R.id.recyclerView);
        companyid = getIntent().getStringExtra("companyid");
        initRecyclerView();
        showProgressDialog("");
        reqTaskList();
    }

    private void reqTaskList() {
        Observable<Result<ListResult<List<Shop>>>> reqUserInfo = walkApiReq.getShopList(pageNum, pageSize, companyid);
        DataCallBack<ListResult<List<Shop>>> dataCallBack = new DataCallBack<ListResult<List<Shop>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Shop>> message) {
                if (pageNum == 1) {
                    shopListAdapter.setNewData(message.getData());
                } else {
                    shopListAdapter.addData(message.getData());
                    shopListAdapter.loadMoreComplete();
                }
                if (pageNum * pageSize >= message.getTotal()) {
                    shopListAdapter.loadMoreEnd();
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
                if (pageNum == 1) {
                    dismissProgressDialog();
                }
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    private void initRecyclerView() {
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        shopListAdapter = new ShopListAdapter();
        shopListAdapter.bindToRecyclerView(recyclerView);
        shopListAdapter.setLoadMoreView(new EasyLoadMoreView());
        shopListAdapter.setOnLoadMoreListener(this, recyclerView);
        shopListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Shop shop = (Shop) adapter.getData().get(position);
                Intent intent = new Intent(ShopListActivity.this, ShopDetailActivity.class);
                intent.putExtra("shopid", shop.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        reqTaskList();
    }
}

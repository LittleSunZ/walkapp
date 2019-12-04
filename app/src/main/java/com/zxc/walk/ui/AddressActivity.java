package com.zxc.walk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxc.walk.R;
import com.zxc.walk.entity.Address;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.Shop;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.adapter.AddressAdapter;
import com.zxc.walk.ui.base.TitleBarActivity;
import com.zxc.walk.ui.widget.CustomToast;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

public class AddressActivity extends TitleBarActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    RecyclerView recyclerView;
    @BindView(R.id.add_layout)
    LinearLayout addLayout;
    private int pageNum = 1;
    private int pageSize = 10;
    AddressAdapter addressAdapter;
    private int total;
    private boolean fromOrder;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_address);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.address_recyclerView);
        setTvTitle("收货地址");
        fromOrder = getIntent().getBooleanExtra("fromOrder", false);
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNum = 1;
        reqAddressList();
    }

    private void reqAddressList() {
        showProgressDialog("");
        Observable<Result<ListResult<List<Address>>>> reqUserInfo = walkApiReq.getAddressList(pageNum, pageSize, userInfo.getUserid());
        DataCallBack<ListResult<List<Address>>> dataCallBack = new DataCallBack<ListResult<List<Address>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Address>> message) {
                total = message.getTotal();
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

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        addressAdapter = new AddressAdapter();
        addressAdapter.bindToRecyclerView(recyclerView);
        addressAdapter.setOnLoadMoreListener(this, recyclerView);
        addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (fromOrder) {
                    Address address = (Address) adapter.getData().get(position);
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    setResult(101, intent);
                    finish();
                }
            }
        });
        addressAdapter.setOnAddressDeleteListener(new AddressAdapter.OnAddressDeleteListener() {
            @Override
            public void onDelete(String id) {
                delete(id);
            }
        });
    }

    private void delete(String id) {
        showProgressDialog("删除中");
        Observable<Result<String>> reqUserInfo = walkApiReq.deleteAddress(id);
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
                CustomToast.showToast(AddressActivity.this, "删除成功!", Toast.LENGTH_SHORT);
                pageNum = 1;
                reqAddressList();
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
    protected void setListener() {
        super.setListener();
        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total < 5) {
                    startActivity(new Intent(AddressActivity.this, AddressEditActivity.class));
                } else {
                    CustomToast.showToast(AddressActivity.this, "收获地址最多只能添加5个!", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        reqAddressList();
    }
}

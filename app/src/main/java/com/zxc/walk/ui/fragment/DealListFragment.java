package com.zxc.walk.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxc.walk.R;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.Transaction;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.DealDetailActivity;
import com.zxc.walk.ui.adapter.BuyAdapter;
import com.zxc.walk.ui.adapter.DealListAdapter;
import com.zxc.walk.ui.base.BaseFragment;
import com.zxc.walk.ui.widget.CustomToast;
import com.zxc.walk.ui.widget.EasyLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import io.reactivex.Observable;

public class DealListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 10;
    private String type;
    DealListAdapter dealListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_deal_list;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {
        type = getArguments().getString("type");
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        pageNum = 1;
        showProgressDialog("");
        reqTaskList();
    }

    private void reqTaskList() {
        Observable<Result<ListResult<List<Transaction>>>> reqUserInfo = null;

        switch (type) {
            case "0":
                reqUserInfo = walkApiReq.getTransactionListForUser(pageNum, pageSize, "0", userInfo.getUserid());
                break;
            case "1":
                reqUserInfo = walkApiReq.getTransactionListForDealUser(pageNum, pageSize, "1", userInfo.getUserid());
                break;
            case "2":
                reqUserInfo = walkApiReq.getTransactionListForUser(pageNum, pageSize, "1", userInfo.getUserid());
                break;
            case "3":
                reqUserInfo = walkApiReq.getTransactionListForDealUser(pageNum, pageSize, "0", userInfo.getUserid());
                break;
            default:
                break;
        }
        DataCallBack<ListResult<List<Transaction>>> dataCallBack = new DataCallBack<ListResult<List<Transaction>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Transaction>> message) {
                if (pageNum == 1) {
                    dealListAdapter.setNewData(message.getData());
                } else {
                    dealListAdapter.addData(message.getData());
                    dealListAdapter.loadMoreComplete();
                }
                if (pageNum * pageSize >= message.getTotal()) {
                    dealListAdapter.loadMoreEnd();
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
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        dealListAdapter = new DealListAdapter(getActivity());
        dealListAdapter.bindToRecyclerView(recyclerView);
        dealListAdapter.setLoadMoreView(new EasyLoadMoreView());
        dealListAdapter.setOnLoadMoreListener(this, recyclerView);
        dealListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        dealListAdapter.setOnDealClickListener(new DealListAdapter.OnDealClickListener() {
            @Override
            public void onGoDetail(Transaction item) {
                Intent intent = new Intent(getActivity(), DealDetailActivity.class);
                intent.putExtra("orderid", item.getId());
                startActivity(intent);
            }

            @Override
            public void onCancel(Transaction item) {
                updateTransaction(item);
            }

            @Override
            public void onSure(Transaction item) {

            }
        });
    }


    /***
     * 取消任务
     */
    public void updateTransaction(Transaction item) {
//        item.setStatus(2);
        Observable<Result<Transaction>> reqUserInfo = walkApiReq.updateTransaction("4", item);
        DataCallBack<Transaction> dataCallBack = new DataCallBack<Transaction>() {
            @Override
            protected void onSuccessData(Transaction message) {
                CustomToast.showToast(getActivity(), "操作成功!", Toast.LENGTH_SHORT);
                pageNum = 1;
                showProgressDialog("");
                reqTaskList();
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(reqUserInfo, dataCallBack);
    }

    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        reqTaskList();
    }
}

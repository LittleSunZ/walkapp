package com.zxc.walk.ui.fragment;

import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxc.walk.R;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.Order;
import com.zxc.walk.entity.Product;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.OrderSureActivity;
import com.zxc.walk.ui.adapter.OrderListAdapter;
import com.zxc.walk.ui.base.BaseFragment;
import com.zxc.walk.ui.widget.CustomToast;
import com.zxc.walk.ui.widget.EasyLoadMoreView;
import com.zxc.walk.ui.widget.PayDialog;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;

public class OrderListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 10;
    private OrderListAdapter orderListAdapter;
    private String status;
    private PayDialog dialog;
    private String productid;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {
        status = getArguments().getString("status");
        initRecyclerView();
        reqTaskList();
    }

    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        orderListAdapter = new OrderListAdapter(getActivity());
        orderListAdapter.bindToRecyclerView(recyclerView);
        orderListAdapter.setLoadMoreView(new EasyLoadMoreView());
        orderListAdapter.setOnLoadMoreListener(this, recyclerView);
        orderListAdapter.setOnOrderClickListener(new OrderListAdapter.OnOrderClickListener() {
            @Override
            public void onClick(int position) {
                Order order = orderListAdapter.getData().get(position);
                productid = order.getId();
                dialog = new PayDialog(getActivity());
                dialog.setOnPayDialogClickListen(new PayDialog.OnPayDialogClickListen() {
                    @Override
                    public void onPay(String password) {
                        pay(password);
                    }

                    @Override
                    public void onPayCancel() {
                        CustomToast.showToast(getActivity(), "取消支付!", Toast.LENGTH_SHORT);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void pay(String password) {
        Observable<Result<String>> reqUserInfo = walkApiReq.pay(userInfo.getUserid(), productid, password);
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
                CustomToast.showToast(getActivity(), "支付成功!", Toast.LENGTH_SHORT);
                if (dialog != null) {
                    dialog.dismiss();
                }
                pageNum = 1;
                reqTaskList();
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }


    private void reqTaskList() {
        Observable<Result<ListResult<List<Order>>>> reqUserInfo = walkApiReq.getOrderList(pageNum, pageSize, userInfo.getUserid(), status);
        DataCallBack<ListResult<List<Order>>> dataCallBack = new DataCallBack<ListResult<List<Order>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Order>> message) {
                if (pageNum == 1) {
                    orderListAdapter.setNewData(message.getData());
                } else {
                    orderListAdapter.addData(message.getData());
                    orderListAdapter.loadMoreComplete();
                }
                if (pageNum * pageSize >= message.getTotal()) {
                    orderListAdapter.loadMoreEnd();
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
                if (pageNum == 1) {
//                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);

    }

    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        reqTaskList();
    }
}

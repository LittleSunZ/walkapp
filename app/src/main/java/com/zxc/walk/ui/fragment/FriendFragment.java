package com.zxc.walk.ui.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxc.walk.R;
import com.zxc.walk.entity.Friend;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.adapter.FriendAdapter;
import com.zxc.walk.ui.base.BaseFragment;
import com.zxc.walk.ui.widget.EasyLoadMoreView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;

public class FriendFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 10;

    FriendAdapter friendAdapter;
    private int type;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {
        type = getArguments().getInt("type");
        initRecyclerView();
        showProgressDialog("");
        getTaskList();
    }


    private void initRecyclerView() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        friendAdapter = new FriendAdapter(type);
        friendAdapter.bindToRecyclerView(recyclerView);
        friendAdapter.setLoadMoreView(new EasyLoadMoreView());
        friendAdapter.setOnLoadMoreListener(this, recyclerView);
    }

    private void getTaskList() {
        Observable<Result<ListResult<List<Friend>>>> reqUserInfo = walkApiReq.getFriendList(type, pageNum, pageSize, userInfo.getUserid());
        DataCallBack<ListResult<List<Friend>>> dataCallBack = new DataCallBack<ListResult<List<Friend>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Friend>> message) {
                if (pageNum == 1) {
                    friendAdapter.setNewData(message.getData());
                } else {
                    friendAdapter.addData(message.getData());
                    friendAdapter.loadMoreComplete();
                }
                if (pageNum * pageSize >= message.getTotal()) {
                    friendAdapter.loadMoreEnd();
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
        getTaskList();
    }
}

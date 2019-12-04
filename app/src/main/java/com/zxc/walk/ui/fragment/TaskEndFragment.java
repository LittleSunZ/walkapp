package com.zxc.walk.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxc.walk.R;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.MyTask;
import com.zxc.walk.entity.Task;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.adapter.MyTaskAdapter;
import com.zxc.walk.ui.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

public class TaskEndFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int pageNum = 1;
    private int pageSize = 10;

    MyTaskAdapter taskAdapter;
    private List<Task> taskList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {
        initRecyclerView();
        getTaskList();
    }

    private void getTaskList() {
        showProgressDialog("");
        Observable<Result<ListResult<List<MyTask>>>> reqUserInfo = walkApiReq.getMyTask(pageNum, pageSize,userInfo.getUserid(),"1");
        DataCallBack<ListResult<List<MyTask>>> dataCallBack = new DataCallBack<ListResult<List<MyTask>>>() {
            @Override
            protected void onSuccessData(ListResult<List<MyTask>> message) {
                List<MyTask> noticeBeans = message.getData();
                taskAdapter.setNewData(noticeBeans);
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
        taskAdapter = new MyTaskAdapter();
        taskAdapter.bindToRecyclerView(recyclerView);
    }
}

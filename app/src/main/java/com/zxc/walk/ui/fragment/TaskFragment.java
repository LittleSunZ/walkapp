package com.zxc.walk.ui.fragment;

import android.widget.Toast;

import com.zxc.walk.R;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.MyTask;
import com.zxc.walk.entity.Task;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.adapter.TaskAdapter;
import com.zxc.walk.ui.base.BaseFragment;
import com.zxc.walk.ui.widget.CustomToast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;

public class TaskFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private int pageNum = 1;
    private int pageSize = 10;

    TaskAdapter taskAdapter;
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
        reqTaskList();
    }

    private void reqTaskList() {
        showProgressDialog("");
        Observable<Result<ListResult<List<Task>>>> reqUserInfo = walkApiReq.getTaskList(pageNum, pageSize);
        DataCallBack<ListResult<List<Task>>> dataCallBack = new DataCallBack<ListResult<List<Task>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Task>> message) {
                taskList = message.getData();
                taskAdapter.setNewData(taskList);
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
        taskAdapter = new TaskAdapter();
        taskAdapter.bindToRecyclerView(recyclerView);
        taskAdapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void duihuan(Task item) {
                reqDuihuan(item);
            }
        });
    }

    private void reqDuihuan(Task item) {
        MyTask task = new MyTask();
        task.setUserid(userInfo.getUserid());
        task.setTaskid(item.getId());
        task.setIsdefault(item.getIsdeafault());

        Observable<Result<String>> reqUserInfo = walkApiReq.insertTask(task);
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
                CustomToast.showToast(getActivity(), "兑换成功!", Toast.LENGTH_SHORT);
                reqTaskList();
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);

    }
}

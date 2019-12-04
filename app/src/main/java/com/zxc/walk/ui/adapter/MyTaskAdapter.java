package com.zxc.walk.ui.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxc.walk.R;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.MyTask;
import com.zxc.walk.entity.Task;
import com.zxc.walk.framework.retrofit.LifeDisposable;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.request.WalkApiReq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MyTaskAdapter extends BaseQuickAdapter<MyTask, BaseViewHolder> implements LifeDisposable {

    private Map<Integer, String> map = new HashMap<>();
    private Map<Integer, Integer> bgmap = new HashMap<>();
    WalkApiReq walkApiReq;
    /**
     * rx被通知者结合，挂载生命周期上，避免内存泄漏
     */
    private CompositeDisposable mCompositeDisposable;

    public MyTaskAdapter() {
        super(R.layout.adapter_my_task_item);
        walkApiReq = new WalkApiReq();
        map.put(0, "试炼卷轴");
        map.put(1, "初级卷轴");
        map.put(2, "中级卷轴");
        map.put(3, "高级卷轴");
        map.put(4, "超级卷轴");
        map.put(5, "进阶卷轴");
        map.put(6, "精英卷轴");
        map.put(7, "专家卷轴");

        bgmap.put(0, R.drawable.juanzhou1);
        bgmap.put(1, R.drawable.juanzhou2);
        bgmap.put(2, R.drawable.juanzhou3);
        bgmap.put(3, R.drawable.juanzhou4);
        bgmap.put(4, R.drawable.juanzhou5);
        bgmap.put(5, R.drawable.juanzhou6);
        bgmap.put(6, R.drawable.juanzhou7);
        bgmap.put(7, R.drawable.juanzhou8);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyTask item) {
        getTaskInfo(helper, item);
    }

    public void getTaskInfo(BaseViewHolder helper, MyTask item) {
        Observable<Result<Task>> reqUserInfo = walkApiReq.getTaskInfo(item.getTaskid());
        DataCallBack<Task> dataCallBack = new DataCallBack<Task>() {
            @Override
            protected void onSuccessData(Task item) {
                View bg = helper.getView(R.id.item_layout);
                bg.setBackgroundResource(bgmap.get(item.getShowlever()));
                helper.setText(R.id.tv_textview1, "卷轴兑换所需：" + item.getYulunumber() + "枚" + "  总共奖励雨露：" + item.getSendyulus() + "枚");
                helper.setText(R.id.tv_textview2, "任务周期：" + item.getDays() + "天");
                helper.setText(R.id.tv_textview3, "每日" + item.getRuningnumbers() + "步");
                helper.setText(R.id.tv_textview4, "任务编号：" + item.getId());
                helper.setText(R.id.tv_level, map.get(item.getShowlever()));
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    /**
     * 添加Rx Disposable
     *
     * @param disposable
     */
    @Override
    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

}


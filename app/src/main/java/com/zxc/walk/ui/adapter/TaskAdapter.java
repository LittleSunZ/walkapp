package com.zxc.walk.ui.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zxc.walk.R;
import com.zxc.walk.entity.Task;

import java.util.HashMap;
import java.util.Map;

public class TaskAdapter extends BaseQuickAdapter<Task, BaseViewHolder> {

    private Map<Integer, String> map = new HashMap<>();
    private Map<Integer, Integer> bgmap = new HashMap<>();
    private OnTaskClickListener onTaskClickListener;

    public TaskAdapter() {
        super(R.layout.adapter_task_item);
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
    protected void convert(@NonNull BaseViewHolder helper, Task item) {
        View bg = helper.getView(R.id.item_layout);
        bg.setBackgroundResource(bgmap.get(item.getShowlever()));
        helper.setText(R.id.tv_days, "周期：" + item.getDays() + "天");
        helper.setText(R.id.tv_textview1, "卷轴兑换所需：" + item.getYulunumber() + "枚");
        helper.setText(R.id.tv_textview2, "总共奖励雨露：" + item.getSendyulus() + "枚");
        helper.setText(R.id.tv_textview3, "每日所需步数：" + item.getRuningnumbers() + "步");
        helper.setText(R.id.tv_level, map.get(item.getShowlever()));
        View view = helper.getView(R.id.tv_duihuan);
        if (!TextUtils.isEmpty(item.getIsdeafault()) && item.getIsdeafault().equals("0")) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTaskClickListener != null) {
                        onTaskClickListener.duihuan(item);
                    }
                }
            });
        }
    }

    public void setOnTaskClickListener(OnTaskClickListener onTaskClickListener) {
        this.onTaskClickListener = onTaskClickListener;
    }

    public interface OnTaskClickListener {
        void duihuan(Task item);
    }
}


package com.zxc.walk.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.zxc.walk.R;
import com.zxc.walk.ui.base.BaseActivity;
import com.zxc.walk.ui.fragment.MyTaskFragment;
import com.zxc.walk.ui.fragment.TaskEndFragment;
import com.zxc.walk.ui.fragment.TaskFragment;

import butterknife.BindView;

public class TaskActivity extends BaseActivity {


    @BindView(R.id.tabHost)
    TabLayout tabHost;

    TaskFragment taskFragment1;
    MyTaskFragment taskFragment2;
    TaskEndFragment taskFragment3;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        taskFragment1 = new TaskFragment();
        taskFragment2 = new MyTaskFragment();
        taskFragment3 = new TaskEndFragment();
        switchContent(taskFragment1);
        tabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    switchContent(taskFragment1);
                } else if (tab.getPosition() == 1) {
                    switchContent(taskFragment2);
                } else {
                    switchContent(taskFragment3);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            Fragment from = this.getSupportFragmentManager().findFragmentById(R.id.task_fragment_content);
            if (null != from) {
                FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                transaction.remove(from);
                transaction.commit();
            }
            this.getSupportFragmentManager().popBackStackImmediate();
        }
    }


    /***
     * 切换fragment
     */
    public void switchContent(Fragment to) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        Fragment from = this.getSupportFragmentManager().findFragmentById(R.id.task_fragment_content);

        if (null == from) {
            if (!to.isAdded()) {
                transaction.add(R.id.task_fragment_content, to).commitAllowingStateLoss();
            }
        } else {
            hideFragments(transaction);
            // 先判断是否被add过
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.task_fragment_content, to);

            }
            // 隐藏当前的fragment，显示下一个
            transaction.show(to).commitAllowingStateLoss();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != taskFragment1) {
            transaction.hide(taskFragment1);
        }
        if (null != taskFragment2) {
            transaction.hide(taskFragment2);
        }
        if (null != taskFragment3) {
            transaction.hide(taskFragment3);
        }
    }
}

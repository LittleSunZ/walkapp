package com.zxc.walk.ui;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.zxc.walk.R;
import com.zxc.walk.ui.base.BaseActivity;
import com.zxc.walk.ui.fragment.FriendFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class TeamActivity extends BaseActivity {

    @BindView(R.id.tabHost)
    TabLayout tabHost;

    FriendFragment taskFragment1;
    FriendFragment taskFragment2;
    FriendFragment taskFragment3;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        taskFragment1 = new FriendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        taskFragment1.setArguments(bundle);

        taskFragment2 = new FriendFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("type", 1);
        taskFragment2.setArguments(bundle2);

        taskFragment3 = new FriendFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("type", 2);
        taskFragment3.setArguments(bundle3);


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
            Fragment from = this.getSupportFragmentManager().findFragmentById(R.id.team_fragment_content);
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
        Fragment from = this.getSupportFragmentManager().findFragmentById(R.id.team_fragment_content);

        if (null == from) {
            if (!to.isAdded()) {
                transaction.add(R.id.team_fragment_content, to).commitAllowingStateLoss();
            }
        } else {
            hideFragments(transaction);
            // 先判断是否被add过
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.team_fragment_content, to);

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

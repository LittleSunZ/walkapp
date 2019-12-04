package com.zxc.walk.ui;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.zxc.walk.R;
import com.zxc.walk.ui.base.BaseActivity;
import com.zxc.walk.ui.fragment.OrderListFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class BusinessActivity extends BaseActivity {

    @BindView(R.id.tabHost)
    TabLayout tabHost;

    OrderListFragment orderListFragment1;
    OrderListFragment orderListFragment2;
    OrderListFragment orderListFragment3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            Fragment from = getSupportFragmentManager().findFragmentById(R.id.business_fragment_content);
            if (null != from) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(from);
                transaction.commit();
            }
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_business;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        orderListFragment1 = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", "0");
        orderListFragment1.setArguments(bundle);

        orderListFragment2 = new OrderListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("status", "1");
        orderListFragment2.setArguments(bundle2);

        orderListFragment3 = new OrderListFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("status", "2");
        orderListFragment3.setArguments(bundle3);


        switchContent(orderListFragment1);
        tabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    switchContent(orderListFragment1);
                } else if (tab.getPosition() == 1) {
                    switchContent(orderListFragment2);
                } else if (tab.getPosition() == 2) {
                    switchContent(orderListFragment3);
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

    /***
     * 切换fragment
     */
    public void switchContent(Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment from = getSupportFragmentManager().findFragmentById(R.id.business_fragment_content);

        if (null == from) {
            if (!to.isAdded()) {
                transaction.add(R.id.business_fragment_content, to).commitAllowingStateLoss();
            }
        } else {
            hideFragments(transaction);
            // 先判断是否被add过
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.business_fragment_content, to);

            }
            // 隐藏当前的fragment，显示下一个
            transaction.show(to).commitAllowingStateLoss();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != orderListFragment1) {
            transaction.hide(orderListFragment1);
        }
        if (null != orderListFragment2) {
            transaction.hide(orderListFragment2);
        }
        if (null != orderListFragment3) {
            transaction.hide(orderListFragment3);
        }

    }
}

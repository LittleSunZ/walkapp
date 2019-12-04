package com.zxc.walk.ui;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.zxc.walk.R;
import com.zxc.walk.ui.base.BaseActivity;
import com.zxc.walk.ui.fragment.DealListFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class DealActivity extends BaseActivity {

    @BindView(R.id.tabHost)
    TabLayout tabHost;

    DealListFragment dealBuyFragment1;
    DealListFragment dealBuyFragment2;
    DealListFragment dealBuyFragment3;
    DealListFragment dealBuyFragment4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            Fragment from = getSupportFragmentManager().findFragmentById(R.id.deal_fragment_content);
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
        return R.layout.activity_deal;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        dealBuyFragment1 = new DealListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type","0");
        dealBuyFragment1.setArguments(bundle);

        dealBuyFragment2 = new DealListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type","1");
        dealBuyFragment2.setArguments(bundle2);

        dealBuyFragment3 = new DealListFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("type","2");
        dealBuyFragment3.setArguments(bundle3);

        dealBuyFragment4 = new DealListFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("type","3");
        dealBuyFragment4.setArguments(bundle4);

        switchContent(dealBuyFragment1);
        tabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    switchContent(dealBuyFragment1);
                } else if (tab.getPosition() == 1) {
                    switchContent(dealBuyFragment2);
                }else if (tab.getPosition() == 2) {
                    switchContent(dealBuyFragment3);
                }else{
                    switchContent(dealBuyFragment4);
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
        Fragment from = getSupportFragmentManager().findFragmentById(R.id.deal_fragment_content);

        if (null == from) {
            if (!to.isAdded()) {
                transaction.add(R.id.deal_fragment_content, to).commitAllowingStateLoss();
            }
        } else {
            hideFragments(transaction);
            // 先判断是否被add过
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.deal_fragment_content, to);

            }
            // 隐藏当前的fragment，显示下一个
            transaction.show(to).commitAllowingStateLoss();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != dealBuyFragment1) {
            transaction.hide(dealBuyFragment1);
        }
        if (null != dealBuyFragment2) {
            transaction.hide(dealBuyFragment2);
        }
        if (null != dealBuyFragment3) {
            transaction.hide(dealBuyFragment3);
        }
        if (null != dealBuyFragment4) {
            transaction.hide(dealBuyFragment4);
        }
    }
    
}

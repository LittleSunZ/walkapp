package com.zxc.walk.ui.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.zxc.walk.R;
import com.zxc.walk.ui.base.BaseFragment;
import com.zxc.walk.ui.widget.DealDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class DealFragment extends BaseFragment {

    @BindView(R.id.tabHost)
    TabLayout tabHost;

    DealBuyFragment dealBuyFragment;
    DealSafeFragment dealSafeFragment;
    @BindView(R.id.tv_push)
    TextView tvPush;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            Fragment from = getActivity().getSupportFragmentManager().findFragmentById(R.id.deal_fragment_content);
            if (null != from) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.remove(from);
                transaction.commit();
            }
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_deal;
    }

    @Override
    protected void setListener() {
        tvPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DealDialog dealDialog = new DealDialog(getActivity());
                dealDialog.show();
            }
        });
    }

    @Override
    protected void init() {
        dealBuyFragment = new DealBuyFragment();
        dealSafeFragment = new DealSafeFragment();
        switchContent(dealBuyFragment);
        tabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    switchContent(dealBuyFragment);
                } else {
                    switchContent(dealSafeFragment);
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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment from = getActivity().getSupportFragmentManager().findFragmentById(R.id.deal_fragment_content);

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
        if (null != dealBuyFragment) {
            transaction.hide(dealBuyFragment);
        }
        if (null != dealSafeFragment) {
            transaction.hide(dealSafeFragment);
        }
    }
}

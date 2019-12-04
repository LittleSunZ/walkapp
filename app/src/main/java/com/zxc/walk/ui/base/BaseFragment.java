/**
 * Copyright (c) hb-cloud 2015
 *
 * @author hubin
 * @date Nov 4, 2015
 */
package com.zxc.walk.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.LifeDisposable;
import com.zxc.walk.framework.utils.UserSp;
import com.zxc.walk.request.WalkApiReq;
import com.zxc.walk.ui.widget.CommonProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author hubin
 */
public abstract class BaseFragment extends Fragment implements LifeDisposable {

    protected View mRootView;
    protected Context mCtx;
    protected LayoutInflater mInflater;
    protected Unbinder mUnbinder;
    public WalkApiReq walkApiReq;
    public UserSp userSp;
    /**
     * rx被通知者结合，挂载生命周期上，避免内存泄漏
     */
    private CompositeDisposable mCompositeDisposable;
    protected CommonProgressDialog mCustomProgressDialog;

    public UserInfo userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (container == null) {
            mCtx = this.getActivity();
        } else {
            mCtx = container.getContext();
        }

        mInflater = inflater;
        //viewpager中fragment复用可能出现recyclerView对象不同问题
        if (null == mRootView) {
            mRootView = inflater.inflate(getLayoutId(), null);
            mUnbinder = ButterKnife.bind(this, mRootView);
            setListener();
            walkApiReq = new WalkApiReq();
            userSp = new UserSp(getActivity());
            userInfo = userSp.getUserInfo();
            init();
        }


        return mRootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
            this.mUnbinder = null;
        }
    }

    /**
     * 获取页面的布局资源id
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected <V extends View> V findViewById(int resId) {
        if (null != mRootView) {
            return (V) mRootView.findViewById(resId);
        } else {
            return null;
        }
    }

    protected abstract void setListener();

    protected abstract void init();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    protected void showProgressDialog(final String msg) {
        if (mCustomProgressDialog == null) {
            mCustomProgressDialog = new CommonProgressDialog(getActivity());
        }
        if (!mCustomProgressDialog.isShowing()) {
            mCustomProgressDialog.setMessage(msg);
            mCustomProgressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (mCustomProgressDialog != null && mCustomProgressDialog.isShowing()) {
            mCustomProgressDialog.dismiss();
        }
    }
}

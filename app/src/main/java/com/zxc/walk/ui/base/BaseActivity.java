package com.zxc.walk.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zxc.walk.R;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.LifeDisposable;
import com.zxc.walk.framework.utils.AtyContainer;
import com.zxc.walk.framework.utils.UserSp;
import com.zxc.walk.request.WalkApiReq;
import com.zxc.walk.ui.widget.CommonProgressDialog;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 通用Activity基类
 *
 * @author: wp
 * @date: 2019-09-03 at 22:50
 */
public abstract class BaseActivity extends AppCompatActivity implements LifeDisposable {

    protected CommonProgressDialog mCustomProgressDialog;
    public WalkApiReq walkApiReq;
    public UserSp userSp;
    /**
     * rx被通知者结合，挂载生命周期上，避免内存泄漏
     */
    private CompositeDisposable mCompositeDisposable;
    public UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        } else {
            setContentView(R.layout.single_fragment_layout);
        }
        bindButterKnife();
        setListener();
        walkApiReq = new WalkApiReq();
        userSp = new UserSp(this);
        userInfo = userSp.getUserInfo();
         init(savedInstanceState);
        // 添加Activity到堆栈
        AtyContainer.getInstance().addActivity(this);
    }

    /**
     * ButterKnife绑定布局
     */
    public void bindButterKnife() {
        ButterKnife.bind(this);
    }

    /**
     * 获取页面的布局资源id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 设置控件的监听事件
     */
    protected abstract void setListener();

    /**
     * 界面数据初始化
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 设置fragment页面
     *
     * @param fragment
     */
    protected void setActivityContent(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_root_view, fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 默认退出
     */
    public void defaultFinish() {
        super.finish();
    }


    /**
     * 是否是要跳相同的页面
     *
     * @param oringeActivity
     * @param targetActivity
     * @return
     */
    private boolean isSameActivity(BaseActivity oringeActivity, Class targetActivity) {
        if (targetActivity == null) {
            return false;
        }
        return oringeActivity.getClass() == targetActivity;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    protected void showProgressDialog(final String msg) {
        if (mCustomProgressDialog == null) {
            mCustomProgressDialog = new CommonProgressDialog(this);
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

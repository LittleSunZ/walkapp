package com.zxc.walk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zxc.walk.core.application.WalkApplication;
import com.zxc.walk.entity.Version;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.AppUtils;
import com.zxc.walk.framework.utils.AtyContainer;
import com.zxc.walk.framework.utils.UIUtils;
import com.zxc.walk.ui.InviteActivity;
import com.zxc.walk.ui.base.BaseActivity;
import com.zxc.walk.ui.fragment.BusinessFragment;
import com.zxc.walk.ui.fragment.DealFragment;
import com.zxc.walk.ui.fragment.IndexFragment;
import com.zxc.walk.ui.fragment.MyFragment;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_index)
    ImageView ivIndex;
    @BindView(R.id.tv_index)
    TextView tvIndex;
    @BindView(R.id.rl_index)
    RelativeLayout rlIndex;
    @BindView(R.id.iv_task)
    ImageView ivTask;
    @BindView(R.id.tv_task)
    TextView tvTask;
    @BindView(R.id.rl_task)
    RelativeLayout rlTask;
    @BindView(R.id.iv_my)
    ImageView ivMy;
    @BindView(R.id.tv_my)
    TextView tvMy;
    @BindView(R.id.rl_my)
    RelativeLayout rlMy;
    @BindView(R.id.iv_business)
    ImageView ivBusiness;
    @BindView(R.id.tv_business)
    TextView tvBusiness;
    @BindView(R.id.rl_business)
    RelativeLayout rlBusiness;
    private IndexFragment indexFragment;
    private DealFragment dealFragment;
    private BusinessFragment businessFragment;
    private MyFragment myFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            Fragment from = MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (null != from) {
                FragmentTransaction transaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
                transaction.remove(from);
                transaction.commit();
            }
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVersion();
        getToken();
    }

    @Override
    protected void setListener() {
        rlIndex.setOnClickListener(this);
        rlTask.setOnClickListener(this);
        rlMy.setOnClickListener(this);
        rlBusiness.setOnClickListener(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //初始化fragment
        indexFragment = new IndexFragment();
        dealFragment = new DealFragment();
        businessFragment = new BusinessFragment();
        myFragment = new MyFragment();
        //默认选择首页fragment
        switchContent(indexFragment);
    }

    /***
     * 切换fragment
     */
    public void switchContent(Fragment to) {
        FragmentTransaction transaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
        Fragment from = MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.fragment_content);

        if (null == from) {
            if (!to.isAdded()) {
                transaction.add(R.id.fragment_content, to).commitAllowingStateLoss();
            }
        } else {
            hideFragments(transaction);
            // 先判断是否被add过
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.fragment_content, to);

            }
            // 隐藏当前的fragment，显示下一个
            transaction.show(to).commitAllowingStateLoss();
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != indexFragment) {
            transaction.hide(indexFragment);
        }
        if (null != dealFragment) {
            transaction.hide(dealFragment);
        }
        if (null != businessFragment) {
            transaction.hide(businessFragment);
        }
        if (null != myFragment) {
            transaction.hide(myFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_index:
                switchContent(indexFragment);
                tvIndex.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_main));
                ivIndex.setImageResource(R.drawable.tab_index_press);
                tvMy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivMy.setImageResource(R.drawable.tab_my);
                tvTask.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivTask.setImageResource(R.drawable.tab_task);
                tvBusiness.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivBusiness.setImageResource(R.drawable.tab_shop);
                break;
            case R.id.rl_task:
                switchContent(dealFragment);
                tvIndex.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivIndex.setImageResource(R.drawable.tab_index);
                tvMy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivMy.setImageResource(R.drawable.tab_my);
                tvTask.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_main));
                ivTask.setImageResource(R.drawable.tab_task_press);
                tvBusiness.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivBusiness.setImageResource(R.drawable.tab_shop);
                break;
            case R.id.rl_business:
                switchContent(businessFragment);
                tvIndex.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivIndex.setImageResource(R.drawable.tab_index);
                tvMy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivMy.setImageResource(R.drawable.tab_my);
                tvTask.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivTask.setImageResource(R.drawable.tab_task);
                tvBusiness.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_main));
                ivBusiness.setImageResource(R.drawable.tab_shop_press);
                break;
            case R.id.rl_my:
                switchContent(myFragment);
                tvIndex.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivIndex.setImageResource(R.drawable.tab_index);
                tvMy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_main));
                ivMy.setImageResource(R.drawable.tab_my_press);
                tvTask.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivTask.setImageResource(R.drawable.tab_task);
                tvBusiness.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_gray));
                ivBusiness.setImageResource(R.drawable.tab_shop);
                break;
            default:
                break;
        }
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            UIUtils.toast("再按一次退出享步");
            mExitTime = System.currentTimeMillis();
        } else {
            AtyContainer.getInstance().finishAllActivity();
        }
    }


    AlertDialog dialog;

    public void getVersion() {
        Observable<Result<List<Version>>> reqUserInfo = walkApiReq.getVersion();
        DataCallBack<List<Version>> dataCallBack = new DataCallBack<List<Version>>() {
            @Override
            protected void onSuccessData(List<Version> message) {
                if (message == null || message.size() <= 0) {
                    return;
                }
                int version = Integer.parseInt(message.get(0).getVersionno().replace(".", ""));
                int nowVersion = Integer.parseInt(AppUtils.getLocalVersionName(MainActivity.this).replace(".", ""));
                if (nowVersion < version) {
                    dialog = new AlertDialog.Builder((MainActivity.this)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("http://www.baby921.top/app/shareapp/share.html");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }).create();
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                if (dialog.isShowing()) {
                                    return true;
                                }
                                return false;
                            } else {
                                return false;
                            }
                        }
                    });
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage("有新版本更新，请更新版本后使用!");
                    dialog.show();
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    public void getToken() {
        Observable<Result<String>> reqUserInfo = walkApiReq.getToken();
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
                if (!TextUtils.isEmpty(message)) {
                    WalkApplication.Instance().setUploadToken(message);
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

}

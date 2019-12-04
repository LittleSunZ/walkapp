package com.zxc.walk.ui.fragment;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zxc.walk.R;
import com.zxc.walk.entity.BannerModel;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.NoticeBean;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.PhoneInfoTools;
import com.zxc.walk.framework.utils.UserSp;
import com.zxc.walk.step.UpdateUiCallBack;
import com.zxc.walk.step.service.StepService;
import com.zxc.walk.ui.LoginActivity;
import com.zxc.walk.ui.NoticeListActivity;
import com.zxc.walk.ui.TaskActivity;
import com.zxc.walk.ui.base.BaseFragment;
import com.zxc.walk.ui.widget.CirclePercentView;
import com.zxc.walk.ui.widget.CustomToast;
import com.zxc.walk.ui.widget.MyFrameAnimation;
import com.zxc.walk.ui.widget.NoticeView;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

import butterknife.BindView;
import io.reactivex.Observable;

public class IndexFragment extends BaseFragment {

    public static final int ANIMATOR_DURATION = 1000;

    @BindView(R.id.circle_percent_progress)
    CirclePercentView progressView;
    @BindView(R.id.tv_task)
    TextView tvTask;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_huoyue)
    TextView tvHuoyue;
    @BindView(R.id.tv_yulu)
    TextView tvYulu;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.notice_view)
    NoticeView noticeView;
    @BindView(R.id.tv_totalfee)
    TextView tvTotalfee;
    @BindView(R.id.iv_run)
    ImageView ivRun;
    private int count = 4000;
    private List<BannerModel> images;

    List<String> noticeList;
    private int number;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected void setListener() {
        tvTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TaskActivity.class));
            }
        });
        noticeView.setOnItemClickListener(new NoticeView.OnItemClickListener() {
            @Override
            public void onItemClick(TextView view, int position) {
                if (noticeList == null || noticeList.size() == 0) {
                    return;
                }
                Intent intent = new Intent(getActivity(), NoticeListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void init() {
        setupService();
        setBanner();
        setNotice();
    }

    MyFrameAnimation animation;
    boolean animIsEnd;

    private void startAnim() {
        if (animation == null) {
            animation = new MyFrameAnimation();
            animation.setOnFrameAnimationListener(new MyFrameAnimation.OnFrameAnimationListener() {
                @Override
                public void onStart() {
                    animIsEnd = false;
                }

                @Override
                public void onEnd() {
                    animIsEnd = true;
                    animation.stop();
                }
            });
            //获取各种drawable
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.run1);
            Drawable drawable1 = ContextCompat.getDrawable(getActivity(), R.drawable.run2);
            Drawable drawable2 = ContextCompat.getDrawable(getActivity(), R.drawable.run3);
            //添加单帧
            animation.addFrame(drawable, 300);
            animation.addFrame(drawable1, 300);
            animation.addFrame(drawable2, 300);
            //设置给ImageView
            ivRun.setImageDrawable(animation);
            //开始帧动画
            animation.start();
        }
        if (animIsEnd) {
            animation.start();
        }
    }

    private void setNotice() {
        Observable<Result<ListResult<List<NoticeBean>>>> reqUserInfo = walkApiReq.getNoticeList(1, 5);
        DataCallBack<ListResult<List<NoticeBean>>> dataCallBack = new DataCallBack<ListResult<List<NoticeBean>>>() {
            @Override
            protected void onSuccessData(ListResult<List<NoticeBean>> message) {
                noticeList = new ArrayList<>();
                List<NoticeBean> noticeBeans = message.getData();
                for (NoticeBean notice : noticeBeans) {
                    noticeList.add(notice.getContent());
                }
                noticeView.setNoticeList(noticeList);
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);

    }

    private void setBanner() {
        images = new ArrayList<>();
        images.add(new BannerModel(R.drawable.banner1, 0));
        images.add(new BannerModel(R.drawable.banner2, 1));
        images.add(new BannerModel(R.drawable.banner3, 2));
        images.add(new BannerModel(R.drawable.banner4, 3));
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
        if (banner != null) {
            banner.startAutoPlay();
        }
        runingUpload(String.valueOf(number));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    public void getUserInfo() {
        if (userInfo == null) {
            return;
        }
        Observable<Result<UserInfo>> reqUserInfo = walkApiReq.getUserInfo(userInfo.getUserid());
        DataCallBack<UserInfo> dataCallBack = new DataCallBack<UserInfo>() {
            @Override
            protected void onSuccessData(UserInfo message) {
                if (message != null) {
                    userSp.setUserInfo(message);
                    tvYulu.setText(message.getCandynumber() + "");
                    tvLevel.setText("LV " + message.getViplevel());
                    tvHuoyue.setText(message.getActivitylevel() + "");
                    tvTotalfee.setText("累计获得雨露： " + message.getTotalcandynumber());
                    if (!TextUtils.isEmpty(message.getAppno()) && !PhoneInfoTools.getSerialNumber().equals(message.getAppno())) {
                        CustomToast.showToast(getActivity(), "您的账号已被冻结，有疑问请联系客服!", Toast.LENGTH_SHORT);
                        userSp.removeUserInfo();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    /***
     * 步数上传
     * @param number
     */
    private void runingUpload(String number) {
        if (Integer.parseInt(number) <= 0) {
            return;
        }
        Observable<Result<String>> reqUserInfo = walkApiReq.runingUpload(userInfo.getUserid(), number);
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(getActivity(), StepService.class);
        isBind = getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            tvCount.setText(String.valueOf(stepService.getStepCount()));
            setData(count, stepService.getStepCount());
            startAnim();
            runingUpload(String.valueOf(stepService.getStepCount()));
            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    tvCount.setText(String.valueOf(stepCount));
                    setData(count, stepCount);
                    startAnim();
                    number = stepCount;
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            getActivity().unbindService(conn);
        }
    }

    /**
     * 设置百分比
     *
     * @param max     最大值
     * @param current 占比
     */
    private void setData(int max, float current) {
        float percentage = (100f * current) / max;
        ObjectAnimator animator = ObjectAnimator.ofFloat(progressView, "percentage", 0, percentage);
        animator.setDuration(ANIMATOR_DURATION);
        animator.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            final BannerModel model = (BannerModel) path;
            imageView.setBackgroundResource(model.getImage());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

}

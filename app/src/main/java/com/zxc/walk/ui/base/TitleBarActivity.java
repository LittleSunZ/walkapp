package com.zxc.walk.ui.base;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zxc.walk.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public abstract class TitleBarActivity extends BaseActivity {

    FrameLayout flContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    FrameLayout ivBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_titlebar;
    }

    @Override
    public void bindButterKnife() {
        //此时还没bindButterKnife，flContent为空，要先通过findViewById找到
        flContent = findViewById(R.id.fl_content);
        initActivityContent();
        super.bindButterKnife();
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 设置页面内容
     */
    protected abstract void initActivityContent();

    /**
     * 设置页面内容
     *
     * @param layoutId
     */
    protected void setActivityContent(int layoutId) {
        if (layoutId != 0) {
            View view = getLayoutInflater().inflate(layoutId, null);
            setActivityContent(view);
        }
    }

    /**
     * 设置页面内容
     *
     * @param view
     */
    protected void setActivityContent(View view) {
        if (null != flContent) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            flContent.addView(view, params);
        }
    }

    @Override
    protected void setActivityContent(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commitAllowingStateLoss();
    }

    public void setTvTitle(String text) {
        tvTitle.setText(text);
    }

}

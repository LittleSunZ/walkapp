package com.zxc.walk.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.zxc.walk.R;
import com.zxc.walk.ui.base.TitleBarActivity;

import butterknife.BindView;

public class NoticeActivity extends TitleBarActivity {
    @BindView(R.id.tv_text)
    TextView tvText;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_notice);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("公告内容");
        tvText.setText(getIntent().getStringExtra("content"));
    }

}

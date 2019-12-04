package com.zxc.walk.ui;

import android.os.Bundle;

import com.zxc.walk.R;
import com.zxc.walk.ui.base.TitleBarActivity;

public class RuleActivity extends TitleBarActivity {
    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_rule);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("规则");
    }
}

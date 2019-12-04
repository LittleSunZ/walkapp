package com.zxc.walk.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zxc.walk.R;

import androidx.annotation.NonNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DealDialog extends Dialog {

    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_safe)
    TextView tvSafe;

    private Context context;

    public DealDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        this.context = context;
        setContentView(R.layout.dialog_deal);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_buy, R.id.tv_safe})
    public void onViewClicked(View view) {
        PublishDialog dialog;
        switch (view.getId()) {
            case R.id.tv_buy:
                dismiss();
                dialog = new PublishDialog(context, 0);
                dialog.show();
                break;
            case R.id.tv_safe:
                dismiss();
                dialog = new PublishDialog(context, 1);
                dialog.show();
                break;
        }
    }
}

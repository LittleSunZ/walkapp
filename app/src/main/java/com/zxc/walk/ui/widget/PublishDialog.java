package com.zxc.walk.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.zxc.walk.R;
import com.zxc.walk.entity.Statistical;
import com.zxc.walk.entity.Transaction;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.UserSp;
import com.zxc.walk.request.WalkApiReq;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

public class PublishDialog extends Dialog {

    /***
     * 0表示买1表示卖
     */
    private int type;
    private Context context;

    private EditText tvNumber;
    private EditText tvPrice;
    private TextView tvPublish;
    public UserInfo userInfo;

    public PublishDialog(@NonNull Context context, int type) {
        super(context, R.style.BottomInAndOutStyle);
        setContentView(R.layout.dialog_publish);
        this.type = type;
        this.context = context;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.BottomInAndOutStyle;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);

        UserSp userSp = new UserSp(context);
        tvPublish = findViewById(R.id.tv_publish);
        tvNumber = findViewById(R.id.tv_number);
        tvPrice = findViewById(R.id.tv_price);
        tvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo = userSp.getUserInfo();
//                if (type == 0 && userInfo.getViplevel() == 0) {
//                    CustomToast.showToast(context, "VIP0 不能购买!", Toast.LENGTH_SHORT);
//                    return;
//                }
                if (TextUtils.isEmpty(userInfo.getName())
                        || TextUtils.isEmpty(userInfo.getCardid())
                        || TextUtils.isEmpty(userInfo.getBankname())
                        || TextUtils.isEmpty(userInfo.getCardnum())) {
                    CustomToast.showToast(context, "个人信息未完善，请去完善后再来交易!", Toast.LENGTH_SHORT);
                    return;
                }
                inserOrder();
            }
        });
        reqData();
    }

    private void reqData() {
        WalkApiReq walkApiReq = new WalkApiReq();
        Observable<Result<Statistical>> reqUserInfo = walkApiReq.statistical(type);
        DataCallBack<Statistical> dataCallBack = new DataCallBack<Statistical>() {
            @Override
            protected void onSuccessData(Statistical message) {
                tvPrice.setHint(message.getLowData() + "-" + message.getHighData());
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(reqUserInfo, dataCallBack);
    }

    public void inserOrder() {
        String number = tvNumber.getText().toString();
        String price = tvPrice.getText().toString();
        if (TextUtils.isEmpty(number)) {
            CustomToast.showToast(context, "数量不能为空!", Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(price)) {
            CustomToast.showToast(context, "价格不能为空!", Toast.LENGTH_SHORT);
            return;
        }
        int number1 = Integer.parseInt(number);
        if (number1 < 10) {
            CustomToast.showToast(context, "数量最少要10个!", Toast.LENGTH_SHORT);
            return;
        }
        Transaction transaction = new Transaction();
        transaction.setNumber(number1);
        transaction.setPrice(Double.parseDouble(price));
        transaction.setTransactiontype(type);
        transaction.setUserid(userInfo.getUserid());

        WalkApiReq walkApiReq = new WalkApiReq();
        Observable<Result<String>> reqUserInfo = walkApiReq.insertOrder(transaction);
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
                CustomToast.showToast(context, "发布成功!", Toast.LENGTH_SHORT);
                dismiss();
                EventBus.getDefault().post("pubblish_success");
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(reqUserInfo, dataCallBack);
    }
}

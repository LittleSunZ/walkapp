package com.zxc.walk.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zxc.walk.R;
import com.zxc.walk.entity.Shop;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.ui.base.TitleBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class ShopDetailActivity extends TitleBarActivity {

    @BindView(R.id.shop_image)
    SimpleDraweeView shopImage;
    @BindView(R.id.shop_image1)
    ImageView shopImage1;
    @BindView(R.id.shop_image2)
    ImageView shopImage2;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_stocks)
    TextView tvStocks;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.tv_gift_yulu)
    TextView tvGiftYulu;
    @BindView(R.id.image_layout)
    LinearLayout imageLayout;
    private String shopid;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_shop_detail);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("详情");
        shopid = getIntent().getStringExtra("shopid");
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopDetailActivity.this, OrderSureActivity.class);
                intent.putExtra("shopid", shopid);
                startActivity(intent);
            }
        });
        showProgressDialog("");
        reqShopDetail();
    }

    private void reqShopDetail() {
        Observable<Result<Shop>> reqUserInfo = walkApiReq.getShopDetail(shopid);
        DataCallBack<Shop> dataCallBack = new DataCallBack<Shop>() {
            @Override
            protected void onSuccessData(Shop message) {
                if (!TextUtils.isEmpty(message.getImage0())) {
                    shopImage.setImageURI(Uri.parse(message.getImage0()));
                }
                if (!TextUtils.isEmpty(message.getImage1())) {
                    Glide.with(ShopDetailActivity.this).load(message.getImage1()).into(shopImage1);
                }
                if (!TextUtils.isEmpty(message.getImage2())) {
                    Glide.with(ShopDetailActivity.this).load(message.getImage2()).into(shopImage2);
                }
                if (!TextUtils.isEmpty(message.getTime())) {
                    String[] images = message.getTime().split(";");
                    for (int i = 0; i < images.length; i++) {
                        ImageView imageView = new ImageView(ShopDetailActivity.this);
                        imageLayout.addView(imageView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        Glide.with(ShopDetailActivity.this).load(images[i]).into(imageView);
                    }
                }
                tvGiftYulu.setText("赠送雨露：" + message.getYulunumber());
                tvPrice.setText("雨露：" + message.getYuluprice());
                tvStocks.setText("库存：" + message.getStocks());
                tvDesc.setText(message.getMarks());
            }

            @Override
            protected void onEnd() {
                super.onEnd();
                dismissProgressDialog();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }


}

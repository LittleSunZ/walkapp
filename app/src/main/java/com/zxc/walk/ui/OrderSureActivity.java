package com.zxc.walk.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zxc.walk.R;
import com.zxc.walk.entity.Address;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.Order;
import com.zxc.walk.entity.Product;
import com.zxc.walk.entity.Shop;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.AppUtils;
import com.zxc.walk.framework.utils.PhoneInfoTools;
import com.zxc.walk.ui.base.TitleBarActivity;
import com.zxc.walk.ui.widget.CommonDialog;
import com.zxc.walk.ui.widget.CustomToast;
import com.zxc.walk.ui.widget.PayDialog;

import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class OrderSureActivity extends TitleBarActivity {

    @BindView(R.id.address_layout)
    LinearLayout addressLayout;
    @BindView(R.id.tv_shop_id)
    TextView tvShopId;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.shop_image)
    SimpleDraweeView shopImage;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.address_add_layout)
    LinearLayout addressAddLayout;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    Address address;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    private String shopid;
    Shop shop;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_order_sure);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("确认订单");
        shopid = getIntent().getStringExtra("shopid");

        reqAddressList();
        reqShopDetail();
    }

    private void reqShopDetail() {
        showProgressDialog("");
        Observable<Result<Shop>> reqUserInfo = walkApiReq.getShopDetail(shopid);
        DataCallBack<Shop> dataCallBack = new DataCallBack<Shop>() {
            @Override
            protected void onSuccessData(Shop message) {
                shop = message;
                setView(message);
            }

            @Override
            protected void onEnd() {
                super.onEnd();
                dismissProgressDialog();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    PayDialog dialog;
    Product product;

    private void order() {
        if (shop == null) {
            return;
        }
        if (address == null) {
            CustomToast.showToast(this, "收获地址未填写!", Toast.LENGTH_SHORT);
            return;
        }
        showProgressDialog("");
        Order order = new Order();
        order.setUserid(userInfo.getUserid());
        order.setPrice(shop.getYuluprice());
        order.setProductid(shop.getId());
        order.setNumber("1");
        order.setAddressid(address.getId());
        Observable<Result<Product>> reqUserInfo = walkApiReq.insertShopOrder(order);
        DataCallBack<Product> dataCallBack = new DataCallBack<Product>() {
            @Override
            protected void onSuccessData(Product message) {
                product = message;
                CustomToast.showToast(OrderSureActivity.this, "下单成功，请支付!", Toast.LENGTH_SHORT);
                dialog = new PayDialog(OrderSureActivity.this);
                dialog.setOnPayDialogClickListen(new PayDialog.OnPayDialogClickListen() {
                    @Override
                    public void onPay(String password) {
                        pay(password);
                    }

                    @Override
                    public void onPayCancel() {
                        CustomToast.showToast(OrderSureActivity.this, "取消支付!", Toast.LENGTH_SHORT);
                        finish();
                    }
                });
                dialog.show();
            }

            @Override
            protected void onEnd() {
                super.onEnd();
                dismissProgressDialog();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }


    private void pay(String password) {
        Observable<Result<String>> reqUserInfo = walkApiReq.pay(userInfo.getUserid(), product.getId(), password);
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
                CustomToast.showToast(OrderSureActivity.this, "支付成功!", Toast.LENGTH_SHORT);
                if (dialog != null) {
                    dialog.dismiss();
                }
                finish();
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    @Override
    protected void setListener() {
        super.setListener();
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSureActivity.this, AddressActivity.class);
                intent.putExtra("fromOrder", true);
                startActivityForResult(intent, 101);
            }
        });
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(OrderSureActivity.this, "是否购买此商品?");
                dialog.setOnCommonDialogClickListen(new CommonDialog.onCommonDialogClickListen() {
                    @Override
                    public void onSure() {
                        dialog.dismiss();
                        order();
                    }
                });
                dialog.show();
            }
        });
        addressAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSureActivity.this, AddressActivity.class);
                intent.putExtra("fromOrder", true);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            addressAddLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.VISIBLE);
            address = (Address) data.getSerializableExtra("address");
            tvAddress.setText(address.getAddress());
            tvName.setText(address.getName());
            tvPhone.setText(PhoneInfoTools.phoneNumerParserFourStar(address.getPhone()));
        }
    }

    private void setView(Shop shop) {
        tvShopId.setText("商品编号：" + shop.getId());
        tvShopName.setText(shop.getName());
        tvShopPrice.setText("价格：" + shop.getYuluprice() + "雨露");
        if (!TextUtils.isEmpty(shop.getImage0())) {
            shopImage.setImageURI(shop.getImage0());
        }
        tvSum.setText("合计：" + shop.getYuluprice() + "雨露");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /***
     * 获取默认地址
     */
    private void reqAddressList() {
        Observable<Result<ListResult<List<Address>>>> reqUserInfo = walkApiReq.getAddressList(1, 10, userInfo.getUserid());
        DataCallBack<ListResult<List<Address>>> dataCallBack = new DataCallBack<ListResult<List<Address>>>() {
            @Override
            protected void onSuccessData(ListResult<List<Address>> message) {
                if (message.getData().size() > 0) {
                    addressAddLayout.setVisibility(View.GONE);
                    addressLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < message.getData().size(); i++) {
                        if (message.getData().get(i).getIsdefault().equals("1")) {
                            address = message.getData().get(i);
                            tvAddress.setText(address.getAddress());
                            tvName.setText(address.getName());
                            tvPhone.setText(PhoneInfoTools.phoneNumerParserFourStar(address.getPhone()));
                        }
                    }
                    if (address == null) {
                        address = message.getData().get(0);
                        tvAddress.setText(address.getAddress());
                        tvName.setText(address.getName());
                        tvPhone.setText(PhoneInfoTools.phoneNumerParserFourStar(address.getPhone()));
                    }
                } else {
                    addressAddLayout.setVisibility(View.VISIBLE);
                    addressLayout.setVisibility(View.GONE);
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

package com.zxc.walk.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.zxc.walk.R;
import com.zxc.walk.core.application.WalkApplication;
import com.zxc.walk.entity.Transaction;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.callback.DataCallBack;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.utils.HttpUtils;
import com.zxc.walk.framework.utils.AppUtils;
import com.zxc.walk.framework.utils.PathUtil;
import com.zxc.walk.request.Constants;
import com.zxc.walk.ui.base.TitleBarActivity;
import com.zxc.walk.ui.widget.CustomToast;
import com.zxc.walk.ui.widget.SelectPhotoDialog;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import io.reactivex.Observable;

public class DealDetailActivity extends TitleBarActivity implements TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_totalfee)
    TextView tvTotalfee;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_safe_name)
    TextView tvSafeName;
    @BindView(R.id.tv_safe_phone)
    TextView tvSafePhone;
    @BindView(R.id.tv_safe_bank)
    TextView tvSafeBank;
    @BindView(R.id.tv_safe_bank_id)
    TextView tvSafeBankId;
    @BindView(R.id.tv_buy_name)
    TextView tvBuyName;
    @BindView(R.id.tv_buy_phone)
    TextView tvBuyPhone;
    @BindView(R.id.tv_upload)
    TextView tvUpload;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_button)
    TextView tvButton;
    @BindView(R.id.tv_shouxu)
    TextView tvShouxu;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private String orderid;
    private String imageUrl;
    /***
     * 选取照片组件
     */
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    SelectPhotoDialog photoDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_deal_detail);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTvTitle("订单详情");
        orderid = getIntent().getStringExtra("orderid");
        reqShopDetail();
    }

    @Override
    protected void setListener() {
        super.setListener();
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DealDetailActivity.this,ImageActivity.class);
                intent.putExtra("imageUrl",imageUrl);
                startActivity(intent);
            }
        });
    }

    private void reqShopDetail() {
        showProgressDialog("");
        Observable<Result<Transaction>> reqUserInfo = walkApiReq.getTransactionInfo(orderid);
        DataCallBack<Transaction> dataCallBack = new DataCallBack<Transaction>() {
            @Override
            protected void onSuccessData(Transaction message) {
                if (message.getStatus() == 2 && !TextUtils.isEmpty(message.getImageurl())) {
                    tvStatus.setText("待确认");
                } else {
                    tvStatus.setText("待支付");
                }
                tvPrice.setText("￥" + message.getPrice());
                tvId.setText(message.getId());
                tvTotalfee.setText("￥" + message.getTotalfee());
                tvNumber.setText(message.getNumber() + "枚");
                tvShouxu.setText(message.getYongjin() + "枚");

                SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    tvTime.setText(simpleDateFormat.format(sim.parse(message.getDealtime()).getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (!TextUtils.isEmpty(message.getImageurl())) {
                    ivImage.setVisibility(View.VISIBLE);
                    imageUrl = message.getImageurl();
                    Glide.with(DealDetailActivity.this).load(imageUrl).into(ivImage);
                }

                if (message.getTransactiontype() == 0) {
                    //发布的买单
                    if (userInfo.getUserid().equals(message.getUserid())) {
                        //我发布的买单=别人卖给我
                        getBuyUserInfo(message.getUserid());
                        getSafeUserInfo(message.getDealuserid());
                    } else {
                        //别人发布的买单=我卖给别人
                        getSafeUserInfo(message.getUserid());
                        getBuyUserInfo(message.getDealuserid());
                    }
                } else {
                    //发布的卖单
                    if (userInfo.getUserid().equals(message.getUserid())) {
                        //我发布的卖单=别人买
                        getBuyUserInfo(message.getDealuserid());
                        getSafeUserInfo(message.getUserid());
                    } else {
                        //别人发布的卖单=我买
                        getSafeUserInfo(message.getDealuserid());
                        getBuyUserInfo(message.getUserid());
                    }
                }
                if (message.getTransactiontype() == 0) {
                    if (userInfo.getUserid().equals(message.getUserid()) && TextUtils.isEmpty(message.getImageurl())) {
                        payView();
                    } else if (userInfo.getUserid().equals(message.getDealuserid()) && !TextUtils.isEmpty(message.getImageurl())) {
                        tvButton.setText("确认收款");
                        tvButton.setVisibility(View.VISIBLE);
                        tvButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateTransaction(imageUrl, 2, "3");
                            }
                        });
                    } else {
                        tvButton.setVisibility(View.GONE);
                    }
                } else {
                    if (!TextUtils.isEmpty(message.getDealuserid()) && userInfo.getUserid().equals(message.getDealuserid()) && TextUtils.isEmpty(message.getImageurl())) {
                        payView();
                    } else {
                        if (!userInfo.getUserid().equals(message.getDealuserid()) && !TextUtils.isEmpty(message.getImageurl())) {
                            tvButton.setText("确认收款");
                            tvButton.setVisibility(View.VISIBLE);
                            tvButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateTransaction(imageUrl, 2, "3");
                                }
                            });
                        } else {
                            tvButton.setVisibility(View.GONE);
                        }
                    }
                }


            }

            @Override
            protected void onEnd() {
                super.onEnd();
                dismissProgressDialog();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    private void payView() {
        tvUpload.setVisibility(View.VISIBLE);
        tvButton.setText("我已支付");
        tvButton.setVisibility(View.VISIBLE);
        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(imageUrl)) {
                    CustomToast.showToast(DealDetailActivity.this, "未上传支付截图", Toast.LENGTH_SHORT);
                    return;
                }
                updateTransaction(imageUrl, 1, "2");
            }
        });
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog = new SelectPhotoDialog(DealDetailActivity.this);
                photoDialog.setLisnter(new SelectPhotoDialog.OnSelectClickLisnter() {
                    @Override
                    public void onFromCamera() {
                        getTakePhoto();
                        File tmpDir = new File(PathUtil.picturePath);
                        if (!tmpDir.exists()) {
                            tmpDir.mkdir();
                        }
                        File file = new File(PathUtil.picturePath + "/" + System.currentTimeMillis() + ".jpg");
                        Uri imageUri = Uri.fromFile(file);
                        configCompress(takePhoto);
                        configTakePhotoOption(takePhoto);
                        takePhoto.onPickFromCapture(imageUri);
                    }

                    @Override
                    public void onFromGallery() {
                        getTakePhoto();
                        File tmpDir = new File(PathUtil.picturePath);
                        if (!tmpDir.exists()) {
                            tmpDir.mkdir();
                        }
                        File file = new File(PathUtil.picturePath + "/" + System.currentTimeMillis() + ".jpg");
                        Uri imageUri = Uri.fromFile(file);
                        configCompress(takePhoto);
                        configTakePhotoOption(takePhoto);
                        takePhoto.onPickFromGallery();
                    }
                });
                photoDialog.show();
            }
        });
    }

    public void updateTransaction(String imageUrl, int status, String type) {
        showProgressDialog("");
        Transaction transaction = new Transaction();
        transaction.setId(orderid);
        transaction.setDealuserid(userInfo.getUserid());
        transaction.setImageurl(imageUrl);
        Observable<Result<Transaction>> reqUserInfo = walkApiReq.updateTransaction(type, transaction);
        DataCallBack<Transaction> dataCallBack = new DataCallBack<Transaction>() {
            @Override
            protected void onSuccessData(Transaction message) {
                CustomToast.showToast(DealDetailActivity.this, "操作成功!", Toast.LENGTH_SHORT);
                if (status == 1) {
                    reqShopDetail();
                } else {
                    finish();
                }


            }

            @Override
            protected void onEnd() {
                super.onEnd();
                dismissProgressDialog();
            }
        };
        HttpUtils.invoke(reqUserInfo, dataCallBack);
    }

    private void getSafeUserInfo(String id) {
        Observable<Result<UserInfo>> reqUserInfo = walkApiReq.getUserInfo(id);
        DataCallBack<UserInfo> dataCallBack = new DataCallBack<UserInfo>() {
            @Override
            protected void onSuccessData(UserInfo message) {
                if (message != null) {
                    tvSafeName.setText(message.getName());
                    tvSafePhone.setText(message.getPhone());
                    tvSafeBank.setText(message.getBankname());
                    tvSafeBankId.setText(message.getCardnum());
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    private void getBuyUserInfo(String id) {
        Observable<Result<UserInfo>> reqUserInfo = walkApiReq.getUserInfo(id);
        DataCallBack<UserInfo> dataCallBack = new DataCallBack<UserInfo>() {
            @Override
            protected void onSuccessData(UserInfo message) {
                if (message != null) {
                    tvBuyName.setText(message.getName());
                    tvBuyPhone.setText(message.getPhone());
                }
            }

            @Override
            protected void onEnd() {
                super.onEnd();
            }
        };
        HttpUtils.invoke(this, reqUserInfo, dataCallBack);
    }

    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 102400;
        int width = 800;
        int height = 800;
        boolean showProgressBar = true;
        boolean enableRawFile = true;
        CompressConfig config;
        config = new CompressConfig.Builder()
                .setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());

    }

    @Override
    public void takeSuccess(TResult result) {
        photoDialog.dismiss();
        ivImage.setVisibility(View.VISIBLE);
        File imageFile = new File(result.getImage().getCompressPath());
        Glide.with(this).load(imageFile).into(ivImage);
        try {
//            String fileBytes = AppUtils.encodeBase64File(result.getImage().getCompressPath());
//            imageUrl = fileBytes;
            showProgressDialog("上传中");
            uploadImage(result.getImage().getCompressPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void uploadImage(String path) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(path, userInfo.getUserid() + "-" + System.currentTimeMillis(), WalkApplication.Instance().getUploadToken(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                dismissProgressDialog();
                if (info.isOK()) {
                    Log.i("qiniu", "Upload Success");
                    imageUrl = Constants.IMAGE_HOST + key;
                } else {
                    CustomToast.showToast(DealDetailActivity.this, "上传失败，请重试!", Toast.LENGTH_SHORT);
                    Log.i("qiniu", "Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
            }
        }, null);
    }


    @Override
    public void takeFail(TResult result, String msg) {
        photoDialog.dismiss();
    }

    @Override
    public void takeCancel() {
        Logger.i("操作被取消");
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }
}

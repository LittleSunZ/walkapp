package com.zxc.walk.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;

public class UserInfoActivity extends TitleBarActivity implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.touxiang_layout)
    RelativeLayout touxiangLayout;
    @BindView(R.id.userinfo_layout)
    ImageView userinfoLayout;
    @BindView(R.id.tv_userid)
    TextView tvUserid;
    @BindView(R.id.tv_nickname)
    EditText tvNickname;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_cardid)
    EditText tvCardid;
    @BindView(R.id.tv_bankname)
    EditText tvBankname;
    @BindView(R.id.tv_bankid)
    EditText tvBankid;

    /***
     * 选取照片组件
     */
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    SelectPhotoDialog photoDialog;

    @Override
    protected void initActivityContent() {
        setActivityContent(R.layout.activity_usreinfo);
    }

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
    protected void init(Bundle savedInstanceState) {
        setTvTitle("个人信息");
        getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getUserInfo() {
        if (userInfo == null) {
            return;
        }
        showProgressDialog("");
        Observable<Result<UserInfo>> reqUserInfo = walkApiReq.getUserInfoImage(userInfo.getUserid());
        DataCallBack<UserInfo> dataCallBack = new DataCallBack<UserInfo>() {
            @Override
            protected void onSuccessData(UserInfo message) {
                if (message != null) {
                    userSp.setUserInfo(message);
                    tvUserid.setText("" + message.getUserid());
                    //禁止手机软键盘
                    if (!TextUtils.isEmpty(message.getCardnum())) {
                        tvBankid.setInputType(InputType.TYPE_NULL);
                    }
                    if (!TextUtils.isEmpty(message.getName())) {
                        tvName.setInputType(InputType.TYPE_NULL);
                    }
                    tvNickname.setText(getText(message.getNickname()));
                    tvName.setText(getText(message.getName()));
                    tvBankid.setText(getText(message.getCardnum()));
                    tvBankname.setText(getText(message.getBankname()));
                    tvCardid.setText(getText(message.getCardid()));
                    if (!TextUtils.isEmpty(userInfo.getHeadimage())) {
                        Glide.with(UserInfoActivity.this).load(userInfo.getHeadimage()).into(userinfoLayout);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getText(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return text;
    }

    @OnClick({R.id.touxiang_layout, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.touxiang_layout:
                photoDialog = new SelectPhotoDialog(UserInfoActivity.this);
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
                        takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
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
                        takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                    }
                });
                photoDialog.show();
                break;
            case R.id.tv_save:
                String nickName = tvNickname.getText().toString();
                String name = tvName.getText().toString();
                String cardid = tvCardid.getText().toString();
                String bankname = tvBankname.getText().toString();
                String bankid = tvBankid.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(cardid) || TextUtils.isEmpty(bankname) || TextUtils.isEmpty(bankid)) {
                    CustomToast.showToast(UserInfoActivity.this, "信息填写不完整!", Toast.LENGTH_SHORT);
                    return;
                }

                UserInfo nuserInfo = new UserInfo();
                nuserInfo.setUserid(userInfo.getUserid());
                nuserInfo.setNickname(nickName);
                nuserInfo.setName(name);
                nuserInfo.setCardid(cardid);
                nuserInfo.setBankname(bankname);
                nuserInfo.setCardnum(bankid);

                Observable<Result<String>> reqUserInfo = walkApiReq.updateUserInfo(nuserInfo);
                DataCallBack<String> dataCallBack = new DataCallBack<String>() {
                    @Override
                    protected void onSuccessData(String message) {
                        CustomToast.showToast(UserInfoActivity.this, "修改完成!", Toast.LENGTH_SHORT);
                    }

                    @Override
                    protected void onEnd() {
                        super.onEnd();
                    }
                };
                HttpUtils.invoke(this, reqUserInfo, dataCallBack);
                break;
            default:
                break;
        }
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

    private CropOptions getCropOptions() {
        int height = 800;
        int width = 800;
        boolean withWonCrop = true;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
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
        File imageFile = new File(result.getImage().getCompressPath());
        Glide.with(this).load(imageFile).into(userinfoLayout);
        try {
            showProgressDialog("上传头像中");
            uploadImage(result.getImage().getCompressPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void uploadImage(String path) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(path, userInfo.getUserid() + "-" + System.currentTimeMillis(), WalkApplication.Instance().getUploadToken(), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    Log.i("qiniu", "Upload Success");
                    updateUserHead(Constants.IMAGE_HOST + key);
                } else {
                    dismissProgressDialog();
                    CustomToast.showToast(UserInfoActivity.this, "上传失败，请重试!", Toast.LENGTH_SHORT);
                    Log.i("qiniu", "Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
            }
        }, null);
    }

    public void updateUserHead(String path) {
        UserInfo nuserInfo = new UserInfo();
        nuserInfo.setUserid(userInfo.getUserid());
        nuserInfo.setHeadimage(path);
        Observable<Result<String>> reqUserInfo = walkApiReq.updateUserInfo(nuserInfo);
        DataCallBack<String> dataCallBack = new DataCallBack<String>() {
            @Override
            protected void onSuccessData(String message) {
                CustomToast.showToast(UserInfoActivity.this, "修改头像成功!", Toast.LENGTH_SHORT);
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

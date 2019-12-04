package com.zxc.walk.framework.retrofit.callback;

import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zxc.walk.framework.retrofit.constants.Constants;
import com.zxc.walk.framework.retrofit.constants.ResultCode;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.retrofit.exception.ApiException;
import com.zxc.walk.framework.retrofit.exception.ReqErr;
import com.zxc.walk.framework.utils.LogUtil;
import com.zxc.walk.framework.utils.UIUtils;
import com.zxc.walk.ui.widget.CustomToast;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:结果回调
 */
public abstract class ResultCallback<T extends Result> extends Callback<T> {

    private TypeToken<T> typeToken;

    public ResultCallback() {
    }

    @Override
    public final void onNext(T result) {
        onSuccessData(result);//
    }

    protected abstract void onSuccessData(T t);

    @Override
    protected final void onError(ApiException exception) {
        forceDo(exception);
        onFail(exception);
    }

    /**
     * 针对错误进行一些强制性需要处理的逻辑。
     *
     * @param exception
     */
    private void forceDo(ApiException exception) {
        Throwable t = exception.getCause() == null ? exception : exception.getCause();

        LogUtil.w(Constants.TAG, LogUtil.getStackTrace(exception));
        //针对所有code进行模板处理，同时也支持重写
        switch (exception.getCode()) {
            case ResultCode.CODE_TOKEN_FAIL://token失效
                //TODO token失效，清除登录信息
                break;
            default:
                break;
        }

    }

    /**
     * 交由用户实现，目前简单toast
     *
     * @param exception
     */
    protected void onFail(ApiException exception) {
        //针对所有code进行模板处理，同时也支持重写
        switch (exception.getCode()) {
            case ResultCode.CODE_FAIL://失败
                onFailData(exception);
                break;
            case ResultCode.CODE_TOKEN_FAIL://token失效
                onTokenFail(exception);
                break;
            case ResultCode.CODE_REORDER_FAIL://该用户已订购该产品（内容或者套餐）
                onReOrderFail(exception);
                break;
            case ReqErr.HTTP_ERROR:
                onNetError(exception);
                break;
            case ReqErr.NETWORK_ERROR:
                onNetError(exception);
                break;
            case ReqErr.PARSE_ERROR:
                CustomToast.showToast(UIUtils.getContext(),exception.getDisplayMessage(), Toast.LENGTH_SHORT);
                break;
            default:
                CustomToast.showToast(UIUtils.getContext(),exception.getDisplayMessage(), Toast.LENGTH_SHORT);
                break;
        }
    }

    protected void onNetError(ApiException exception) {
        CustomToast.showToast(UIUtils.getContext(),exception.getDisplayMessage(), Toast.LENGTH_SHORT);
    }

    /* ------------------------- CODE处理区 --------------------------------*/


    protected void onReOrderFail(ApiException ex) {
        CustomToast.showToast(UIUtils.getContext(),ex.getDisplayMessage(), Toast.LENGTH_SHORT);
    }

    protected void onTokenFail(ApiException ex) {
        CustomToast.showToast(UIUtils.getContext(),ex.getDisplayMessage(), Toast.LENGTH_SHORT);
    }

    protected void onFailData(ApiException ex) {
        CustomToast.showToast(UIUtils.getContext(),ex.getDisplayMessage(), Toast.LENGTH_SHORT);
    }

    /* ------------------------- CODE处理区 end --------------------------------*/
}

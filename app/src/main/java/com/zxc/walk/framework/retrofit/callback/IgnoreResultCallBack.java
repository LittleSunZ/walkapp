package com.zxc.walk.framework.retrofit.callback;

/**
 * @author xyu
 * @date 2019/1/11
 * Describe:不需要处理成功回调的CallBack
 */
public class IgnoreResultCallBack<T> extends DataCallBack<T> {
    @Override
    protected final void onSuccessData(T message) {

    }
}

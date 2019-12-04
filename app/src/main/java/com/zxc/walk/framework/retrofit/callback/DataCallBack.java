package com.zxc.walk.framework.retrofit.callback;


import com.zxc.walk.framework.retrofit.entity.Result;

/**
 * @author xyu
 * @date 2018/9/20
 * Describe:只需要实体数据的CallBack
 */
public abstract class DataCallBack<T> extends ResultCallback<Result<T>> {

    @Override
    protected final void onSuccessData(Result<T> tResult) {
        onSuccessData(tResult.getMessage());
    }

    protected abstract void onSuccessData(T message);

}

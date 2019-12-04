package com.zxc.walk.framework.retrofit.callback;


import com.zxc.walk.framework.retrofit.exception.ApiException;

import io.reactivex.disposables.Disposable;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:请求回调处理
 */
public abstract class Callback<T> {

    private boolean isLoading;


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public final void onComplete() {
        onEnd();
    }

    public final void onSubscribe(Disposable d) {

    }

    /**
     * 错误处理
     *
     * @param e
     */
    public final void onError(Throwable e) {
        e.printStackTrace();
        onEnd();
//        onFail(e);
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            //没有捕捉的错误
            onError(new ApiException(e, "123321"));
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);

    public abstract void onNext(T t);


    /**
     * 无论成功失败，都会执行的回调
     */
    protected void onEnd() {

    }

}

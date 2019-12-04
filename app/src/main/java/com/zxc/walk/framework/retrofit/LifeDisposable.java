package com.zxc.walk.framework.retrofit;


import io.reactivex.disposables.Disposable;

/**
 * @author xyu
 * @date 2018/9/20
 * Describe:
 */
public interface LifeDisposable {
    void addDisposable(Disposable disposable);
}


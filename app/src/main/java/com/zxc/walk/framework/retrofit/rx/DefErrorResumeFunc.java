package com.zxc.walk.framework.retrofit.rx;



import com.zxc.walk.framework.retrofit.exception.ExceptionEngine;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author xyu
 * @date 2019/1/28
 * Describe:默认错误处理
 */
public class DefErrorResumeFunc<T> implements Function<Throwable, ObservableSource<? extends T>> {
    @Override
    public ObservableSource<? extends T> apply(Throwable throwable) {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
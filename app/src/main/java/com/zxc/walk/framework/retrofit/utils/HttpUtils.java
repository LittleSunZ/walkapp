package com.zxc.walk.framework.retrofit.utils;


import com.zxc.walk.framework.retrofit.LifeDisposable;
import com.zxc.walk.framework.retrofit.callback.Callback;
import com.zxc.walk.framework.retrofit.rx.DefErrorResumeFunc;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:
 */
public class HttpUtils {

    /**
     * 用的最多的请求模板链，如果有其他需求，则可试试自己定义
     *
     * @param lifecycle
     * @param observable
     * @param callback
     * @param showLoading
     * @param <T>
     */
    public static <T> Disposable invoke(LifeDisposable lifecycle, Observable<T> observable, final Callback<T> callback, final boolean showLoading) {
        /**
         * 先判断网络连接状态和网络是否可用，放在回调那里好呢，还是放这里每次请求都去判断下网络是否可用好呢？
         * 如果放在请求前面太耗时了，如果放回掉提示的速度慢，要10秒钟请求超时后才提示。
         * 最后采取的方法是判断网络是否连接放在外面，网络是否可用放在回掉。
         */
//        if (!PhoneInfoTools.checkNetworkInfo(UIUtils.getContext())) {
//            callback.onError(ExceptionEngine.handleException(new ConnectException()));
//            callback.onComplete();
//            return null;
//        }
        observable = observable.subscribeOn(Schedulers.io());
      /*  if (showLoading) {
            callback.setLoading(true);
            observable = observable.doOnSubscribe(
                    new Consumer<Disposable>() {2.
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            //使在请求网络期间，进行加载动画
//                            BaseActivity foregroundActivity = BaseActivity.getForegroundActivity();
//                            if (foregroundActivity != null && foregroundActivity instanceof ILoading)
//                                ((ILoading) foregroundActivity).netLoading(callback);
                        }
                    }
            ).subscribeOn(AndroidSchedulers.mainThread());
        }*/
        Disposable disposable = observable
                .onErrorResumeNext(new DefErrorResumeFunc<T>())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> callback.onNext(t)
                        , throwable -> callback.onError(throwable)
                        , () -> callback.onComplete()
                        , disposable1 -> callback.onSubscribe(disposable1));
        if (lifecycle != null) {
            lifecycle.addDisposable(disposable);
        }
        return disposable;
    }

    public static <T> Disposable invoke(LifeDisposable lifecycle, Observable<T> observable, Callback<T> callback) {
        return invoke(lifecycle, observable, callback, false);
    }

    public static <T> Disposable invoke(Observable<T> observable, Callback<T> callback) {
        return invoke(null, observable, callback);
    }

}

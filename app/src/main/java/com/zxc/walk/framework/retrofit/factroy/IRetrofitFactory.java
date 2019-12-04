package com.zxc.walk.framework.retrofit.factroy;

import retrofit2.Retrofit;

/**
 * @author xyu
 * @date 2018/9/19
 * Describe:retrofit工厂
 */
public interface IRetrofitFactory {
    Retrofit createRetrofit(String url);
}

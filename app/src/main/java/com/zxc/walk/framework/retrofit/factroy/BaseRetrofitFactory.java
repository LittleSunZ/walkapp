package com.zxc.walk.framework.retrofit.factroy;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:子类只负责提供按照模板提供配置，生产retrofit都交给base处理
 */
public abstract class BaseRetrofitFactory implements IRetrofitFactory {
    BaseRetrofitFactory() {
    }

    protected abstract OkHttpClient.Builder provideClientBuilder();

    protected abstract Retrofit.Builder provideRetrofitBuilder(OkHttpClient client);

    @Override
    public final Retrofit createRetrofit(String url) {
        OkHttpClient.Builder builder = provideClientBuilder();
        Retrofit.Builder retrofitBuilder = provideRetrofitBuilder(builder.build());
        retrofitBuilder.baseUrl(url);
        return retrofitBuilder.build();
    }
}

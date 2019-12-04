package com.zxc.walk.framework.retrofit;


import com.zxc.walk.framework.retrofit.constants.RetrofitFactoryType;
import com.zxc.walk.framework.retrofit.factroy.DefaultRetrofitFactory;
import com.zxc.walk.framework.retrofit.factroy.IRetrofitFactory;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:API管理类
 */
public final class ApiManager {
    private ApiManager() {
        init();
    }

    /*----------------------------单例固定区块---------------------------------*/
    private static ApiManager apiManager = null;

    private static class ApiFactoryHolder {
        private static final ApiManager INSTANCE = new ApiManager();
    }

    public static ApiManager getInstance() {
        return ApiFactoryHolder.INSTANCE;
    }

    /**
     * 避免反序列化重新生成对象，破坏 单例
     *
     * @return
     */
    private Object readResolve() {
        return getInstance();
    }

    /*----------------------------类区---------------------------------*/

    /**
     * 所有service集合
     * key的规则是由baseurl和service和配置共同生成
     */
    private Map<String, SoftReference<Object>> services;

    private void init() {
        services = new HashMap<>();
    }

    /**
     * 创建api
     *
     * @param service
     * @param baseUrl
     * @param type
     * @param useCache 是否使用缓存的service
     * @param <T>
     * @return
     */
    public <T> T createApi(Class<T> service, String baseUrl, RetrofitFactoryType type, boolean useCache) {
        if (useCache) {
            String key = baseUrl + service.getSimpleName() + type.name();
            Object o;
            if (!services.containsKey(key) || (o = services.get(key).get()) == null) {
                o = createApi(service, baseUrl, type);
                services.put(key, new SoftReference<>(o));
            }
            return service.cast(o);
        }
        return createApi(service, baseUrl, type);
    }

    /**
     * 创建api
     *
     * @param service
     * @param baseUrl
     * @param type
     * @param <T>
     * @return
     */
    public <T> T createApi(Class<T> service, String baseUrl, RetrofitFactoryType type) {
        IRetrofitFactory retrofitFactory;

        //策略决定使用哪个Retroft工厂
        switch (type) {
            case def:
            default:
                //能不用反射就尽量不使用反射
                retrofitFactory = new DefaultRetrofitFactory();
                break;
        }

        Retrofit retrofit = retrofitFactory.createRetrofit(baseUrl);
        return retrofit.create(service);
    }


}

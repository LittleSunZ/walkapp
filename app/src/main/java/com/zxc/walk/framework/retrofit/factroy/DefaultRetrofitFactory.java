package com.zxc.walk.framework.retrofit.factroy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zxc.walk.BuildConfig;
import com.zxc.walk.core.application.WalkApplication;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.framework.retrofit.constants.Constants;
import com.zxc.walk.framework.utils.LogUtil;
import com.zxc.walk.framework.utils.MD5;
import com.zxc.walk.framework.utils.PhoneInfoTools;
import com.zxc.walk.framework.utils.UserSp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author xyu
 * @date 2018/9/13
 * Describe:retrofit默认策略
 */
public class DefaultRetrofitFactory extends BaseRetrofitFactory {

    @Override
    protected OkHttpClient.Builder provideClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置缓存
//        File httpCacheDirectory = new File(FileUtils.getDir(FileUtils.CACHE_DIR + File.separator + "responses"));  //设置缓存路径
//        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);   //设置缓存 10M
//        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl.Builder builder = original.url().newBuilder();
                HttpUrl url = builder.build();

                UserSp userSp = new UserSp(WalkApplication.Instance());
                UserInfo userInfo = userSp.getUserInfo();

                Request.Builder builder1 = original.newBuilder()
                        .addHeader("token", userInfo != null ? MD5.doMD5(userInfo.getUserid() + userInfo.getPassword()) : "")
                        .addHeader("userid", userInfo != null ? userInfo.getUserid() : "")
                        .addHeader("password", userInfo != null ? userInfo.getPassword() : "")
                        .addHeader("type", "0")
                        .addHeader("appno", PhoneInfoTools.getSerialNumber())
                        .url(url);
                LogUtil.d(" ComicRetrofitFactory HttpRequstUrl", url.encodedPath() + " , " + url.encodedQuery());
                return chain.proceed(builder1.build());
            }
        });
        /*-----------------拦截器的顺序是很重要的，切记-------------------*/

        // 引入baseurl切换
//        RetrofitUrlManager.getInstance().with(builder);

        //加入header拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                LogUtil.d(Constants.TAG, "头部拦截器");
                Request request = chain.request();
//                Request.Builder builder1 = request.newBuilder().addHeader("", "");
                Request.Builder builder1 = request.newBuilder();
                LogUtil.d("HttpRequstUrl", request.url().toString());
                return chain.proceed(builder1.build());
            }
        });
        // 引入进度监听
//        ProgressManager.getInstance().with(builder);
        //如果是debug模式添加log拦截器
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    LogUtil.i(Constants.TAG, "retrofitBack = " + message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(loggingInterceptor);
        }
        return builder;
    }

    @Override
    public Retrofit.Builder provideRetrofitBuilder(OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder();
        //调整json的时间格式
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                //毫秒直接转Date
//                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                        return new Date(json.getAsJsonPrimitive().getAsLong());
//                    }
//                })
                .create();
        return builder
                .callFactory(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MyGsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                ;
    }
}

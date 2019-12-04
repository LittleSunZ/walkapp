package com.zxc.walk.request;

import com.zxc.walk.framework.utils.GsonUtils;

import okhttp3.RequestBody;

/**
 * @author xyu
 * @date 2019/1/28
 * Describe:
 */
public abstract class BaseApiReq<T> {
    private T api;

    public T getApi() {
        if (api == null) {
            api = createApi();
        }
        return api;
    }

    protected abstract T createApi();

    /**
     * 默认方式创建RequestBody
     *
     * @param obj 直接以json方式传递
     * @return
     */
    public RequestBody buildRequestBody(Object obj) {
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), GsonUtils.toJson(obj));
    }
}

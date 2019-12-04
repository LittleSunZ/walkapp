package com.zxc.walk.framework.retrofit.rx;



import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.utils.GsonUtils;
import com.zxc.walk.framework.utils.ReflectionUtils;

import io.reactivex.functions.Function;

/**
 * @author xyu
 * @date 2019/1/29
 * Describe:这里进行解析message至data
 */
public class TransFunc<T> implements Function<T, T> {
    @Override
    public T apply(T t) {
        if (!(t instanceof Result)) {
            return t;
        }
        Result result = (Result) t;
        Object message = result.getMessage();
        if (message == null) {
            return t;
        }
        Object o = GsonUtils.fromJson(message.toString(), ReflectionUtils.mygetSuperClassGenricType(t.getClass(), 0));
        try {
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return t;
    }
}

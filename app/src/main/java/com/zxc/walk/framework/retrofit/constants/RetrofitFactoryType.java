package com.zxc.walk.framework.retrofit.constants;


import com.zxc.walk.framework.retrofit.factroy.DefaultRetrofitFactory;
import com.zxc.walk.framework.retrofit.factroy.IRetrofitFactory;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:这里登记着retrofit的多个策略
 */
public enum RetrofitFactoryType {
    /**
     * 默认的retorfit配置
     */
    def(DefaultRetrofitFactory.class);
    Class<? extends IRetrofitFactory> factoryClz;

    public Class<? extends IRetrofitFactory> getFactoryClz() {
        return factoryClz;
    }

    /**
     * 记录每个枚举对应哪个策略，避免混乱
     *
     * @param factoryClz
     */
    RetrofitFactoryType(Class<? extends IRetrofitFactory> factoryClz) {
        this.factoryClz = factoryClz;
    }
}

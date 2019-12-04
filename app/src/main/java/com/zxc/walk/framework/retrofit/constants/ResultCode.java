package com.zxc.walk.framework.retrofit.constants;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:
 */
public interface ResultCode {
    /**
     * 成功
     */
    String CODE_SUCCESS = "0000";

    /**
     * 失败
     */
    String CODE_FAIL = "9999";

    /**
     * token失效
     */
    String CODE_TOKEN_FAIL = "6001";

    /**
     * 该用户已订购该产品（内容或者套餐）
     */
    String CODE_REORDER_FAIL = "9014";
}

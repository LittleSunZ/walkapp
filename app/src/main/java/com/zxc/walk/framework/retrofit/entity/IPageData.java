package com.zxc.walk.framework.retrofit.entity;

import java.util.List;

/**
 * @author xyu
 * @date 2018/9/19
 * Describe:page数据型接口
 */
public interface IPageData<T> {
    /*是否是最后一页*/
    boolean isLastPage();

    /*是否没有数据*/
    boolean isNoData();

    /*当前页数*/
    int getPageNum();

    /*单页请求多少条*/
    int getLimit();

    /*总页数*/
    int totalPages();

    /*数据总数*/
    long getTotal();

    List<T> getList();

}

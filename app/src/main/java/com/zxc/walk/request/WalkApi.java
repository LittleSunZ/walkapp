package com.zxc.walk.request;

import com.zxc.walk.entity.Address;
import com.zxc.walk.entity.Company;
import com.zxc.walk.entity.DealTable;
import com.zxc.walk.entity.Friend;
import com.zxc.walk.entity.ListResult;
import com.zxc.walk.entity.MyTask;
import com.zxc.walk.entity.NoticeBean;
import com.zxc.walk.entity.Order;
import com.zxc.walk.entity.Product;
import com.zxc.walk.entity.Shop;
import com.zxc.walk.entity.Statistical;
import com.zxc.walk.entity.Task;
import com.zxc.walk.entity.Transaction;
import com.zxc.walk.entity.UserInfo;
import com.zxc.walk.entity.Version;
import com.zxc.walk.framework.retrofit.entity.PageResult;
import com.zxc.walk.framework.retrofit.entity.Result;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WalkApi {

    /**
     * 注册
     */
    @GET(value = "userinfo/register")
    Observable<Result<UserInfo>> regsiter(
            @Query("phone") String phone,
            @Query("password") String password,
            @Query("suserid") String suserid
    );

    /**
     * 登录
     */
    @GET(value = "userinfo/login")
    Observable<Result<UserInfo>> login(
            @Query("phone") String phone,
            @Query("password") String password
    );

    /**
     * 获取用户信息
     */
    @GET(value = "userinfo/item/{id}")
    Observable<Result<UserInfo>> getUserInfo(
            @Path("id") String userid
    );
    /**
     * 获取用户信息
     */
    @GET(value = "userinfo/itemImage/{id}")
    Observable<Result<UserInfo>> getUserInfoImage(
            @Path("id") String userid
    );

    /**
     * 修改个人信息
     */
    @POST(value = "userinfo/update")
    Observable<Result<String>> updateUserInfo(
            @Body RequestBody requestBody
    );

    /**
     * 找回密码
     */
    @POST(value = "userinfo/updatePsdByPhone")
    Observable<Result<String>> updatePsdByPhone(
            @Body RequestBody requestBody
    );

    /**
     * 获取公告列表
     */
    @GET(value = "commontinfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<NoticeBean>>>> getNoticeList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize
    );

    /***
     * 获取任务列表
     */
    @GET(value = "taskinfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Task>>>> getTaskList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize
    );

    /***
     * 获取我的任务列表
     */
    @GET(value = "mytaskinfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<MyTask>>>> getMyTask(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("userid") String userid,
            @Query("status") String status
    );

    /***
     * 查询任务信息
     */
    @GET(value = "taskinfo/item/{id}")
    Observable<Result<Task>> getTaskInfo(
            @Path("id") String taskid
    );

    /***
     * 获取商家列表
     */
    @GET(value = "companyinfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Company>>>> getCompanyList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize
    );

    /***
     * 获取商品列表
     */
    @GET(value = "productinfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Shop>>>> getShopList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("companyid") String companyid
    );

    /***
     * 获取商品详情
     */
    @GET(value = "productinfo/item/{id}")
    Observable<Result<Shop>> getShopDetail(
            @Path("id") String id
    );

    /**
     * 新增收货地址
     */
    @POST(value = "receiveaddress/insert")
    Observable<Result<String>> insertAddress(
            @Body RequestBody requestBody
    );

    /***
     * 查询用户默认收货地址
     */
    @GET(value = "receiveaddress/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Address>>>> getAddressList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("userid") String userid
    );

    /***
     * 删除收货地址
     */
    @GET(value = "receiveaddress/delete/{id}")
    Observable<Result<String>> deleteAddress(
            @Path("id") String id
    );

    /***
     * 获取验证码
     */
    @GET(value = "userinfo/sendmessage")
    Observable<Result<String>> getSmsCode(
            @Query("phone") String phone
    );

    /***
     * 下单
     */
    @POST(value = "orderinfo/insert")
    Observable<Result<Product>> insertShopOrder(
            @Body RequestBody requestBody
    );

    /***
     * 发布订单
     */
    @POST(value = "transactioninfo/insert")
    Observable<Result<String>> insertOrder(
            @Body RequestBody requestBody
    );

    /***
     * 查询交易列表（卖/买）
     */
    @GET(value = "transactioninfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Transaction>>>> getTransactionList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("transactiontype") String transationtype,
            @Query("status") String status
    );

    /***
     * 查询交易列表（卖/买）
     */
    @GET(value = "transactioninfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Transaction>>>> getTransactionList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("transactiontype") String transationtype,
            @Query("status") String status,
            @Query("phone") String phone
    );

    /***
     * 查询交易列表（卖/买）
     */
    @GET(value = "transactioninfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Transaction>>>> getTransactionListForUser(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("transactiontype") String transactiontype,
            @Query("userid") String userid
    );

    /***
     * 查询交易列表（卖/买）
     */
    @GET(value = "transactioninfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Transaction>>>> getTransactionListForDealUser(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("transactiontype") String transactiontype,
            @Query("dealuserid") String dealuserid
    );

    /***
     * 支付接口
     */
    @POST(value = "orderinfo/pay/{userid}/{orderid}/{password}")
    Observable<Result<String>> pay(
            @Path("userid") String userid,
            @Path("orderid") String orderid,
            @Path("password") String password
    );

    /***
     * 查询订单列表
     */
    @GET(value = "orderinfo/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Order>>>> getOrderList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("userid") String userid,
            @Query("status") String status
    );

    /***
     * 步数上传
     */
    @GET(value = "runinginfo/upload")
    Observable<Result<String>> runingUpload(
            @Query("userid") String userid,
            @Query("number") String number,
            @Query("sign") String sign,
            @Query("time") String time
    );

    /***
     * 交易操作
     * 1 买  2上传截图  3确认交易 4取消
     */
    @POST(value = "transactioninfo/update/{type}")
    Observable<Result<Transaction>> updateTransaction(
            @Path("type") String type,
            @Body RequestBody requestBody
    );

    /***
     * 查询交易详情
     */
    @GET(value = "transactioninfo/item/{id}")
    Observable<Result<Transaction>> getTransactionInfo(
            @Path("id") String id
    );

    /***
     * 获取一级好友
     */
    @GET(value = "fuserteam/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Friend>>>> getFriendList(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("userid") String userid
    );

    /***
     * 获取二级好友
     */
    @GET(value = "suserteam/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Friend>>>> getFriendList2(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("userid") String userid
    );

    /***
     * 获取三级好友
     */
    @GET(value = "tuserteam/list/{pageNum}/{pageSize}")
    Observable<Result<ListResult<List<Friend>>>> getFriendList3(
            @Path("pageNum") int pageNum,
            @Path("pageSize") int pageSize,
            @Query("userid") String userid
    );

    @POST(value = "mytaskinfo/insert")
    Observable<Result<String>> insertTask(
            @Body RequestBody requestBody
    );

    /***
     * 交易统计
     */
    @GET(value = "transactioninfo/queryCountData/{type}")
    Observable<Result<Statistical>> statistical(
            @Path("type") int type
    );

    /***
     * 十天内平均数
     */
    @GET(value = "transactioninfo/queryLastTenDaysData/{type}")
    Observable<Result<List<DealTable>>> getDealTable(
            @Path("type") int type
    );

    /***
     * 查询版本号
     */
    @GET(value = "activeinfo/selectVersion")
    Observable<Result<List<Version>>> getVersion(
    );
    /***
     * 获取上传token
     */
    @GET(value = "userinfo/getUploadToken")
    Observable<Result<String>> getToken(
    );
}

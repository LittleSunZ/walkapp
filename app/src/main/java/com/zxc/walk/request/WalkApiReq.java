package com.zxc.walk.request;

import android.text.TextUtils;

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
import com.zxc.walk.framework.retrofit.ApiManager;
import com.zxc.walk.framework.retrofit.constants.RetrofitFactoryType;
import com.zxc.walk.framework.retrofit.entity.Result;
import com.zxc.walk.framework.utils.MD5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class WalkApiReq extends BaseApiReq<WalkApi> {
    @Override
    protected WalkApi createApi() {
        return ApiManager.getInstance().createApi(WalkApi.class, Constants.WALK_HOST, RetrofitFactoryType.def);
    }

    /***
     * 注册
     */
    public Observable<Result<UserInfo>> regsiter(String phone, String password, String suserid) {
        return getApi().regsiter(phone, MD5.doMD5(password), suserid);
    }

    /***
     * 登录
     */
    public Observable<Result<UserInfo>> login(String phone, String password) {
        return getApi().login(phone, MD5.doMD5(password));
    }

    /***
     * 获取用户详情
     */
    public Observable<Result<UserInfo>> getUserInfo(String userid) {
        return getApi().getUserInfo(userid);
    }

    /***
     * 获取用户详情
     */
    public Observable<Result<UserInfo>> getUserInfoImage(String userid) {
        return getApi().getUserInfoImage(userid);
    }

    /***
     * 修改用户信息
     */
    public Observable<Result<String>> updateUserInfo(UserInfo userInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userInfo.getUserid());
        if (!TextUtils.isEmpty(userInfo.getNickname())) {
            map.put("nickname", userInfo.getNickname());
        }
        if (!TextUtils.isEmpty(userInfo.getName())) {
            map.put("name", userInfo.getName());
        }
        if (!TextUtils.isEmpty(userInfo.getCardid())) {
            map.put("cardid", userInfo.getCardid());
        }
        if (!TextUtils.isEmpty(userInfo.getBankname())) {
            map.put("bankname", userInfo.getBankname());
        }
        if (!TextUtils.isEmpty(userInfo.getCardnum())) {
            map.put("cardnum", userInfo.getCardnum());
        }
        if (!TextUtils.isEmpty(userInfo.getCardnum())) {
            map.put("cardnum", userInfo.getCardnum());
        }
        if (!TextUtils.isEmpty(userInfo.getPaypassword())) {
            map.put("paypassword", MD5.doMD5(userInfo.getPaypassword()));
        }
        if (!TextUtils.isEmpty(userInfo.getPassword())) {
            map.put("password", MD5.doMD5(userInfo.getPassword()));
        }
        if (!TextUtils.isEmpty(userInfo.getHeadimage())) {
            map.put("headimage", userInfo.getHeadimage());
        }
        return getApi().updateUserInfo(buildRequestBody(map));
    }

    /***
     * 获取公告列表
     */
    public Observable<Result<ListResult<List<NoticeBean>>>> getNoticeList(int pageNum, int pageSize) {
        return getApi().getNoticeList(pageNum, pageSize);
    }

    /***
     * 获取任务列表
     */
    public Observable<Result<ListResult<List<Task>>>> getTaskList(int pageNum, int pageSize) {
        return getApi().getTaskList(pageNum, pageSize);
    }

    /***
     * 获取我的任务列表
     */
    public Observable<Result<ListResult<List<MyTask>>>> getMyTask(int pageNum, int pageSize, String userid, String status) {
        return getApi().getMyTask(pageNum, pageSize, userid, status);
    }

    /***
     * 获取任务详情
     */
    public Observable<Result<Task>> getTaskInfo(String taskid) {
        return getApi().getTaskInfo(taskid);
    }

    /***
     * 获取商家列表
     */
    public Observable<Result<ListResult<List<Company>>>> getCompanyList(int pageNum, int pageSize) {
        return getApi().getCompanyList(pageNum, pageSize);
    }

    /***
     * 获取商品列表
     */
    public Observable<Result<ListResult<List<Shop>>>> getShopList(int pageNum, int pageSize, String companyid) {
        return getApi().getShopList(pageNum, pageSize, companyid);
    }

    /***
     * 获取商品详情
     */
    public Observable<Result<Shop>> getShopDetail(String id) {
        return getApi().getShopDetail(id);
    }

    /***
     * 新增收货地址
     */
    public Observable<Result<String>> insertAddress(Address address) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", address.getUserid());
        map.put("name", address.getName());
        map.put("phone", address.getPhone());
        map.put("address", address.getAddress());
        map.put("isdefault", address.getIsdefault());
        return getApi().insertAddress(buildRequestBody(map));
    }

    /***
     * 查询用户收货地址
     */
    public Observable<Result<ListResult<List<Address>>>> getAddressList(int pageNum, int pageSize, String userid) {
        return getApi().getAddressList(pageNum, pageSize, userid);
    }

    /***
     * 删除收获地址
     */
    public Observable<Result<String>> deleteAddress(String id) {
        return getApi().deleteAddress(id);
    }

    /***
     * 获取验证码
     */
    public Observable<Result<String>> getSmsCode(String id) {
        return getApi().getSmsCode(id);
    }

    /***
     * 下单
     */
    public Observable<Result<Product>> insertShopOrder(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", order.getUserid());
        map.put("price", order.getPrice());
        map.put("productid", order.getProductid());
        map.put("number", order.getNumber());
        map.put("addressid", order.getAddressid());
        return getApi().insertShopOrder(buildRequestBody(map));
    }

    /***
     * 发布交易
     */
    public Observable<Result<String>> insertOrder(Transaction transaction) {
        Map<String, Object> map = new HashMap<>();
        map.put("number", transaction.getNumber());
        map.put("userid", transaction.getUserid());
        map.put("price", transaction.getPrice());
        map.put("transactiontype", transaction.getTransactiontype());
        return getApi().insertOrder(buildRequestBody(map));
    }

    /***
     * 查询交易列表（卖/买）
     * 0表示买1表示卖
     */
    public Observable<Result<ListResult<List<Transaction>>>> getTransactionList(int pageNum, int pageSize, String transactiontype, String phone) {
        if (TextUtils.isEmpty(phone)) {
            return getApi().getTransactionList(pageNum, pageSize, transactiontype, "0");
        } else {
            return getApi().getTransactionList(pageNum, pageSize, transactiontype, "0", phone);
        }
    }

    /***
     * 查询用户  交易列表（卖/买）
     * 0表示买1表示卖
     */
    public Observable<Result<ListResult<List<Transaction>>>> getTransactionListForUser(int pageNum, int pageSize, String transactiontype, String userid) {
        return getApi().getTransactionListForUser(pageNum, pageSize, transactiontype, userid);
    }

    /***
     * 查询用户  交易列表（卖/买）
     * 0表示买1表示卖
     */
    public Observable<Result<ListResult<List<Transaction>>>> getTransactionListForDealUser(int pageNum, int pageSize, String transactiontype, String dealuserid) {
        return getApi().getTransactionListForDealUser(pageNum, pageSize, transactiontype, dealuserid);
    }

    /***
     * 支付
     */
    public Observable<Result<String>> pay(String userid, String orderid, String password) {
        return getApi().pay(userid, orderid, MD5.doMD5(password));
    }

    /***
     * 查询订单列表
     * 0未支付，1已支付，2已发货
     */
    public Observable<Result<ListResult<List<Order>>>> getOrderList(int pageNum, int pageSize, String userid, String status) {
        return getApi().getOrderList(pageNum, pageSize, userid, status);
    }

    /***
     * 步数上传
     */
    public Observable<Result<String>> runingUpload(String userid, String number) {
        String MD5key = "123456@qwe";
        String time = String.valueOf(System.currentTimeMillis());
        String sign = userid + number + time + MD5key;
        return getApi().runingUpload(userid, number, MD5.doMD5(sign), time);
    }

    /***
     * 交易操作
     *1 买  2上传截图  3确认交易 4取消
     */
    public Observable<Result<Transaction>> updateTransaction(String type, Transaction transaction) {
        Map<String, Object> map = new HashMap<>();
        map.put("dealuserid", transaction.getDealuserid());
        map.put("id", transaction.getId());
//        map.put("status", transaction.getStatus());
        if (!TextUtils.isEmpty(transaction.getImageurl())) {
            map.put("imageurl", transaction.getImageurl());
        }
        return getApi().updateTransaction(type, buildRequestBody(map));
    }

    /***
     * 步数上传
     */
    public Observable<Result<Transaction>> getTransactionInfo(String id) {
        return getApi().getTransactionInfo(id);
    }

    /***
     * 获取一级好友
     */
    public Observable<Result<ListResult<List<Friend>>>> getFriendList(int type, int pageNum, int pageSize, String userid) {
        switch (type) {
            case 0:
                return getApi().getFriendList(pageNum, pageSize, userid);
            case 1:
                return getApi().getFriendList2(pageNum, pageSize, userid);
            case 2:
                return getApi().getFriendList3(pageNum, pageSize, userid);
        }
        return getApi().getFriendList(pageNum, pageSize, userid);
    }

    public Observable<Result<String>> insertTask(MyTask task) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", task.getUserid());
        map.put("taskid", task.getTaskid());
        map.put("isdefault", task.getIsdefault());
        return getApi().insertTask(buildRequestBody(map));
    }

    public Observable<Result<Statistical>> statistical(int type) {
        return getApi().statistical(type);
    }

    public Observable<Result<List<DealTable>>> getDealTable(int type) {
        return getApi().getDealTable(type);
    }

    public Observable<Result<List<Version>>> getVersion() {
        return getApi().getVersion();
    }

    public Observable<Result<String>> getToken() {
        return getApi().getToken();
    }
}

package com.zxc.walk.framework.retrofit.exception;

import com.google.gson.JsonParseException;
import com.zxc.walk.BuildConfig;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * @author xyu
 * @date 2018/9/18
 * Describe:
 */
public class ExceptionEngine {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e){
        ApiException ex;
        e.printStackTrace();
        if (e instanceof HttpException){             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ReqErr.HTTP_ERROR);
            switch(httpException.code()){
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.setDisplayMessage("网络繁忙，请稍后再试！");  //均视为网络错误
                    break;
            }
            return ex;
        } else if (e instanceof ServerException){    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.getCode());
            ex.setDisplayMessage(resultException.getMsg());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            ex = new ApiException(e, ReqErr.PARSE_ERROR);
            if(BuildConfig.DEBUG){
                ex.setDisplayMessage("解析错误");
            }
            return ex;
        }else if(e instanceof ConnectException ||e instanceof UnknownHostException){
            ex = new ApiException(e, ReqErr.NETWORK_ERROR);
            ex.setDisplayMessage("请检查您的网络连接是否正常");  //均视为网络错误
            return ex;
        }else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ReqErr.NETWORK_ERROR);
            ex.setDisplayMessage("网络繁忙，请稍后再试！");  //均视为网络错误
            return ex;
        }else {
            ex = new ApiException(e, ReqErr.UNKNOWN);
            if(BuildConfig.DEBUG){
//                ex.setDisplayMessage("未知错误");
                ex.setDisplayMessage(e.getMessage());
            }
            return ex;
        }
    }
}

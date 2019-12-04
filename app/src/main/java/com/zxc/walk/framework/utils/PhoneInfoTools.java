package com.zxc.walk.framework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneInfoTools {

    /**
     * 检查网络状态
     */
    public static final boolean checkNetworkInfo(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;

    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 隐藏手机号码中间四位
     *
     * @param phoneNum
     * @return
     */
    public static String phoneNumerParserFourStar(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum) && isMobilePhone(phoneNum)) {
            String phoneNumber = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7);
            return phoneNumber;
        }
        return phoneNum;
    }
    /**
     * 判断是否为疑似手机号   此判断是不全面的**
     *
     * @param phoneNumber
     * @return
     */
    static Pattern pattern1 = Pattern.compile("^1[0-9]{10}");

    public static boolean isMobilePhone(String phoneNumber) {
        boolean isMobile = false;
        if (TextUtils.isEmpty(phoneNumber)) {
            return isMobile;
        }
        if (phoneNumber.startsWith("10")
                || phoneNumber.startsWith("11")
                || phoneNumber.startsWith("12")) {
            return isMobile;
        }
        Matcher matcher1 = pattern1.matcher(phoneNumber);
        if (matcher1.matches()) {
            isMobile = true; // 手机号码格式不正确
        }

        return isMobile;
    }

    /**
     * 获取手机序列号
     *
     * @return 手机序列号
     */
    public static String getSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serial;

    }
}

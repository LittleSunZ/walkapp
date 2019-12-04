package com.zxc.walk.framework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull Context context) {
        AppUtils.context = context.getApplicationContext();
        if (isDebug == null) {
            isDebug = context.getApplicationInfo() != null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("u should init first");
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionNameNoDian(Context ctx) {
       return getLocalVersionName(ctx).replace(".", "");
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static Bitmap convertStringToIcon(String output) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(output, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean isDebug = null;

    public static boolean isDebug() {
        return isDebug == null ? false : isDebug.booleanValue();
    }

    /**
     * 隐藏手机号码中间四位
     *
     * @param phoneNum
     * @return
     */
    public static String phoneNumerParserFourStar(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum) && isMobilePhone(phoneNum)) {
            String phoneNumber = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
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
        Pattern pattern1 = Pattern.compile("^1[0-9]{10}");
        Matcher matcher1 = pattern1.matcher(phoneNumber);
        if (matcher1.matches()) {
            isMobile = true; // 手机号码格式不正确
        }

        return isMobile;
    }


    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

}

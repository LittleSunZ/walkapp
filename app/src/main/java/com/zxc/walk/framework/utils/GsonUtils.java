//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zxc.walk.framework.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.SoftReference;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * json转换工具类
 *
 * @author wp
 */
public class GsonUtils {
    private static final String EMPTY_JSON = "{}";
    public static final String EMPTY_JSON_ARRAY = "[]";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static SoftReference<Gson> gsonSoftReference = new SoftReference<>(null);

    private GsonUtils() {
    }

    public static String toJson(Object target, Type targetType, Gson gson) {
        if (target == null) {
            return "{}";
        } else {
            if (gson == null) {
                gson = getGson();
            }

            String result = "{}";

            try {
                if (targetType == null) {
                    result = gson.toJson(target);
                } else {
                    result = gson.toJson(target, targetType);
                }
            } catch (Exception var5) {
                Log.w(GsonUtils.class.getSimpleName(), "目标对象 " + target.getClass().getName() + " 转换 JSON 字符串时，发生异常！", var5);
                if (target instanceof Collection || target instanceof Iterator || target instanceof Enumeration || target.getClass().isArray()) {
                    result = "[]";
                }
            }

            return result;
        }
    }

    public static String toJson(Object obj) {
        return toJson(obj, null, null);
    }

    public static String toJson(Object obj, Type targetType) {
        return toJson(obj, targetType, null);
    }

    public static <T> T fromJson(String json, TypeToken<T> token) {
        return fromJson(json, token.getType());
    }

    public static <T> T fromJson(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            Gson gson = getGson();

            try {
                return gson.fromJson(json, type);
            } catch (Exception var4) {
                var4.printStackTrace();
                Log.e(GsonUtils.class.getSimpleName(), var4.getMessage(), var4);
                return null;
            }
        }
    }

    public static Gson getGson() {
        Gson gson = gsonSoftReference.get();
        if (gson == null) {
            String datePattern = "yyyy-MM-dd HH:mm:ss";
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat(datePattern);
            gson = gsonBuilder.create();
            gsonSoftReference = new SoftReference<>(gson);
        }

        return gson;
    }

    public static String parserListTToJson(List pListT) {
        String _JosnStr;
        if (pListT != null) {
            Gson _Gson = getGson();
            _JosnStr = _Gson.toJson(pListT);
        } else {
            _JosnStr = "";
        }

        return _JosnStr;
    }
}

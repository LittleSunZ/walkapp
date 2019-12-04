package com.zxc.walk.framework.utils;

import android.os.Environment;

/**
 * 路径工具类
 *
 * @author zxc
 */

public class PathUtil {

    public static String appName = "xiangdong";

    public static String mainPath = Environment.getExternalStorageDirectory().getPath();

    public static String picturePath = mainPath + "/" + appName + "/picture/";

    public static String apk = mainPath + "/" + appName + "/downloads/apk/";
}

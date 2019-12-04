/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description: 日志打印类实现文件
 * </p>
 *
 * @Title LogUtil.java
 * @Package com.unicom.zworeader.framework.util
 * @version 1.0
 * @author yijiangtao 10140151 移植
 * @date 2013-04-18
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 * 阅读客户端Android通用功能包
 */

/** 阅读客户端Android通用功能包  */
package com.zxc.walk.framework.utils;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日志打印类，汇集日志打印相关的功能函数
 * @author wp
 */
public class LogUtil {

    private final static String LOG_TAG = "LogUtil";

    /** 默认java的文件后缀名 */
    public final static String STR_DEFAULT_JAVA_FILE_EXT = ".java";

    /** 日志文件名 */
    public final static String STR_LOG_FILE_NAME = "clientlog";

    /** 备份日志文件名 */
    public final static String STR_BAK_LOG_FILE_NAME = "clientlogbak";

    /** 空字符 */
    public final static String STR_EMPTY_STRING = "";

    /** 日志输出到文件的标识，Debug版本置true，输出到文件，release版本置false输出到文件 */
    private static boolean FLAG_OUTPUT_LOGINFO_TO_FILE = true;

    /** 日志级别 */
    private static LogLevelType m_typeLogLevel = LogLevelType.TYPE_LOG_LEVEL_DEBUG;

    /** SD卡路径 */
    private static String m_strSDAdress = null;

    /** 日志文件大小，单位MB */
    private static int m_ilogFileSize = 5;

    /**
     * 日志级别类型，描述日志打印的级别
     *
     * @ClassName:LogLevelType
     * @Description: 描述日志打印的级别
     * @author: yijiangtao 10140151
     * @date: 2013-4-19
     *
     */
    public enum LogLevelType {
        /** 调试类型日志 */
        TYPE_LOG_LEVEL_DEBUG(1),
        /** 关键信息类型日志 */
        TYPE_LOG_LEVEL_INFO(2),
        /** 警告类型日志 */
        TYPE_LOG_LEVEL_WARNING(3),
        /** 错误类型日志 */
        TYPE_LOG_LEVEL_ERROR(4);

        /**
         * 构造函数,初始化枚举元素值
         *
         * @param iValue
         *            输入整数值
         */
        LogLevelType(int iValue) {
            this.m_iEnumValue = iValue;
        }

        /**
         * 获取枚举型对应的整数值
         * <p>
         * Description: 获取枚举型对应的整数值
         * <p>
         *
         * @date 2013-4-19
         * @return int型，该枚举元素对应的值
         */
        public int getValue() {
            return m_iEnumValue;
        }

        /** 枚举元素对应的整数值 */
        private final int m_iEnumValue;
    }

    /**
     * 不允许创建实例，隐藏构造函数
     */
    private LogUtil() {

    }

    /**
     *
     * 设置当前日志级别，详见LogLevelType
     *
     * @date 2013-4-19
     * @param typeLevel
     *            日志级别
     */
    public static void setLogLevel(LogLevelType typeLevel) {
        Log.d(LOG_TAG, "typeLevel=" + typeLevel);
        m_typeLogLevel = typeLevel;
    }

    /**
     *
     * 获取当前日志级别
     *
     * @date 2013-4-19
     */
    public static LogLevelType getLogLevel() {
        return m_typeLogLevel;
    }

    /**
     * 格式化日志输出
     *
     * @date 2013-4-19
     * @param strLevel
     *            日志级别
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     * @param strLineInfo
     *            行、文件名、方法名信息
     */
    private static String formatLogString(String strLevel, String strModuleName, String strInfo, String strLineInfo) {

        if (null == strModuleName || null == strInfo || null == strLineInfo) {
            return null;
        }

        // 日志格式：[D][Login][2013-04-19
        // 14:32:15.042]L[52][ZWoReaderActivity.java][onCreate] onCreate start
        String strReturnInfo = null;

        strReturnInfo = String.format("[%s][%20s][%-24s]%s%s", strLevel, strModuleName,
                TimeUtil.getSysTime().toLocaleString(), strLineInfo, strInfo);
        return strReturnInfo;
        // return String.format("%23s", TimeUtil.getFormattedLocalTimeStr());

    }

    /**
     * 格式化日志输出，如2013-04-19 14:32:15.042 F[ZWoReaderActivity] L[52] [onCreate]
     * onCreate start
     *
     * @date 2012-2-28
     * @param strInfo
     *            日志信息
     * @param strLineInfo
     *            行、文件名、方法名信息
     */
    public static String formatDateString(String strInfo, String strLineInfo) {
        if (null == strInfo || null == strLineInfo) {
            return null;
        }

        // 日志格式：Login 2013-04-19 14:32:15.042
        // L[52][ZWoReaderActivity.java][onCreate] onCreate start

        String strReturnInfo = null;

        try {
            strReturnInfo = String.format("%-23s%s%s", TimeUtil.getSysTime().toLocaleString(), strLineInfo, strInfo);
        } catch (Exception e) {
            strReturnInfo = " " + e.getMessage();
        }

        return strReturnInfo;
        // return String.format("%23s", TimeUtil.getFormattedLocalTimeStr());

    }

    /**
     *
     * 设置是否输出日志文件到文件
     *
     * @date 2013-4-19
     * @param bWrite
     *            写还是不写
     */
    public static void setLogToFileFlag(boolean bWrite) {
        FLAG_OUTPUT_LOGINFO_TO_FILE = bWrite;
    }

    /**
     * 设置日志文件存放路径
     *
     * @date 2013-4-19
     * @param strPath
     *            日志文件存放路径
     */
    public static void setLogFilePath(String strPath) {
        m_strSDAdress = strPath;
    }

    /**
     * 设置日志文件大小
     *
     * @date 2013-4-19
     * @param ilogFileSize
     *            日志文件大小，单位MB
     */
    public static void setLogFileSize(int ilogFileSize) {
        LogUtil.m_ilogFileSize = ilogFileSize;
    }

    /**
     * 打印调试日志
     *
     * @date 2013-4-19
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     */
    public static void d(String strModuleName, String strInfo) {
        // 模块名和内容不能为空
        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
            return;
        }
        Logger.t(strModuleName).d(strInfo);
//        // 只有调试级别的日志才打
//        if (LogLevelType.TYPE_LOG_LEVEL_DEBUG != m_typeLogLevel) {
//            return;
//        }
//
//        // 模块名和内容不能为空
//        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
//            return;
//        }
//
//        // 日志格式：Login 2013-04-19 14:32:15.042
//        // L[52][ZWoReaderActivity.java][onCreate] onCreate start
//
//        // Debug版本将日志写入文件，release版本将日志输出到LogCat
//        Log.d(strModuleName, formatDateString(strInfo, getLineInfo(new Throwable())));
//        if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
//            try {
//                saveLogToLocalFile(formatLogString("D", strModuleName, strInfo, getLineInfo(new Throwable())));
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    /***
     * 打印调试日志
     * 用统一TAG
     * @param strInfo 日志信息
     */
    public static void d(String strInfo) {
        if (TextUtils.isEmpty(strInfo)) {
            return;
        }
        Logger.d(strInfo);
    }

    /**
     * 打印警告日志
     *
     * @date 2013-4-19
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     */
    public static void w(String strModuleName, String strInfo) {
        // 模块名和内容不能为空
        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
            return;
        }
        Logger.t(strModuleName).w(strInfo);
        // Err级别时的日志不打印
//        if (LogLevelType.TYPE_LOG_LEVEL_ERROR == m_typeLogLevel) {
//            return;
//        }
//
//        // 模块名和内容不能为空
//        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
//            return;
//        }
//
//        // 日志格式：Login 2013-04-19 14:32:15.042
//        // L[62][ZWoReaderActivity.java][onCreate] onCreate finish
//        // Debug版本将日志写入文件，release版本将日志输出到LogCat
//        Log.w(strModuleName, formatDateString(strInfo, getLineInfo(new Throwable())));
//        if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
//            try {
//                saveLogToLocalFile(formatLogString("W", strModuleName, strInfo, getLineInfo(new Throwable())));
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    /***
     * 打印警告日志
     * 用统一TAG
     * @param strInfo 日志信息
     */
    public static void w(String strInfo) {
        if (TextUtils.isEmpty(strInfo)) {
            return;
        }
        Logger.w(strInfo);
    }

    /**
     * 打印错误日志
     *
     * @date 2013-4-19
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     */
    public static void e(String strModuleName, String strInfo) {
        // 模块名和内容不能为空
        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
            return;
        }
        Logger.t(strModuleName).e(strInfo);
//
//        // 日志格式：Login 2013-04-19 14:32:15.042
//        // L[63][ZWoReaderActivity.java][onCreate] onCreate finish
//        // Debug版本将日志写入文件，release版本将日志输出到LogCat
//        Log.e(strModuleName, formatDateString(strInfo, getLineInfo(new Throwable())));
//        if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
//            try {
//                saveLogToLocalFile(formatLogString("E", strModuleName, strInfo, getLineInfo(new Throwable())));
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    /***
     * 打印错误日志
     * 用统一TAG
     * @param strInfo 日志信息
     */
    public static void e(String strInfo) {
        if (TextUtils.isEmpty(strInfo)) {
            return;
        }
        Logger.e(strInfo);
    }

    /**
     * 打印提示日志
     *
     * @date 2013-4-19
     * @param strModuleName
     *            模块名称描述
     * @param strInfo
     *            日志信息
     */
    public static void i(String strModuleName, String strInfo) {
        // 模块名和内容不能为空
        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
            return;
        }
        Logger.t(strModuleName).i(strInfo);
//        // Err\Warn级别时的日志不打印
//        if (LogLevelType.TYPE_LOG_LEVEL_ERROR == m_typeLogLevel
//                || LogLevelType.TYPE_LOG_LEVEL_WARNING == m_typeLogLevel) {
//            return;
//        }
//        // 模块名和内容不能为空
//        if (TextUtils.isEmpty(strModuleName) || TextUtils.isEmpty(strInfo)) {
//            return;
//        }
//
//        // 日志格式：Login 2013-04-19 14:32:15.042
//        // L[64][ZWoReaderActivity.java][onCreate] onCreate finish
//        // Debug版本将日志写入文件，release版本将日志输出到LogCat
//        Log.i(strModuleName, formatDateString(strInfo, getLineInfo(new Throwable())));
//
//        if (FLAG_OUTPUT_LOGINFO_TO_FILE) {
//            try {
//                saveLogToLocalFile(formatLogString("I", strModuleName, strInfo, getLineInfo(new Throwable())));
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
    }

    /***
     * 打印提示日志
     * 用统一TAG
     * @param strInfo 日志信息
     */
    public static void i(String strInfo) {
        if (TextUtils.isEmpty(strInfo)) {
            return;
        }
        Logger.i(strInfo);
    }

    /**
     * 获取代码行的信息，包括文件名、行号、函数名等信息
     *
     * @date 2013-4-19
     * @param ta
     *            Throwable对象
     * @return String类型，包含格式化的文件名、行号、函数名。
     */
    public static String getLineInfo(Throwable ta) {
        if (null == ta) {
            return null;
        }

        // 行号
        String strLineNumber = null;
        String strFileName = null;
        String strFileNamePostfix = null;
        String strMethodName = null;

        // 获得格式字符串-行号、文件名、格式字符串-方法名
        StackTraceElement[] stackTraceElement = ta.getStackTrace();
        if (null != stackTraceElement && stackTraceElement.length > 1) {
            int iLineNumberTemp = -1;
            iLineNumberTemp = stackTraceElement[1].getLineNumber();

            if (-1 == iLineNumberTemp) {
                Log.w(LOG_TAG, "iLineNumberTemp = " + iLineNumberTemp);
                return STR_EMPTY_STRING;
            } else {
                strLineNumber = String.format("L[%d]", iLineNumberTemp);
            }

            String strFileNameTemp = stackTraceElement[1].getFileName();
            if (TextUtils.isEmpty(strFileNameTemp)) {
                Log.w(LOG_TAG, "strFileNameTemp = " + strFileNameTemp);
                return STR_EMPTY_STRING;
            } else {
                strFileName = strFileNameTemp;
            }

            String strMethodNameTemp = stackTraceElement[1].getMethodName();
            if (TextUtils.isEmpty(strMethodNameTemp)) {
                Log.w(LOG_TAG, "strMethodNameTemp = " + strMethodNameTemp);
                return STR_EMPTY_STRING;
            } else {
                strMethodName = strMethodNameTemp;
            }

            // 若文件名以.java为后缀，则去除后缀
            if (strFileName.substring(strFileName.length() - STR_DEFAULT_JAVA_FILE_EXT.length())
                    .equalsIgnoreCase(STR_DEFAULT_JAVA_FILE_EXT)) {
                strFileNamePostfix = strFileName.substring(0,
                        strFileName.length() - STR_DEFAULT_JAVA_FILE_EXT.length());
            } else {
                strFileNamePostfix = strFileName;
            }

            // 格式化文件名字符串
            // strFileName = null;
            strFileName = String.format("F[%s]", strFileNamePostfix);

            // 返回值："F[ZWoReaderActivity] L[51] [onCreate] "
            return String.format("%-23s%-7s%-22s", strFileName, strLineNumber, strMethodName);
        } else {
            Log.w(LOG_TAG, "stackTraceElement = " + stackTraceElement);
            return STR_EMPTY_STRING;
        }
    }

    /**
     * 日志写入本地文件， 日志信息写入SD卡本地文件，如果存在指定文件则写入，不存在则创建文件后写入 限制文件大小，若超过10M，则删除文件，重新创建。
     *
     * @date 2013-4-19
     * @param strLogInfo
     *            日志信息
     * @throws IOException
     *             输入输出异常
     */
    public synchronized static void saveLogToLocalFile(String strLogInfo) throws IOException {
        if (TextUtils.isEmpty(strLogInfo)) {
            return;
        }

        if (TextUtils.isEmpty(m_strSDAdress)) {
            return;
        }

        int iReturn = 0;
        // 日志创建时间
        String strLogFileCreateTime = null;
        Date dateLogFileCreate = null;
        // Calendar类的日志创建时间及当前时间
        Calendar calOldLogFileCreateTime = null;
        Calendar calNewLogFileCreateTime = null;

        // 日志文件、备份文件
        File fileLog = null;
        File fileLogBak = null;
        // 文件输出流
        FileOutputStream outputStream = null;

        // 读文件
        BufferedReader bufReader = null;

        long lDaysBetween = 0;
        fileLog = new File(m_strSDAdress + STR_LOG_FILE_NAME);
        fileLogBak = new File(m_strSDAdress + STR_BAK_LOG_FILE_NAME);

        iReturn = FileUtil.append2File(fileLog, TimeUtil.format(TimeUtil.getSysTime(), "yyyyMMddHHmmss") + "\n");
        if (FileUtil.FILE_UTIL_STATUS_FILE_EXIST == iReturn) {
            // 日志文件存在时，检查文件大小,超过5M则删除文件，重新创建
            if ((fileLog.length() / (1024.0 * 1024.0)) > m_ilogFileSize) {
                if (fileLogBak.exists()) {
                    fileLogBak.delete();
                }
                fileLog.renameTo(new File(m_strSDAdress + STR_BAK_LOG_FILE_NAME));

                fileLog = new File(m_strSDAdress + STR_LOG_FILE_NAME);

                FileUtil.appendContent2File(fileLog, TimeUtil.format(TimeUtil.getSysTime(), "yyyyMMddHHmmss") + "\n");

            }
            // 检查日志存在时间，超过7天则备份文件，删除上一次的备份文件（如果存在的话）。
            // 重新创建Iptv.log
            FileReader fileReader = new FileReader(m_strSDAdress + STR_LOG_FILE_NAME);
            bufReader = new BufferedReader(fileReader);
            strLogFileCreateTime = bufReader.readLine();
            if (null == strLogFileCreateTime) {
                bufReader.close();
                fileReader.close();
                return;
            }

            SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            try {
                dateLogFileCreate = sdfDateFormat.parse(strLogFileCreateTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (fileLogBak.exists()) {
                    fileLogBak.delete();
                }
                fileLog.renameTo(new File(m_strSDAdress + STR_BAK_LOG_FILE_NAME));
                bufReader.close();
                fileReader.close();
                return;

            }

            calOldLogFileCreateTime = Calendar.getInstance();
            calOldLogFileCreateTime.set(dateLogFileCreate.getYear() + 1900, dateLogFileCreate.getMonth(),
                    dateLogFileCreate.getDate(), dateLogFileCreate.getHours(), dateLogFileCreate.getMinutes(),
                    dateLogFileCreate.getSeconds());

            bufReader.close();
            fileReader.close();
            calNewLogFileCreateTime = Calendar.getInstance();
            while (calOldLogFileCreateTime.before(calNewLogFileCreateTime) && lDaysBetween <= 7) {
                calOldLogFileCreateTime.add(Calendar.DAY_OF_MONTH, 1);
                lDaysBetween++;
            }
            if (lDaysBetween > 7) {
                if (fileLogBak.exists()) {
                    fileLogBak.delete();
                }
                fileLog.renameTo(new File(m_strSDAdress + STR_BAK_LOG_FILE_NAME));
                fileLog = new File(m_strSDAdress + STR_LOG_FILE_NAME);

                FileUtil.appendContent2File(fileLog, TimeUtil.format(TimeUtil.getSysTime(), "yyyyMMddHHmmss") + "\n");

                calOldLogFileCreateTime = Calendar.getInstance();
            }
        }

        // 创建文件输出流对象
        outputStream = new FileOutputStream(fileLog, true);
        // 写日志到文件中
        outputStream.write((strLogInfo + "\n").getBytes("utf-8"));

        // 关闭文件流
        outputStream.close();
    }

    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        String ret = sw.toString();
        try {
            sw.close();
            pw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return ret;
    }
}

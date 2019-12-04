/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description: 时间工具类实现文件
 * </p>
 * @Title TimeUtil.java
 * @Package com.unicom.zworeader.framework.util
 * @version 1.0
 * @author zhang.longfei 10126853
 * @date 2012-02-11
 */

/** 阅读客户端Android通用功能包  */
package com.zxc.walk.framework.utils;


import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * 时间工具类，汇集时间获取、时间转换时间处理函数
 * @ClassName:TimeUtil 
 * @Description: 汇集时间获取、时间转换时间处理函数
 * @author: zhang.longfei 10126853
 * @date: 2012-02-06
 *
 */
public final class TimeUtil
{

    /** 时间格式yyyyMMddHHmmss */
    public static final String STR_FORMAT_DATE_TIME = "yyyyMMddHHmmss";
    /** 时间格式 yyyyMMdd */
    public static final String STR_FORMAT_DATE = "yyyyMMdd";

    /** 时间格式 yyyy-MM-dd */
    public static final String STR_FORMAT_DATE_WITH_DASH = "yyyy-MM-dd";
    /** 时间格式 yyyy年MM月dd日*/
    public static final String STR_FORMAT_DATE_TIME_WITH_HANZI = "yyyy年MM月dd日";
    /** 服务器时区 */
    static TimeZone mServerTimeZone = TimeZone.getDefault();
    /** 地区 */
    static Locale mLocale = Locale.getDefault();


    /**
     * 不允许创建实例，隐藏构造函数
     */
    private TimeUtil()
    {
    }

    /**
     * 
     * @Deprecated
     * <p>
     * Description: 获取服务器时区,一般是本地时区.一般无需使用
     * <p>
     * 
     * @return 服务器时区
     */
    @Deprecated
    public static TimeZone getServerTimeZone()
    {
        return mServerTimeZone;
    }


    /**
     * getSysTime
     * <p>
     * Description: 取系统本地日历设置的时间
     * <p>
     * 
     * @return 系统本地日历时间
     */
    public static Date getSysTime()
    {
        return Calendar.getInstance().getTime();
    }

    /**
     * format
     * <p>
     * Description: 格式化本地时间
     * <p>
     * 
     * @param date 待转日期
     * @param strOutFormat 转换后格式
     * @return 格式化的字符型日期
     *         转换失败,返回""
     */
    public static String format(Date date, String strOutFormat)
    {
        if (null == date || TextUtils.isEmpty(strOutFormat))
        {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(strOutFormat, mLocale);
        sdf.setTimeZone(mServerTimeZone);
        return sdf.format(date);
    }
    
    
    /**
     * 
     * <p>
     * Description: 根据给定时间，获取新格式的时间串
     * <p>
     * @date 2013-4-25 
     * @author yijiangtao 10140151
     * @param strTime 待转格式的时间串
     * @param strSrcTimeType  原时间字符串描述，形如yyyy.MM.dd HH:mm:ss
     * @param strDesTimeType  新时间字符串描述，形如MM.dd.yyyy HH:mm:ss
     * @return 新格式的字符串 例如04.25.2013 02:35:28, 错误返回null
     */
    public static String transformTimeFormat(String strTime, String strSrcTimeType,
                                             String strDesTimeType)
    {
        if (TextUtils.isEmpty(strSrcTimeType)
            || TextUtils.isEmpty(strDesTimeType)
            || TextUtils.isEmpty(strTime))
        {
            return null;
        }
        if (!checkTimeFormatType(strTime, strSrcTimeType))
        {
            return null;
        }
        
        String strNewTypeTime;
        Date dateTime = null;
        SimpleDateFormat sdfDateFormat = null;
        try
        {
            dateTime = getDateByStrDate(strTime, strSrcTimeType);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return null;
        }

        sdfDateFormat = new SimpleDateFormat(strDesTimeType);
        strNewTypeTime = sdfDateFormat.format(dateTime);

        return strNewTypeTime;
    }

    /**
     * 
     * <p>
     * Description: 根据原时间字符串格式yyyyMMddHHmmss，获取新格式的时间串
     * <p>
     * @date 2012-4-24 
     * @author yijiangtao 10140151
     * @param strTime 待转格式的时间串
     * @param strDesTimeType  新时间字符串描述，形如MM.dd.yyyy HH:mm:ss
     * @return 新格式的字符串 例如04.25.2013 02:35:28, 错误返回null
     */
    public static String transformTimeFormat(String strTime, String strDesTimeType)
    {
        if (TextUtils.isEmpty(strDesTimeType) || TextUtils.isEmpty(strTime))
        {
            return null;
        }
        if (!checkTimeFormatType(strTime, STR_FORMAT_DATE_TIME))
        {
            return null;
        }
        
        String strNewTypeTime;
        Date dateTime = null;
        SimpleDateFormat sdfDateFormat = null;
        try
        {
            dateTime = getDateByStrDate(strTime, STR_FORMAT_DATE_TIME);
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        sdfDateFormat = new SimpleDateFormat(strDesTimeType);
        strNewTypeTime = sdfDateFormat.format(dateTime);

        return strNewTypeTime;
    }
    
    /**
     * <p>
     * Description: 根据给定的字符串日期和对应的格式信息，解析出日期
     * <p>
     * @date 2013-4-24 
     * @author yijiangtao 10140151
     * @param strDate 字符串日期，如"2013-4-24 09:12:32"
     * @param strFormatType 字符串日期对应的格式字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return 日期, 错误返回null
     * @throws ParseException 日期解析错误
     */
    public static Date getDateByStrDate(String strDate, String strFormatType)
            throws ParseException
    {
        if (TextUtils.isEmpty(strDate) || TextUtils.isEmpty(strFormatType))
        {
            return null;
        }
        
        if (!checkTimeFormatType(strDate, strFormatType))
        {
            return null;
        }
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(strFormatType);
        return sdfDateFormat.parse(strDate);
    }
    
    /**
     * 
     * <p>
     * Description: 判断时间是否为指定格式
     * <p>
     * @date 2013-4-24 
     * @author yijiangtao 10140151
     * @param strDate 时间串
     * @param strTimeType 待识别时间格式
     * @return 格式相符返回true，错误或异常返回false
     */
    public static boolean checkTimeFormatType(String strDate, String strTimeType)
    {
        if (TextUtils.isEmpty(strDate) || TextUtils.isEmpty(strTimeType))
        {
            return false;
        }
        
        Date date = null;
        DateFormat dateFormat = null;
        try
        {
            dateFormat = new SimpleDateFormat(strTimeType);
            date = dateFormat.parse(strDate);
        }
        catch(Exception e)
        {
            //如果不能转换,肯定是错误格式
            return false;
        }
//        String strNewTime = dateFormat.format(date);
//        // 转换后的日期再转换回String,如果不等,逻辑错误.如format为"yyyy-MM-dd",date为
//        // "2013-04-24",转换为日期后再转换回字符串为"2013-03-03",说明格式虽然对,但日期
//        // 逻辑上不对.
//        return strDate.equals(strNewTime);
        return true;
    }
    
    
    /*
     * Return the offset by millisecond (endTime - startTime).
     */
    public static long getDiffTime(String startTime, String endTime, String pattern)
    {
       SimpleDateFormat sdf = new SimpleDateFormat(pattern);
       
       try 
       {
          Date d1 = sdf.parse(startTime);
          Date d2 = sdf.parse(endTime);
          
          return d1.getTime() - d2.getTime();
       }
       catch (ParseException e)
       {
          e.printStackTrace();
       }
       
       return 0;
    }
    
    /**
     * 获取指定时间对应的毫秒数
     * @param time "HH:mm:ss"
     * @return
     */
    public static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static int getLongTime(String mmss){
        SimpleDateFormat sdf= new SimpleDateFormat("mm:ss");
        Date start = null;
        int time = 0;
        try {
            start = sdf.parse(mmss);
            int minutes = start.getMinutes();
            int seconds = start.getSeconds();
            if(0 != minutes){
                time = minutes*60;
            }
            if(0 != seconds){
                time += seconds;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time*1000;
    }

    /**
     * 获取当前星期几
     * @param stringDate
     * @return
     */
    public static int getWeekOfDate(String stringDate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateByStrDate(stringDate, TimeUtil.STR_FORMAT_DATE_WITH_DASH));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    public enum SimpleChieseWeek{
        Sunday("日",0),Monday("一",1),Tuesday("二",2),Wednesday("三",3),Thursday("四",4),Friday("五",5),Saturday("六",6);
        private String name;
        private int index;
        SimpleChieseWeek(String name, int index){
            this.name = name ;
            this.index = index;
        }

        public static String getName(int index){
            for (SimpleChieseWeek s : SimpleChieseWeek.values()){
                if(s.index == index){
                    return s.name;
                }
            }
            return "";
        }
    }


    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年
    /**
     * 返回文字描述的日期
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = System.currentTimeMillis() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }


    /**
     *
     * @param timeParam 参数为分钟
     * @return
     */
    public static String formatTimeSsMm(long timeParam) {

        int second = (int) (timeParam %60);
        int minuteTemp = (int) (timeParam / 60);
        if (minuteTemp > 0) {
            return (minuteTemp >= 10 ? (minuteTemp + "") : ("0" + minuteTemp)) + ":"
                    + (second >= 10 ? (second + "") : ("0" + second));
        } else {
            return "00:"+ (second >= 10 ? (second + "") : ("0" + second));
        }
    }

    /***
     * 时分秒转换
     * @param time
     * @return
     */
    public static String formatTimeHhSsMm(long time){
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }
}

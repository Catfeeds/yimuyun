package com.yimuyun.lowraiseapp.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.currentTimeMillis;

/**
 * Created by codeest on 16/8/13.
 */

public class DateUtil {
    public static final String DATE_FORMATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMATE_YYYY_年_MM_月_DD日 = "yyyy年MM月dd日";
    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date());
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getTomorrowDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return String.valueOf(Integer.valueOf(df.format(new Date())) + 1);
    }

    /**
     * 获取当前日期字符串
     * @return
     */
    public static String getCurrentDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(new Date());
    }

    /**
     * 获取当前年
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取当前日
     * @return
     */
    public static int getCurrentDay() {
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    /**
     * 切割标准时间
     * @param time
     * @return
     */
    @Nullable
    public static String subStandardTime(String time) {
        int idx = time.indexOf(".");
        if (idx > 0) {
            return time.substring(0, idx).replace("T"," ");
        }
        return null;
    }

    /**
     * 将时间戳转化为字符串
     * @param showTime
     * @return
     */
    public static String formatTime2String(long showTime) {
        return formatTime2String(showTime,false);
    }

    public static String formartTime2String(long showTime){
        Calendar current = Calendar.getInstance();
        current.setTime(new Date(showTime));
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);
        return year+"年"+(month+1)+"月"+(day+1)+"日";
    }

    public static String formartTime2String_(long showTime){
        Calendar current = Calendar.getInstance();
        current.setTime(new Date(showTime));
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH)+1;
        int day = current.get(Calendar.DAY_OF_MONTH)+1;
        if(month<10){
            if(day<10){
                return year+"0"+month+"0"+day;
            }else{
                return year+"0"+month+day;
            }
        }else{
            if(day<10){
                return year+month+"0"+day;
            }else{
                return year+""+month+day;
            }
        }
    }

    public static String formatTime2String(long showTime , boolean haveYear) {
        String str = "";
        long distance = currentTimeMillis()/1000 - showTime;
        if(distance < 300){
            str = "刚刚";
        }else if(distance >= 300 && distance < 600){
            str = "5分钟前";
        }else if(distance >= 600 && distance < 1200){
            str = "10分钟前";
        }else if(distance >= 1200 && distance < 1800){
            str = "20分钟前";
        }else if(distance >= 1800 && distance < 2700){
            str = "半小时前";
        }else if(distance >= 2700){
            Date date = new Date(showTime * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = formatDateTime(sdf.format(date) , haveYear);
        }
        return str;
    }

    public static String formatDate2String(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(time == null){
            return "未知";
        }
        try {
            long createTime = format.parse(time).getTime() / 1000;
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime - createTime - 24 * 3600 > 0) { //超出一天
                return (currentTime - createTime) / (24 * 3600) + "天前";
            } else {
                return (currentTime - createTime) / 3600 + "小时前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    public static String formatDateTime(String time ,boolean haveYear) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(time == null){
            return "";
        }
        Date date;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        Calendar yesterday = Calendar.getInstance();
        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);
        if(current.after(today)){
            return "今天 "+time.split(" ")[1];
        }else if(current.before(today) && current.after(yesterday)){
            return "昨天 "+time.split(" ")[1];
        }else{
            if(haveYear) {
                int index = time.indexOf(" ");
                return time.substring(0,index);
            }else {
                int yearIndex = time.indexOf("-")+1;
                int index = time.indexOf(" ");
                return time.substring(yearIndex,time.length()).substring(0,index);
            }
        }
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 传入当前时间往之前移动day天得到指定的格式
     * @param date
     * @param pattern
     * @param day
     * @return
     */
    public static String formatBeforeDate(Date date,String pattern,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,day);
        Date beforeDate = calendar.getTime();
        return format(beforeDate,pattern);
    }

    public static boolean compareAfterDate(String dateStr1,String dateStr2,String pattern){
        Date date1,date2;
        try {
            date1 = parse(dateStr1, pattern);
            date2 = parse(dateStr2, pattern);
        }catch (Exception e){
            return false;
        }
        return date1.after(date2);
    }


}

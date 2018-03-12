package com.poso2o.lechuan.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by mr zhang on 2017/6/19.
 * 日历工具类
 */

public class CalendarUtil {


    //当天
    public static String getDate() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        //date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//SSS
        String dateString = formatter.format(date);
        return dateString;
    }

    // 获取本年第一天
    public static String getYearFirstDay(){
        // 规定返回日期格式
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "-" + "01" + "-" + "01";
    }

    // 获得本月第一天
    public static String getFirstDay() {
        // 规定返回日期格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        // 设置为第一天
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        return sf.format(gcLast.getTime());
    }

    // 七天前
    public static String getSevenDaysDate() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -6);//把日期往后增加一天.整数往后推,负数往前移动
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    // 当天
    public static String getTodayDate() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        //date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);
    }


    /**
     * 时间大小比较
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return          若开始时间大于结束时间返回false，否则返回true
     */
    public static boolean TimeCompare(String beginDate, String endDate) {
        // 格式化时间
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date beginTime = CurrentTime.parse(beginDate);
            Date endTime = CurrentTime.parse(endDate);
            // 判断是否大于两天
            return (endTime.getTime() - beginTime.getTime()) >= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 调用此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String timeStamp(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            times = String.valueOf(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
}
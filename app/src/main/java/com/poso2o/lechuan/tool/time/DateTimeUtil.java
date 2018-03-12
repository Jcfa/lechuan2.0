package com.poso2o.lechuan.tool.time;

import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.util.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by J on 2017/1/18.
 */

public class DateTimeUtil {

    public static String LongToConsume(long l) {
        if (l > 1000 * 60 * 60) {
            return LongToData(l, "HH时mm分ss秒");
        }
        return LongToData(l, "mm分ss秒");
    }

    public static String LongToTime(long l) {
        if (l == 0) {
            return " - - : - - : - - ";
        }
        return LongToData(l, "HH:mm:ss");
    }

    public static String LongToData(long l, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        // 若传进来的是秒为单位则需要*1000
        return dateFormat.format(new Date(l));
    }

    public static String StringToData(String str, String pattern) {
        if (str != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Long lon = Long.valueOf(str);
            //若传进来的是秒为单位则需要*1000
            return dateFormat.format(new Date(lon));
        } else {
            return null;
        }
    }


    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 调此方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String stringToDate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 13);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    // 获得本月第一天
    public static String getFirstDay() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        return dateFormater.format(cal.getTime()) + " 00:00:00 000";
    }

    //当天
    public static String getTodayDate() {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormater.format(cal.getTime()) + "";
    }

    //时间大小比较
    private static boolean TimeCompare(BaseActivity activity, String beginDate, String endDate) {
        //格式化时间
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date beginTime = CurrentTime.parse(beginDate);
            Date endTime = CurrentTime.parse(endDate);
            //判断是否大于两天
            if (!((endTime.getTime() - beginTime.getTime()) >= 0)) {
                Toast.show(activity, "选择日期范围不正确");
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}

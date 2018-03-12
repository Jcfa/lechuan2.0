package com.poso2o.lechuan.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by J on 2017/1/18.
 */

public class DateTimeUtil {

    public static String LongToData(Long str, String pattern){
        if(str!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            // 若传进来的是秒为单位则需要 * 1000
            return dateFormat.format(new Date(str));
        }else{
            return null;
        }
    }

    public static String StringToData(String str,String pattern){
        if(str!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Long lon = Long.valueOf(str);
            //若传进来的是秒为单位则需要*1000
            return dateFormat.format(new Date(lon));
        }else{
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
            times = stf.substring(0, 13);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 传入一个时间字符串，返回该时间的时间戳
     * @param time
     * @return
     */
    public static long stringToLong(String time){
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            return l;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
}

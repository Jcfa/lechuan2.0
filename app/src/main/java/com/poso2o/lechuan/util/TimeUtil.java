package com.poso2o.lechuan.util;

import android.icu.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017-12-02.
 */

public class TimeUtil {
    /**
     * 时间戳转指定格式日期字符串
     * @param time 需要转换的时间戳
     * @param format 转换后的时间格式 例yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String longToDateString(long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(time));
    }
}

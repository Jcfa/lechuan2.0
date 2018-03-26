package com.poso2o.lechuan.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${cbf} on 2018/3/26 0026.
 */

public class InsertPoint {

    // assign为原字符串
    public static String Stringinsert(String assign) {
        StringBuilder sb = new StringBuilder();
        String str = null;
        for (int i = 0; i < assign.length(); i++) {
            sb.append(assign.charAt(0));
            sb.append(".");
            sb.append(assign.charAt(1));
            sb.append(assign.charAt(2));
            sb.append("W");
            assign = sb.substring(0, 5);
        }
        str = sb.substring(0, 5);
        return str;
    }

    //如果插入的是字符
    public static String Stringinsert(String a, char b, int t) {
        return a.substring(0, t) + b + a.substring(t + 1, a.length());
    }

    public static String getNumber(String str) {
        // 需要取整数和小数的字符串
        // 控制正则表达式的匹配行为的参数(小数)
        Pattern p = Pattern.compile("(\\d+\\.+)");
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = p.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            str = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                str = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                str = "";
            }
        }
        return str;
    }
}

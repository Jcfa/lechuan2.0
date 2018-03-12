/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.util;

import android.annotation.SuppressLint;

import com.poso2o.lechuan.configs.AppConfig;

import org.apache.http.ProtocolException;
import org.apache.http.conn.util.InetAddressUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * 工具类
 * 
 * @author apple
 */
@SuppressWarnings("deprecation")
public class WXPayUtils {

    /**
     * 微信支付签名算法sign
     */
    @SuppressLint("DefaultLocale")
    @SuppressWarnings("rawtypes")
    public static String createSign(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + AppConfig.WEIXIN_APPKEY);
        LogUtils.i("sign = " + sb.toString());
        return MD5Util.MD5Encode(sb.toString()).toUpperCase();// + "key=" + Common.WX_API_KEY;
    }

    /**
     * 获取微信支付随机字符串
     * 
     * @param length
     * @return
     * @author Zheng Jie Dong
     * @date 2016-11-23
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        } 
        return SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_MOBILE) + "_" + sb.toString();
    }

    /**
     * 要求外部订单号必须唯一。
     * 
     * @return
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_MOBILE) + "_" + key;
    }
    
    /**
     * 要求外部订单号必须唯一。
     * 
     * @return
     */
    public static String getWeixinOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * 获取IPV4地址
     *
     * @return
     * @author Zheng Jie Dong
     * @date 2016-11-23
     */
    public static String getIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements();) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    // ipv4地址
                    if (!inetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static XmlPullParser httpsRequestToXML(String requestUrl, String requestMethod, String outputStr) {
        try {
            StringBuffer buffer = httpsRequest(requestUrl, requestMethod, outputStr);
            LogUtils.i("请求结果" + buffer.toString());
            String xml = buffer.toString();
            // result = MessageUtil.parseXml(buffer.toString());
            ByteArrayInputStream tInputStringStream = null;
            try {
                if (!xml.trim().equals("")) {
                    tInputStringStream = new ByteArrayInputStream(xml.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(tInputStringStream, "utf-8");
            return parser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static StringBuffer httpsRequest(String requestUrl, String requestMethod, String output)
            throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException, MalformedURLException,
            IOException, ProtocolException, UnsupportedEncodingException {
        URL url = new URL(requestUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod(requestMethod);
        if (null != output) {
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(output.getBytes("utf-8"));
            outputStream.close();
        }
        // 从输入流读取返回内容
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        StringBuffer buffer = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        inputStream = null;
        connection.disconnect();
        return buffer;
    }
}

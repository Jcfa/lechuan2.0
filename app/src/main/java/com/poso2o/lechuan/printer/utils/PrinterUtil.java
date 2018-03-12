package com.poso2o.lechuan.printer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.printer.PrinterBluetoothActivity;
import com.poso2o.lechuan.printer.command.EscCommand;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrinterUtil {

    //订单数据
    private OrderDTO mOrderData;

    public PrinterUtil() {
    }

    /**
     * ============================================================================ 打印收银销售小票内容
     */
    public EscCommand printerCashText(BaseActivity activity, EscCommand esc, OrderDTO orderDTO) {

        this.mOrderData = orderDTO;

        /*打印图片*/
        //esc.addText("Print bitmap!\n");   //  打印文字
        //Bitmap b = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logo_d);
        //esc.addRastBitImage(b,b.getWidth(),0);   //打印图片

        int maxLen = 32;//48;
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);//设置为倍高倍宽
        if (true) {
            maxLen = 32;
            esc.addSetCharcterSize(EscCommand.WIDTH_ZOOM.MUL_2, EscCommand.HEIGHT_ZOOM.MUL_2);
        } else {
            maxLen = 48;
            esc.addSetCharcterSize(EscCommand.WIDTH_ZOOM.MUL_3, EscCommand.HEIGHT_ZOOM.MUL_3);
        }

        // 设置字体大小
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        if (true) {
            esc.addSetCharcterSize(EscCommand.WIDTH_ZOOM.MUL_1, EscCommand.HEIGHT_ZOOM.MUL_1);
        } else {
            esc.addSetCharcterSize(EscCommand.WIDTH_ZOOM.MUL_1, EscCommand.HEIGHT_ZOOM.MUL_2);
        }

        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
        esc.addText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK) + "销售单\n");

        /*打印文字*/
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < maxLen; i++) {
            sb.append("-");
        }
        sb.append("\n");

        // 日期
        String date = "日期:" + DateTimeUtil.StringToData("" + mOrderData.build_time, "yyyy-MM-dd HH:mm:ss");
        sb.append(date + "\n");
        // 单号
        String orderid = "订单:" + mOrderData.order_id;
        sb.append(orderid + "\n");

        // 客户
        String customerName = mOrderData.receipt_name;
        ;
        if ("".equals(customerName)) {//普通客户
            //customerName = "普通客户";
            //sb.append(customerName + "\n");
        } else {
            String kehu = "收货人:" + customerName;
            sb.append(kehu + "\n");
            // 客户手机
            String customerMoblie = "手机:" + mOrderData.receipt_mobile;
            sb.append(customerMoblie + "\n");
            //客户地址
            String customerAddress = mOrderData.receipt_province_name + mOrderData.receipt_city_name + mOrderData.receipt_area_name + mOrderData.receipt_address;
            System.out.println("X:" + customerAddress);
            if (!customerAddress.equals("")) {
                String customerAddressTitle = "地址:";
                int customernameLen = printerLengths(customerAddress);
                int customerlineLen = maxLen - printerLengths(customerAddressTitle);
                int customernameSize = customerAddress.length();
                if (customernameLen > customerlineLen) {// 如果超过一行
                    int line = customernameLen / customerlineLen;
                    if (customerlineLen * line > customernameLen)
                        line = line + 1;
                    int startIndex = 0;
                    sb.append(customerAddressTitle);
                    for (int first = 0; first < customernameSize; first++) {// 第一行
                        String firstLine = customerAddress.substring(0, first);
                        if (printerLengths(firstLine) >= customerlineLen - 1) {
                            startIndex = first;
                            sb.append(firstLine + "\n");
                            break;
                        }
                    }
                    int nameTitleLen = printerLengths(customerAddressTitle);
                    for (int n = 0; n < line - 1; n++) {
                        for (int i = 0; i < customernameSize; i++) {
                            String newName = customerAddress.substring(startIndex, startIndex + i);
                            if (printerLengths(newName) >= customerlineLen - 1) {
                                sb.append(getBlankNumber(nameTitleLen));
                                sb.append(newName + "\n");
                                startIndex = startIndex + i;
                                break;
                            }
                        }
                    }
                    if (startIndex < customernameSize) {
                        String newName = customerAddress.substring(startIndex, customernameSize);
                        sb.append(getBlankNumber(nameTitleLen));
                        sb.append(newName + "\n");
                    }
                } else {
                    sb.append(customerAddressTitle + customerAddress + "\n");
                }
            }
        }
        for (int i = 0; i < maxLen; i++) {
            sb.append("-");
        }
        sb.append("\n");
        // 商品
        sb.append("品名/货号\n");
        String ggT = "规格";
        String slT = "数量";
        String djT = "单价";
        String jeT = "金额";
        int ggTLen = printerLengths(ggT);
        sb.append(ggT);
        // 9,9,5,9
        // 14,14,6,14
        int len1 = 12;
        int len2 = 8;
        int len3 = 4;
        if (true) {
            len1 = 12;
            len2 = 8;
            len3 = 4;
        } else {//48
            len1 = 16;
            len2 = 12;
            len3 = 8;
        }
        sb.append(getBlankNumber(len1 - ggTLen));
        int slTLen = printerLengths(slT);
        sb.append(slT);
        sb.append(getBlankNumber(len3 - slTLen));
        int djTLen = printerLengths(djT);
        sb.append(getBlankNumber(len2 - djTLen));
        sb.append(djT);
        int jeTLen = printerLengths(jeT);
        sb.append(getBlankNumber(len2 - jeTLen));
        sb.append(jeT + "\n");

        String spec = "";

        for (int i = 0; i < mOrderData.order_goods.size(); i++) {

            String bh = mOrderData.order_goods.get(i).goods_no;
            String name = mOrderData.order_goods.get(i).goods_name;
            sb.append("\n" + name);
            sb.append("/" + bh + "\n");

            String spec_name = mOrderData.order_goods.get(i).goods_spec_name;

            if (!"".equals(mOrderData.order_goods.get(i).goods_discount) && mOrderData.order_goods.get(i).goods_discount != 100 && mOrderData.order_goods.get(i).goods_discount > 0) {

                String gg = spec;//规格
                String sl = "" + mOrderData.order_goods.get(i).purchase_munber;//数量
                String dj = "" + toDoubleNum("" + mOrderData.order_goods.get(i).spec_price);//单价
                String je = "";//金额
                int ggLen = printerLengths(gg);
                sb.append(gg);
                sb.append(getBlankNumber(len1 - ggLen));
                int slLen = printerLengths("");
                sb.append("");
                sb.append(getBlankNumber(len3 - slLen));
                int djLen = printerLengths(dj);
                sb.append(getBlankNumber(len2 - djLen));
                sb.append(dj);
                int jeLen = printerLengths(je);
                sb.append(getBlankNumber(len2 - jeLen));
                sb.append(je + "\n");

                String x = "折扣:" + toSingleNum("" + mOrderData.order_goods.get(i).goods_discount) + "%";
                String xx = "" + mOrderData.order_goods.get(i).purchase_munber;
                String xxx = toDoubleNum(""+mOrderData.order_goods.get(i).goods_amount);
                String xxxx = "" + toDoubleNum("" + toDouble(xx) * toDouble(""+mOrderData.order_goods.get(i).goods_amount));
                int xLen = printerLengths(x);
                sb.append(x);
                sb.append(getBlankNumber(len1 - xLen));
                int xxLen = printerLengths(xx);
                sb.append(sl);
                sb.append(getBlankNumber(len3 - xxLen));
                int xxxLen = printerLengths(xxx);
                sb.append(getBlankNumber(len2 - xxxLen));
                sb.append(xxx);
                int xxxxLen = printerLengths(xxxx);
                sb.append(getBlankNumber(len2 - xxxxLen));
                sb.append(xxxx + "\n");
            } else {
                String gg = spec;//规格
                String sl = "" + mOrderData.order_goods.get(i).purchase_munber;//数量
                String dj = toDoubleNum("" + mOrderData.order_goods.get(i).spec_price) + "";//单价
                String je = toDoubleNum("" + (toDouble(sl) * toDouble(dj))) + "";//金额
                int ggLen = printerLengths(gg);
                sb.append(gg);
                sb.append(getBlankNumber(len1 - ggLen));
                int slLen = printerLengths(sl);
                sb.append(sl);
                sb.append(getBlankNumber(len3 - slLen));
                int djLen = printerLengths(dj);
                sb.append(getBlankNumber(len2 - djLen));
                sb.append(dj);
                int jeLen = printerLengths(je);
                sb.append(getBlankNumber(len2 - jeLen));
                sb.append(je + "\n");
            }

        }

        String order_num = "" + mOrderData.order_total_goods_munber;//订单总数量
        String order_amount = toDoubleNum("" + mOrderData.order_amount);//订单金额
        String payment_amount = toDoubleNum("" + mOrderData.order_payable_amount);//结算金额
        String receipts = toDoubleNum("" + mOrderData.order_paid_amount);//实收
        String order_discount = toDoubleNum("" + mOrderData.order_discount);//整单折扣

        for (int i = 0; i < maxLen; i++) {
            sb.append("-");
        }
        sb.append("\n");
        String totalNumberText = "总数:" + order_num;
        String totalMoneyText = "总额:" + order_amount;
        sb.append(getHeadAndLastText(maxLen, totalNumberText, totalMoneyText));

        String discount = "0";
        if (!"".equals(order_discount) || toDouble(order_discount) > 0) {
            discount = order_discount;
        }
        if (toDouble(order_discount) == 0) {
            discount = "100";
        }
        String discountText = "整单折扣:" + subZeroAndDot(toValidNumber(discount)) + "%";
        String rateText = "" + (toDouble(order_amount) - toDouble(payment_amount));

        if (Math.abs(toDouble(rateText)) == toDouble(order_amount)) {
            rateText = "0.00";
        } else {
            rateText = "-" + rateText;
        }
        sb.append(getHeadAndLastText(maxLen, discountText, "" + toDoubleNum(rateText)));

        String shihouText = "应收:";
        sb.append(getHeadAndLastText(maxLen, shihouText, payment_amount));

        String shishouText = "实收:";
        sb.append(getHeadAndLastText(maxLen, shishouText, receipts));

        String jsfsText = "结算方式:";
        /** 1:现金    2：支付宝    3：微信     4：余额     5：刷卡 */
        String jsfsTextAmount = "现金";
        if (mOrderData.payment_method_type == 1){
            jsfsTextAmount = "现金";
        }else if (mOrderData.payment_method_type == 2){
            jsfsTextAmount = "支付宝";
        }else if (mOrderData.payment_method_type == 3){
            jsfsTextAmount = "微信";
        }else if (mOrderData.payment_method_type == 4){
            jsfsTextAmount = "余额";
        }else if (mOrderData.payment_method_type == 5){
            jsfsTextAmount = "刷卡";
        }
        sb.append(getHeadAndLastText(maxLen, jsfsText, jsfsTextAmount));

        for (int i = 0; i < maxLen; i++) {
            sb.append("-");
        }
        sb.append("\n");

        String dianpuming = "店铺名:" + SharedPreferencesUtils.getString("v_shop_name") + "\n";
        String lianxiren = "联系人:" + SharedPreferencesUtils.getString("v_shop_contacts") + "\n";
        int differ = printerLengths("店铺名:")
                - printerLengths("电话:");
        String dianhua = "电" + getBlankNumber(differ) + "话:" + SharedPreferencesUtils.getString("v_shop_tel") + "\n";
        sb.append(dianpuming);
        sb.append(lianxiren);
        sb.append(dianhua);
        String address = SharedPreferencesUtils.getString("v_shop_address");
        String addressTitle = "地" + getBlankNumber(differ) + "址:";
        int nameLen = printerLengths(address);
        int lineLen = maxLen - printerLengths(addressTitle);
        int nameSize = address.length();
        if (nameLen > lineLen) {// 如果超过一行
            int line = nameLen / lineLen;
            if (lineLen * line > nameLen)
                line = line + 1;
            int startIndex = 0;
            sb.append(addressTitle);
            for (int first = 0; first < nameSize; first++) {// 第一行
                String firstLine = address.substring(0, first);
                if (printerLengths(firstLine) >= lineLen - 1) {
                    startIndex = first;
                    sb.append(firstLine + "\n");
                    break;
                }
            }
            int nameTitleLen = printerLengths(addressTitle);
            for (int n = 0; n < line - 1; n++) {
                for (int i = 0; i < nameSize; i++) {
                    String newName = address.substring(startIndex, startIndex + i);
                    if (printerLengths(newName) >= lineLen - 1) {
                        sb.append(getBlankNumber(nameTitleLen));
                        sb.append(newName + "\n");
                        startIndex = startIndex + i;
                        break;
                    }
                }
            }
            if (startIndex < nameSize) {
                String newName = address.substring(startIndex, nameSize);
                sb.append(getBlankNumber(nameTitleLen));
                sb.append(newName + "\n");
            }
        } else {
            sb.append(addressTitle + address + "\n");
        }
        sb.append("\n");

        esc.addText(sb.toString()); // 打印文字

        esc.addText("\n\n\n");

        return esc;
    }

    // ============================================================================= 公用类

    /**
     * 虚线----线条
     */
    private String dottedLine(int maxLen) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < maxLen; i++) {
            sb.append("-");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int LableTextLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 系统日期时间的获取
     */
    private String getCurrentTimeMillis() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    public static int printerLengths(String value) {
        int valueLength = 0;
        if (value == null)
            return valueLength;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * @return size多少位空格
     */
    private static String getBlankNumber(int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    private static String getHeadAndLastText(int max, String head, String last) {
        StringBuilder builder = new StringBuilder();
        int headLen = printerLengths(head);
        int lastLen = printerLengths(last);
        int totalLen = headLen + lastLen;
        if (totalLen < max) {
            builder.append(head);
            builder.append(getBlankNumber(max - totalLen));
            builder.append(last + "\n");
        } else {
            builder.append(getLimitStringBlank(head, max - lastLen));
            builder.append(last + "\n");
        }
        return builder.toString();
    }

    /**
     * string原字符串
     * maxLength需要保留的最大长度
     */
    private static String getLimitStringBlank(String string, int maxLength) {
        String S = string;
        int length = printerLengths(string);
        if (length > maxLength) {
            int size = string.length();
            for (int i = 0; i < size; i++) {
                String newS = string.substring(0, i);
                if (printerLengths(newS) >= maxLength - 3) {
                    S = newS + "...";
                    break;
                }
            }
        } else {
            int differ = maxLength - length;
            if (differ > 1) {
                S = string + getBlankNumber(differ);
            } else if (differ == 1) {
                S = string + " ";
            }
        }
        return S;
    }

    public static double toDouble(String str) {
        try {
            Double d = Double.parseDouble("".equals(str) ? "0" : str);
            return d;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String toDoubleNum(String str) {
        try {
            Double f = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(f);
        } catch (Exception e) {
            // throw new Exception("");
            return "0.00";
        }
    }

    public static String toSingleNum(String str) {
        try {
            Double f = Double.parseDouble(str);
            DecimalFormat df = new DecimalFormat("#0");
            return df.format(f);
        } catch (Exception e) {
            // throw new Exception("");
            return "0";
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 去小数点后0，保留有效数字
     *
     * @param text
     * @return
     */
    public static String toValidNumber(String text) {
        float number = toFloat(text);
        float f_number = (number * 100 / 100);
        if (f_number % 1d == 0) {
            return ((int) f_number) + "";
        }
        return f_number + "";
    }

    /**
     * 数据类型转换：String转Float
     *
     * @param str
     * @return
     */
    public static float toFloat(String str) {
        try {
            Float f = Float.parseFloat("".equals(str) ? "0" : str);
            return f;
        } catch (Exception e) {
            return 0;
        }
    }

}

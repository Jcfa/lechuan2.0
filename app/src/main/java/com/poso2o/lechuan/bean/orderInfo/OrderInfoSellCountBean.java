package com.poso2o.lechuan.bean.orderInfo;

import com.poso2o.lechuan.util.InsertPoint;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by ${cbf} on 2018/3/13 0013.
 * 销售统计
 */

public class OrderInfoSellCountBean implements Serializable {
    private String nick;
    private String completion_rate;
    private String remaining_assignment;
    private String assignment;
    private String gross_profit;
    private String order_amounts;
    private int today_sales_number;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCompletion_rate() {
        double staff_dc = Double.parseDouble(completion_rate);
        BigDecimal bg1 = new BigDecimal(staff_dc);
        double value1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (completion_rate == null && completion_rate.equals("0.0"))
            return "0.00";
        return value1 + "";
    }

    public void setCompletion_rate(String completion_rate) {
        this.completion_rate = completion_rate;
    }

    public String getRemaining_assignment() {
        return remaining_assignment;
    }

    public void setRemaining_assignment(String remaining_assignment) {
        this.remaining_assignment = remaining_assignment;
    }

    public String getAssignment() {
        String assign = null;
        int indexOf = assignment.indexOf(".");
        if (indexOf >= 5) {
            assign = InsertPoint.Stringinsert(assignment);
        } else {
            double staff_dc = Double.parseDouble(assignment);
            BigDecimal bg1 = new BigDecimal(staff_dc);
            double value = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            assign = df.format(value);
//            assign = value + "";
        }

        if (assignment == null && assignment.equals("0.0"))
            return "0.00";
        return assign + "";
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getGross_profit() {
        double staff_dc = Double.parseDouble(gross_profit);
        //判断小数点前面有几位数
        int indexOf = gross_profit.indexOf(".");
        String assign = null;
        if (indexOf >= 5) {
            assign = InsertPoint.Stringinsert(gross_profit);
        } else {
            BigDecimal bg1 = new BigDecimal(staff_dc);
            double value1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            assign = df.format(value1);
//            assign = value1 + "";
        }
        if (gross_profit == null && gross_profit.equals("0.0"))
            return "0.00%";
        return assign + "";
    }

    public void setGross_profit(String gross_profit) {
        this.gross_profit = gross_profit;
    }

    public String getOrder_amounts() {
        double staff_dc = Double.parseDouble(order_amounts);
        int indexOf = order_amounts.indexOf(".");
        String assign = null;
        if (indexOf >= 5) {
            assign = InsertPoint.Stringinsert(order_amounts);
        } else {
            BigDecimal bg1 = new BigDecimal(staff_dc);
            double value1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
            assign = df.format(value1);
//            assign = value1 + "";
        }
        if (order_amounts == null && order_amounts.equals("0.0"))
            return "0.00";
        return assign;
    }

    public void setOrder_amounts(String order_amounts) {
        this.order_amounts = order_amounts;
    }

    public int getToday_sales_number() {
        return today_sales_number;
    }

    public void setToday_sales_number(int today_sales_number) {
        this.today_sales_number = today_sales_number;
    }
}

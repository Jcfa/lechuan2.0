package com.poso2o.lechuan.bean.transfer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 交接班管理类
 * Created by peng on 2017/7/3.
 */

public class Transfer implements Serializable{
    /** 交接id */
    public String transfer_id ="";

    /** 店铺id */
    public String shop_id ="0";

    /** 分店id */
    public String shop_branch_id ="0";

    /** 交接时间 */
    public Long transfe_date =0L;

    /** 上交金额 */
    public Double amounts_success =0.00;

    /** 上次预留备用金 */
    public Double transfe_reservefund_last =0.00;

    /** 本次预留备用金 */
    public Double transfe_reservefund =0.00;

    /** 会员充值-支付宝 */
    public Double member_amount_alipay =0.00;

    /** 会员充值-转账 */
    public Double member_amount_bank =0.00;

    /** 会员充值-现金 */
    public Double member_amount_cash =0.00;

    /** 会员充值-微信 */
    public Double member_amount_wx =0.00;

    /** 退款金额 -现金*/
    public Double refund_amount_cash =0.00;

    /** 退款金额-支付宝 */
    public Double refund_amount_alipay =0.00;

    /** 退款金额-刷卡 */
    public Double refund_amount_bank =0.00;

    /**退款金额-余额 */
    public Double refund_amount_member =0.00;

    /** 退款金额-微信 */
    public Double refund_amount_wx =0.00;

    /** 销售金额 -现金*/
    public Double sales_amount_cash =0.00;

    /** 销售收入-支付宝 */
    public Double sales_amount_alipay =0.00;

    /** 销售收入-刷卡 */
    public Double sales_amount_bank =0.00;

    /** 销售收入-余额 */
    public Double sales_amount_member =0.00;

    /** 销售收入-微信 */
    public Double sales_amount_wx =0.00;

    /** 其他收入-合计 */
    public Double other_amount_total =0.00;

    /** 钱箱总额 收入合计（包括退款)==钱箱余额 */
    public Double cash_total =0.00;

    /** 钱箱补钱 */
    public Double cash_add =0.00;

    /** 交接员工 */
    public String czy ="";

    /** 交接员工名称 */
    public String czyname ="";

    /** 备注 */
    public String transfer_remark ="";

    /** 交接班详情 */
    public ArrayList<TransferPaymentMethod> paymentMethod;

    //旧版服装版数据结构

    public String end_date = "";
    public String amount_user = "0";
    public String amount = "0";
//    public String czy = "";
    public String amount_wx = "0";
    public String begin_date = "";
    public String b_amount = "0";
    public String zengsong_amount = "0";
    public String addAmounts_bank = "0";
    public String amount_bank = "0";
    public String sales_amount = "0";
    public String addAmounts_alipay = "0";
    public String remark = "";
    public String b_amount_back = "0";
    public String amount_cash = "0";
    public String del_amount = "0";
    public String dat = "";
    public String addAmounts_wx = "0";
    public String sales_num = "0";
    public String add_amount = "0";
//    public String czyname = "";
    public String id = "";
    public String amount_alipay = "0";
//    public String amounts_success = "";
}

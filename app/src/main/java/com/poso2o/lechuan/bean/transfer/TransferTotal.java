package com.poso2o.lechuan.bean.transfer;

import java.io.Serializable;

/**
 * Created by Luo on 2017/2/8.
 */

public class TransferTotal implements Serializable {
    public int currPage ;
    public int pages ;
    public int rowcount ;
    public int countPerPage ; // 每页显示的记录数

    public double total_alipay = 0.00;
    public double total_bank = 0.00;
    public double total_cash = 0.00;
    public double total_wx = 0.00;

}

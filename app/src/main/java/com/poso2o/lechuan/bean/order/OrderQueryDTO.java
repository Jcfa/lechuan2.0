package com.poso2o.lechuan.bean.order;

import com.poso2o.lechuan.bean.BaseBean;

import java.util.ArrayList;


/**
 * 订单管理
 * Created by Luo on 2017-12-11
 */
public class OrderQueryDTO  extends BaseBean {

	public ArrayList<OrderDTO> list;
	public OrderTotalBean total;

}

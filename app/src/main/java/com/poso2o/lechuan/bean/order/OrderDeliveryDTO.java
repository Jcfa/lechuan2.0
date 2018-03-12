package com.poso2o.lechuan.bean.order;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 订单发货管理
 *
 * Created by Luo on 2017-12-11
 */
public class OrderDeliveryDTO implements Serializable {

	/** 订单ID */
	public long order_id = 0L;

	/** 快递单ID */
	public String express_order_id = "";

}

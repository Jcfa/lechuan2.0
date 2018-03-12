package com.poso2o.lechuan.bean.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品购买评价
 * Created by Luo on 2017-12-11
 * 
 */
public class GoodsAppraiseQueryDTO implements Serializable {

	public ArrayList<GoodsAppraiseDTO> list;

	public GoodsAppraiseTotalBean total;
}

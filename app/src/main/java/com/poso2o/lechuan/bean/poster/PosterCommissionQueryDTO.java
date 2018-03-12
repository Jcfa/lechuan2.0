package com.poso2o.lechuan.bean.poster;

import com.poso2o.lechuan.bean.BaseBean;

import java.util.ArrayList;


/**
 * 分销商佣金对账表(T_SHOP_DISTRIBUTION_ACCOUNT)
 * 
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class PosterCommissionQueryDTO extends BaseBean {

	public ArrayList<PosterCommissionDTO> list;

	public PosterCommissionTotalBean total;

}

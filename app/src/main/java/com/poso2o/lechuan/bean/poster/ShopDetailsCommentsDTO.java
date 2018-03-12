package com.poso2o.lechuan.bean.poster;

import java.io.Serializable;

/**
 * 评论别人的购买评价
 * 
 * @author peng
 * @version 1.0.0 2017-05-08
 */
public class ShopDetailsCommentsDTO implements Serializable {

	/** 评论ID */
    public Long comment_id =0L;
	
	/**购买评论ID */
    public Long appraise_id =0L;
    
    /** 账号ID */
    public Long uid =0L;
    
    /** 账号昵称 */
    public String nick ="";
    
    /** 账号LOGO */
    public String logo ="";
    
    /** 评论 */
    public String comments ="";
    
    /** 时间 */
    public Long build_time =0L;

}

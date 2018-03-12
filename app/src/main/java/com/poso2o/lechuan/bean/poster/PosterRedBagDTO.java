package com.poso2o.lechuan.bean.poster;

import java.io.Serializable;

/**
 * 红包历史记录(T_NEWS_RED_ENVELOPES)
 * 
 * @author Luo
 * @version 1.0.0 2017-11-29
 */

public class PosterRedBagDTO  implements Serializable {
	 
    /** 资讯ID */
    public Long news_id =0L;
    
    /** 账号ID */
    public Long uid =0L;
    
    /** 账号昵称 */
    public String nick ="";
    
    /** 账号LOGO */
    public String logo ="";
    
    /** 红包金额 */
    public Double amount =0.00;
    
    /** 时间 */
    public Long build_time =0L;

}

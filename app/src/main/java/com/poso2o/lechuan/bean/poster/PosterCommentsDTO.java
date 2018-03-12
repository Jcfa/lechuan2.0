package com.poso2o.lechuan.bean.poster;

import java.io.Serializable;

/**
 * 资讯评论
 * 
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class PosterCommentsDTO implements Serializable {

	/** 评论ID */
    public Long comment_id =0L;
	
	/** 资讯ID */
    public Long news_id =0L;
    
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

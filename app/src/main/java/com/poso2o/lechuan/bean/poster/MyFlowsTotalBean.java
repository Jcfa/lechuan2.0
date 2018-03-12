package com.poso2o.lechuan.bean.poster;


import java.io.Serializable;

/**
 * 
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class MyFlowsTotalBean implements Serializable {

    public int currPage = 0; // 当前页
    public int countPerPage = 20; // 每页显示的记录数
    public int rowcount = 0;// 总记录数
    public int pages = 1;// 总页数

}

package com.poso2o.lechuan.bean.member;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fynner on 2017/1/13.
 * <p>
 * currPage	1
 * pages	1
 * rowcount	1
 */
public class MemberAllData implements Serializable {
    public MemberTotal total;
    public ArrayList<Member> list = new ArrayList<>();

    public class MemberTotal{
        public int countPerPage;
        public int currPage;
        public int pages ;
        public int rowcount ;
        public String total_member_balance;
        public String total_member_consume;
        public String total_member_integral;
        public String total_member_purchase_number;
    }
}
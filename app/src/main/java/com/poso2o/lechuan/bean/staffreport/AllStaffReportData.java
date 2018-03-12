package com.poso2o.lechuan.bean.staffreport;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/15.
 */

public class AllStaffReportData {
    public ArrayList<StaffReportData> list = new ArrayList<>();
    public Total total = new Total();

    public class Total{
        public String total_assignments = "0.00";
        public String total_order_amounts = "0.00";
    }
}

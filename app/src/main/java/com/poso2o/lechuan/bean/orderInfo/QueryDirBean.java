package com.poso2o.lechuan.bean.orderInfo;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/28 0028.
 */

public class QueryDirBean {

    /**
     * total : {}
     * list : [{"fid":"134236789306288891609","directory":"帐篷系列",
     * "productNum":"5"},{"fid":"40cdb5916bcee8fcdd4f9545af62bac7","directory":
     * "夏装","productNum":"2"},{"fid":"704d5c7355ae1b3331574ed19d3fbe59","dire
     * ctory":"男装","productNum":"7"},{"fid":"134236789308991555538","directory
     * ":"女装","productNum":"24"},{"fid":"134236789309563945294","directory":"上衣",
     * "productNum":"16"},{"fid":"fe2928da5548f5614cdceb1656409e9c","directory":"裙子","productNum":"4"},{"fid":"48961d3392194b13","directory":"裤子","productNum":"8"},{"fid":"aed3d733cdd6d1862c601f057c4cafc3","directory":"包包","productNum":"17"}]
     */

    private TotalBean total;
    private List<ListBean> list;

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class TotalBean {
    }

    public static class ListBean {
        /**
         * fid : 134236789306288891609
         * directory : 帐篷系列
         * productNum : 5
         */

        private String fid;
        private String directory;
        private String productNum;

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getDirectory() {
            if (directory.length() >= 5) {
                String sub = directory.substring(0, 6);
                return sub;
            }
            return directory;
        }

        public void setDirectory(String directory) {
            this.directory = directory;
        }

        public String getProductNum() {
            return productNum;
        }

        public void setProductNum(String productNum) {
            this.productNum = productNum;
        }
    }
}

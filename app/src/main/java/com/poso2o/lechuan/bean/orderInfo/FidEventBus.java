package com.poso2o.lechuan.bean.orderInfo;

/**
 * Created by ${cbf} on 2018/3/28 0028.
 */

public class FidEventBus {
    private String fid;
    private String directory;
    private String productNum;

    public FidEventBus(String fid, String directory, String productNum) {
        this.fid = fid;
        this.directory = directory;
        this.productNum = productNum;
    }

    public String getDirectory() {
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


    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}

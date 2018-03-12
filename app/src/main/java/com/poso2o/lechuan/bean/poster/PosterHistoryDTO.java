package com.poso2o.lechuan.bean.poster;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 广告
 *
 * @author Luo
 * @version 1.0.0 2017-12-02
 */
public class PosterHistoryDTO implements Serializable {

    /**
     * 关键字
     */
    public String keywords = "";

    public PosterHistoryDTO(String keywords) {
        this.keywords = keywords;
    }
}

package com.poso2o.lechuan.bean.redbag;

import java.math.BigDecimal;

public class Redbag {

    /**
     * 是否随机红包  0=随机金额，1=固定金额
     */
    public int has_random_red = 0;

    /**
     * 数量
     */
    public int number = 0;

    /**
     * 金额
     */
    public BigDecimal money = BigDecimal.ZERO;

    /**
     * 单个金额
     */
    public BigDecimal oneMoney = BigDecimal.ZERO;

    public boolean isRandom() {
        return has_random_red == 0;
    }

    public void setRandom(boolean isRandom) {
        has_random_red = isRandom ? 0 : 1;
    }

}
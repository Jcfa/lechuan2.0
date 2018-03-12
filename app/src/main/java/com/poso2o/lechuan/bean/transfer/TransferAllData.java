package com.poso2o.lechuan.bean.transfer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by L on 2017/uo07/8.
 */

public class TransferAllData implements Serializable {
    public ArrayList<Transfer> list = new ArrayList<>();
    public TransferTotal total = new TransferTotal();
}

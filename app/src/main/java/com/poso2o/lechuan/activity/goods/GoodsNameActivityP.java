package com.poso2o.lechuan.activity.goods;

import com.poso2o.lechuan.bean.goodsdata.GoodsNameData;
import com.poso2o.lechuan.configs.MenuConstant;
import com.poso2o.lechuan.util.FileManage;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/19.
 */

public class GoodsNameActivityP {

    private final String GOODS_NAME_PHAT = MenuConstant.GOODS_NAME_PATH;

    /**
     * 获取商品名记录
     *
     * @return
     */
    public ArrayList<GoodsNameData> getGoodsNameDatas() {
        ArrayList<GoodsNameData> goodsNameDatas = new ArrayList<GoodsNameData>();
        Object goodsNameDataObj = FileManage.restoreObject(GOODS_NAME_PHAT);

        if (goodsNameDataObj != null) {
            goodsNameDatas = (ArrayList<GoodsNameData>) goodsNameDataObj;
        }
        return goodsNameDatas;
    }


    /**
     * 必须开启线程执行
     * 保存商品名记录
     *
     * @param goodsNameDatas 商品名列表
     */
    public synchronized void cacheGoodsName(ArrayList<GoodsNameData> goodsNameDatas, GoodsNameData goodNameData) {
        ArrayList<GoodsNameData> nameDatas = new ArrayList<GoodsNameData>();
        for (GoodsNameData gnd : goodsNameDatas) {
            if (gnd.goodsName.equals(goodNameData.goodsName)) {
                continue;
            }
            nameDatas.add(gnd);
        }
        nameDatas.add(goodNameData);
        //当缓存文件大于10000的时候清除1个数据
        if (nameDatas.size() > 5000) {
            nameDatas.remove(0);
        }
        FileManage.saveObject(GOODS_NAME_PHAT, nameDatas);
    }

    /**
     * 清空缓存数据
     */
    public void clearCacheInfo() {
        ArrayList<GoodsNameData> nameDatas = new ArrayList<GoodsNameData>();
        FileManage.saveObject(GOODS_NAME_PHAT, nameDatas);
    }
}

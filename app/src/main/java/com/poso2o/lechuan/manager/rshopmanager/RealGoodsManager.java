package com.poso2o.lechuan.manager.rshopmanager;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsAndCatalog;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsMovePicture;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.bean.goodsdata.MoreViewData;
import com.poso2o.lechuan.bean.goodsdata.OldGoodsImageBean;
import com.poso2o.lechuan.bean.goodsdata.OldSpec;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.realshop.RealGoodsAPI;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mr zhang on 2017/12/7.
 */
public class RealGoodsManager extends BaseManager {

//    private static final String REAL_GOODS_ADD_URL = "http://fuzhuang.poso2o.com/productService.htm?Act=add2";
//    private static final String REAL_GOODS_URL = "http://fuzhuang.poso2o.com/productService.htm?Act=query";
//    private static final String REAL_BATCH_DEL_GOODS_URL = "http://fuzhuang.poso2o.com/productService.htm?Act=batchDelete";
    private static final int LIST = 10001;// 获取商品信息
    private static final int ADD = 10002;// 添加商品
    private static final int EDIT = 10003;// 添加商品
    private static final int BATCH_DEL = 10004;// 批量删除产品



    private static volatile RealGoodsManager realGoodsManager;

    public static RealGoodsManager getInstance() {
        if (realGoodsManager == null) {
            synchronized (RealGoodsManager.class) {
                if (realGoodsManager == null) {
                    realGoodsManager = new RealGoodsManager();
                }
            }
        }
        return realGoodsManager;
    }

    public void loadGoodsAndCatalog(final BaseActivity baseActivity, String order, String sort, String fid, String keywords, final IRequestCallBack<AllGoodsAndCatalog> iRequestCallBack) {
        Request request = getStringRequest(RealGoodsAPI.LIST);

        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("order", order);
        request.add("sort", sort);
        request.add("fid", fid);
        request.add("keywords", keywords);

        baseActivity.request(LIST, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                AllGoodsAndCatalog allGoodsAndCatalog = new Gson().fromJson(response, AllGoodsAndCatalog.class);

                if (allGoodsAndCatalog == null) {
                    iRequestCallBack.onResult(LIST, null);
                    return;
                }

                goodsDatas.clear();
                goodsBackDatas.clear();
                if (ListUtils.isNotEmpty(allGoodsAndCatalog.list)) {
                    goodsDatas.addAll(allGoodsAndCatalog.list);
                }
                catalogs = new ArrayList<Catalog>();
                catalogs.addAll(allGoodsAndCatalog.directory);
                goodsBackDatas.addAll(goodsDatas);

                packetGoods(goodsDatas);

                iRequestCallBack.onResult(LIST, allGoodsAndCatalog);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(LIST, response);
            }
        }, true, false);
    }

    /**
     * 添加商品
     *
     * @param baseActivity
     * @param goods
     * @param iRequestCallBack
     */
    public void add(final BaseActivity baseActivity, Goods goods, final IRequestCallBack iRequestCallBack) {
        ArrayList<OldGoodsImageBean> imgs = new ArrayList<>();
        for (GoodsMovePicture picture : goods.goods_move_picture) {
            OldGoodsImageBean imageBean = new OldGoodsImageBean();
            imageBean.filename = picture.picture_type;
            imageBean.data = picture.picture_url;

            imgs.add(imageBean);
        }

        ArrayList<MoreViewData> moreViewDatas = new ArrayList<>();
        for (OldSpec goodsSpec : goods.nums) {
            MoreViewData moreViewData = new MoreViewData();
            moreViewData.colorid = goodsSpec.colorid;
            if (TextUtils.isEmpty(goodsSpec.barcode)){
                goodsSpec.barcode = RandomStringUtil.randomGUID(13,"0123456789");
            }
            moreViewData.barcode = goodsSpec.barcode;
            moreViewData.sizeid = goodsSpec.sizeid;
            moreViewData.num = goodsSpec.num;
            moreViewDatas.add(moreViewData);
        }

        final Request request = getStringRequest(RealGoodsAPI.ADD);

        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("imgs", new Gson().toJson(imgs));
        request.add("bh", goods.bh);
        request.add("name", goods.name);
        request.add("price", goods.price);
        request.add("fprice", goods.fprice);
        request.add("fid", goods.fid);
        request.add("directory", goods.directory);
        request.add("nums", new Gson().toJson(moreViewDatas));

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Print.println("测试添加商品：" + response);
                iRequestCallBack.onResult(ADD, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(ADD, response);
            }
        }, true, false);
    }

    /**
     * 修改商品
     *
     * @param baseActivity
     * @param goods
     * @param iRequestCallBack
     */
    public void edit(final BaseActivity baseActivity, Goods goods, final IRequestCallBack iRequestCallBack) {
        ArrayList<OldGoodsImageBean> imgs = new ArrayList<>();
        for (GoodsMovePicture picture : goods.goods_move_picture) {
            OldGoodsImageBean imageBean = new OldGoodsImageBean();
            imageBean.filename = picture.picture_type;
            imageBean.data = picture.picture_url;

            imgs.add(imageBean);
        }

        ArrayList<MoreViewData> moreViewDatas = new ArrayList<>();
        for (OldSpec goodsSpec : goods.nums) {
            MoreViewData moreViewData = new MoreViewData();
            moreViewData.colorid = goodsSpec.colorid;
            if (TextUtils.isEmpty(goodsSpec.barcode)){
                goodsSpec.barcode = RandomStringUtil.randomGUID(13,"0123456789");
            }
            moreViewData.barcode = goodsSpec.barcode;
            moreViewData.sizeid = goodsSpec.sizeid;
            moreViewData.num = goodsSpec.num;
            moreViewDatas.add(moreViewData);
        }

        final Request request = getStringRequest(RealGoodsAPI.EDIT);

        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("imgs", new Gson().toJson(imgs));
        request.add("guid", goods.guid);
        request.add("bh", goods.bh);
        request.add("name", goods.name);
        request.add("price", goods.price);
        request.add("fprice", goods.fprice);
        request.add("fid", goods.fid);
        request.add("nums", new Gson().toJson(moreViewDatas));

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Print.println("测试添加商品：" + response);
                iRequestCallBack.onResult(what, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 批量删除产品
     *
     * @param baseActivity
     * @param goodses
     * @param iRequestCallBack
     */
    public void batchDelGoods(final BaseActivity baseActivity, ArrayList<Goods> goodses, final IRequestCallBack iRequestCallBack) {
        String guids = "";
        for (int i = 0; i < goodses.size(); i++) {
            if (i == goodses.size() - 1) {
                guids = guids + "" + goodses.get(i).guid + "";
            } else {
                guids = guids + "" + goodses.get(i).guid + ",";
            }
        }

        Request request = getStringRequest(RealGoodsAPI.BATCH_DEL);

        request.add("sessionUid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("sessionKey", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("shopid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("czy", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("guids", guids);

        baseActivity.request(BATCH_DEL, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(BATCH_DEL, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(BATCH_DEL, response);
            }
        }, true, true);
    }

    //************************************************本地数据处理******************************************************************

    private ArrayList<Goods> goodsDatas = new ArrayList<>();

    private ArrayList<Goods> goodsBackDatas = new ArrayList<>();

    private ArrayList<Catalog> catalogs = new ArrayList<>();

    private ConcurrentHashMap<String, ArrayList<Goods>> packetData;

    //实体店还是微店
    private boolean is_online = false;

    /**
     * 注意有可能拿到的数为null 需要进行判空
     *
     * @return
     */
    public ArrayList<Goods> getGoodsDatas() {
        return goodsDatas;
    }

    public int getGoodsNumber() {
        return goodsDatas.size();
    }

    /**
     * 模糊搜索全部接口
     *
     * @param baseActivity
     * @param keyword      搜索字段
     * @return 返回搜索到的数据集合, 当返回null时，找不到该商品
     */
    public synchronized ArrayList<Goods> search(BaseActivity baseActivity, String keyword, Catalog catalog) {
        if (goodsDatas == null || catalog == null) {
            return null;
        }
        String catalog_id = catalog.fid;
        ArrayList<Goods> search_goodses = new ArrayList<>();
        ArrayList<Goods> search_goodsDatas;
        if ("-1".equals(catalog_id)) {
            search_goodsDatas = goodsDatas;
        } else {
            if (packetData.containsKey(catalog_id)) {
                search_goodsDatas = packetData.get(catalog_id);
            } else {
                Toast.show(baseActivity, "该目录不存在");
                return null;
            }
        }

        if (TextUtils.isEmpty(keyword)) {
            return search_goodsDatas;
        }

        for (Goods search_goods : search_goodsDatas) {
            if (search_goods.bh.contains(keyword)) {
                Goods goodsData = new_goodsData(search_goods);
                if (goodsData == null) {
                    return null;
                }
                // 高亮处理
                goodsData.isNoLight = true;
                int noIndex = goodsData.bh.indexOf(keyword);
                goodsData.light_no = goodsData.bh.substring(0, noIndex) + getBankName(keyword)
                        + goodsData.bh.substring(noIndex + keyword.length(), goodsData.bh.length());
                search_goodses.add(goodsData);
                continue;
            }
            if (search_goods.name.contains(keyword)) {
                Goods goodsData = new_goodsData(search_goods);
                if (goodsData == null) {
                    return null;
                }
                // 高亮处理
                goodsData.isNameLight = true;
                int noIndex = goodsData.name.indexOf(keyword);
                goodsData.light_name = goodsData.name.substring(0, noIndex) + getBankName(keyword)
                        + goodsData.name.substring(noIndex + keyword.length(), goodsData.name.length());
                search_goodses.add(goodsData);
                continue;
            }
            if (ListUtils.isNotEmpty(search_goods.nums)) {
                for (OldSpec spec : search_goods.nums) {
                    if (spec.barcode.contains(keyword)) {
                        Goods goodsData = new_goodsData(search_goods);
                        if (goodsData == null) {
                            return null;
                        }
                        search_goodses.add(goodsData);
                    }
                }
            }
        }
        return search_goodses;
    }

    private String getBankName(String editText) {
        return "<font color=\"#ff0000\">" + editText + "</font>";
    }

    /**
     * 新建商品缓冲对象
     *
     * @param search_goods
     * @return
     */
    private Goods new_goodsData(Goods search_goods) {
        if (search_goods == null) {
            return null;
        }
        Goods goods = null;
        try {
            goods = search_goods.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return goods;
    }


    /**
     * 获取对应商品对象
     *
     * @param goods_id 商品id
     * @return 商品对象
     */
    public Goods findGoods(String goods_id) {
        if (goods_id == null || goods_id.equals("")) {
            return null;
        }

        if (goodsBackDatas == null) {
            return null;
        }

        for (Goods goods : goodsBackDatas) {
            if (goods_id.equals(goods.guid)) {
                return goods;
            }
        }

        return null;
    }

    /**
     * 判断商品是否存在
     *
     * @param baseActivity
     * @param goods_id     商品id
     * @return
     */
    public boolean hasGoods(BaseActivity baseActivity, String goods_id) {
        if (goods_id == null || goods_id.equals("")) {
            return false;
        }

        if (goodsBackDatas == null) {
            Toast.show(baseActivity, "商品数据异常，请先刷新商品列表");
            return false;
        }

        for (Goods goods : goodsBackDatas) {
            if (goods_id.equals(goods.guid)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 按照销量排序
     */
    public synchronized void saleSort(final boolean isAsc, ArrayList<Goods> goodsDatas, OnSortListener onSortListener) {
        ArrayList<Goods> sortGoodsDatas = new ArrayList<>();
        sortGoodsDatas.addAll(goodsDatas);

        Collections.sort(sortGoodsDatas, new Comparator<Goods>() {

            @Override
            public int compare(Goods o1, Goods o2) {
                Integer num1 = Integer.parseInt(o1.salesnum);
                Integer num2 = Integer.parseInt(o2.salesnum);
                if (isAsc) {// 升序
                    return num1.compareTo(num2);
                } else {// 降序
                    return num2.compareTo(num1);
                }
            }
        });

        declineSellOut(sortGoodsDatas);

        if (onSortListener != null) {
            onSortListener.onSort(sortGoodsDatas);
        }
    }

    /**
     * 按照库存排序
     */
    public synchronized void stockSort(final boolean isAsc, ArrayList<Goods> goodsDatas, OnSortListener onSortListener) {
        ArrayList<Goods> sortGoodsDatas = new ArrayList<>();
        sortGoodsDatas.addAll(goodsDatas);

        Collections.sort(sortGoodsDatas, new Comparator<Goods>() {

            @Override
            public int compare(Goods o1, Goods o2) {
                Integer num1 = Integer.parseInt(o1.kcnum);
                Integer num2 = Integer.parseInt(o2.kcnum);
                if (isAsc) {// 升序
                    return num1.compareTo(num2);
                } else {// 降序
                    return num2.compareTo(num1);
                }
            }
        });

        declineSellOut(sortGoodsDatas);

        if (onSortListener != null) {
            onSortListener.onSort(sortGoodsDatas);
        }
    }

    /**
     * 将售罄商品移动到列表底部
     */
    public void declineSellOut(ArrayList<Goods> goodsDatas) {
        ArrayList<Goods> sellOuts = new ArrayList<>();
        for (Goods goods : goodsDatas) {
            if (goods.sale_type == 0) {
                sellOuts.add(goods);
            }
        }
        goodsDatas.removeAll(sellOuts);
        goodsDatas.addAll(sellOuts);
    }

    /**
     * 获取到的网络数据进行分组
     *
     * @param goodsDatas
     */
    private synchronized void packetGoods(ArrayList<Goods> goodsDatas) {
        if (packetData == null) {
            packetData = new ConcurrentHashMap<String, ArrayList<Goods>>();
        } else {
            packetData.clear();
        }

        for (Catalog catalog : catalogs) {
            ArrayList<Goods> goodses = new ArrayList<Goods>();
            for (Goods goods : goodsDatas) {
                if (goods.fid.equals(catalog.fid)) {
                    goodses.add(goods);
                }
            }
            catalog.productNum = goodses.size() + "";
            packetData.put(catalog.fid, goodses);
        }
    }

    /**
     * 获取分组数据
     *
     * @param fid           分组id
     * @param onPacketGoods 监听
     */
    public synchronized void getPacketGoods(final String fid, final OnPacketGoods onPacketGoods) {
        if (TextUtils.equals(fid, "-1")) {
            if (onPacketGoods != null) {
                onPacketGoods.packetGoods(goodsDatas);
            }
            return;
        }

        if (packetData == null) {
            goodsDatas.clear();
            goodsDatas.addAll(goodsBackDatas);
            packetGoods(goodsDatas);
        }

        if (packetData.containsKey(fid)) {
            if (onPacketGoods != null) {
                declineSellOut(packetData.get(fid));
                onPacketGoods.packetGoods(packetData.get(fid));
            }
        } else {
            if (onPacketGoods != null) {
                onPacketGoods.packetGoodsFail();
            }
        }
    }

    /**
     * 获取商品分组集合
     *
     * @return 当商品没有进行过分组的请求，返回值为null
     */
    public ConcurrentHashMap<String, ArrayList<Goods>> getPacketMap() {
        return packetData;
    }

    /**
     * 获取分组数据监听器
     */
    public interface OnPacketGoods {

        void packetGoods(ArrayList<Goods> packetGoodsDatas);

        void packetGoodsFail();
    }

    /**
     * 排序监听
     */
    public interface OnSortListener {

        void onSort(ArrayList<Goods> goodsDatas);
    }

    //获取目录
    public Catalog findCatalog(String catalogid) {
        if (catalogs == null || catalogs.size() == 0) return null;
        for (Catalog catalog : catalogs) {
            if (catalog.fid.equals(catalogid)) {
                return catalog;
            }
        }
        return null;
    }
}

package com.poso2o.lechuan.manager.vdian;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.AllGoodsAndCatalog;
import com.poso2o.lechuan.bean.goodsdata.BatchUpGoodsBean;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsBean;
import com.poso2o.lechuan.bean.goodsdata.GoodsMovePicture;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.bean.goodsdata.GoodsSupplier;
import com.poso2o.lechuan.bean.goodsdata.RemoveGoods;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.http.vdian.VdianGoodsAPI;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.RandomStringUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 微店商品数据请求类
 * Created by mr zhang on 2017/12/1.
 */
public class VdianGoodsManager extends BaseManager {

    /**
     * 商品列表数据请求
     */
    private static final int LIST = 1001;

    /**
     * 商品详情请求
     */
    private static final int INFO = 1002;

    /**
     * 添加商品请求
     */
    private static final int ADD = 1003;

    /**
     * 删除商品请求
     */
    private static final int DEL = 1004;

    /**
     * 查询商品数据请求
     */
    private static final int QUERY = 1005;

    /**
     * 编辑商品数据请求
     */
    private static final int EDIT = 1006;

    /**
     * 批量上传商品
     */
    public static final int BATCH_UP_ID = 1007;

    /**
     * 排序类型
     */
    public static final String SORT_TYPE_SALE_NUMBER = "goods_sale_number";// 销量
    public static final String SORT_TYPE_COMMISSION = "commission_amount";// 佣金

    private static volatile VdianGoodsManager vdianGoodsManager;

    public static VdianGoodsManager getInstance() {
        if (vdianGoodsManager == null) {
            synchronized (VdianGoodsManager.class) {
                if (vdianGoodsManager == null) vdianGoodsManager = new VdianGoodsManager();
            }
        }
        return vdianGoodsManager;
    }

    public void query(final BaseActivity baseActivity, long shop_id, String catalog_id, String orderByName, String sort, String keywords, final int pageType, final IRequestCallBack<ArrayList<Goods>> callback) {
        if (pageType == FIRST) {
            currPage = 0;
        }
        Request<String> request = getStringRequest(VdianGoodsAPI.QUERY);
        request.add("catalog_id", catalog_id);
        request.add("orderByName", orderByName);
        request.add("sort", sort);
        request.add("keywords", keywords);
        request.add("currPage", ++currPage);
        defaultParam(request);
        if (shop_id != 0) {
            request.add("shop_id", shop_id);
        }
        baseActivity.request(QUERY, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                GoodsBean goodsBean = gson.fromJson(response, GoodsBean.class);
                if (goodsBean != null && goodsBean.list != null) {
                    if (pageType == NEXT && goodsBean.list.size() == 0) {
                        currPage--;
                        callback.onFailed(what, baseActivity.getString(R.string.toast_no_more_data));
                    } else {
                        callback.onResult(what, goodsBean.list);
                    }
                } else {
                    callback.onFailed(what, baseActivity.getString(R.string.toast_load_goods_fail));
                }
            }

            @Override
            public void onFailed(int what, String response) {
                callback.onFailed(what, response);
            }
        }, true, false);
    }

    /**
     * 商品列表信息
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void loadList(final BaseActivity baseActivity, final IRequestCallBack<AllGoodsAndCatalog> iRequestCallBack) {
        VdianCatalogManager.getInstance().loadList(baseActivity, new IRequestCallBack<CatalogBean>() {

            @Override
            public void onResult(int tag, CatalogBean result) {
                catalogs = new ArrayList<Catalog>();
                catalogs.addAll(result.list);

                final Request<String> request = getStringRequest(VdianGoodsAPI.LIST);
                defaultParam(request);

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
                        goodsBackDatas.addAll(goodsDatas);
                        allGoodsAndCatalog.catalogs = catalogs;

                        packetGoods(goodsDatas);

                        iRequestCallBack.onResult(LIST, allGoodsAndCatalog);
                    }

                    @Override
                    public void onFailed(int what, String response) {
                        baseActivity.dismissLoading();
                        Toast.show(baseActivity, response);
                    }
                }, true, false);
            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }

    /**
     * 商品详情 TODO
     *
     * @param baseActivity
     * @param goods_id
     * @param iRequestCallBack
     */
    public void info(final BaseActivity baseActivity, String goods_id, final IRequestCallBack iRequestCallBack) {
        final Request<String> request = getStringRequest(VdianGoodsAPI.INFO);
        request.add("goods_id", goods_id);
        defaultParam(request);

        baseActivity.request(INFO, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                Goods goods = new Gson().fromJson(response, Goods.class);
                iRequestCallBack.onResult(INFO, goods);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(what, response);
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
        final Request<String> request = getStringRequest(VdianGoodsAPI.ADD);
        request.add("goods_id", RandomStringUtil.getOrderId());
        request.add("goods_name", goods.goods_name);
        request.add("catalog_id", goods.catalog_id);
        request.add("catalog_name", goods.catalog_name);
        request.add("goods_no", goods.goods_no);
        request.add("goods_unit", goods.goods_unit);
        request.add("goods_discount", NumberFormatUtils.format(goods.goods_discount));
        request.add("goods_cost_section", goods.goods_cost_section);
        request.add("goods_price_section", goods.goods_price_section);
//        request.add("goods_auxiliary_unit", goods.goods_auxiliary_unit);
//        request.add("goods_auxiliary_unit_packingrate", Integer.toString(goods.goods_auxiliary_unit_packingrate));
        request.add("goods_number", NumberFormatUtils.formatToInteger(goods.goods_number));
        request.add("goods_sale_number", NumberFormatUtils.formatToInteger(goods.goods_sale_number));
        request.add("sale_type", NumberFormatUtils.formatToInteger(goods.sale_type));
//        request.add("goods_type", NumberFormatUtils.formatToInteger(goods.goods_type));
        request.add("remark", goods.remark);
        request.add("goods_move_picture", getImgsJson(goods.goods_move_picture));
        request.add("goods_spec", getSpecJson(goods.goods_spec));
//        request.add("goods_supplier", getSupplierJson(goods.goods_supplier));
//        request.add("main_supplier_id", goods.main_supplier_id);
//        request.add("main_supplier_name", goods.main_supplier_name);
        defaultParam(request);

        baseActivity.request(ADD, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(ADD, response);
            }

            @Override
            public void onFailed(int what, String response) {
                baseActivity.dismissLoading();
                Toast.show(baseActivity, response);
            }
        }, true, false);
    }

    private String getSupplierJson(ArrayList<GoodsSupplier> goods_supplier) {
        Gson gson = new Gson();
        if (ListUtils.isEmpty(goods_supplier)) {
            return "[]";
        } else {
            return gson.toJson(goods_supplier);
        }
    }

    private String getSpecJson(ArrayList<GoodsSpec> goods_spec) {
        Gson gson = new Gson();
        if (ListUtils.isEmpty(goods_spec)) {
            return "[]";
        } else {
            return gson.toJson(goods_spec);
        }
    }

    private String getImgsJson(ArrayList<GoodsMovePicture> goods_move_picture) {
        Gson gson = new Gson();
        try {
            if (ListUtils.isEmpty(goods_move_picture)) {
                return "[]";
            } else {
                return gson.toJson(goods_move_picture);
            }
        } catch (Exception e) {
            return "[]";
        }
    }

    /**
     * 删除商品
     *
     * @param baseActivity
     * @param goods_id
     * @param iRequestCallBack
     */
    public void del(final BaseActivity baseActivity, String goods_id, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(VdianGoodsAPI.DEL);
        request.add("goods_id", goods_id);
        defaultParam(request);

        baseActivity.request(DEL, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(DEL, response);
            }

            @Override
            public void onFailed(int what, String response) {
                baseActivity.dismissLoading();
                Toast.show(baseActivity, response);
            }
        }, true, false);
    }

    /**
     * 批量删除商品
     */
    public void batchDel(final BaseActivity baseActivity, ArrayList<Goods> goodsDatas, final IRequestCallBack iRequestCallBack) {
        ArrayList<RemoveGoods> removeGoodses = new ArrayList<>();
        for (Goods goodsData : goodsDatas) {
            RemoveGoods removeGoods = new RemoveGoods();
            removeGoods.goods_id = goodsData.goods_id;
            removeGoods.catalog_id = goodsData.catalog_id;
            removeGoodses.add(removeGoods);
        }

        final Request<String> request = getStringRequest(VdianGoodsAPI.BATCH_DEL);
        request.add("datas", new Gson().toJson(removeGoodses));
        defaultParam(request);

        baseActivity.request(DEL, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(DEL, response);
            }

            @Override
            public void onFailed(int what, String response) {
                baseActivity.dismissLoading();
                Toast.show(baseActivity, response);
            }
        }, true, false);
    }

    /**
     * 商品查询
     *
     * @param baseActivity
     * @param iRequestCallBack
     */
    public void query(final BaseActivity baseActivity, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(VdianGoodsAPI.QUERY);

        baseActivity.request(QUERY, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(QUERY, response);
            }

            @Override
            public void onFailed(int what, String response) {
                baseActivity.dismissLoading();
                Toast.show(baseActivity, response);
            }
        }, true, false);
    }

    /**
     * 商品编辑
     *
     * @param baseActivity
     * @param goods
     * @param iRequestCallBack
     */
    public void edit(final BaseActivity baseActivity, Goods goods, final IRequestCallBack iRequestCallBack) {
        final Request<String> request = getStringRequest(VdianGoodsAPI.EDIT);
        request.add("goods_id", goods.goods_id);
        request.add("goods_name", goods.goods_name);
        request.add("catalog_id", goods.catalog_id);
        request.add("catalog_name", goods.catalog_name);
        request.add("goods_no", goods.goods_no);
        request.add("goods_unit", goods.goods_unit);
        request.add("goods_discount", NumberFormatUtils.format(goods.goods_discount));
        request.add("goods_cost_section", goods.goods_cost_section);
        request.add("goods_price_section", goods.goods_price_section);
//        request.add("goods_auxiliary_unit", goods.goods_auxiliary_unit);
//        request.add("goods_auxiliary_unit_packingrate", Integer.toString(goods.goods_auxiliary_unit_packingrate));
        request.add("goods_number", NumberFormatUtils.formatToInteger(goods.goods_number));
        request.add("goods_sale_number", NumberFormatUtils.formatToInteger(goods.goods_sale_number));
        request.add("sale_type", NumberFormatUtils.formatToInteger(goods.sale_type));
//        request.add("goods_type", NumberFormatUtils.formatToInteger(goods.goods_type));
        request.add("remark", goods.remark);
        request.add("goods_move_picture", getImgsJson(goods.goods_move_picture));
        request.add("goods_spec", getSpecJson(goods.goods_spec));
//        request.add("goods_supplier", getSupplierJson(goods.goods_supplier));
//        request.add("main_supplier_id", goods.main_supplier_id);
//        request.add("main_supplier_name", goods.main_supplier_name);
        defaultParam(request);

        baseActivity.request(EDIT, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(EDIT, response);
            }

            @Override
            public void onFailed(int what, String response) {
                baseActivity.dismissLoading();
                Toast.show(baseActivity, response);
            }
        }, true, false);
    }

    /**
     * 批量上架商品
     *
     * @param baseActivity
     * @param goodsBeen
     * @param iRequestCallBack
     */
    public void putaway(final BaseActivity baseActivity, ArrayList<BatchUpGoodsBean> goodsBeen, String sale_type, final IRequestCallBack iRequestCallBack) {

        final Request<String> request = getStringRequest(VdianGoodsAPI.BATCH_UP);
        defaultParam(request);
        request.add("uid", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("shop_id", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
        request.add("key", SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN));
        request.add("sale_type", sale_type);
        request.add("datas", new Gson().toJson(goodsBeen));

        baseActivity.request(BATCH_UP_ID, request, new HttpListener<String>() {

            @Override
            public void onSucceed(int what, String response) {
                iRequestCallBack.onResult(BATCH_UP_ID, response);
            }

            @Override
            public void onFailed(int what, String response) {
                iRequestCallBack.onFailed(BATCH_UP_ID, response);
            }
        }, true, false);
    }

    // TODO ************************************************ 本地数据处理 ************************************************

    private ArrayList<Goods> goodsDatas = new ArrayList<>();

    private ArrayList<Goods> goodsBackDatas = new ArrayList<>();

    private ArrayList<Catalog> catalogs = new ArrayList<>();

    private ConcurrentHashMap<String, ArrayList<Goods>> packetData;

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
        String catalog_id = catalog.catalog_id;
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
            if (search_goods.goods_no.contains(keyword)) {
                Goods goodsData = new_goodsData(search_goods);
                if (goodsData == null) {
                    return null;
                }
                // 高亮处理
                goodsData.isNoLight = true;
                int noIndex = goodsData.goods_no.indexOf(keyword);
                goodsData.light_no = goodsData.goods_no.substring(0, noIndex) + getBankName(keyword)
                        + goodsData.goods_no.substring(noIndex + keyword.length(), goodsData.goods_no.length());
                search_goodses.add(goodsData);
                continue;
            }
            if (search_goods.goods_name.contains(keyword)) {
                Goods goodsData = new_goodsData(search_goods);
                if (goodsData == null) {
                    return null;
                }
                // 高亮处理
                goodsData.isNameLight = true;
                int noIndex = goodsData.goods_name.indexOf(keyword);
                goodsData.light_name = goodsData.goods_name.substring(0, noIndex) + getBankName(keyword)
                        + goodsData.goods_name.substring(noIndex + keyword.length(), goodsData.goods_name.length());
                search_goodses.add(goodsData);
                continue;
            }
            if (ListUtils.isNotEmpty(search_goods.goods_spec)) {
                for (GoodsSpec spec : search_goods.goods_spec) {
                    if (spec.spec_bar_code.contains(keyword)) {
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
            if (goods_id.equals(goods.goods_id)) {
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
            if (goods_id.equals(goods.goods_id)) {
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
                Integer num1 = o1.goods_sale_number;
                Integer num2 = o2.goods_sale_number;
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
                Integer num1 = o1.goods_number;
                Integer num2 = o2.goods_number;
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
        ArrayList<Catalog> catalogCurrentDatas = catalogs;
        if (packetData == null) {
            packetData = new ConcurrentHashMap<String, ArrayList<Goods>>();
        } else {
            packetData.clear();
        }

        for (Catalog catalog : catalogCurrentDatas) {
            ArrayList<Goods> goodses = new ArrayList<Goods>();
            for (Goods goods : goodsDatas) {
                if (goods.catalog_id.equals(catalog.catalog_id)) {
                    goodses.add(goods);
                }
            }
            catalog.catalog_goods_number = goodses.size();
            packetData.put(catalog.catalog_id, goodses);
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
}

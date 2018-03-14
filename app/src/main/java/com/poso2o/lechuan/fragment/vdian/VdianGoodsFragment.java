package com.poso2o.lechuan.fragment.vdian;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.GoodsListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealShopManager;
import com.poso2o.lechuan.manager.vdian.VdianGoodsManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.base.BaseManager.FIRST;

/**
 * 微店商品列表
 *
 * Created by Jaydon on 2018/3/14.
 */
public class VdianGoodsFragment extends BaseFragment {

    /**
     * 商户头像
     */
    private ImageView shop_info_icon;

    /**
     * 商户信息布局
     */
    private View shop_info_group;

    /**
     * 商户信息提示语
     */
    private View shop_info_hint;

    /**
     * 商户信息主视图
     */
    private View shop_info_main;

    /**
     * 商户名称
     */
    private TextView shop_info_name;

    /**
     * 商户描述
     */
    private TextView shop_info_describe;

    /**
     * 销量
     */
    private TextView shop_sort_sale;

    /**
     * 库存
     */
    private TextView shop_sort_stock;

    /**
     * 目录
     */
    private TextView shop_catalog;

    /**
     * 无商品提示
     **/
    private TextView no_goods_tips;

    /**
     * 列表
     */
    private SwipeRefreshLayout shop_swipe;
    private RecyclerView shop_recycle;
    private GoodsListAdapter shopGoodsAdapter;
    private View shop_shade;

    /**
     * 店铺数据
     */
    private ShopData shopData;

    /**
     * 下拉菜单
     */
    private CatalogPopupWindow catalogPopupWindow;

    /**
     * 选中的目录
     */
    private Catalog selectCatalog;

    /**
     * 排序：是否升序
     */
    private boolean isAscSale = false;
    private boolean isAscStock = false;

    /**
     * 目录ID
     */
    private String catalogId = "-1";

    /**
     * 排序类型
     */
    private String orderByName = "";

    /**
     * 排序方向
     */
    private String sort = BaseManager.ASC;

    /**
     * 搜索关键词
     */
    private String keywords = "";

    /**
     * 店铺ID
     */
    private long shop_id = 0;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vdian_goods, container, false);
    }

    @Override
    public void initView() {
        shop_info_icon = findView(R.id.shop_info_icon);
        shop_info_group = findView(R.id.shop_info_group);
        shop_info_hint = findView(R.id.shop_info_hint);
        shop_info_main = findView(R.id.shop_info_main);
        shop_info_name = findView(R.id.shop_info_name);
        shop_info_describe = findView(R.id.shop_info_describe);

        no_goods_tips = findView(R.id.no_goods_tips);

        shop_sort_sale = findView(R.id.shop_sort_sale);
        shop_sort_stock = findView(R.id.shop_sort_stock);
        shop_catalog = findView(R.id.shop_catalog);
        shop_swipe = findView(R.id.shop_swipe);
        shop_recycle = findView(R.id.shop_recycle);
        shop_shade = findView(R.id.shop_shade);

        shop_swipe.setColorSchemeColors(Color.RED);
    }

    @Override
    public void initData() {
        shopGoodsAdapter = new GoodsListAdapter(context, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shop_recycle.setLayoutManager(linearLayoutManager);
        shop_recycle.setAdapter(shopGoodsAdapter);

//        shopData = (ShopData) getIntent().getExtras().get(RShopMainActivity.DATA_SHOP);
//        refreshShopData();
        loadShopInfo();
        shop_swipe.setRefreshing(true);
        loadGoodsData(FIRST);
    }

    /**
     * 加载商品数据
     */
    private void loadGoodsData(final int pageType) {
        no_goods_tips.setVisibility(GONE);
        VdianGoodsManager.getInstance().query((BaseActivity) context, shop_id, selectCatalog.catalog_id, orderByName, sort, keywords, pageType, new IRequestCallBack<ArrayList<Goods>>() {

            @Override
            public void onResult(int tag, ArrayList<Goods> goodsDatas) {
                if (pageType == FIRST) {
                    if (ListUtils.isEmpty(goodsDatas)) {
                        no_goods_tips.setVisibility(VISIBLE);
                        no_goods_tips.setText(R.string.hint_load_goods_null);
                    } else {
                        no_goods_tips.setVisibility(GONE);
                    }
                    shopGoodsAdapter.notifyDataSetChanged(goodsDatas);
                } else {
                    shopGoodsAdapter.addItems(goodsDatas);
                }
                shop_swipe.setRefreshing(false);
            }

            @Override
            public void onFailed(int tag, String msg) {
                if (pageType == FIRST) {
                    no_goods_tips.setVisibility(VISIBLE);
                    no_goods_tips.setText(R.string.hint_load_goods_fail);
                }
                Toast.show(context, msg);
                shop_swipe.setRefreshing(false);
            }
        });
    }

    /**
     * 加载店铺数据 TODO 是否能用实体店的
     */
    private void loadShopInfo() {
        RealShopManager.getRealShopManager().rShopInfo((BaseActivity) context, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                shopData = (ShopData) result;
                refreshShopData();
//                if (change_shop_info) {
//                    Intent intent = new Intent();
//                    intent.putExtra(RShopMainActivity.DATA_SHOP, shopData);
//                    ((BaseActivity) context).setResult(Activity.RESULT_OK, intent);
//                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context, msg);
            }
        });
    }

    /**
     * 刷新店铺数据
     */
    private void refreshShopData() {
        if (shopData == null) return;
        Glide.with(this).load(shopData.pic222_222).placeholder(R.mipmap.background_image).into(shop_info_icon);
        shop_info_name.setText(shopData.shopname);
        shop_info_describe.setText(shopData.remark);
    }

    @Override
    public void initListener() {

    }

    /**
     * 搜索商品
     */
    public void search(String keywords) {
        this.keywords = keywords;
        loadGoodsData(FIRST);
    }
}

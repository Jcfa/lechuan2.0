package com.poso2o.lechuan.activity.mine;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.mine.GoodsData;
import com.poso2o.lechuan.dialog.CommissionSettingDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.GoodsDataManager;
import com.poso2o.lechuan.popubwindow.CatalogPopupWindow;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-05.
 */

public class CommissionSettingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ImageView ivSort;
    private String mCatalogId = "-1";//商品类目ID
    private String mKeywords = "";//关键字
    private int mCurrentPage = 1;//当前页
    private int mTotalPage = 1;
    private int mSaleType = 1;//销售状态，1正常销售，0停止销售
    private String mOrderByName = "";//  =排序类型  goods_sale_number 销量/build_time 新款/goods_price_section 售价
    private String mSort = "";//  排序方式  ASC从小到大，DESC从大到小
    private CatalogPopupWindow catalogPopupWindow;
    private Catalog selectCatalog;
    private ArrayList<Goods> goodses = new ArrayList<>();
    private ArrayList<Catalog> catalogs = new ArrayList<>();
    private BaseAdapter mAdapter;
    private TextView tvCatalog, tvRate, tvDiscount;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_commission_setting;
    }

    @Override
    protected void initView() {
        setTitle("佣金设置");
        ivSort = findView(R.id.iv_sort);
        tvCatalog = findView(R.id.tv_catalog);
        tvRate = findView(R.id.tv_commission_rate);
        tvDiscount = findView(R.id.tv_commission_discount);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(
                activity, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if (!mRecyclerView.canScrollVertically(1)) {
                    loadGoodsData(true);
                }
            }
        });
        mAdapter = new BaseAdapter(activity, goodses) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {
                final Goods goods = (Goods) item;
                ImageView ivLogo = holder.getView(R.id.iv_logo);
                TextView tvTitle = holder.getView(R.id.tv_title);
                TextView tvHh = holder.getView(R.id.tv_hh);
                TextView tvPrice = holder.getView(R.id.tv_price);
                TextView tvRate = holder.getView(R.id.tv_rate);
                TextView tvCommission = holder.getView(R.id.tv_commission);
                Glide.with(activity).load(goods.main_picture).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(ivLogo);
                tvTitle.setText(goods.goods_name);
                tvHh.setText(goods.goods_no);
                tvPrice.setText(goods.goods_price_section);
                tvRate.setText((int) goods.commission_rate + "%");
                tvCommission.setText(NumberUtils.format2(goods.commission_amount));
                LinearLayout layoutSetting = holder.getView(R.id.layout_setting);
                layoutSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCommissionSettingDialog(CommissionSettingDialog.SINGLE_TYPE, goods.goods_id);//单品佣金设置
                    }
                });
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.layout_mine_goods_item, viewGroup));
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        tvRate.setText((int) SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE) + "%");
        tvDiscount.setText((int) SharedPreferencesUtils.getFloat(SharedPreferencesUtils.KEY_USER_COMMISSION_DISCOUNT) + "");
        mSwipeRefreshLayout.setRefreshing(true);
        getCatalogList();
        getGoodsList();
    }

    @Override
    protected void initListener() {
        findView(R.id.layout_commission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOrderByName = GoodsDataManager.SORT_TYPE_COMMISSION;
                if (TextUtil.isEmpty(mSort)) {
                    mSort = BaseManager.ASC;
                    ivSort.setImageResource(R.mipmap.asc_icon);
                } else {
                    if (mSort.equals(BaseManager.ASC)) {
                        mSort = BaseManager.DESC;
                        ivSort.setImageResource(R.mipmap.desc_icon);
                    } else {
                        mSort = BaseManager.ASC;
                        ivSort.setImageResource(R.mipmap.asc_icon);
                    }
                }
                mSwipeRefreshLayout.setRefreshing(true);
                mCurrentPage = 1;
                loadGoodsData(false);
            }
        });
        tvCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (catalogPopupWindow != null && catalogs.size() > 0) {
                    catalogPopupWindow.showAsDropDown(tvCatalog);
                    setAlpha(0.7f);
                }
            }
        });
        findView(R.id.layout_commission_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommissionSettingDialog(CommissionSettingDialog.COMMON_TYPE, null);
            }
        });
    }

    /**
     * 显示佣金设置对话框
     */
    private void showCommissionSettingDialog(final int type, final String goodsId) {
        CommissionSettingDialog dialog = new CommissionSettingDialog(activity, type, new CommissionSettingDialog.SettingCallBack() {
            @Override
            public void setFinish(final float rate, final float discount) {
                GoodsDataManager manager = GoodsDataManager.getGoodsDataManager();
                if (type == CommissionSettingDialog.COMMON_TYPE) {
                    WaitDialog.showLoaddingDialog(activity);
                    manager.setCommissionRate(activity, rate + "", discount + "", new IRequestCallBack() {
                        @Override
                        public void onFailed(int tag, String msg) {
                            Toast.show(activity, msg);
                        }

                        @Override
                        public void onResult(int tag, Object object) {
                            Toast.show(activity, "设置成功！");
                            SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE, rate);
                            SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_COMMISSION_DISCOUNT, discount);
                            SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_HAS_COMMISSION, 1);
                            tvRate.setText((int) rate + "%");
                            tvDiscount.setText((int) discount + "");
                        }
                    });
                } else if (type == CommissionSettingDialog.SINGLE_TYPE) {
                    WaitDialog.showLoaddingDialog(activity);
                    manager.setGoodsCommissionRate(activity, rate + "", discount + "", goodsId, new IRequestCallBack() {
                        @Override
                        public void onFailed(int tag, String msg) {

                        }

                        @Override
                        public void onResult(int tag, Object object) {
                            Toast.show(activity, "设置成功！");
                            mSwipeRefreshLayout.setRefreshing(true);
                            WaitDialog.showLoaddingDialog(activity);
                            loadGoodsData(false);
                        }
                    });
                }
            }
        });
        dialog.show();
    }

    private void setAlpha(float alpha) {
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = alpha;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
    }

    @Override
    public void onRefresh() {
        loadGoodsData(false);
    }

    private void loadGoodsData(boolean next) {
        if (next) {
            if (mCurrentPage < mTotalPage) {
                ++mCurrentPage;
                getGoodsList();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        } else {
            mCurrentPage = 1;
            getGoodsList();
        }
    }

    /**
     * 读取商品列表
     */
    private void getGoodsList() {
        GoodsDataManager goodsDataManager = GoodsDataManager.getGoodsDataManager();
        goodsDataManager.getGoodsList(activity, mCatalogId, mOrderByName, mSort, "", mCurrentPage, new IRequestCallBack<GoodsData>() {
            @Override
            public void onFailed(int tag, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onResult(int tag, GoodsData object) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (object == null) {
                    return;
                }
                if (object.total != null) {
                    mTotalPage = object.total.pages;
                }
                if (mCurrentPage == 1) {
                    goodses.clear();
                }
                goodses.addAll(object.list);
                mAdapter.notifyDataSetChanged(goodses);
            }
        });
    }

    /**
     * 读取商品类目列表
     */
    private void getCatalogList() {
        GoodsDataManager goodsDataManager = GoodsDataManager.getGoodsDataManager();
        goodsDataManager.getCatalogList(activity, new IRequestCallBack<CatalogBean>() {
            @Override
            public void onFailed(int tag, String msg) {

            }

            @Override
            public void onResult(int tag, CatalogBean object) {
                if (selectCatalog == null) {
                    selectCatalog = new Catalog();
                    selectCatalog.catalog_id = "-1";
                    selectCatalog.catalog_name = "全部";
                }
                catalogs = object.list;
                initCatalogPopupWindow(catalogs);
            }
        });
    }

    /**
     * 初始化目录列表
     *
     * @param catalogs
     */
    private void initCatalogPopupWindow(ArrayList<Catalog> catalogs) {
        catalogPopupWindow = new CatalogPopupWindow(this, catalogs, selectCatalog);
        catalogPopupWindow.setOnItemClickListener(new CatalogPopupWindow.OnItemClickListener() {

            @Override
            public void onItemClick(Catalog catalog) {
                tvCatalog.setSelected(true);
                tvCatalog.setText(catalog.getNameAndNum());
                selectCatalog = catalog;
                mCatalogId = catalog.catalog_id;
                mSwipeRefreshLayout.setRefreshing(true);
                loadGoodsData(false);
            }
        });

        // 窗口撤销监听
        catalogPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                setAlpha(1);
//                select_goods_shade.setVisibility(GONE);
            }
        });
    }


}

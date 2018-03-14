package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MainMenuAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.bosstotal.BossTotalBean;
import com.poso2o.lechuan.bean.main_menu.MainMenuBean;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.configs.MenuConstant;
import com.poso2o.lechuan.configs.RShopPreferenceConfig;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.layoutmanager.MyGridLayoutManager;
import com.poso2o.lechuan.layoutmanager.MyItemTouchCallback;
import com.poso2o.lechuan.manager.rshopmanager.RReportManager;
import com.poso2o.lechuan.manager.rshopmanager.RealShopManager;
import com.poso2o.lechuan.manager.wshopmanager.WReportManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.popubwindow.ShopListPop;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.FileManage;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.ProgressChart;
import com.poso2o.lechuan.view.SaleTendencyChart;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/29.
 */

public class RShopMainActivity extends BaseActivity implements View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener{

    public static final int CODE_SHOP = 1001;
    //店铺信息
    public static final String DATA_SHOP = "shop_data";
    //跳转设置页
    public static final int CODE_MENU_SETTING = 2233;

    //店铺类型,1为实体店，2为微店，默认实体店
    private int mType = 1;

    private Context context;

    //商家编号布局
    private RelativeLayout r_shop_go;
    //商家头像
    private ImageView r_shop_img;
    //商家名称
    private TextView shop_name;
    //商家编号
    private TextView r_shop_id;
    //实体店微店下拉标识
    private ImageView shop_list_down;

    private SwipeRefreshLayout refresh_swipe;
    private ScrollView main_scroll;

    //销售额
    private RelativeLayout main_sale_amount;
    private TextView main_total_sale_amount;
    //完成率
    private RelativeLayout main_finish_rate;
    private TextView main_total_finish;
    //毛利润
    private RelativeLayout main_profit;
    private TextView main_total_profit;

    //图表容器
    private LinearLayout main_chart_view_root;
    //销售趋势图
    private SaleTendencyChart saleTendencyChart;
    //进度标尺图
    private ProgressChart progressChart;

    //菜单列表
    private RecyclerView menu_container;

    //菜单集合
    private ArrayList<MainMenuBean> beans;
    //宫格适配器
    private MainMenuAdapter menuAdapter;

    //是否微店
    private boolean is_online;
    private ShopData shopData;
    private int width;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_r_shop_main;
    }

    @Override
    protected void initView() {
        context = this;

        r_shop_go = (RelativeLayout) findViewById(R.id.r_shop_go);
        r_shop_img = (ImageView) findViewById(R.id.r_shop_img);
        shop_name = (TextView) findViewById(R.id.shop_name);
        r_shop_id = (TextView) findViewById(R.id.r_shop_id);
        shop_list_down = (ImageView) findViewById(R.id.shop_list_down);

        refresh_swipe = (SwipeRefreshLayout) findViewById(R.id.refresh_swipe);
        main_scroll = (ScrollView) findViewById(R.id.main_scroll);

        main_sale_amount = (RelativeLayout) findViewById(R.id.main_sale_amount);
        main_total_sale_amount = (TextView) findViewById(R.id.main_total_sale_amount);

        main_finish_rate = (RelativeLayout) findViewById(R.id.main_finish_rate);
        main_total_finish = (TextView) findViewById(R.id.main_total_finish);

        main_profit = (RelativeLayout) findViewById(R.id.main_profit);
        main_total_profit = (TextView) findViewById(R.id.main_total_profit);

        main_chart_view_root = (LinearLayout) findViewById(R.id.main_chart_view_root);

        menu_container = (RecyclerView) findViewById(R.id.menu_container);

        refresh_swipe.setColorSchemeColors(0xffff0000);
    }

    @Override
    protected void initData() {

        saleTendencyChart = new SaleTendencyChart(context);
        progressChart = new ProgressChart(context);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();

        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_SHOP) == 1 && SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_WEBSHOP) == 1) {
            shop_list_down.setVisibility(View.VISIBLE);
        } else {
            shop_list_down.setVisibility(View.GONE);
        }

        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_SHOP) == 1) {
            mType = 1;
            SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_SHOP_TYPE, false);
            is_online = false;
            requestRealShop();
        } else {
            mType = 2;
            SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_SHOP_TYPE, true);
            is_online = true;
            requestVShop();
        }

        initMenu();
        showLoading("正在加载数据...");
        initTotalData();
        main_scroll.smoothScrollTo(0,0);
    }

    @Override
    protected void initListener() {
        refresh_swipe.setOnRefreshListener(this);
        r_shop_go.setOnClickListener(this);
        main_sale_amount.setOnClickListener(this);
        main_finish_rate.setOnClickListener(this);
        main_profit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.r_shop_go:
                showListPop();
                break;
            case R.id.main_sale_amount:
                main_finish_rate.setSelected(false);
                main_profit.setSelected(false);
                if (main_sale_amount.isSelected()) {
                    main_chart_view_root.setVisibility(View.GONE);
                    main_sale_amount.setSelected(false);
                } else {
                    main_sale_amount.setSelected(true);
                    main_chart_view_root.setVisibility(View.VISIBLE);
                    main_chart_view_root.removeAllViews();
                    main_chart_view_root.addView(saleTendencyChart.getRootView());
                }
                break;
            case R.id.main_finish_rate:
                main_sale_amount.setSelected(false);
                main_profit.setSelected(false);
                if (main_finish_rate.isSelected()) {
                    main_finish_rate.setSelected(false);
                    main_chart_view_root.setVisibility(View.GONE);
                } else {
                    main_finish_rate.setSelected(true);
                    main_chart_view_root.setVisibility(View.VISIBLE);
                    main_chart_view_root.removeAllViews();
                    main_chart_view_root.addView(progressChart.getRootView());
                }
                break;
            case R.id.main_profit:
                main_sale_amount.setSelected(false);
                main_finish_rate.setSelected(false);
                if (main_profit.isSelected()) {
                    main_profit.setSelected(false);
                    main_chart_view_root.setVisibility(View.GONE);
                } else {
                    main_profit.setSelected(true);
                    main_chart_view_root.setVisibility(View.VISIBLE);
                    main_chart_view_root.removeAllViews();
                    main_chart_view_root.addView(saleTendencyChart.getRootView());
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        initTotalData();
    }

    private void initMenu() {

        if (mType == 1) {
            initMenuData();
        } else {
            initWMenuData();
        }

        menuAdapter = new MainMenuAdapter(this, beans);
        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setScrollEnabled(false);
        menu_container.setLayoutManager(gridLayoutManager);
        menu_container.setAdapter(menuAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(menuAdapter));
        itemTouchHelper.attachToRecyclerView(menu_container);
    }

    private ArrayList<MainMenuBean> initMenuData() {
        beans = new ArrayList<>();
        Object obj = FileManage.restoreObject(MenuConstant.SETTING_MENU_SELECT_DATA);
        if ((obj == null)) {

            MainMenuBean realshop = new MainMenuBean();
            realshop.menu_id = 13;
            realshop.menu_name = "创建实体店";
            realshop.isSelected = true;
            beans.add(realshop);

            MainMenuBean wShop = new MainMenuBean();
            wShop.menu_id = 8;
            wShop.menu_name = "创建微店";
            wShop.isSelected = true;
            beans.add(wShop);

            MainMenuBean addGoods = new MainMenuBean();
            addGoods.menu_id = 2;
            addGoods.menu_name = "商品管理";
            addGoods.isSelected = true;
            beans.add(addGoods);

            MainMenuBean order = new MainMenuBean();
            order.menu_id = 3;
            order.menu_name = "订单";
            order.isSelected = true;
            beans.add(order);

            MainMenuBean member = new MainMenuBean();
            member.menu_id = 4;
            member.menu_name = "会员";
            member.isSelected = true;
            beans.add(member);

            MainMenuBean staff = new MainMenuBean();
            staff.menu_id = 5;
            staff.menu_name = "员工";
            staff.isSelected = true;
            beans.add(staff);

            MainMenuBean report = new MainMenuBean();
            report.menu_id = 6;
            report.menu_name = "报表";
            report.isSelected = true;
            beans.add(report);

            MainMenuBean marketing = new MainMenuBean();
            marketing.menu_id = 7;
            marketing.menu_name = "营销";
            marketing.isSelected = true;
            beans.add(marketing);

            /*MainMenuBean newec = new MainMenuBean();
            newec.menu_id = 8;
            newec.menu_name = "创建微店";
            newec.isSelected = true;
            beans.add(newec);*/

            /*MainMenuBean wechat = new MainMenuBean();
            wechat.menu_id = 9;
            wechat.menu_name = "公众号助手";
            wechat.isSelected = true;
            beans.add(wechat);*/

            MainMenuBean lcd = new MainMenuBean();
            lcd.menu_id = 10;
            lcd.menu_name = "乐传分销";
            lcd.isSelected = true;
            beans.add(lcd);

            MainMenuBean toMarket = new MainMenuBean();
            toMarket.menu_id = 15;
            toMarket.menu_name = "我要分销";
            toMarket.isSelected = true;
            beans.add(toMarket);

            MainMenuBean helper = new MainMenuBean();
            helper.menu_id = 11;
            helper.menu_name = "帮助";
            helper.isSelected = true;
            beans.add(helper);

            MainMenuBean setting = new MainMenuBean();
            setting.menu_id = 12;
            setting.menu_name = "设置";
            setting.isSelected = true;
            beans.add(setting);

            FileManage.saveObject(MenuConstant.SETTING_MENU_SELECT_DATA, beans);
        } else {
            ArrayList<MainMenuBean> temp = (ArrayList<MainMenuBean>) obj;
            for (MainMenuBean bean : temp) {
                if (bean.isSelected) beans.add(bean);
            }
        }
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_SHOP) == 0) {
            //没有实体店，把本地保存的实体店改为创建实体店
            for (MainMenuBean bean : beans) {
                if (bean.menu_id == 1) {
                    bean.menu_id = 13;
                    bean.menu_name = "创建实体店";
                    break;
                }
            }
        } else {
            //有实体店，把本地保存的创建实体店改为实体店
            for (MainMenuBean bean : beans) {
                if (bean.menu_id == 13) {
                    bean.menu_id = 1;
                    bean.menu_name = "实体店";
                    break;
                }
            }
        }
        /*if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_WEBSHOP) == 0) {
            //没有微店，把本地保存的微店改为创建微店
            for (MainMenuBean bean : beans) {
                if (bean.menu_id == 14) {
                    bean.menu_id = 8;
                    bean.menu_name = "创建微店";
                    break;
                }
            }
        } else {
            //有微店，把本地保存的创建微店改为微店
            for (MainMenuBean bean : beans) {
                if (bean.menu_id == 8) {
                    bean.menu_id = 14;
                    bean.menu_name = "微店";
                    break;
                }
            }
        }*/
        return beans;
    }

    private void initWMenuData() {
        beans = new ArrayList<>();
        Object obj = FileManage.restoreObject(MenuConstant.SETTING_W_MENU_SELECT_DATA);
        if ((obj == null)) {

            /*MainMenuBean realshop = new MainMenuBean();
            realshop.menu_id = 1;
            realshop.menu_name = "实体店";
            realshop.isSelected = true;
            beans.add(realshop);*/

            MainMenuBean wShop = new MainMenuBean();
            wShop.menu_id = 14;
            wShop.menu_name = "微店";
            wShop.isSelected = true;
            beans.add(wShop);

            MainMenuBean addGoods = new MainMenuBean();
            addGoods.menu_id = 2;
            addGoods.menu_name = "商品管理";
            addGoods.isSelected = true;
            beans.add(addGoods);

            MainMenuBean market = new MainMenuBean();
            market.menu_id = 7;
            market.menu_name = "营销";
            market.isSelected = true;
            beans.add(market);

            MainMenuBean order = new MainMenuBean();
            order.menu_id = 3;
            order.menu_name = "订单";
            order.isSelected = true;
            beans.add(order);

            MainMenuBean member = new MainMenuBean();
            member.menu_id = 4;
            member.menu_name = "会员";
            member.isSelected = true;
            beans.add(member);

            /*MainMenuBean staff = new MainMenuBean();
            staff.menu_id = 5;
            staff.menu_name = "员工";
            staff.isSelected = true;
            beans.add(staff);*/

            MainMenuBean report = new MainMenuBean();
            report.menu_id = 6;
            report.menu_name = "报表";
            report.isSelected = true;
            beans.add(report);

            MainMenuBean wechat = new MainMenuBean();
            wechat.menu_id = 9;
            wechat.menu_name = "公众号助手";
            wechat.isSelected = true;
            beans.add(wechat);

            MainMenuBean lcd = new MainMenuBean();
            lcd.menu_id = 10;
            lcd.menu_name = "乐传分销";
            lcd.isSelected = true;
            beans.add(lcd);

            MainMenuBean toMarket = new MainMenuBean();
            toMarket.menu_id = 15;
            toMarket.menu_name = "我要分销";
            toMarket.isSelected = true;
            beans.add(toMarket);

            MainMenuBean helper = new MainMenuBean();
            helper.menu_id = 11;
            helper.menu_name = "帮助";
            helper.isSelected = true;
            beans.add(helper);

            /*MainMenuBean newrshop = new MainMenuBean();
            newrshop.menu_id = 13;
            newrshop.menu_name = "创建实体店";
            newrshop.isSelected = true;
            beans.add(newrshop);*/

            MainMenuBean setting = new MainMenuBean();
            setting.menu_id = 12;
            setting.menu_name = "设置";
            setting.isSelected = true;
            beans.add(setting);

            FileManage.saveObject(MenuConstant.SETTING_W_MENU_SELECT_DATA, beans);
        } else {
            ArrayList<MainMenuBean> temp = (ArrayList<MainMenuBean>) obj;
            for (MainMenuBean bean : temp) {
                if (bean.isSelected) beans.add(bean);
            }
        }
        /*if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_SHOP) == 0) {
            //没有实体店，把本地保存的实体店改为创建实体店
            for (MainMenuBean bean : beans) {
                if (bean.menu_id == 1) {
                    bean.menu_id = 13;
                    bean.menu_name = "创建实体店";
                    break;
                }
            }
        } else {
            //有实体店，把本地保存的创建实体店改为实体店
            for (MainMenuBean bean : beans) {
                if (bean.menu_id == 13) {
                    bean.menu_id = 1;
                    bean.menu_name = "实体店";
                    break;
                }
            }
        }*/
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_WEBSHOP) == 0) {
            //没有微店，把本地保存的微店改为创建微店
            for (MainMenuBean bean : beans) {
                if (bean.menu_id == 14) {
                    bean.menu_id = 8;
                    bean.menu_name = "创建微店";
                    break;
                }
            }
        } else {
            //有微店，把本地保存的创建微店改为微店
            for (MainMenuBean bean : beans) {
                if (bean.menu_id == 8) {
                    bean.menu_id = 14;
                    bean.menu_name = "微店";
                    break;
                }
            }
        }
    }

    private void showListPop() {
        if (!(SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_SHOP) == 1 && SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_WEBSHOP) == 1))
            return;
        ShopListPop pop = new ShopListPop(context, mType);
        pop.showAsDropDown(r_shop_go, width*40/720, 0);
        pop.setOnShopClickListener(new ShopListPop.OnShopClickListener() {
            @Override
            public void onShopClick(int type) {
                mType = type;
                SharedPreferencesUtils.put(RShopPreferenceConfig.SHOP_TYPE, type);
                shop_name.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
                r_shop_id.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID));
                String logo = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO);
                if (TextUtil.isNotEmpty(logo))
                    Glide.with(context).load(logo).transform(new GlideCircleTransform(context)).into(r_shop_img);
                if (type == 1) {
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_SHOP_TYPE, false);
                    is_online = false;
                    requestRealShop();
                } else if (type == 2) {
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_SHOP_TYPE, true);
                    is_online = true;
                    requestVShop();
                }
                initMenu();
                initTotalData();
            }
        });
    }

    private void initTotalData(){
        if (is_online){
            WReportManager.getwReportManager().wBossTotal(this, "", "", new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    refresh_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    refresh_swipe.setRefreshing(false);
                    BossTotalBean bossTotalBean = (BossTotalBean) object;
                    if (bossTotalBean == null)return;
                    main_total_sale_amount.setText(NumberFormatUtils.format(bossTotalBean.sale_amounts));
                    main_total_finish.setText(bossTotalBean.completion_rate + "%");
                    main_total_profit.setText(bossTotalBean.gross_profit + "");
                }
            });
        }else {
            RReportManager.getRReportManger().totalReport(this,new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    refresh_swipe.setRefreshing(false);
                    Toast.show(context,msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    dismissLoading();
                    refresh_swipe.setRefreshing(false);
                    BossTotalBean bossTotalBean = (BossTotalBean) object;
                    if (bossTotalBean == null)return;
                    main_total_sale_amount.setText(NumberFormatUtils.format(bossTotalBean.order_amounts));
                    main_total_finish.setText(NumberFormatUtils.format(bossTotalBean.completion_rate) + "%");
                    main_total_profit.setText(NumberFormatUtils.format(bossTotalBean.gross_profit) + "");
                }
            });
        }
    }

    //加载实体店店铺信息
    private void requestRealShop(){
        RealShopManager.getRealShopManager().rShopInfo(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                shopData = (ShopData) result;
                if (shopData == null)return;
                shop_name.setText(shopData.shopname);
                r_shop_id.setText(shopData.shopid);
                if (TextUtil.isNotEmpty(shopData.pic222_222))
                    Glide.with(context).load(shopData.pic222_222).placeholder(R.mipmap.background_image).transform(new GlideCircleTransform(context)).into(r_shop_img);
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context,"加载店铺信息失败");
            }
        });
    }

    public ShopData getShopData(){
        return shopData;
    }

    //加载微店店铺信息
    private void requestVShop(){
        WShopManager.getrShopManager().wShopInfo(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                shopData = (ShopData) result;
                if (shopData == null)return;
                shop_name.setText(shopData.shop_name);
                r_shop_id.setText(shopData.shop_id);
                if (TextUtil.isNotEmpty(shopData.shop_logo))
                    Glide.with(context).load(shopData.shop_logo).placeholder(R.mipmap.background_image).transform(new GlideCircleTransform(context)).into(r_shop_img);

            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context,"加载店铺信息失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_MENU_SETTING && resultCode == RESULT_OK) {
            initMenu();
            menuAdapter.notifyAdapter(beans);
        }else if (requestCode == CODE_SHOP && resultCode == RESULT_OK && data != null){
            shopData = (ShopData) data.getExtras().get(DATA_SHOP);
            setShopData();
        }
    }

    private boolean onceAgain = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!onceAgain) {
                Toast.show(activity, getString(R.string.exit_app));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onceAgain = false;
                    }
                }, 2000);
                onceAgain = true;
                return false;
            }
            AppUtil.exitApp(activity, false);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setShopData(){
        if (shopData == null)return;
        if (is_online){
            shop_name.setText(shopData.shop_name);
            r_shop_id.setText(shopData.shop_id);
            if (TextUtil.isNotEmpty(shopData.shop_logo))
                Glide.with(context).load(shopData.shop_logo).placeholder(R.mipmap.background_image).transform(new GlideCircleTransform(context)).into(r_shop_img);
        }else {
            shop_name.setText(shopData.shopname);
            r_shop_id.setText(shopData.shopid);
            if (TextUtil.isNotEmpty(shopData.pic222_222))
                Glide.with(context).load(shopData.pic222_222).placeholder(R.mipmap.background_image).transform(new GlideCircleTransform(context)).into(r_shop_img);
        }
    }

}

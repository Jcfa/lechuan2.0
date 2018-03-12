package com.poso2o.lechuan.activity.realshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.login.LoginActivity;
import com.poso2o.lechuan.adapter.SettingMenuAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.main_menu.MainMenuBean;
import com.poso2o.lechuan.configs.MenuConstant;
import com.poso2o.lechuan.layoutmanager.MyGridLayoutManager;
import com.poso2o.lechuan.layoutmanager.MyItemTouchCallback;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.FileManage;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/7/31.
 * 设置页
 */

public class MenuSettingActivity extends BaseActivity {

    //左上角返回键
    private ImageView setting_back;
    //界面标题
    private TextView setting_title;
    //右上角确定按钮
    private Button setting_sure;
    //菜单列表
    private RecyclerView setting_menu_recycler;

    //退出登录
    private Button exit_app;

    //列表适配器
    private SettingMenuAdapter menuAdapter;
    //所有可选菜单集合
    private ArrayList<MainMenuBean> beans;

    //是否微店
    private boolean isOnline;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

        setting_back = (ImageView) findViewById(R.id.setting_back);

        setting_title = (TextView) findViewById(R.id.setting_title);

        setting_sure = (Button) findViewById(R.id.setting_sure);

        setting_menu_recycler = (RecyclerView) findViewById(R.id.setting_menu_recycler);

        exit_app = (Button) findViewById(R.id.exit_app);

        exit_app.setSelected(true);
    }

    @Override
    protected void initData() {
        isOnline = SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE);
        initAdapter();
    }

    @Override
    protected void initListener() {

        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setting_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSureClick();
            }
        });

        exit_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                exitApp();
                AppUtil.exitApp(activity, true);
            }
        });
    }

    private void initAdapter() {

        if (isOnline) {
            initWMenuData();
        } else {
            initMenuData();
        }

        menuAdapter = new SettingMenuAdapter(this, beans, isOnline);
        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setting_menu_recycler.setLayoutManager(gridLayoutManager);
        setting_menu_recycler.setAdapter(menuAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new MyItemTouchCallback(menuAdapter));
        touchHelper.attachToRecyclerView(setting_menu_recycler);
    }

    private ArrayList<MainMenuBean> initMenuData() {

        beans = new ArrayList<>();

        Object obj = FileManage.restoreObject(MenuConstant.SETTING_MENU_SELECT_DATA);

        if (obj == null) {

            MainMenuBean realshop = new MainMenuBean();
            realshop.menu_id = 1;
            realshop.menu_name = "实体店";
            realshop.isSelected = true;
            beans.add(realshop);

            /*MainMenuBean wShop = new MainMenuBean();
            wShop.menu_id = 14;
            wShop.menu_name = "微店";
            wShop.isSelected = true;
            beans.add(wShop);*/

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

            MainMenuBean setting = new MainMenuBean();
            setting.menu_id = 12;
            setting.menu_name = "设置";
            setting.isSelected = true;
            beans.add(setting);

            FileManage.saveObject(MenuConstant.SETTING_MENU_SELECT_DATA, beans);
        } else {
            beans = (ArrayList<MainMenuBean>) obj;
        }
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_SHOP)==0){
            //没有实体店，把本地保存的实体店改为创建实体店
            for (MainMenuBean bean : beans){
                if (bean.menu_id == 1){
                    bean.menu_id = 13;
                    bean.menu_name = "创建实体店";
                    break;
                }
            }
        }else {
            //有实体店，把本地保存的创建实体店改为实体店
            for (MainMenuBean bean : beans){
                if (bean.menu_id == 13){
                    bean.menu_id = 1;
                    bean.menu_name = "实体店";
                    break;
                }
            }
        }
        /*if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_WEBSHOP)==0){
            //没有微店，把本地保存的微店改为创建微店
            for (MainMenuBean bean : beans){
                if (bean.menu_id == 14){
                    bean.menu_id = 13;
                    bean.menu_name = "创建微店";
                    break;
                }
            }
        }else {
            //有微店，把本地保存的创建微店改为微店
            for (MainMenuBean bean : beans){
                if (bean.menu_id == 13){
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
            beans = (ArrayList<MainMenuBean>) obj;
        }
        /*if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_SHOP)==0){
            //没有实体店，把本地保存的实体店改为创建实体店
            for (MainMenuBean bean : beans){
                if (bean.menu_id == 1){
                    bean.menu_id = 13;
                    bean.menu_name = "创建实体店";
                    break;
                }
            }
        }else {
            //有实体店，把本地保存的创建实体店改为实体店
            for (MainMenuBean bean : beans){
                if (bean.menu_id == 13){
                    bean.menu_id = 1;
                    bean.menu_name = "实体店";
                    break;
                }
            }
        }*/
        if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_HAS_WEBSHOP)==0){
            //没有微店，把本地保存的微店改为创建微店
            for (MainMenuBean bean : beans){
                if (bean.menu_id == 14){
                    bean.menu_id = 8;
                    bean.menu_name = "创建微店";
                    break;
                }
            }
        }else {
            //有微店，把本地保存的创建微店改为微店
            for (MainMenuBean bean : beans){
                if (bean.menu_id == 8){
                    bean.menu_id = 14;
                    bean.menu_name = "微店";
                    break;
                }
            }
        }
    }

    //点击确定
    private void onSureClick() {
        /*ArrayList<MainMenuBean> temp = new ArrayList<>();
        for(MainMenuBean bean : beans){
            if(bean.isSelected){
                temp.add(bean);
            }
        }
        FileManage.saveObject(MenuConstant.SETTING_MENU_SELECT_DATA,temp);
        FileManage.saveObject(MenuConstant.SETTING_MENU_DATA,beans);*/
        menuAdapter.saveSettingMenu();
        setResult(RESULT_OK);
        finish();
    }

    //退出登录
    private void exitApp() {
        Intent exit = new Intent();
        exit.setAction("");
        sendBroadcast(exit);
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

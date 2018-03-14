package com.poso2o.lechuan.bean.main_menu;

import android.content.Context;
import android.content.Intent;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.activity.goods.RGoodsActivity;
import com.poso2o.lechuan.activity.order.OrderActivity;
import com.poso2o.lechuan.activity.realshop.MemberActivity;
import com.poso2o.lechuan.activity.realshop.MenuSettingActivity;
import com.poso2o.lechuan.activity.realshop.MenuStaffActivity;
import com.poso2o.lechuan.activity.realshop.OAHelperActivity;
import com.poso2o.lechuan.activity.realshop.OldOrderActivity;
import com.poso2o.lechuan.activity.realshop.RReportActivity;
import com.poso2o.lechuan.activity.realshop.RShopActivity;
import com.poso2o.lechuan.activity.realshop.RShopDesActivity;
import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.activity.wopenaccount.EmpowermentActivity;
import com.poso2o.lechuan.activity.wshop.LechuanServiceActivity;
import com.poso2o.lechuan.activity.wshop.VdianActivity;
import com.poso2o.lechuan.activity.wshop.WShopActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.io.Serializable;

/**
 * Created by mr zhang on 2017/7/29.
 * 设置页的菜单项
 */

public class MainMenuBean implements Serializable{

    public boolean isSelected;

    public int menu_id ; // 1为实体店，2为商品管理，3为订单，4为会员，5为员工，6为报表，7为营销，8为创建微店，9为公众号助手，10为乐传分销，11为帮助，12为设置，13为创建实体店,14为微店，15为我要分销

    public int menu_picture  = R.mipmap.ic_launcher;

    public String menu_name = "";

    public boolean isTips;

    public int tips;

    public void goToMenu(Context context,int menuType){
        Intent intent = new Intent();
        switch (menuType){
            case 1:
                RShopMainActivity activity = (RShopMainActivity) context;
                intent.setClass(context, RShopActivity.class);
                intent.putExtra(RShopMainActivity.DATA_SHOP,activity.getShopData());
                activity.startActivityForResult(intent,RShopMainActivity.CODE_SHOP);
                return;
            case 2:
                intent.setClass(context, RGoodsActivity.class);
                break;
            case 3:
                if (SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE)){
                    intent.setClass(context, OrderActivity.class);
                }else {
                    intent.setClass(context, OldOrderActivity.class);
                }
                break;
            case 4:
                intent.setClass(context, MemberActivity.class);
                break;
            case 5:
                if (SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.KEY_SHOP_TYPE)){
                    Toast.show(context,"该功能尚未开放，敬请等待");
                    return;
                }
                intent.setClass(context, MenuStaffActivity.class);
                break;
            case 6:
                intent.setClass(context, RReportActivity.class);
                break;
            case 7:
                Toast.show(context,"该功能尚未开放，敬请等待");
                return;
//                intent.setClass(context, RMarketActivity.class);
//                break;
            case 8:
                intent.setClass(context, VdianActivity.class);
//                int type = SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SHOP_VERIFY);
//                if (type == 1){
//                    intent.setClass(context, WShopDesActivity.class);
//                }else if (type == 2 || type == 4){
//                    intent.setClass(context, CompanyDefineActivity.class);
//                }else {
//                    intent.setClass(context, WShopDesActivity.class);
//                }
                break;
            case 9:
                intent.setClass(context, OAHelperActivity.class);
                break;
            case 10:
                if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SERVICE_DAYS) <= 7){
                    intent.setClass(context, LechuanServiceActivity.class);
                }else {
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, Constant.MERCHANT_TYPE);
                    intent.setClass(context,MainActivity.class);
                }
                break;
            case 11:
                Toast.show(context,"该功能尚未开放，敬请等待");
                return;
            case 12:
                //特殊处理，需要从设置页拿到返回数据，如果数据变动，需要刷新首页菜单内容
                intent.setClass(context, MenuSettingActivity.class);
                ((BaseActivity)context).startActivityForResult(intent, RShopMainActivity.CODE_MENU_SETTING);
                return;
            case 13:
                intent.setClass(context, RShopDesActivity.class);
                break;
            case 14:
                RShopMainActivity vactivity = (RShopMainActivity) context;
            //    intent.setClass(context, WShopActivity.class);
                intent.setClass(context, EmpowermentActivity.class);
                intent.putExtra(RShopMainActivity.DATA_SHOP,vactivity.getShopData());
                vactivity.startActivityForResult(intent,RShopMainActivity.CODE_SHOP);
                return;
            case 15:
                SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, Constant.DISTRIBUTION_TYPE);
                intent.setClass(context,MainActivity.class);
                break;
        }
        context.startActivity(intent);
    }

}
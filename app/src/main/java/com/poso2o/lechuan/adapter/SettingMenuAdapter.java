package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.main_menu.MainMenuBean;
import com.poso2o.lechuan.configs.MenuConstant;
import com.poso2o.lechuan.util.FileManage;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/7/31.
 * 设置页菜单列表适配器
 */

public class SettingMenuAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MainMenuBean> mDatas;
    //是否微店
    private boolean isOnline;

    public SettingMenuAdapter(Context context, ArrayList<MainMenuBean> menuBeens,boolean isOnline) {
        this.context = context;
        mDatas = menuBeens;
        this.isOnline = isOnline;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_setting_menu,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MainMenuBean bean = mDatas.get(position);
        if(bean == null)return;
        MenuViewHolder vh = (MenuViewHolder) holder;
        vh.setting_menu_name.setText(bean.menu_name);
        setImageResourse(bean.menu_id,vh.setting_menu_icon);
        if(bean.isSelected){
            vh.setting_menu_select.setImageResource(R.mipmap.setting_menu_select);
        }else {
            vh.setting_menu_select.setImageResource(R.mipmap.setting_menu_unselect);
        }

        vh.setting_menu_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean.menu_id == 12){
                    Toast.show(context,"设置项不可取消");
                }else {
                    bean.isSelected = !bean.isSelected;
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mDatas == null) return 0;
        return mDatas.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout setting_menu_root;

        ImageView setting_menu_icon;

        TextView setting_menu_name;

        ImageView setting_menu_select;

        public MenuViewHolder(View itemView) {
            super(itemView);

            setting_menu_root = (RelativeLayout) itemView.findViewById(R.id.setting_menu_root);

            setting_menu_icon = (ImageView) itemView.findViewById(R.id.setting_menu_icon);

            setting_menu_name = (TextView) itemView.findViewById(R.id.setting_menu_name);

            setting_menu_select = (ImageView) itemView.findViewById(R.id.setting_menu_select);
        }
    }

    public ArrayList<MainMenuBean> getDataList(){
        return mDatas;
    }

    public void saveSettingMenu(){
        if (isOnline){
            FileManage.saveObject(MenuConstant.SETTING_W_MENU_SELECT_DATA,mDatas);
        }else {
            FileManage.saveObject(MenuConstant.SETTING_MENU_SELECT_DATA,mDatas);
        }
    }

    private void setImageResourse(int type , ImageView imageView){
        if(type == 1){
            imageView.setImageResource(R.mipmap.icon_entity_shop_90);
        }else if (type == 2){
            imageView.setImageResource(R.mipmap.icon_menu_up);
        }else if (type == 3){
            imageView.setImageResource(R.mipmap.order_icon);
        }else if (type == 4){
            imageView.setImageResource(R.mipmap.ware_manage);
        }else if (type == 5){
            imageView.setImageResource(R.mipmap.employee_manage);
        }else if (type == 6){
            imageView.setImageResource(R.mipmap.performance_icon);
        }else if (type == 7){
            imageView.setImageResource(R.mipmap.icon_marketing_90);
        }else if (type == 8){
            imageView.setImageResource(R.mipmap.icon_add_with_circle_64);
        }else if (type == 9){
            imageView.setImageResource(R.mipmap.icon_menu_vx);
        }else if (type == 10){
            imageView.setImageResource(R.mipmap.icon_goods_up_90);
        }else if (type == 11){
            imageView.setImageResource(R.mipmap.order_icon);
        }else if (type == 12){
            imageView.setImageResource(R.mipmap.icon_menu_setting);
        }else if (type == 13){
            imageView.setImageResource(R.mipmap.icon_add_with_circle_64);
        }else if (type == 14){
            imageView.setImageResource(R.mipmap.icon_menu_online);
        }else if (type == 15){
            imageView.setImageResource(R.mipmap.icon_goods_up_90);
        }
    }
}

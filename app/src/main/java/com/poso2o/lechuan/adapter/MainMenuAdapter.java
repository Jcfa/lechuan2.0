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
import com.poso2o.lechuan.util.FileManage;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/7/29.
 */

public class MainMenuAdapter extends RecyclerView.Adapter {

    private Context context ;
    private ArrayList<MainMenuBean> mDatas ;

    public MainMenuAdapter(Context context, ArrayList<MainMenuBean> menuBeans) {
        this.context = context;
        mDatas = menuBeans;
    }

    public void notifyAdapter(ArrayList<MainMenuBean> menuBeans){
        mDatas = menuBeans;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_menu,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mDatas == null) return;
        final MainMenuBean bean = mDatas.get(position);
        MenuViewHolder vh = (MenuViewHolder) holder;
        vh.main_menu_name.setText(bean.menu_name);
        setImageResourse(bean.menu_id,vh.main_menu_icon);

        if (bean.isTips){
            vh.red_order_tips.setText(bean.tips + "");
            vh.red_order_tips.setVisibility(View.VISIBLE);
        }else {
            vh.red_order_tips.setVisibility(View.GONE);
        }

        vh.main_menu_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.goToMenu(context,bean.menu_id);
                if(bean.menu_id == 9){
                    //如果跳转到设置页，设置标记为true，方便从设置页回到首页时刷新九宫格
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) return 0;
        return mDatas.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout main_menu_root;

        ImageView main_menu_icon;

        TextView main_menu_name;

        TextView red_order_tips;

        public MenuViewHolder(View itemView) {
            super(itemView);

            main_menu_root = (RelativeLayout) itemView.findViewById(R.id.main_menu_root);

            main_menu_icon = (ImageView) itemView.findViewById(R.id.main_menu_icon);

            main_menu_name = (TextView) itemView.findViewById(R.id.main_menu_name);

            red_order_tips = (TextView) itemView.findViewById(R.id.red_order_tips);
        }
    }

    public ArrayList<MainMenuBean> getDataList(){
        return mDatas;
    }

    public void saveMenus(){
        FileManage.saveObject("",mDatas);
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
            imageView.setImageResource(R.mipmap.poster_details_logo);
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

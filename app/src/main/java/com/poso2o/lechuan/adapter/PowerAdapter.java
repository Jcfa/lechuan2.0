package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.powerdata.LocalPowerCatalogData;
import com.poso2o.lechuan.bean.powerdata.LocalPowerData;
import com.poso2o.lechuan.tool.print.Print;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/23.
 *
 * 权限列表适配器
 */

public class PowerAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<LocalPowerCatalogData> catalogs ;
    private ArrayList<ArrayList<LocalPowerData>> powers;

    public PowerAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapter(ArrayList<LocalPowerCatalogData> catalogs,ArrayList<ArrayList<LocalPowerData>> powers){
        this.catalogs = catalogs;
        this.powers = powers;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        if (catalogs == null)return 0;
        return catalogs.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (powers == null)return 0;
        return powers.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return catalogs.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return powers.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        CatalogViewHolder vh ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_power_catalog,viewGroup,false);
            vh = new CatalogViewHolder();
            vh.layout_power_catalog = (LinearLayout) view.findViewById(R.id.layout_power_catalog);
            vh.power_catalog_name = (TextView) view.findViewById(R.id.power_catalog_name);
            vh.power_catalog_checkbox = (CheckBox) view.findViewById(R.id.power_catalog_checkbox);
            view.setTag(vh);
        }else {
            vh = (CatalogViewHolder) view.getTag();
        }
        LocalPowerCatalogData catalogData = catalogs.get(i);
        vh.power_catalog_name.setText(catalogData.power_catalog_name);
        vh.power_catalog_checkbox.setChecked(catalogData.is_all_on);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        PowerViewHolder vh ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_power_list,viewGroup,false);
            vh = new PowerViewHolder();
            vh.layout_power = (LinearLayout) view.findViewById(R.id.layout_power);
            vh.power_name = (TextView) view.findViewById(R.id.power_name);
            vh.checkbox_power = (CheckBox) view.findViewById(R.id.checkbox_power);
            view.setTag(vh);
        }else {
            vh = (PowerViewHolder) view.getTag();
        }
        LocalPowerData powerData = powers.get(i).get(i1);
        vh.power_name.setText(powerData.power_name);
        vh.checkbox_power.setChecked(powerData.power_on);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class CatalogViewHolder{
        LinearLayout layout_power_catalog;
        TextView power_catalog_name;
        CheckBox power_catalog_checkbox;
    }

    class PowerViewHolder{
        LinearLayout layout_power;
        TextView power_name;
        CheckBox checkbox_power;
    }

    //传入0返回false，1返回true
    private boolean isTrue(String b){
        if (b.equals("0"))return false;
        return true;
    }
}

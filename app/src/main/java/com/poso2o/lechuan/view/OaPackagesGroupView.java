package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.oa.OaService;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/2/2.
 *
 * 公众号设置-购买时长
 */

public class OaPackagesGroupView extends BaseView implements View.OnClickListener{

    private View view;

    //套餐一
    private LinearLayout oa_packages_one;
    //名称
    private TextView oa_packages_name_one;
    //价格
    private TextView oa_packages_price_one;
    //描述
    private TextView oa_packages_des_one;

    //套餐二
    private LinearLayout oa_packages_two;
    //名称
    private TextView oa_packages_name_two;
    //价格
    private TextView oa_packages_price_two;
    //描述
    private TextView oa_packages_des_two;

    //套餐三
    private LinearLayout oa_packages_three;
    //名称
    private TextView oa_packages_name_three;
    //价格
    private TextView oa_packages_price_three;
    //描述
    private TextView oa_packages_des_three;

    //套餐四
    private LinearLayout oa_packages_four;
    //名称
    private TextView oa_packages_name_four;
    //价格
    private TextView oa_packages_price_four;
    //描述
    private TextView oa_packages_des_four;

    //选中的套餐
    private LinearLayout select_packages;
    private TextView select_price;
    private OaService.Item selectedItem;

    //所有的数据
    private ArrayList<OaService.Item> items;

    /**
     * 注意继承后 先走了初始化控件  data
     * @param context
     */
    public OaPackagesGroupView(Context context,OnPackageChangeListener onPackageChangeListener) {
        super(context);
        this.onPackageChangeListener = onPackageChangeListener;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.group_oa_packages,null);
        return view;
    }

    @Override
    public void initView() {
        oa_packages_one = (LinearLayout) view.findViewById(R.id.oa_packages_one);
        oa_packages_name_one = (TextView) view.findViewById(R.id.oa_packages_name_one);
        oa_packages_price_one = (TextView) view.findViewById(R.id.oa_packages_price_one);
        oa_packages_des_one = (TextView) view.findViewById(R.id.oa_packages_des_one);

        oa_packages_two = (LinearLayout) view.findViewById(R.id.oa_packages_two);
        oa_packages_name_two = (TextView) view.findViewById(R.id.oa_packages_name_two);
        oa_packages_price_two = (TextView) view.findViewById(R.id.oa_packages_price_two);
        oa_packages_des_two = (TextView) view.findViewById(R.id.oa_packages_des_two);

        oa_packages_three = (LinearLayout) view.findViewById(R.id.oa_packages_three);
        oa_packages_name_three = (TextView) view.findViewById(R.id.oa_packages_name_three);
        oa_packages_price_three = (TextView) view.findViewById(R.id.oa_packages_price_three);
        oa_packages_des_three = (TextView) view.findViewById(R.id.oa_packages_des_three);

        oa_packages_four = (LinearLayout) view.findViewById(R.id.oa_packages_four);
        oa_packages_name_four = (TextView) view.findViewById(R.id.oa_packages_name_four);
        oa_packages_price_four = (TextView) view.findViewById(R.id.oa_packages_price_four);
        oa_packages_des_four = (TextView) view.findViewById(R.id.oa_packages_des_four);
    }

    @Override
    public void initData() {
        oa_packages_one.setSelected(true);
        oa_packages_price_one.setSelected(true);
        select_packages = oa_packages_one;
        select_price = oa_packages_price_one;

        oa_packages_one.setOnClickListener(this);
        oa_packages_two.setOnClickListener(this);
        oa_packages_three.setOnClickListener(this);
        oa_packages_four.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.oa_packages_one:
                changePackage(oa_packages_one,oa_packages_price_one);
                selectedItem = items.get(0);
                break;
            case R.id.oa_packages_two:
                changePackage(oa_packages_two,oa_packages_price_two);
                selectedItem = items.get(1);
                break;
            case R.id.oa_packages_three:
                changePackage(oa_packages_three,oa_packages_price_three);
                selectedItem = items.get(2);
                break;
            case R.id.oa_packages_four:
                changePackage(oa_packages_four,oa_packages_price_four);
                selectedItem = items.get(3);
                break;
        }
        onPackageChangeListener.onPackageChange(selectedItem);
    }

    //设置数据
    public void setDatas(ArrayList<OaService.Item> items){
        this.items = items;
        for (int i = 0;i<items.size();i++){
            OaService.Item item = items.get(i);
            if (i == 0){
                selectedItem = item;
                changePackage(oa_packages_one,oa_packages_price_one);

                oa_packages_one.setVisibility(View.VISIBLE);
                oa_packages_name_one.setText(item.unit);
                oa_packages_price_one.setText("¥" + item.amount);
                oa_packages_des_one.setText(item.remark);
            }else if (i == 1){
                oa_packages_two.setVisibility(View.VISIBLE);
                oa_packages_name_two.setText(item.unit);
                oa_packages_price_two.setText("¥" + item.amount);
                oa_packages_des_two.setText(item.remark);
            }else if (i == 2){
                oa_packages_three.setVisibility(View.VISIBLE);
                oa_packages_name_three.setText(item.unit);
                oa_packages_price_three.setText("¥" + item.amount);
                oa_packages_des_three.setText(item.remark);
            }else if (i == 3){
                oa_packages_four.setVisibility(View.VISIBLE);
                oa_packages_name_four.setText(item.unit);
                oa_packages_price_four.setText("¥" + item.amount);
                oa_packages_des_four.setText(item.remark);
            }
        }
    }

    //点击切换套餐
    private void changePackage(LinearLayout layout,TextView price){
        select_packages.setSelected(false);
        select_price.setSelected(false);

        select_packages = layout;
        select_price = price;

        select_packages.setSelected(true);
        select_price.setSelected(true);
    }

    public OaService.Item getSelectItem(){
        return selectedItem;
    }

    //切换套餐回调
    private OnPackageChangeListener onPackageChangeListener;
    public interface OnPackageChangeListener{
        void onPackageChange(OaService.Item item);
    }
}

package com.poso2o.lechuan.popubwindow;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/12/1.
 *
 * 首页店铺切换弹窗
 */

public class ShopListPop extends PopupWindow {

    private Context context;
    private View view;

    //实体店
    private TextView r_shop;
    //微店
    private TextView w_shop;

    //店铺类型
    private int mType;

    public ShopListPop(Context context,int type){
        this.context = context;
        mType = type;

        initView();
        initData();
        initListener();
    }

    private void initView(){
        view = View.inflate(context, R.layout.pop_shop_list,null);

        setContentView(view);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        setWidth(width*300/720);
        setHeight(height*240/1280);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.selectmenu_bg_downward));

        r_shop = (TextView) view.findViewById(R.id.r_shop);
        w_shop = (TextView) view.findViewById(R.id.w_shop);
    }

    private void initData(){
        if (mType == 1){
            r_shop.setTextColor(0xFFFF6537);
        }else {
            w_shop.setTextColor(0xFFFF6537);
        }
    }

    private void initListener(){
        r_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShopClickListener != null)onShopClickListener.onShopClick(1);
                dismiss();
            }
        });

        w_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onShopClickListener != null)onShopClickListener.onShopClick(2);
                dismiss();
            }
        });
    }

    private OnShopClickListener onShopClickListener;
    public interface OnShopClickListener{
        void onShopClick(int type);
    }
    public void setOnShopClickListener(OnShopClickListener onShopClickListener){
        this.onShopClickListener = onShopClickListener;
    }
}

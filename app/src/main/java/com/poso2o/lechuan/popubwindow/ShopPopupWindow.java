package com.poso2o.lechuan.popubwindow;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.distributor.DistShop;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.ScreenInfo;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.RoundAngleImage;

import java.util.ArrayList;

/**
 *
 * Created by Jaydon on 2017/8/15.
 */
public class ShopPopupWindow extends PopupWindow {

    private View view;

    private Context context;

    private DistShop selectDistShop;

    private ArrayList<DistShop> shopDatas;

    private OnItemClickListener onItemClickListener;

    public ShopPopupWindow(Context context, ArrayList<DistShop> shopDatas, DistShop selectDistShop) {
        this.context = context;
        this.shopDatas = shopDatas;
        this.selectDistShop = selectDistShop;
        initView();
        initData();
        initListener();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initView() {
        view = View.inflate(context, R.layout.popup_shop_list, null);
        setContentView(view);
        setWidth(LayoutParams.MATCH_PARENT);
        if (shopDatas.size() >= 4) {
            setHeight(ScreenInfo.dpCpx(264));
        } else {
            setHeight(ScreenInfo.dpCpx(56 * shopDatas.size() + 40));
        }
        setFocusable(true);
        setOutsideTouchable(true);
        update();
        setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.selectmenu_bg_downward));
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        initData();
    }

    private void initData() {
        // 初始化布局
        LinearLayout l = (LinearLayout) view.findViewById(R.id.shop_list_group);
        l.removeAllViews();

        if (shopDatas == null) {
            Toast.show(context, "未找到店铺列表数据");
            return;
        }

        for (int i = 0; i < shopDatas.size(); i++) {
            final DistShop shopData = shopDatas.get(i);

            View item = View.inflate(context, R.layout.popup_shop_list_item, null);
            RoundAngleImage shop_icon = (RoundAngleImage) item.findViewById(R.id.shop_icon);
            Glide.with(context).load(shopData.shop_logo).into(shop_icon);
            TextView shop_name = (TextView) item.findViewById(R.id.shop_name);

            if (selectDistShop.shop_id == shopData.shop_id) {
                shop_name.setTextColor(context.getResources().getColor(R.color.colorRed));
            }
            shop_name.setText(shopData.shop_name);
            item.setTag(i + "");
            item.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(shopData);
                    }
                    // 设置当前目录
                    selectDistShop = shopData;
                    dismiss();
                }
            });

            l.addView(item);
        }
    }

    private void initListener() {
        setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
        view.findViewById(R.id.shop_list_back_up).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(DistShop catalog);
    }
}

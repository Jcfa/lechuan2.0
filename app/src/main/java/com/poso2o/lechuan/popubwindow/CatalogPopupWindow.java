package com.poso2o.lechuan.popubwindow;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.ScreenInfo;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by Jaydon on 2017/8/15.
 */
public class CatalogPopupWindow extends PopupWindow {

    private View view;

    private Context context;

    private Catalog selectCatalog;

    private ArrayList<Catalog> catalogs;

    private OnItemClickListener onItemClickListener;

    /**
     * 店铺类型：实体店，微店，实体店用旧版数据，微店用新版数据
     */
    private boolean isVdian = true;

    public CatalogPopupWindow(Context context, ArrayList<Catalog> catalogs, Catalog selectCatalog, boolean isVdian) {
        this.context = context;
        this.catalogs = catalogs;
        this.selectCatalog = selectCatalog;
        this.isVdian = isVdian;
        initView();
        initData();
        initListener();
    }

    public CatalogPopupWindow(Context context, ArrayList<Catalog> catalogs, Catalog selectCatalog) {
        this(context, catalogs, selectCatalog, true);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initView() {
        view = View.inflate(context, R.layout.popup_home_all_goods, null);
        setContentView(view);
        setWidth(LayoutParams.WRAP_CONTENT);
        if (catalogs.size() > 11) {
            setHeight((int) (ScreenInfo.HEIGHT * 0.7));
        } else {
            setHeight(LayoutParams.WRAP_CONTENT);
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
        LinearLayout l = (LinearLayout) view.findViewById(R.id.home_all_goods_group);
        l.removeAllViews();

        if (catalogs == null) {
            View item = View.inflate(context, R.layout.popup_home_all_goods_item, null);
            TextView item_tv = (TextView) item.findViewById(R.id.home_good_tv);
            item_tv.setText("没有分类");
            item_tv.setTextColor(0xffff0000);
            l.addView(item);
            item.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    dismiss();
                    Toast.show(context, "没有分类商品");
                }
            });
            return;
        }

        for (int i = 0; i < catalogs.size(); i++) {
            final Catalog catalog = catalogs.get(i);

            View item = View.inflate(context, R.layout.popup_home_all_goods_item, null);
            TextView item_tv = (TextView) item.findViewById(R.id.home_good_tv);

            if (isVdian) {
                if (TextUtils.equals(selectCatalog.catalog_id, catalog.catalog_id)) {
                    item_tv.setTextColor(context.getResources().getColor(R.color.textGreen));
                }
                item_tv.setText(catalog.getNameAndNum());
            } else {
                if (TextUtils.equals(selectCatalog.fid, catalog.fid)) {
                    item_tv.setTextColor(context.getResources().getColor(R.color.textGreen));
                }
                item_tv.setText(catalog.getDirNameAndNum());
            }

            item.setTag(i + "");
            item.setOnClickListener(new NoDoubleClickListener() {

                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(catalog);
                    }
                    // 设置当前目录
                    selectCatalog = catalog;
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
    }

    public interface OnItemClickListener {
        void onItemClick(Catalog catalog);
    }
}

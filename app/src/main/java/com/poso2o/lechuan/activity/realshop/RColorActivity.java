package com.poso2o.lechuan.activity.realshop;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.poso2o.lechuan.activity.goods.ColorActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.color.ColorBean;
import com.poso2o.lechuan.bean.color.GoodsColor;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonEditDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealColorManager;
import com.poso2o.lechuan.manager.vdian.VdianColorManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/20.
 */

public class RColorActivity extends BaseListActivity {

    private BaseListAdapter adapter;

    @Override
    public void initData() {
        setThemeText("颜色");
        ArrayList<GoodsColor> data = (ArrayList<GoodsColor>) getIntent().getSerializableExtra(Constant.DATA);

        adapter = new BaseListAdapter<GoodsColor>(this, true) {

            @Override
            public void initItemView(BaseListAdapter.ListViewHolder holder, GoodsColor item, int position) {
                holder.list_item_text.setText(TextUtil.trimToEmpty(item.colorid));
            }
        };

        adapter.setSelects(data);
        setAdapter(adapter);

        showLoading();
        loadData();
    }

    @Override
    public void initListener() {
        setSwipeRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadData();
            }
        });

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<GoodsColor>() {

            @Override
            public void onItemClick(final GoodsColor item) {
                /*if (isVdian) {
                    showEditDialog(item.goods_colour_name, new CommonEditDialog.OnEditListener() {

                        @Override
                        public void onConfirm(String name) {
                            GoodsColor color = new GoodsColor();
                            color.goods_colour_id = item.goods_colour_id;
                            color.goods_colour_name = name;
                            update(color);
                        }
                    });
                }*/
            }
        });

        adapter.setOnItemDeleteListener(new BaseListAdapter.OnItemDeleteListener<GoodsColor>() {

            @Override
            public void onItemDelete(GoodsColor item) {
                delete(item);
            }
        });

        setOnConfirmClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constant.DATA, adapter.getSelects());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onAddClick() {
        showAddDialog(new CommonEditDialog.OnEditListener() {

            @Override
            public void onConfirm(String name) {
                add(name);
            }
        });
    }

    private void loadData() {
        RealColorManager.getInstance().loadList(this, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                ColorBean colorBean = (ColorBean) object;
                adapter.notifyDataSetChanged(colorBean.list);
                setRefreshing(false);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RColorActivity.this, msg);
                setRefreshing(false);
                dismissLoading();
            }
        });
    }

    private void add(String name) {
        showLoading();
        final GoodsColor color = new GoodsColor();
        color.goods_colour_name = name;
        color.colorid = name;
        RealColorManager.getInstance().add(this, color, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                adapter.addItem(color);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RColorActivity.this, msg);
                dismissLoading();
            }
        });
    }

    private void update(GoodsColor colour) {
        showLoading();
        VdianColorManager.getInstance().edit(this, colour, new IRequestCallBack<GoodsColor>() {

            @Override
            public void onResult(int tag, GoodsColor result) {
                adapter.updateItem(result);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RColorActivity.this, msg);
                dismissLoading();
            }
        });
    }

    private void delete(final GoodsColor color) {
        showLoading();
        RealColorManager.getInstance().del(this, color.colorid, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                adapter.removeItem(color);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RColorActivity.this, msg);
                dismissLoading();
            }
        });
    }

    @Override
    protected void onEditClick() {
        adapter.setEdit(true);
    }

    @Override
    protected void onFinishClick() {
        adapter.setEdit(false);
    }
}

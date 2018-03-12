package com.poso2o.lechuan.activity.realshop;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.poso2o.lechuan.activity.goods.SizeActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.size.GoodsSize;
import com.poso2o.lechuan.bean.size.SizeBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonEditDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealSizeManager;
import com.poso2o.lechuan.manager.vdian.VdianSizeManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/12/20.
 */

public class RSizeActivity extends BaseListActivity {

    private BaseListAdapter adapter;

    @Override
    public void initData() {
        setThemeText("尺码");

        ArrayList<GoodsSize> data = (ArrayList<GoodsSize>) getIntent().getSerializableExtra(Constant.DATA);

        adapter = new BaseListAdapter<GoodsSize>(this, true) {

            @Override
            public void initItemView(BaseListAdapter.ListViewHolder holder, GoodsSize item, int position) {
                holder.list_item_text.setText(TextUtil.trimToEmpty(item.sizeid));
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

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<GoodsSize>() {

            @Override
            public void onItemClick(final GoodsSize item) {
                /*if (isVdian) {
                    showEditDialog(item.goods_size_name, new CommonEditDialog.OnEditListener() {

                        @Override
                        public void onConfirm(String name) {
                            GoodsSize size = new GoodsSize();
                            size.goods_size_id = item.goods_size_id;
                            size.goods_size_name = name;
                            update(size);
                        }
                    });
                }*/
            }
        });

        adapter.setOnItemDeleteListener(new BaseListAdapter.OnItemDeleteListener<GoodsSize>() {

            @Override
            public void onItemDelete(GoodsSize item) {
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
        RealSizeManager.getInstance().loadList(this, new IRequestCallBack<SizeBean>() {

            @Override
            public void onResult(int tag, SizeBean sizeBean) {
                adapter.notifyDataSetChanged(sizeBean.list);
                setRefreshing(false);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RSizeActivity.this, msg);
                dismissLoading();
            }
        });
    }

    private void add(String name) {
        showLoading("正在新增尺码");
        GoodsSize size = new GoodsSize();
        size.goods_size_name = name;
        size.sizeid = name;
        RealSizeManager.getInstance().add(this, size, new IRequestCallBack<GoodsSize>() {

            @Override
            public void onResult(int tag, GoodsSize size) {
                adapter.addItem(size);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RSizeActivity.this, msg);
                dismissLoading();
            }
        });
    }

    private void update(GoodsSize size) {
        showLoading();
        VdianSizeManager.getInstance().edit(this, size, new IRequestCallBack<GoodsSize>() {

            @Override
            public void onResult(int tag, GoodsSize result) {
                adapter.updateItem(result);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }

    private void delete(final GoodsSize size) {
        RealSizeManager.getInstance().del(this, size.sizeid, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                adapter.removeItem(size);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RSizeActivity.this, msg);
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

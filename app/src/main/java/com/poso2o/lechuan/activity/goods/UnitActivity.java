package com.poso2o.lechuan.activity.goods;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;

import com.poso2o.lechuan.activity.realshop.BaseListActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseListAdapter;
import com.poso2o.lechuan.bean.unit.GoodsUnit;
import com.poso2o.lechuan.bean.unit.UnitBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonEditDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.VdianUnitManager;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;


/**
 * 单位选择界面
 * Created by Jaydon on 2017/8/21.
 */
public class UnitActivity extends BaseListActivity {

    private BaseListAdapter adapter;

    private String selectUnit;

    @Override
    public void initData() {
        setThemeText("单位");

        selectUnit = getIntent().getStringExtra(Constant.DATA);

        adapter = new BaseListAdapter<GoodsUnit>(this, false) {

            @Override
            public void initItemView(BaseListAdapter.ListViewHolder holder, GoodsUnit item, int position) {
                holder.list_item_text.setText(TextUtil.trimToEmpty(item.unit_name));
                if (TextUtil.equals(item.unit_name, selectUnit)) {
                    holder.list_item_text.setTextColor(0xffff6537);
                } else {
                    holder.list_item_text.setTextColor(0xff000000);
                }
            }
        };
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

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<GoodsUnit>() {

            @Override
            public void onItemClick(final GoodsUnit item) {
                if (adapter.isEdit()) {
                    showEditDialog(item.unit_name, new CommonEditDialog.OnEditListener() {

                        @Override
                        public void onConfirm(String name) {
                            update(item, name);
                        }
                    });
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.DATA, item);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        adapter.setOnItemDeleteListener(new BaseListAdapter.OnItemDeleteListener<GoodsUnit>(){

            @Override
            public void onItemDelete(GoodsUnit item) {
                delete(item);
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
        VdianUnitManager.getInstance().loadList(this, new IRequestCallBack<UnitBean>() {

            @Override
            public void onResult(int tag, UnitBean result) {
                adapter.notifyDataSetChanged(result.list);
                setRefreshing(false);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                setRefreshing(false);
                dismissLoading();
            }
        });
    }

    private void add(String name) {
        showLoading();
        GoodsUnit unit = new GoodsUnit();
        unit.unit_name = name;
        VdianUnitManager.getInstance().add(this, unit, new IRequestCallBack<GoodsUnit>() {

            @Override
            public void onResult(int tag, GoodsUnit result) {
                adapter.addItem(result);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }

    private void update(final GoodsUnit item, String name) {
        showLoading();
        GoodsUnit unit = new GoodsUnit();
        unit.unit_id = item.unit_id;
        unit.unit_name = name;
        VdianUnitManager.getInstance().edit(this, unit, new IRequestCallBack<GoodsUnit>() {

            @Override
            public void onResult(int tag, GoodsUnit unit) {
                if (TextUtil.equals(selectUnit, item.unit_name)) {
                    selectUnit = unit.unit_name;
                    Intent intent = new Intent();
                    intent.putExtra(Constant.DATA, unit);
                    setResult(RESULT_OK, intent);
                }
                adapter.updateItem(unit);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }

    private void delete(final GoodsUnit unit) {
        showLoading();
        VdianUnitManager.getInstance().del(this, unit.unit_id, new IRequestCallBack<String>() {

            @Override
            public void onResult(int tag, String result) {
                adapter.removeItem(unit);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
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

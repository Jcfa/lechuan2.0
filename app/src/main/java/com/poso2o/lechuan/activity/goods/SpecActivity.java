package com.poso2o.lechuan.activity.goods;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.poso2o.lechuan.activity.realshop.BaseListActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseListAdapter;
import com.poso2o.lechuan.bean.spec.Spec;
import com.poso2o.lechuan.bean.spec.SpecBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonEditDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.VdianSpecManager;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;


/**
 * 规格选择界面
 * Created by Jaydon on 2017/8/21.
 */
public class SpecActivity extends BaseListActivity {

    private BaseListAdapter adapter;

    @Override
    public void initData() {
        setThemeText("规格");

        ArrayList<Spec> data = (ArrayList<Spec>) getIntent().getSerializableExtra(Constant.DATA);

        adapter = new BaseListAdapter<Spec>(this, true) {

            @Override
            public void initItemView(BaseListAdapter.ListViewHolder holder, Spec item, int position) {
                holder.list_item_text.setText(TextUtil.trimToEmpty(item.spec_name));
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

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Spec>() {

            @Override
            public void onItemClick(final Spec item) {
                showEditDialog(item.spec_name, new CommonEditDialog.OnEditListener() {

                    @Override
                    public void onConfirm(String name) {
                        Spec spec = new Spec();
                        spec.spec_id = item.spec_id;
                        spec.spec_name = name;
                        update(spec);
                    }
                });
            }
        });

        adapter.setOnItemDeleteListener(new BaseListAdapter.OnItemDeleteListener<Spec>(){

            @Override
            public void onItemDelete(Spec item) {
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
        VdianSpecManager.getInstance().loadList(this, new IRequestCallBack<SpecBean>() {

            @Override
            public void onResult(int tag, SpecBean result) {
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
        Spec spec = new Spec();
        spec.spec_name = name;
        VdianSpecManager.getInstance().add(this, spec, new IRequestCallBack<Spec>() {

            @Override
            public void onResult(int tag, Spec result) {
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

    private void update(Spec spec) {
        showLoading();
        VdianSpecManager.getInstance().edit(this, spec, new IRequestCallBack<Spec>() {

            @Override
            public void onResult(int tag, Spec result) {
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

    private void delete(final Spec spec) {
        showLoading();
        VdianSpecManager.getInstance().del(this, spec.spec_id, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                adapter.removeItem(spec);
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

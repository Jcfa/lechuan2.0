package com.poso2o.lechuan.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.SupplierAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.GoodsSupplier;
import com.poso2o.lechuan.bean.supplier.Supplier;
import com.poso2o.lechuan.bean.supplier.SupplierBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonDeleteDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.VdianSupplierManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 供应商列表界面
 * Created by lenovo on 2016/12/14.
 */
public class SupplierActivity extends BaseActivity {

    private Context context;

    private final int REQUEST_ADD = 1;

    private final int REQUEST_UPDATE = 2;

    /**
     * 返回键
     */
    private ImageView supplier_back;

    /**
     * 确定
     */
    private View supplier_confirm;

    /**
     * 列表
     */
    private RecyclerView supplier_recycle;
    private SupplierAdapter supplierAdapter;

    /**
     * 刷新
     */
    private SwipeRefreshLayout supplier_swipe;

    /**
     * 删除对话框
     */
    private CommonDeleteDialog supplierDeleteDialog;

    /**
     * 增加供应商
     */
    private View supplier_add;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_supplier;
    }

    @Override
    public void initView() {
        context = this;

        // 退出
        supplier_back = (ImageView) findViewById(R.id.supplier_back);
        // 确定
        supplier_confirm = findViewById(R.id.supplier_confirm);

        // 分组列表展示
        supplier_recycle = (RecyclerView) findViewById(R.id.supplier_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        supplier_recycle.setLayoutManager(layoutManager);

        // 刷新
        supplier_swipe = (SwipeRefreshLayout) findViewById(R.id.supplier_swipe);
        supplier_swipe.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

        // 添加供应商
        supplier_add = findViewById(R.id.supplier_add);

    }

    @Override
    public void initData() {
        supplierAdapter = new SupplierAdapter(context, null);
        supplier_recycle.setAdapter(supplierAdapter);

        // 获取显示数据
        Intent intent = getIntent();
        ArrayList<GoodsSupplier> selects = (ArrayList<GoodsSupplier>) intent.getSerializableExtra(Constant.DATA);
        if (selects != null) {
            supplierAdapter.setSelects(selects);
        }

        supplier_swipe.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        VdianSupplierManager.getInstance().loadList(this, new IRequestCallBack<SupplierBean>() {

            @Override
            public void onResult(int tag, SupplierBean result) {
                supplierAdapter.notifyDataSetChanged(result.list);
                supplier_swipe.setRefreshing(false);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                supplier_swipe.setRefreshing(false);
                dismissLoading();
            }
        });
    }

    @Override
    public void initListener() {
        supplierAdapter.setOnEditListener(new SupplierAdapter.OnEditListener() {

            @Override
            public void onEdit(Supplier item) {
                Intent intent = new Intent();
                intent.setClass(context, EditSupplierActivity.class);
                intent.putExtra(Constant.TYPE, Constant.UPDATE);
                intent.putExtra(Constant.DATA, item);
                startActivityForResult(intent, REQUEST_UPDATE);
            }
        });

        supplierAdapter.setOnDeleteListener(new SupplierAdapter.OnDeleteListener() {

            @Override
            public void onDelete(Supplier item) {
                showDeleteDialog(item);
            }
        });

        // 添加供应商
        supplier_add.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, EditSupplierActivity.class);
                intent.putExtra(Constant.TYPE, Constant.ADD);
                startActivityForResult(intent, REQUEST_ADD);
            }
        });

        // 刷新
        supplier_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadData();
            }
        });

        // 退出
        supplier_back.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });

        // 确定
        supplier_confirm.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                Intent data = new Intent();
                data.putExtra(Constant.DATA, supplierAdapter.getSelects());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void showDeleteDialog(final Supplier supplier) {
        // 删除供应商分组
        supplierDeleteDialog = new CommonDeleteDialog(context, supplier.supplier_shortname, getString(R.string.supplier));
        supplierDeleteDialog.show(new CommonDeleteDialog.OnDeleteListener() {

            @Override
            public void onConfirm() {
                supplierDeleteDialog.dismiss();
                delete(supplier);
            }
        });
    }

    private void delete(final Supplier supplier) {
        showLoading();
        VdianSupplierManager.getInstance().del(this, supplier.supplier_id, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                supplierAdapter.removeItem(supplier);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            supplier_swipe.setRefreshing(true);
            loadData();
        }
    }
}

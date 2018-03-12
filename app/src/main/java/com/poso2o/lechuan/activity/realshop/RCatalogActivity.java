package com.poso2o.lechuan.activity.realshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.CatalogActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.CatalogAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.bean.goodsdata.CatalogBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CatalogDeleteDialog;
import com.poso2o.lechuan.dialog.CommonDialog;
import com.poso2o.lechuan.dialog.EditCatalogDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.rshopmanager.RealCatalogManager;
import com.poso2o.lechuan.manager.vdian.VdianCatalogManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by mr zhang on 2017/12/20.
 */

public class RCatalogActivity extends BaseActivity {

    private Context context;

    /**
     * 退出
     */
    private ImageView catalog_back;

    /**
     * 分组列表展示
     */
    private RecyclerView groupsRecycle;
    private CatalogAdapter catalogAdapter;

    /**
     * 刷新
     */
    private SwipeRefreshLayout catalog_swipe;

    /**
     * 编辑目录
     */
    private LinearLayout catalog_foot_main_edit;
    private LinearLayout catalog_edit_complete;
    private LinearLayout catalog_foot_main;

    /**
     * 折扣率
     */
    private View catalog_discount_tag;

    /**
     * 删除目录分组
     */
    private CatalogDeleteDialog catalogDeleteDialog;

    /**
     * 增加目录
     */
    private LinearLayout catalog_foot_main_add;

    /**
     * 编辑目录
     */
    private EditCatalogDialog editCatalogDialog;

    private boolean isSort;

    /**
     * 备份数据
     */
//    private ArrayList<Catalog> backData;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_catalog;
    }

    @Override
    public void initView() {
        context = this;
        // 退出
        catalog_back = findView(R.id.catalog_back);

        // 分组列表展示
        groupsRecycle = findView(R.id.catalog_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        groupsRecycle.setLayoutManager(layoutManager);

        // 刷新
        catalog_swipe = findView(R.id.catalog_swipe);
        catalog_swipe.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

        // 编辑目录
        catalog_foot_main_edit = findView(R.id.catalog_foot_main_edit);
        catalog_edit_complete = findView(R.id.catalog_edit_complete);
        catalog_foot_main = findView(R.id.catalog_foot_main);

        catalog_discount_tag = findView(R.id.catalog_discount_tag);

        // 添加目录
        catalog_foot_main_add = findView(R.id.catalog_foot_main_add);

    }

    @Override
    public void initData() {
        catalog_discount_tag.setVisibility(View.INVISIBLE);

        catalogAdapter = new CatalogAdapter(context, false);
        groupsRecycle.setAdapter(catalogAdapter);

        // 获取显示数据
        Intent intent = getIntent();
        String selectId = intent.getStringExtra(Constant.CATALOG);
        if (selectId != null) {
            catalogAdapter.setSelectId(selectId);
        }

        itemTouchHelper.attachToRecyclerView(groupsRecycle);

        showLoading("正在加载目录...");
        loadData();
    }

    @Override
    public void initListener() {
        // 添加分组目录
        catalog_foot_main_add.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                showAddDialog();
            }
        });

        // 刷新
        catalog_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadData();
            }
        });

        // 列表项点击
        catalogAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Catalog>() {

            @Override
            public void onItemClick(Catalog item) {
                if (catalogAdapter.isEdit()) {
                    showUpdateDialog(item);
                } else {
                    select(item);
                }
            }
        });

        // 完成/分组排序
        catalog_edit_complete.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                visibleEditView(false);
                if (isSort) {
                    catalogSort();
                    isSort = false;
                }
            }
        });

        // 退出
        catalog_back.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                setResult(RESULT_CANCELED);
                RCatalogActivity.super.finish();
            }
        });

        // 编辑目录
        catalog_foot_main_edit.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                visibleEditView(true);
            }
        });
    }

    private void showDeleteDialog(int position) {
        Catalog catalog = catalogAdapter.getItem(position);
        int num;
        num = NumberUtils.toInt(catalog.productNum);
        if (num > 0) {
            CommonDialog dialog = new CommonDialog(activity, R.string.dialog_not_delete_catalog);
            dialog.show(new CommonDialog.OnConfirmListener() {

                @Override
                public void onConfirm() {
                    catalogAdapter.notifyDataSetChanged();
                }
            });
            dialog.setOnCancelListener(new CommonDialog.OnCancelListener() {

                @Override
                public void onCancel() {
                    catalogAdapter.notifyDataSetChanged();
                }
            });
            return;
        }
        // 删除目录分组
        catalogDeleteDialog = new CatalogDeleteDialog(context, false, new CatalogDeleteDialog.OnGroupDelete() {

            @Override
            public void onCancel() {
                // 取消
                catalogDeleteDialog.dismiss();
                catalogAdapter.notifyDataSetChanged();
            }

            @Override
            public void onConfirm(Catalog catalog) {
                catalogDeleteDialog.dismiss();
                delete(catalog);
            }
        });
        catalogDeleteDialog.show();
        catalogDeleteDialog.setDeleteName(catalog);
    }

    private void loadData() {
        RealCatalogManager.getInstance().loadList(this, new IRequestCallBack<CatalogBean>() {

            @Override
            public void onResult(int tag, CatalogBean catalogBean) {
                catalogAdapter.notifyDataSetChanged(catalogBean.list);
                catalog_swipe.setRefreshing(false);
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                catalog_swipe.setRefreshing(false);
                dismissLoading();
            }
        });
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(Constant.CATALOG, catalogAdapter.getSelectItem());
        setResult(RESULT_OK, data);
        super.finish();
    }

    private void showAddDialog() {
        editCatalogDialog = new EditCatalogDialog(context, false, new EditCatalogDialog.OnGroupEditListener() {

            @Override
            public void onCancel() {
                editCatalogDialog.dismiss();
            }

            @Override
            public void onConfirm(Catalog catalog) {
                if (catalog != null) {
                    add(catalog);
                }
                editCatalogDialog.dismiss();
            }
        });
        editCatalogDialog.show();
    }

    private void add(Catalog catalog) {
        showLoading("正在提交数据...");
        RealCatalogManager.getInstance().add(this, catalog, new IRequestCallBack<Catalog>() {

            @Override
            public void onResult(int tag, Catalog catalog) {
                catalogAdapter.addItem(catalog);
                EventBus.getDefault().post(new EventBean(EventBean.CODE_CATALOG_ADD));
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RCatalogActivity.this, msg);
                dismissLoading();
            }
        });
    }

    private void showUpdateDialog(Catalog item) {
        editCatalogDialog = new EditCatalogDialog(context, false, new EditCatalogDialog.OnGroupEditListener() {

            @Override
            public void onCancel() {
                editCatalogDialog.dismiss();
            }

            @Override
            public void onConfirm(Catalog catalog) {
                if (catalog != null) {
                    update(catalog);
                }

                editCatalogDialog.dismiss();
            }
        });
        editCatalogDialog.show(item);
    }

    private void update(final Catalog catalog) {
        showLoading();
        RealCatalogManager.getInstance().edit(this, catalog, new IRequestCallBack<Catalog>() {

            @Override
            public void onResult(int tag, Catalog catalog) {
                catalogAdapter.updateItemData(catalog);
                EventBus.getDefault().post(new EventBean(EventBean.CODE_CATALOG_UPDATE));
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }

    private void delete(final Catalog catalog) {
        showLoading("正在删除目录...");
        RealCatalogManager.getInstance().del(this, catalog.fid, new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object object) {
                catalogAdapter.deleteItemData(catalog);
                EventBus.getDefault().post(new EventBean(EventBean.CODE_CATALOG_UPDATE));
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(RCatalogActivity.this, msg);
                dismissLoading();
            }
        });
    }

    private void select(Catalog item) {
        Intent data = new Intent();
        data.putExtra(Constant.CATALOG, item);
        setResult(RESULT_OK, data);
        super.finish();
    }

    private void visibleEditView(boolean b) {
        catalog_foot_main.setVisibility(b ? GONE : VISIBLE);
        catalog_edit_complete.setVisibility(b ? VISIBLE : GONE);
        catalogAdapter.setEdit(b);
    }

    @Override
    public void onBackPressed() {
        if (catalog_foot_main.getVisibility() == GONE) {
            visibleEditView(false);
        } else {
            setResult(RESULT_CANCELED);
            super.finish();
        }
    }

    /**
     * 重新排序分组
     */
    private void moveGroups(int fromPosition, int toPosition) {
        if (fromPosition != toPosition) {
            // 改变数据排序
            Collections.swap(catalogAdapter.getItems(), fromPosition, toPosition);
            // 改变显示排序
            catalogAdapter.notifyItemMoved(fromPosition, toPosition);

            isSort = true;
        }
    }

    /**
     * 目录排序
     */
    public void catalogSort() {
        showLoading();
        RealCatalogManager.getInstance().sort(this, catalogAdapter.getItems(), new IRequestCallBack<String>(){

            @Override
            public void onResult(int tag, String object) {
                catalogAdapter.notifyDataSetChanged();
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }

    /**
     * 拖拽
     */
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (catalogAdapter.isEdit()) {
                // 判断recyclerView传入的LayoutManager是不是GridLayoutManager
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    // Grid的拖拽方向，上、下、左、右
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    // 设置为0，表示不支持滑动
                    final int swipeFlags = 0;
                    // 创建拖拽或者滑动标志的快速方式
                    return makeMovementFlags(dragFlags, swipeFlags);
                } else {
                    // List类型的拖拽方向，上或者下
                    final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    // List类型支持滑动，左或者右
                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                    // 创建拖拽或者滑动标志的快速方式
                    return makeMovementFlags(dragFlags, swipeFlags);
                }
            } else {
                // 不支持拖拽和滑动
                return makeMovementFlags(0, 0);
            }
        }

        /**
         * 当拖拽开始的时候调用
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            //拖拽的时候改变一下选中Item的颜色
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof CatalogAdapter.GroupsViewHolder) {
                    //让ViewHolder知道Item开始选中
                    CatalogAdapter.GroupsViewHolder itemViewHolder = (CatalogAdapter.GroupsViewHolder) viewHolder;
                    //回调ItemTouchHelperVIewHolder的方法
                    itemViewHolder.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 告诉ItemTouchHelper是否需要RecyclerView支持长按拖拽,返回true是支持，false是不支持
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        /**
         * 当拖拽结束的时候调用
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1);
            if (viewHolder instanceof CatalogAdapter.GroupsViewHolder) {
                CatalogAdapter.GroupsViewHolder itemViewHolder = (CatalogAdapter.GroupsViewHolder) viewHolder;
                //Item移动完成之后的回调
                itemViewHolder.onItemClear();
            }
        }

        /**
         * 当ItemTouchHelper要拖动的Item从原来位置拖动到新的位置的时候调用
         * 当我们拖拽的时候调用这个方法
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //如果两个item不是一个类型的，我们让他不可以拖拽
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }

            //得到拖动ViewHolder的position
            int fromPosition = viewHolder.getAdapterPosition();
            //得到目标ViewHolder的position
            int toPosition = target.getAdapterPosition();

            moveGroups(fromPosition, toPosition);

            return true;
        }

        /**
         * 当ViewHolder滑动的时候调用
         * 当滑动Item的时候调用此方法
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            showDeleteDialog(viewHolder.getAdapterPosition());
        }

        /**
         * 由 ItemTouchHelper 在 RecyclerView 的 onDraw方法中回调调用
         */
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    });
}

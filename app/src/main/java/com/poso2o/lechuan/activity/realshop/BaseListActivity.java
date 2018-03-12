package com.poso2o.lechuan.activity.realshop;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseListAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.CommonEditDialog;
import com.poso2o.lechuan.util.TextUtil;

/**
 * 列表
 * Created by Jaydon on 2017/8/21.
 */
public abstract class BaseListActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 返回键
     */
    private View base_list_back;

    /**
     * 标题
     */
    private TextView base_list_title;

    /**
     * 确定
     */
    private FrameLayout base_list_confirm;

    /**
     * 编辑
     */
    private View base_list_edit_group;
    private View base_list_edit;
    private TextView base_list_edit_text;

    /**
     * 新增
     */
    private View base_list_add;
    private TextView base_list_add_text;

    /**
     * 完成
     */
    private View base_list_finish;

    /**
     * 刷新
     */
    private SwipeRefreshLayout base_list_swipe;

    /**
     * 列表
     */
    private RecyclerView base_list_recycler;

    private BaseListAdapter adapter;

    private CommonEditDialog commonEditDialog;

    /**
     * 主题文本
     */
    private String themeText;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_base_list;
    }

    @Override
    public void initView() {
        base_list_back = findViewById(R.id.base_list_back);
        base_list_title = (TextView) findViewById(R.id.base_list_title);
        base_list_confirm = (FrameLayout) findViewById(R.id.base_list_confirm);
        base_list_edit = findViewById(R.id.base_list_edit);
        base_list_edit_group = findViewById(R.id.base_list_edit_group);
        base_list_edit_text = (TextView) findViewById(R.id.base_list_edit_text);
        base_list_add = findViewById(R.id.base_list_add);
        base_list_add_text = (TextView) findViewById(R.id.base_list_add_text);
        base_list_finish = findViewById(R.id.base_list_finish);

        base_list_swipe = (SwipeRefreshLayout) findViewById(R.id.base_list_swipe);
        base_list_swipe.setColorSchemeColors(Color.RED, Color.BLUE, Color.YELLOW, Color.BLUE);

        base_list_recycler = (RecyclerView) findViewById(R.id.base_list_recycler);
        base_list_recycler.setLayoutManager(new LinearLayoutManager(this));

        initClick();
    }

    /**
     * 设置确定按钮点击事件
     * @param onClickListener
     */
    protected void setOnConfirmClickListener(View.OnClickListener onClickListener) {
        base_list_confirm.setVisibility(View.VISIBLE);
        base_list_confirm.setOnClickListener(onClickListener);
    }

    /**
     * 设置下拉刷新事件
     * @param onRefreshListener
     */
    protected void setSwipeRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        base_list_swipe.setOnRefreshListener(onRefreshListener);
    }

    /**
     * 设置列表刷新load显示状态
     * @param refreshing
     */
    protected void setRefreshing(boolean refreshing) {
        base_list_swipe.setRefreshing(refreshing);
    }

    protected void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
        base_list_recycler.setAdapter(adapter);
    }

    /**
     * 设置主题文本
     * @param resId
     */
    public void setThemeText(int resId) {
        themeText = getString(resId);
        if (TextUtil.isNotEmpty(themeText)) {
            base_list_title.setText(themeText);
            base_list_add_text.append(themeText);
            base_list_edit_text.append(themeText);
        }
    }

    /**
     * 设置主题文本
     * @param text
     */
    public void setThemeText(String text) {
        themeText = text;
        if (TextUtil.isNotEmpty(themeText)) {
            base_list_title.setText(themeText);
            base_list_add_text.append(themeText);
            base_list_edit_text.append(themeText);
        }
    }

    @Override
    public void onBackPressed() {
        if (adapter.isEdit()) {
            setEditMode(false);
            adapter.setEdit(false);
            return;
        }
        super.onBackPressed();
    }

    private void initClick() {
        base_list_back.setOnClickListener(this);
        base_list_add.setOnClickListener(this);
        base_list_edit.setOnClickListener(this);
        base_list_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.base_list_back) {
            finish();

        } else if (i == R.id.base_list_add) {
            onAddClick();

        } else if (i == R.id.base_list_edit) {
            setEditMode(true);
            onEditClick();

        } else if (i == R.id.base_list_finish) {
            setEditMode(false);
            onFinishClick();

        }
    }

    protected void showAddDialog(CommonEditDialog.OnEditListener onEditListener) {
        commonEditDialog = new CommonEditDialog(this, themeText);
        commonEditDialog.show(onEditListener);
    }

    protected void showEditDialog(String name, CommonEditDialog.OnEditListener onEditListener) {
        commonEditDialog = new CommonEditDialog(this, name, themeText);
        commonEditDialog.show(onEditListener);
    }

    /**
     * 设置编辑模式
     * @param isEdit
     */
    protected void setEditMode(boolean isEdit) {
        if (isEdit) {
            base_list_finish.setVisibility(View.VISIBLE);
            base_list_edit_group.setVisibility(View.GONE);
        } else {
            base_list_finish.setVisibility(View.GONE);
            base_list_edit_group.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 新增按钮点击事件
     */
    protected abstract void onAddClick();

    /**
     * 编辑按钮点击事件
     */
    protected abstract void onEditClick();

    /**
     * 完成按钮点击事件
     */
    protected abstract void onFinishClick();
}
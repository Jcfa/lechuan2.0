package com.poso2o.lechuan.activity.wshop;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.dialog.TipsNoAuthorDialog;
import com.poso2o.lechuan.fragment.vdian.VdianGoodsFragment;
import com.poso2o.lechuan.fragment.vdian.VdianOrderFragment;
import com.poso2o.lechuan.tool.edit.TextUtils;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.poso2o.lechuan.base.BaseManager.FIRST;

/**
 * 微店
 *
 * Created by Jaydon on 2018/3/13.
 */
public class VdianActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 微店导入商品
     */
    public final static int REQUEST_IMPORT_CODE = 112;

    /**
     * 商品搜索
     */
    private LinearLayout vdian_search_group;
    private EditText vdian_search;
    private View vdian_search_delete;

    /**
     * 添加商品
     */
    private ImageView vdian_add_goods;

    /**
     * 底部标签
     */
    private TextView vdian_goods_tag, vdian_order_tag;

    /**
     * 商品列表视图
     */
    private VdianGoodsFragment vdianGoodsFragment;

    /**
     * 订单列表视图
     */
    private VdianOrderFragment vdianOrderFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_vdian;
    }

    @Override
    protected void initView() {
        vdian_search_group = findView(R.id.vdian_search_group);
        vdian_search = findView(R.id.vdian_search);
        vdian_search_delete = findView(R.id.vdian_search_delete);
        vdian_add_goods = findView(R.id.vdian_add_goods);
        vdian_goods_tag = findView(R.id.vdian_goods_tag);
        vdian_order_tag = findView(R.id.vdian_order_tag);
    }

    @Override
    protected void initData() {
        vdianGoodsFragment = new VdianGoodsFragment();
        vdianOrderFragment = new VdianOrderFragment();
        addFragment(R.id.vdian_content, vdianGoodsFragment);
    }

    @Override
    protected void initListener() {
        vdian_search.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    vdianGoodsFragment.search(vdian_search.getText().toString());
                    return true;
                }
                return false;
            }
        });
        vdian_search.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    vdian_search_delete.setVisibility(GONE);
                    vdianGoodsFragment.search(s);
                } else {
                    vdian_search_delete.setVisibility(VISIBLE);
                }
            }
        });
        vdian_search_delete.setOnClickListener(this);
        vdian_add_goods.setOnClickListener(this);
        vdian_goods_tag.setOnClickListener(this);
        vdian_order_tag.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vdian_search_delete:// 清空搜索
                vdian_search.setText("");
                break;

            case R.id.vdian_add_goods:// 导入商品
                if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SHOP_VERIFY) == 0){
                    TipsNoAuthorDialog noAuthorDialog = new TipsNoAuthorDialog(activity);
                    noAuthorDialog.show();
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(activity, VdianImportGoodsActivity.class);
                startActivityForResult(intent, REQUEST_IMPORT_CODE);
                break;

            case R.id.vdian_goods_tag:// 商品列表
                if (vdianOrderFragment.isVisible()) {
                    vdian_search_group.setVisibility(VISIBLE);
                    vdian_add_goods.setVisibility(VISIBLE);
                    setTitle("");
                    vdian_goods_tag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_vdian_goods_on, 0, 0, 0);
                    vdian_goods_tag.setTextColor(getColorValue(R.color.textGreen));
                    vdian_order_tag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_vdian_order_off, 0, 0, 0);
                    vdian_order_tag.setTextColor(getColorValue(R.color.textBlack));
                    replaceFragment(R.id.vdian_content, vdianGoodsFragment);
                }
                break;

            case R.id.vdian_order_tag:// 订单管理
                if (vdianGoodsFragment.isVisible()) {
                    vdian_search_group.setVisibility(GONE);
                    vdian_add_goods.setVisibility(GONE);
                    setTitle("订单");
                    vdian_order_tag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_vdian_order_on, 0, 0, 0);
                    vdian_order_tag.setTextColor(getColorValue(R.color.textGreen));
                    vdian_goods_tag.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_vdian_goods_off, 0, 0, 0);
                    vdian_goods_tag.setTextColor(getColorValue(R.color.textBlack));
                    replaceFragment(R.id.vdian_content, vdianOrderFragment);
                }
                break;
        }
    }

    @Override
    public void onEvent(EventBean event) {
        super.onEvent(event);
        vdianOrderFragment.onEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMPORT_CODE:
                    vdianGoodsFragment.loadGoodsData(FIRST);
                    break;
            }
        }
    }
}

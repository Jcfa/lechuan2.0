package com.poso2o.lechuan.activity.oa;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.SelectGoodsActivity;
import com.poso2o.lechuan.activity.image.SelectImagesActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.oa.Template;
import com.poso2o.lechuan.fragment.oa.FreeEditFragment;
import com.poso2o.lechuan.fragment.oa.TemplateFragment;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.TemplateDataManager;
import com.poso2o.lechuan.views.SpaceItemDecoration;

import java.util.ArrayList;

/**
 * 公众号助手-自由编辑，模版部分由h5实现
 * Created by Administrator on 2018-02-05.
 */

public class OAFreeEditActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<Template> commonTemplates = new ArrayList<>();//常用的模版列表
    private ArrayList<Template> templatesGroup = new ArrayList<>();//模版组
    private BaseAdapter mTemplateAdapter;
    private BaseAdapter mTemplateGroupAdapter;
    private LinearLayout layoutTemplatesGroup;//模版组
    private LinearLayout layoutTemplatesCommon;//常用模版
    private LinearLayout layoutButtomView;//底部的加入稿件箱/去发布
    private FreeEditFragment mFreeEditFragment;//自由编辑
    private TemplateFragment mTemplateFragment;//使用模版
    //    private WebView mWebView;
    private static final int REQUEST_PHOTO_ID = 9;// 相册
    private static final int REQUEST_GOODS_ID = 10;// 选商品

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_oa_free_edit;
    }

    @Override
    protected void initView() {
        findView(R.id.iv_back).setOnClickListener(this);
        findView(R.id.preview).setOnClickListener(this);
        findView(R.id.oa_template_change).setOnClickListener(this);//切换模版
        findView(R.id.oa_template_goods).setOnClickListener(this);//添加商品
        findView(R.id.oa_template_image).setOnClickListener(this);//添加图片
        findView(R.id.oa_free_edit_hide_template).setOnClickListener(this);//隐藏/显示常用模版
        findView(R.id.oa_free_edit_draft).setOnClickListener(this);//加入稿件箱
        findView(R.id.oa_free_edit_publish).setOnClickListener(this);//去发布
//        mWebView = findView(R.id.oa_free_edit_webview);
        layoutTemplatesGroup = findView(R.id.templates_group);
        layoutTemplatesCommon = findView(R.id.templates_common);
        layoutButtomView = findView(R.id.oa_free_edit_buttom);
        RecyclerView recyclerViewTemplate = findView(R.id.recyclerView_template);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTemplate.addItemDecoration(new SpaceItemDecoration(10));
        recyclerViewTemplate.setLayoutManager(layoutManager);
        RecyclerView recyclerViewTemplateGroup = findView(R.id.recyclerView_template_group);
        recyclerViewTemplateGroup.setLayoutManager(new GridLayoutManager(activity, 4));
        recyclerViewTemplateGroup.addItemDecoration(new SpaceItemDecoration(10));
        mTemplateAdapter = new BaseAdapter(activity, commonTemplates) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, final int position) {
                holder.getView(R.id.item_main).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position==0){
                            toFreeEdit();
                        }else{
                            toUseTemplate();
                        }
                    }
                });
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.item_oa_template_common, parent));
            }
        };
        recyclerViewTemplate.setAdapter(mTemplateAdapter);
        mTemplateGroupAdapter = new BaseAdapter(activity, commonTemplates) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {

            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.item_oa_template_group_common, parent));
            }
        };
        recyclerViewTemplateGroup.setAdapter(mTemplateGroupAdapter);
        mFreeEditFragment = new FreeEditFragment();
        mTemplateFragment = new TemplateFragment();
    }

    @Override
    protected void initData() {
        commonTemplates.clear();
        templatesGroup.clear();
        for (int i = 0; i < 5; i++) {
            Template template = new Template();
            template.pic = "";
            template.name = "简约模版";
            commonTemplates.add(template);
            templatesGroup.add(template);
        }
        mTemplateAdapter.notifyDataSetChanged(commonTemplates);
        mTemplateGroupAdapter.notifyDataSetChanged(templatesGroup);
//        mWebView.loadUrl("http://blog.csdn.net/ycb1689/article/details/8071892");
        addFragment(R.id.fragment_container, mFreeEditFragment);

        getMyTemplateGroups();
    }

    @Override
    protected void initListener() {

    }

    private void getMyTemplateGroups(){
        TemplateDataManager.getInstance().getMyTemplateGroups(activity, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {

            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }

    /**
     * 自由编辑
     */
    private void toFreeEdit() {
        replaceFragment(R.id.fragment_container,mFreeEditFragment);
    }

    /**
     * 使用模版
     */
    private void toUseTemplate() {
        replaceFragment(R.id.fragment_container,mTemplateFragment);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oa_template_change://显示/隐藏模版组
                if (layoutTemplatesGroup.getVisibility() == View.VISIBLE) {
                    layoutTemplatesGroup.setVisibility(View.GONE);
                } else {
                    layoutTemplatesGroup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.oa_template_goods://选择商品
                mFreeEditFragment.toGoods();
                break;
            case R.id.oa_template_image://选择图片
                mFreeEditFragment.toPhoto();
                break;
            case R.id.oa_free_edit_hide_template://隐藏常用模版
                if (layoutTemplatesCommon.getVisibility() == View.VISIBLE) {
                    layoutTemplatesCommon.setVisibility(View.GONE);
                } else {
                    layoutTemplatesCommon.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.oa_free_edit_draft://加入稿件箱

                break;
            case R.id.oa_free_edit_publish://去发布

                break;
        }
    }
}

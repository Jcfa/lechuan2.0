package com.poso2o.lechuan.activity.oa;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.oa.Template;
import com.poso2o.lechuan.bean.oa.TemplateBean;
import com.poso2o.lechuan.bean.oa.TemplateGroup;
import com.poso2o.lechuan.bean.oa.TemplateGroups;
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

public class OAFreeEditActivity2 extends BaseActivity implements View.OnClickListener {
    private ArrayList<TemplateBean> myTemplates = new ArrayList<>();//常用模版
    private ArrayList<TemplateGroup> templatesGroup = new ArrayList<>();//购买的模版组列表
    private ArrayList<TemplateBean> templates = new ArrayList<>();
    private BaseAdapter mTemplateAdapter;
    private BaseAdapter mTemplateGroupAdapter;
    private LinearLayout layoutTemplatesGroup;//模版组
    private LinearLayout layoutTemplatesCommon;//常用模版
    private LinearLayout layoutButtomView;//底部的加入稿件箱/去发布
    //    private FreeEditFragment mFreeEditFragment;//自由编辑
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
        mTemplateAdapter = new BaseAdapter(activity, myTemplates) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, final int position) {
                final TemplateBean templateBean = (TemplateBean) item;
                ImageView templatePic = holder.getView(R.id.iv_template_pic);
                Glide.with(activity).load(templateBean.pic).placeholder(R.mipmap.ic_launcher).into(templatePic);
                TextView templateName = holder.getView(R.id.tv_template_name);
                templateName.setText(templateBean.template_name);
                holder.getView(R.id.item_main).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (position == 0) {
//                            toFreeEdit();
//                        } else {
//                            toUseTemplate();
//                        }
                        mTemplateFragment.changeTemplate(templateBean);
                        setTemplateGroupSubList(templateBean);
                    }
                });
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.item_oa_template_common, parent));
            }
        };
        recyclerViewTemplate.setAdapter(mTemplateAdapter);
        mTemplateGroupAdapter = new BaseAdapter(activity, templatesGroup) {
            @Override
            public void initItemView(BaseViewHolder holder, Object item, int position) {

            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.item_oa_template_group_common, parent));
            }
        };
        recyclerViewTemplateGroup.setAdapter(mTemplateGroupAdapter);
//        mFreeEditFragment = new FreeEditFragment();
        mTemplateFragment = new TemplateFragment();
    }

    @Override
    protected void initData() {
        addFragment(R.id.fragment_container, mTemplateFragment);

        getMyTemplateGroups();
    }

    @Override
    protected void initListener() {

    }

    /**
     * 加载购买的模版
     */
    private void getMyTemplateGroups() {
        showLoading();
        TemplateDataManager.getInstance().getMyTemplateGroups(activity, new IRequestCallBack<TemplateGroups>() {
            @Override
            public void onResult(int tag, TemplateGroups result) {
                dismissLoading();
                if (result != null && result.list != null) {
                    templatesGroup = result.list;
                    setCommonTemplate();
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
            }
        });
    }

    /**
     * 加载常用模版
     */
    private void getCommonTemplate() {

    }

    /**
     * 设置常用模版,默认取各个模版组的第一个模版
     */
    private void setCommonTemplate() {
        if (templatesGroup == null) {
            return;
        }
        myTemplates.clear();
        for (TemplateGroup templateGroup : templatesGroup) {
            if (templateGroup.templates != null && templateGroup.templates.size() > 0) {
                myTemplates.add(templateGroup.templates.get(0));
            }
        }
        mTemplateAdapter.notifyDataSetChanged(myTemplates);
        if (myTemplates.size() > 0) {
            mTemplateFragment.changeTemplate(myTemplates.get(0));
            setTemplateGroupSubList(myTemplates.get(0));
        }
    }

    /**
     * 设置显示模版组的模版列表
     */
    private void setTemplateGroupSubList(TemplateBean templateBean) {
        for (TemplateGroup templateGroup : templatesGroup) {
            if (templateGroup.group_id.equals(templateBean.group_id)) {
                templates.clear();
                templates = templateGroup.templates;
                mTemplateGroupAdapter.notifyDataSetChanged(templates);
            }
        }
    }

    /**
     * 自由编辑
     */
    private void toFreeEdit() {
//        replaceFragment(R.id.fragment_container,mFreeEditFragment);
    }

    /**
     * 使用模版
     */
    private void toUseTemplate() {
        replaceFragment(R.id.fragment_container, mTemplateFragment);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oa_template_change://显示/隐藏模版组
//                if (layoutTemplatesGroup.getVisibility() == View.VISIBLE) {
//                    layoutTemplatesGroup.setVisibility(View.GONE);
//                } else {
//                    layoutTemplatesGroup.setVisibility(View.VISIBLE);
//                }
                mTemplateFragment.showTemplateDialog();
                break;
            case R.id.oa_template_goods://选择商品
//                mFreeEditFragment.toGoods();
                break;
            case R.id.oa_template_image://选择图片
//                mFreeEditFragment.toPhoto();
                mTemplateFragment.openPhoto(TemplateFragment.INSERT_PIC);
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

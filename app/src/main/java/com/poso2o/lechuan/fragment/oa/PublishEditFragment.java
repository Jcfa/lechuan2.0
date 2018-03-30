package com.poso2o.lechuan.fragment.oa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.ArticleInfoActivity;
import com.poso2o.lechuan.activity.oa.ArticleInfoNewActivity;
import com.poso2o.lechuan.activity.oa.FreeEditActivity;
import com.poso2o.lechuan.activity.oa.ArticleAdActivity;
import com.poso2o.lechuan.activity.wshop.WCAuthorityActivity;
import com.poso2o.lechuan.adapter.OAPublishEditAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.shopdata.BangDingData;
import com.poso2o.lechuan.dialog.CommonDelDialog;
import com.poso2o.lechuan.dialog.CommonDeleteDialog;
import com.poso2o.lechuan.dialog.PublishConfirmDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.article.ArticleDataManager;
import com.poso2o.lechuan.manager.oa.RenewalsManager;
import com.poso2o.lechuan.manager.wshopmanager.WShopManager;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.RecyclerViewDivider;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import static android.app.Activity.RESULT_OK;

/**
 * 公众号助手-发布-发布
 * Created by Administrator on 2018-02-03.
 */

public class PublishEditFragment extends BaseFragment implements OAPublishEditAdapter.OAPublishListener {

    public static final int CODE_TO_V_AUTHORITY = 1001;

    //绑定的公众号图标
    private ImageView iv_oa_logo;
    //公众号名称
    private TextView tv_oa_name;
    //发送至公众号
    private TextView publishButton;

    //    private ArrayList<Article> articles = new ArrayList<>();
    private OAPublishEditAdapter mAdapter;

    //绑定公众号信息
    private BangDingData bangDingData;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish_edit, container, false);
    }

    @Override
    public void initView() {

        iv_oa_logo = findView(R.id.iv_oa_logo);
        tv_oa_name = findView(R.id.tv_oa_name);

        SwipeMenuRecyclerView recyclerView = findView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecyclerViewDivider(
                context, LinearLayoutManager.HORIZONTAL, R.drawable.recycler_divider));
        mAdapter = new OAPublishEditAdapter(context, ArticleDataManager.getInstance().getSelectData());
        mAdapter.setOaPublishListener(this);
        View footerView = LayoutInflater.from(context).inflate(R.layout.footer_recyclerview_oa_publish_edit, null);
        footerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        publishButton = (TextView) footerView.findViewById(R.id.btn_ok);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPublishArticles();
            }
        });
        recyclerView.addFooterView(footerView);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        authorizeState();
    }

    @Override
    public void initListener() {
        findView(R.id.oa_bind_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, WCAuthorityActivity.class);
                intent.putExtra(WCAuthorityActivity.BIND_TYPE,1);
                startActivityForResult(intent,CODE_TO_V_AUTHORITY);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)return;
        switch (requestCode){
            case CODE_TO_V_AUTHORITY:
                authorizeState();
                break;
        }
    }

    /**
     * 发布文章至公众号
     */
    public void onPublishArticles() {
        if (!publishButton.isSelected()){
            Toast.show(context,"请添加要发布的文章");
            return;
        }
        //计算已添加广告的篇数
        int adNum = 0;
        for (Article art : ArticleDataManager.getInstance().getSelectData()){
            if (art.content.contains("poso2o_editor_ad_template_item")){
                adNum++;
            }
        }
        PublishConfirmDialog confirmDialog = new PublishConfirmDialog(getContext(),onPublishListener);
        confirmDialog.show();
        confirmDialog.setPublishInfo(ArticleDataManager.getInstance().getSelectData().size(),adNum);
    }
    private PublishConfirmDialog.OnPublishListener onPublishListener = new PublishConfirmDialog.OnPublishListener() {
        @Override
        public void onPublish() {
            showLoading();
            ArticleDataManager.getInstance().sendArticle((BaseActivity) getActivity(), new IRequestCallBack() {
                @Override
                public void onResult(int tag, Object result) {
                    dismissLoading();
                    mAdapter.notifyDataSetChanged(ArticleDataManager.getInstance().getSelectData());
                }

                @Override
                public void onFailed(int tag, String msg) {
                    dismissLoading();
                    Toast.show(getContext(),msg);
                }
            });
        }
    };

    @Override
    public void onEdit() {
        startActivity(FreeEditActivity.class);
    }

    @Override
    public void onArticle() {
        EventBean bean = new EventBean(EventBean.CODE_TO_COLLECT_LIST);
        EventBus.getDefault().post(bean);
    }

    /**
     * 点击文章
     *
     * @param position
     * @param article
     */
    @Override
    public void onItemClick(int position, Article article) {
        Intent intent = new Intent();
//        intent.setClass(getContext(), ArticleInfoActivity.class);

        intent.setClass(getContext(), ArticleInfoNewActivity.class);

        intent.putExtra(ArticleAdActivity.ART_DATA,article);
        intent.putExtra(ArticleAdActivity.FROM_PUBLISH_LIST,true);
        startActivity(intent);
    }

    /**
     * 删除文章
     *
     * @param position
     * @param article
     */
    @Override
    public void onItemDelete(int position,final Article article) {
        final CommonDelDialog delDialog = new CommonDelDialog(getContext());
        delDialog.setOnCommonOkListener(new CommonDelDialog.OnCommonOkListener() {
            @Override
            public void onOkClick() {
                delDialog.dismiss();
                ArticleDataManager.getInstance().removeSelectData(article);
                mAdapter.notifyDataSetChanged(ArticleDataManager.getInstance().getSelectData());
                publishClickable();
            }
        });
        delDialog.show();
        delDialog.setTips("确定删除文章",18,article.title,14);
    }

    /**
     * 添加广告
     *
     * @param position
     * @param article
     */
    @Override
    public void onItemAddAdvertised(int position, Article article) {
        Toast.show(context, "添加广告" + position);
    }

    /**
     * 上移
     *
     * @param position
     * @param article
     */
    @Override
    public void moveUp(int position, Article article) {
        mAdapter.notifyDataSetChanged(ArticleDataManager.getInstance().upArticle(article));
    }

    /**
     * 下移
     *
     * @param position
     * @param article
     */
    @Override
    public void moveDown(int position, Article article) {
        mAdapter.notifyDataSetChanged(ArticleDataManager.getInstance().downArticle(article));
    }

    /**
     * 设为封面
     *
     * @param position
     * @param article
     */
    @Override
    public void setStick(int position, Article article) {
        mAdapter.notifyDataSetChanged(ArticleDataManager.getInstance().topArticle(article));
    }

    /**
     * 保存到草稿箱
     *
     * @param position
     * @param article
     */
    @Override
    public void saveDraft(int position, final Article article) {
        showLoading();
        RenewalsManager.getRenewalsManager().renewalsAdd((BaseActivity)getActivity(), article, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Toast.show(getContext(),"添加成功");
                ArticleDataManager.getInstance().removeSelectData(article);
                mAdapter.notifyDataSetChanged(ArticleDataManager.getInstance().getSelectData());
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getContext(),msg);
            }
        });
    }

    /**
     * 获取绑定公众号的状态
     */
    public void authorizeState(){
        WShopManager.getrShopManager().authorizeState((BaseActivity) getActivity(),false, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                bangDingData = (BangDingData) result;
                if (bangDingData != null && bangDingData.authorized.equals("1")){
                    Glide.with(context).load(bangDingData.head_img).placeholder(R.mipmap.background_image).into(iv_oa_logo);
                    tv_oa_name.setText(bangDingData.nick_name);
                }else{
                    iv_oa_logo.setImageResource(R.mipmap.background_image);
                    tv_oa_name.setText("请绑定公众号");
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context,msg);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged(ArticleDataManager.getInstance().getSelectData());
        publishClickable();
    }

    /**
     * 判断发布至公众号是否可点击
     */
    private void publishClickable(){
        if (ArticleDataManager.getInstance().getSelectData().size() < 1){
            publishButton.setSelected(false);
        }else {
            publishButton.setSelected(true);
        }
    }
}

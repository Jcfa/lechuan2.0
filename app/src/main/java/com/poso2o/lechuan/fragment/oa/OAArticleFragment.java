package com.poso2o.lechuan.fragment.oa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.oa.ArticleAdActivity;
import com.poso2o.lechuan.activity.oa.ArticleInfoActivity;
import com.poso2o.lechuan.activity.oa.ArticleInfoNewActivity;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.article.Article;
import com.poso2o.lechuan.manager.article.ArticleDataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 公众号咨询界面
 *
 * Created by Jaydon on 2018/1/26.
 */
public class OAArticleFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 顶部标签：已发布、收藏、推荐
     */
    private TextView oa_article_published, oa_article_collect, oa_article_fashion;

    /**
     * 选中的标签
     */
    private TextView selectTag;

    /**
     * 文章列表：已发布、收藏、推荐
     */
    private OAPublishedFragment publishedFragment;
    private OAArticleListFragment collectListFragment;
    private OAArticleListFragment fashianListFragment;

    /**
     * 当前显示的列表视图
     */
    private Fragment selectFragment;

    /**
     * 内容
     */
    private ViewPager oa_article_content;
    private List<Fragment> mFragmentlist;

    private OAArticleAdapter oaArticleAdapter;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_oa_article, container, false);
    }

    @Override
    public void initView() {
        oa_article_published = findView(R.id.oa_article_published);
        oa_article_collect = findView(R.id.oa_article_collect);
        oa_article_fashion = findView(R.id.oa_article_fashion);
        oa_article_content = findView(R.id.oa_article_content);

        selectTag = oa_article_published;
    }

    @Override
    public void initData() {
        publishedFragment = new OAPublishedFragment();

        collectListFragment = new OAArticleListFragment();
        collectListFragment.setArticlesType(ArticleDataManager.COLLECT);

        fashianListFragment = new OAArticleListFragment();
        fashianListFragment.setArticlesType(ArticleDataManager.CATE);// TODO

        mFragmentlist = new ArrayList<Fragment>();
        mFragmentlist.add(publishedFragment);
        mFragmentlist.add(collectListFragment);
        mFragmentlist.add(fashianListFragment);
        oaArticleAdapter = new OAArticleAdapter(getChildFragmentManager());
        oa_article_content.setAdapter(oaArticleAdapter);
        oa_article_content.setCurrentItem(2);
        switchTextView(oa_article_fashion);

        selectFragment = fashianListFragment;
    }

    @Override
    public void initListener() {
        oa_article_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        switchTextView(oa_article_published);
                        break;
                    case 1:
                        switchTextView(oa_article_collect);
                        collectListFragment.notifyList();
                        break;
                    case 2:
                        switchTextView(oa_article_fashion);
                        fashianListFragment.notifyList();
                        break;
                }
                selectFragment = oaArticleAdapter.getItem(position);
                if (onPageChangeListener != null)onPageChangeListener.onPageChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        oa_article_published.setOnClickListener(this);
        oa_article_collect.setOnClickListener(this);
        oa_article_fashion.setOnClickListener(this);

        collectListFragment.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                Article article = ArticleDataManager.getInstance().findSelectData((Article) item);
                if (article == null) article = (Article) item;
                Intent intent = new Intent();
                intent.putExtra(ArticleAdActivity.ART_DATA,article);
                intent.setClass(getContext(),ArticleInfoActivity.class);

//                intent.setClass(getContext(),ArticleInfoNewActivity.class);
                startActivity(intent);
            }
        });

        fashianListFragment.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                Article article = ArticleDataManager.getInstance().findSelectData((Article) item);
                if (article == null) article = (Article) item;
                Intent intent = new Intent();
                intent.putExtra(ArticleAdActivity.ART_DATA,article);
                intent.setClass(getContext(),ArticleInfoActivity.class);

//                intent.setClass(getContext(),ArticleInfoNewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void switchTextView(TextView tv) {
        selectTag.setTextColor(getResources().getColor(R.color.textBlack));
        tv.setTextColor(getResources().getColor(R.color.textGreen));
        selectTag = tv;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.oa_article_published:
                oa_article_published.setTextColor(getColorValue(R.color.textGreen));
                oa_article_collect.setTextColor(getColorValue(R.color.textBlack));
                oa_article_fashion.setTextColor(getColorValue(R.color.textBlack));
                oa_article_content.setCurrentItem(0);
                selectFragment = collectListFragment;
                break;

            case R.id.oa_article_collect:
                oa_article_collect.setTextColor(getColorValue(R.color.textGreen));
                oa_article_published.setTextColor(getColorValue(R.color.textBlack));
                oa_article_fashion.setTextColor(getColorValue(R.color.textBlack));
                oa_article_content.setCurrentItem(1);
                selectFragment = collectListFragment;
                break;

            case R.id.oa_article_fashion:
                oa_article_fashion.setTextColor(getColorValue(R.color.textGreen));
                oa_article_published.setTextColor(getColorValue(R.color.textBlack));
                oa_article_collect.setTextColor(getColorValue(R.color.textBlack));
                oa_article_content.setCurrentItem(2);
                selectFragment = fashianListFragment;
                break;
        }
    }

    public void reLoadData(){
        if (oa_article_content.getCurrentItem() == 1){
            collectListFragment.reLoadData();
        }else if (oa_article_content.getCurrentItem() == 2){
            fashianListFragment.reLoadData();
        }
    }

    /**
     * 跳转到收藏列表
     */
    public void toCollectList(){
    }

    /**
     * 处理Fragment和ViewPager的适配器
     */
    private class OAArticleAdapter extends FragmentPagerAdapter{

        public OAArticleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragmentlist.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragmentlist.size();
        }

    }

    //切换监听，切换到已发布列表时，右上角的搜索和筛选按钮隐藏
    private OnPageChangeListener onPageChangeListener;
    public interface OnPageChangeListener{
        void onPageChange(int page);
    }
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener){
        this.onPageChangeListener = onPageChangeListener;
    }
}
package com.poso2o.lechuan.fragment.publish;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.publish.PublishActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.manager.article.ArticleDataManager;

import java.util.ArrayList;

/**
 * 选文发布
 * Created by Jaydon on 2017/12/1.
 */
public class SelectArticleFragment extends BaseFragment implements OnClickListener {

    /**
     * 内容
     */
    private ViewPager select_article_content;
    private SelectArticleAdapter selectArticleAdapter;

    /**
     * 文章标签：收藏、时尚、美食、健康
     */
    private TextView select_article_collect, select_article_fashion, select_article_cate, select_article_health;

    /**
     * 选中的标签
     */
    private TextView selectTag;

    /**
     * 搜索关键词
     */
    private String keywords;

    /**
     * 列表视图
     */
    private ArticleListFragment collectArticleList;// 收藏
    private ArticleListFragment fashionArticleList;// 时尚
    private ArticleListFragment cateArticleList;// 美食
    private ArticleListFragment healthArticleList;// 健康

    /**
     * 当前显示的列表视图
     */
    private ArticleListFragment selectFragment;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_article, container, false);
    }

    @Override
    public void initView() {
        select_article_content = findView(R.id.select_article_content);

        select_article_collect = findView(R.id.select_article_collect);
        select_article_fashion = findView(R.id.select_article_fashion);
        select_article_cate = findView(R.id.select_article_cate);
        select_article_health = findView(R.id.select_article_health);
        // 设置默认选中标签
        selectTag = select_article_collect;
    }

    @Override
    public void initData() {
        collectArticleList = new ArticleListFragment();
        collectArticleList.setArticlesType(ArticleDataManager.COLLECT);

        fashionArticleList = new ArticleListFragment();
        fashionArticleList.setArticlesType(ArticleDataManager.FASHION);

        cateArticleList = new ArticleListFragment();
        cateArticleList.setArticlesType(ArticleDataManager.CATE);

        healthArticleList = new ArticleListFragment();
        healthArticleList.setArticlesType(ArticleDataManager.HEALTH);

        ArrayList<ArticleListFragment> data = new ArrayList<>();
        data.add(collectArticleList);
        data.add(fashionArticleList);
        data.add(cateArticleList);
        data.add(healthArticleList);
        selectArticleAdapter = new SelectArticleAdapter(data);
        select_article_content.setAdapter(selectArticleAdapter);
        select_article_content.setCurrentItem(0);

        selectFragment = collectArticleList;
    }

    public void search(String s) {
        keywords = s;
        selectFragment.search(s);
    }

    @Override
    public void initListener() {
        select_article_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        switchTextView(select_article_collect);
                        break;

                    case 1:
                        switchTextView(select_article_fashion);
                        break;

                    case 2:
                        switchTextView(select_article_cate);
                        break;

                    case 3:
                        switchTextView(select_article_health);
                        break;
                }
                selectFragment = selectArticleAdapter.getItem(position);
                ((PublishActivity) context).clearSearchContent();
                selectFragment.search("");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        select_article_collect.setOnClickListener(this);
        select_article_fashion.setOnClickListener(this);
        select_article_cate.setOnClickListener(this);
        select_article_health.setOnClickListener(this);
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    public void onClick(View v) {
        if (selectTag == v) {
            return;
        }
        TextView tv = (TextView) v;
        switchTextView(tv);
        int position = 0;
        switch (v.getId()) {
            case R.id.select_article_collect:// 收藏
                selectFragment = collectArticleList;
                position = 0;
                break;

            case R.id.select_article_fashion:// 时尚
                selectFragment = fashionArticleList;
                position = 1;
                break;

            case R.id.select_article_cate:// 美食
                selectFragment = cateArticleList;
                position = 2;
                break;

            case R.id.select_article_health:// 健康
                selectFragment = healthArticleList;
                position = 3;
                break;
        }
        keywords = "";
        ((PublishActivity) context).clearSearchContent();
        select_article_content.setCurrentItem(position);
        selectFragment.search("");
    }

    private void switchTextView(TextView tv) {
        selectTag.setBackgroundColor(0x00000000);
        selectTag.setTextColor(getResources().getColor(R.color.textBlack));
        tv.setBackgroundResource(R.drawable.shape_select_article_tag);
        tv.setTextColor(getResources().getColor(R.color.textGreen));
        selectTag = tv;
    }

    private class SelectArticleAdapter extends FragmentPagerAdapter {

        private ArrayList<ArticleListFragment> data;

        public SelectArticleAdapter(ArrayList<ArticleListFragment> data) {
            super(getActivity().getSupportFragmentManager());
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public ArticleListFragment getItem(int position) {
            return data.get(position);
        }
    }
}
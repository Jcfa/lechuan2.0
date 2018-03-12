package com.poso2o.lechuan.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.realshop.ArticleSearchActivity;
import com.poso2o.lechuan.activity.realshop.OfficalAccountActivity;
import com.poso2o.lechuan.adapter.ArticlePagerAdapter;
import com.poso2o.lechuan.base.BaseView;

import java.util.ArrayList;


/**
 * Created by mr zhang on 2017/10/23.
 */

public class OAInformationView extends BaseView implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private View view;
    private OfficalAccountActivity context;

    //返回键
    private ImageButton official_accounts_back;
    //文章搜索
    private ImageView article_search;

    //收藏
    private TextView oa_information_favorite;
    //时尚
    private TextView oa_information_fashion;
    //美食
    private TextView oa_information_food;
    //健康
    private TextView oa_information_healthy;

    //各分类列表容器
    private ViewPager oa_sort_container;
    //viewpager适配器
    private ArticlePagerAdapter pagerAdapter;
    private ArrayList<BaseView> tagViews;

    //上一个选中的tag
    private TextView preSelectTag;
    //当前tag的位置
    private int mCurrTag ;
    private OATagView favoriteView;
    private OATagView fashionView;
    private OATagView foodView;
    private OATagView healthyView;

    /**
     * 注意继承后 先走了初始化控件  data
     *
     * @param context
     */
    public OAInformationView(Context context) {
        super(context);
        this.context = (OfficalAccountActivity) context;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.view_oa_information,null);
        return view;
    }

    @Override
    public void initView() {
        oa_information_favorite = (TextView) view.findViewById(R.id.oa_information_favorite);

        oa_information_fashion = (TextView) view.findViewById(R.id.oa_information_fashion);

        oa_information_food = (TextView) view.findViewById(R.id.oa_information_food);

        oa_information_healthy = (TextView) view.findViewById(R.id.oa_information_healthy);

        oa_sort_container = (ViewPager) view.findViewById(R.id.oa_sort_container);

        official_accounts_back = (ImageButton) view.findViewById(R.id.official_accounts_back);

        article_search = (ImageView) view.findViewById(R.id.article_search);
    }

    @Override
    public void initData() {
        initViewPager();
    }

    @Override
    public void initListener() {
        oa_information_favorite.setOnClickListener(this);
        oa_information_fashion.setOnClickListener(this);
        oa_information_food.setOnClickListener(this);
        oa_information_healthy.setOnClickListener(this);

        official_accounts_back.setOnClickListener(this);
        article_search.setOnClickListener(this);

        oa_sort_container.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.official_accounts_back:
                context.finish();
                break;
            case R.id.article_search:
                Intent intent = new Intent();
                intent.setClass(context, ArticleSearchActivity.class);
                intent.putExtra(OfficalAccountActivity.SENDING_ARTICLE_DATA,context.getSelectedArt());
                context.startActivity(intent);
                break;
            case R.id.oa_information_favorite:
                oa_sort_container.setCurrentItem(0);
                break;
            case R.id.oa_information_fashion:
                oa_sort_container.setCurrentItem(1);
                break;
            case R.id.oa_information_food:
                oa_sort_container.setCurrentItem(2);
                break;
            case R.id.oa_information_healthy:
                oa_sort_container.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrTag = position;
        preSelectTag.setSelected(false);
        switch (position){
            case 0:
                oa_information_favorite.setSelected(true);
                preSelectTag = oa_information_favorite;
                favoriteView.notifyAdapter();
                break;
            case 1:
                oa_information_fashion.setSelected(true);
                preSelectTag = oa_information_fashion;
                fashionView.notifyAdapter();
                break;
            case 2:
                oa_information_food.setSelected(true);
                preSelectTag = oa_information_food;
                foodView.notifyAdapter();
                break;
            case 3:
                oa_information_healthy.setSelected(true);
                preSelectTag = oa_information_healthy;
                healthyView.notifyAdapter();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //初始化viewpager数据
    private void initViewPager(){
        tagViews = new ArrayList<>();
        favoriteView = new OATagView(context,0);
        fashionView = new OATagView(context,1);
        foodView = new OATagView(context,2);
        healthyView = new OATagView(context,3);
        tagViews.add(favoriteView);
        tagViews.add(fashionView);
        tagViews.add(foodView);
        tagViews.add(healthyView);

        pagerAdapter = new ArticlePagerAdapter(tagViews);
        oa_sort_container.setAdapter(pagerAdapter);

        oa_information_favorite.setSelected(true);
        preSelectTag = oa_information_favorite;
    }

    //获取当前tag位置
    public int getCurrTag(){
        return mCurrTag;
    }

    //刷新所有列表的状态，目的是刷新是否已添加发布列表的状态
    public void notifyAllAdapter(){
        if (favoriteView != null)favoriteView.notifyAdapter();
        if (fashionView != null)fashionView.notifyAdapter();
        if (foodView != null)foodView.notifyAdapter();
        if (healthyView != null)healthyView.notifyAdapter();
    }

}

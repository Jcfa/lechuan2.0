package com.poso2o.lechuan.fragment.oa;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.ArticleFiltrateAdapter;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.util.ScreenInfo;

import java.util.ArrayList;

/**
 * 文章筛选
 *
 * Created by Jaydon on 2018/1/29.
 */
public class ArticleFiltrateFragment extends BaseFragment implements View.OnClickListener {

    /**
     * 类型列表
     */
    private RecyclerView filtrate_type_list;
    private ArticleFiltrateAdapter typeAdapter;

    /**
     * 标签列表
     */
    private RecyclerView filtrate_tag_list;
    private ArticleFiltrateAdapter tagAdapter;

    /**
     * 回调
     */
    private Callback callback;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_filtrate, container, false);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void initView() {
        filtrate_type_list = findView(R.id.filtrate_type_list);
        filtrate_tag_list = findView(R.id.filtrate_tag_list);

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = (int) (ScreenInfo.WIDTH * 0.8);
        lp.height = ScreenInfo.HEIGHT - ScreenInfo.STATUS_BAR_HEIGHT;
        view.setLayoutParams(lp);
    }

    @Override
    public void initData() {
        filtrate_type_list.setLayoutManager(new GridLayoutManager(context, 3));
        ArrayList<String> types = new ArrayList<>();
        types.add("推荐");
        types.add("时尚");
        types.add("美食");
        types.add("娱乐");
        types.add("居家");
        types.add("旅游");
        typeAdapter = new ArticleFiltrateAdapter(context, types);
        filtrate_type_list.setAdapter(typeAdapter);

        filtrate_tag_list.setLayoutManager(new GridLayoutManager(context, 3));
        ArrayList<String> tags = new ArrayList<>();
        tags.add("美裙");
        tags.add("明星");
        tags.add("打扮");
        tags.add("早点");
        tags.add("街拍");
        tags.add("睫毛膏");
        tagAdapter = new ArticleFiltrateAdapter(context, tags);
        filtrate_tag_list.setAdapter(tagAdapter);
    }

    @Override
    public void initListener() {
        findView(R.id.filtrate_reset).setOnClickListener(this);
        findView(R.id.filtrate_finish).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filtrate_reset:

                break;

            case R.id.filtrate_finish:
                callback.onFinish();
                hideFragment(true);
                break;
        }
    }

    /**
     * 筛选回调
     */
    public interface Callback {

        void onFinish();
    }
}

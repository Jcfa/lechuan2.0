package com.poso2o.lechuan.fragment.oa;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.ArticleLabelAdapter;
import com.poso2o.lechuan.adapter.ArticleTypeAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.oa.OaTypeAndLables;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.OaTypesManager;
import com.poso2o.lechuan.util.ScreenInfo;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

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
    private ArticleTypeAdapter typeAdapter;

    /**
     * 标签列表
     */
    private RecyclerView filtrate_tag_list;
    private ArticleLabelAdapter tagAdapter;

    /**
     * 回调
     */
    private Callback callback;

    private OaTypeAndLables typeAndLables;

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
        typeAdapter = new ArticleTypeAdapter(context, null);
        filtrate_type_list.setAdapter(typeAdapter);

        filtrate_tag_list.setLayoutManager(new GridLayoutManager(context, 3));
        tagAdapter = new ArticleLabelAdapter(context, null);
        filtrate_tag_list.setAdapter(tagAdapter);

        showLoading();
        OaTypesManager.getOaTypesManager().getTypesAndLabels((BaseActivity) getActivity(), new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                typeAndLables = (OaTypeAndLables) result;
                if (typeAndLables != null){
                    typeAdapter.notifyDataSetChanged(typeAndLables.types.list);
                    tagAdapter.notifyDataSetChanged(typeAndLables.labels.list);
                }
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getContext(),msg);
            }
        });
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
                SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_OA_TYPES,"");
                SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_OA_LAYBELS,"");
                typeAdapter = new ArticleTypeAdapter(context,typeAndLables.types.list);
                filtrate_type_list.setAdapter(typeAdapter);
                tagAdapter = new ArticleLabelAdapter(context, typeAndLables.labels.list);
                filtrate_tag_list.setAdapter(tagAdapter);
                break;

            case R.id.filtrate_finish:
                saveSelect();
                callback.onFinish();
                hideFragment(true);
                break;
        }
    }

    //保存选择好的类型标签
    private void saveSelect(){
        ArrayList<String> types = typeAdapter.getSelects();
        String t = "";
        for (int i = 0;i<types.size();i++){
            if (i == types.size()){
                t = t + types.get(i);
            }else {
                t = t + types.get(i) + ",";
            }
        }
        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_OA_TYPES,t);

        ArrayList<String> laybels = tagAdapter.getSelects();
        String l = "";
        for (int i = 0;i<laybels.size();i++){
            if (i == laybels.size()){
                l = l + laybels.get(i);
            }else {
                l = l + laybels.get(i) + ",";
            }
        }
        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_OA_LAYBELS,l);
    }

    /**
     * 筛选回调
     */
    public interface Callback {

        void onFinish();
    }
}

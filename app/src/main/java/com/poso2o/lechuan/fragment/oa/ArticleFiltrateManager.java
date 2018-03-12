package com.poso2o.lechuan.fragment.oa;

import android.app.Activity;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.fragment.manage.BaseFragmentManager;

/**
 * 文章筛选模块管理
 *
 * Created by Jaydon on 2018/1/29.
 */
public class ArticleFiltrateManager extends BaseFragmentManager {

    private ArticleFiltrateFragment articleFiltrateFragment;

    public ArticleFiltrateManager(Activity activity) {
        super(activity);
        articleFiltrateFragment = new ArticleFiltrateFragment();
    }

    public void show(ArticleFiltrateFragment.Callback callback) {
        articleFiltrateFragment.setCallback(callback);
        show(articleFiltrateFragment, R.anim.right_show);
    }

    public void hide(boolean isCancel) {
        super.hide(R.anim.right_hide);
    }
}

package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;


/**
 *
 * Created by lenovo on 2016/12/21.
 */
public class GoodsDetailsImgsItemView extends BaseView {

    private ImageView goods_details_item_imgs;

    // 图片链接
    private String urlString;
    private View view;

    public GoodsDetailsImgsItemView(Context context, String urlString) {
        super(context);
        this.urlString = urlString;
    }

    @Override
    public View initGroupView() {
        return view = View.inflate(context, R.layout.view_goods_details_img_item, null);
    }

    @Override
    public void initView() {
        goods_details_item_imgs = (ImageView) view.findViewById(R.id.goods_details_item_imgs);
    }

    @Override
    public void initData() {
        Glide.with(context).load(urlString).into(goods_details_item_imgs);
    }
}
package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.tool.print.Print;

/**
 * Created by Administrator on 2018-03-24.
 */

public class ViewPagerImageView extends BaseView {
    private Context mContext;
    private View view;
    private ImageView imageView;

    public ViewPagerImageView(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.view_image, null);
        return view;
    }

    @Override
    public void initView() {
        imageView = (ImageView) view.findViewById(R.id.iv_dot3);
        Glide.with(mContext).load("http://pic.58pic.com/58pic/13/68/03/86S58PIC26b_1024.jpg").error(R.mipmap.logo_d).placeholder(R.mipmap.logo_d).into(imageView);

    }

    @Override
    public void initData() {

    }

    public void loadImage(String url) {
        Print.println("loadImage_url:" + url);
        Glide.with(mContext).load("http://pic.58pic.com/58pic/13/68/03/86S58PIC26b_1024.jpg").error(R.mipmap.logo_d).placeholder(R.mipmap.logo_d).into(imageView);
    }

}

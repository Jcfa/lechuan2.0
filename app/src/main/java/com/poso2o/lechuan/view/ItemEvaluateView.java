package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.util.TextUtil;

/**
 * Created by mr zhang on 2018/1/26.
 *
 * 微店订单——客户评价图片行（最多3张）
 */

public class ItemEvaluateView extends BaseView {

    private Context context;
    private View view;

    //第一张图片
    private ImageView evaluate_pic_one;
    //第二张图片
    private ImageView evaluate_pic_two;
    //第三张图片
    private ImageView evaluate_pic_three;

    //图片路径数据
    private String [] paths ;

    /**
     * 注意继承后 先走了初始化控件  data
     * @param context
     */
    public ItemEvaluateView(Context context,String[] paths) {
        super(context);
        this.context = context;
        this.paths = paths;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context,R.layout.item_evaluate_pic,null);
        return view;
    }

    @Override
    public void initView() {
        evaluate_pic_one = (ImageView) view.findViewById(R.id.evaluate_pic_one);
        evaluate_pic_two = (ImageView) view.findViewById(R.id.evaluate_pic_two);
        evaluate_pic_three = (ImageView) view.findViewById(R.id.evaluate_pic_three);
    }

    @Override
    public void initData() {
        for (int i = 0; i<paths.length; i++){
            if (TextUtil.isEmpty(paths[i]))return;
            int r = i%3;
            if (r == 0){
                Glide.with(context).load(paths[i]).placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into(evaluate_pic_one);
            }else if (r == 1){
                Glide.with(context).load(paths[i]).placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into(evaluate_pic_two);
            }else if (r == 2){
                Glide.with(context).load(paths[i]).placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into(evaluate_pic_three);
            }
        }
    }
}

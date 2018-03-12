package com.poso2o.lechuan.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.order.GoodsAppraiseDTO;
import com.poso2o.lechuan.layoutmanager.MyGridLayoutManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.view.ItemEvaluateView;

import java.util.ArrayList;

/**
 * 订单评论列表适配器
 * Created by Luo on 2017/02/10.
 */
public class OrderEvaluateAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    public ArrayList<GoodsAppraiseDTO> posterData;
    private RequestManager glideRequest;

    public OrderEvaluateAdapter(Context context, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.posterData = new ArrayList<>();
        if (posterData != null) {
            this.posterData.addAll(posterData);
        }
        glideRequest = Glide.with(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_order_evaluater_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return posterData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;

            if (!posterData.get(position).buyer_logo.isEmpty()){
                glideRequest.load("" + posterData.get(position).buyer_logo).transform(new GlideCircleTransform(context)).into(posterHolder.evaluater_item_logo);
            }else{
                posterHolder.evaluater_item_logo.setImageResource(R.mipmap.logo_c);
            }
            if (!posterData.get(position).buyer_nick.isEmpty()){
                posterHolder.evaluater_item_user_name.setText("" + posterData.get(position).buyer_nick);
            }else{
                posterHolder.evaluater_item_user_name.setText("用户名称");
            }

            String date = DateTimeUtil.LongToData(posterData.get(position).build_time, "yyyy-MM-dd HH:mm:ss");
            posterHolder.evaluater_item_dat.setText("" + date);

            if (posterData.get(position).appraise_grade == 1){//3=好，2=中，1=差
                posterHolder.evaluater_item_comment_lable.setText("差评");
                posterHolder.evaluater_item_comment_lable.setTextColor(Color.parseColor("#5E5E5E"));
                posterHolder.evaluater_item_comment_type.setImageResource(R.mipmap.comment_c);
            }else if (posterData.get(position).appraise_grade == 2){
                posterHolder.evaluater_item_comment_lable.setText("中评");
                posterHolder.evaluater_item_comment_lable.setTextColor(Color.parseColor("#FF6537"));
                posterHolder.evaluater_item_comment_type.setImageResource(R.mipmap.comment_b);
            }else if (posterData.get(position).appraise_grade == 3){
                posterHolder.evaluater_item_comment_lable.setText("好评");
                posterHolder.evaluater_item_comment_lable.setTextColor(Color.parseColor("#FF6537"));
                posterHolder.evaluater_item_comment_type.setImageResource(R.mipmap.comment_a);
            }else{
                posterHolder.evaluater_item_comment_lable.setText("好评");
                posterHolder.evaluater_item_comment_lable.setTextColor(Color.parseColor("#FF6537"));
                posterHolder.evaluater_item_comment_type.setImageResource(R.mipmap.comment_a);
            }

            posterHolder.evaluater_item_title.setText("" + posterData.get(position).appraise_remark);
            posterHolder.evaluater_item_goods_name.setText("" + posterData.get(position).goods_spec_name);
            posterHolder.evaluater_item_goods_price.setText("¥" + posterData.get(position).spec_price);

            glideRequest.load(posterData.get(position).main_picture).asBitmap().centerCrop().placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into(posterHolder.evaluater_item_goods_logo);

            posterHolder.evaluater_item_group.setTag(posterData.get(position));
            posterHolder.evaluater_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItem((GoodsAppraiseDTO)v.getTag());
                        return;
                    }
                }
            });

            posterHolder.evaluater_item_comment.setText("" + posterData.get(position).comment_number);
            posterHolder.evaluate_pic_container.removeAllViews();
            if (!posterData.get(position).appraise_imgs.isEmpty()){
                String[] appraise_imgs = posterData.get(position).appraise_imgs.split(",");
                ArrayList<String[]> datas = sortData(appraise_imgs);
                for (String[] s : datas){
                    ItemEvaluateView evaluateView = new ItemEvaluateView(context,s);
                    posterHolder.evaluate_pic_container.addView(evaluateView.getRootView());
                }
            }
        }
    }

    public void notifyData(ArrayList<GoodsAppraiseDTO> posterData) {
        this.posterData.clear();
        if (posterData != null) {
            this.posterData.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<GoodsAppraiseDTO> posterData) {
        if (posterData != null) {
            this.posterData.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(GoodsAppraiseDTO posterData);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View evaluater_item_group;
        ImageView evaluater_item_logo;
        TextView evaluater_item_user_name;
        TextView evaluater_item_dat;
        TextView evaluater_item_comment_lable;
        ImageView evaluater_item_comment_type;
        TextView evaluater_item_title;
        ImageView evaluater_item_goods_logo;
        TextView evaluater_item_goods_name;
        TextView evaluater_item_goods_price;
        TextView evaluater_item_comment;
        LinearLayout evaluate_pic_container;

        PosterHolder(View itemView) {
            super(itemView);
            evaluater_item_group = itemView.findViewById(R.id.evaluater_item_group);
            evaluater_item_logo = (ImageView) itemView.findViewById(R.id.evaluater_item_logo);
            evaluater_item_user_name = (TextView) itemView.findViewById(R.id.evaluater_item_user_name);
            evaluater_item_dat = (TextView) itemView.findViewById(R.id.evaluater_item_dat);
            evaluater_item_comment_lable = (TextView) itemView.findViewById(R.id.evaluater_item_comment_lable);
            evaluater_item_comment_type = (ImageView) itemView.findViewById(R.id.evaluater_item_comment_type);
            evaluater_item_title = (TextView) itemView.findViewById(R.id.evaluater_item_title);
            evaluater_item_goods_logo = (ImageView) itemView.findViewById(R.id.evaluater_item_goods_logo);
            evaluater_item_goods_name = (TextView) itemView.findViewById(R.id.evaluater_item_goods_name);
            evaluater_item_goods_price = (TextView) itemView.findViewById(R.id.evaluater_item_goods_price);
            evaluater_item_comment = (TextView) itemView.findViewById(R.id.evaluater_item_comment);
            evaluate_pic_container = (LinearLayout) itemView.findViewById(R.id.evaluate_pic_container);

        }
    }

    private ArrayList<String[]> sortData(String[] appraise_imgs){
        ArrayList<String[]> paths = new ArrayList<>();
        int p = appraise_imgs.length/3;
        int r = appraise_imgs.length%3;
        if (r>0)p = p+1;
        for (int i = 0;i<p;i++){
            String[] s = new String[3];
            paths.add(s);
        }
        for (int j = 0;j<appraise_imgs.length;j++){
            int p1 = j/3;
            int r1 = j%3;
            paths.get(p1)[r1] = appraise_imgs[j];
        }
        return paths;
    }
}

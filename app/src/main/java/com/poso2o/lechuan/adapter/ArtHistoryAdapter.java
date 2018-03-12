package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.articledata.AllArticleData;
import com.poso2o.lechuan.bean.articledata.ArticleData;
import com.poso2o.lechuan.bean.articledata.ArticleHistory;
import com.poso2o.lechuan.dialog.DelAhDialog;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.view.ArtHistoryItemView;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/10/27.
 */

public class ArtHistoryAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ArticleHistory> mDatas;

    public ArtHistoryAdapter(Context context) {
        this.context = context;
    }

    public void notifyDatas(ArrayList<ArticleHistory> articleHistories){
        mDatas = articleHistories;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_art_history,parent,false);
        return new ArtHistoryVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ArticleHistory articleHistory = mDatas.get(position);
        if (articleHistory == null)return;
        ArtHistoryVH vh = (ArtHistoryVH) holder;
        vh.art_history_time.setText(DateTimeUtil.LongToData(articleHistory.build_time,"yyyy-MM-dd HH:mm:ss"));
        vh.art_send_info.setText("成功发送 " + articleHistory.sendflag + "人");

        String arts = "{\"list\":" + articleHistory.articles + "}";
        AllArticleData allArts = new Gson().fromJson(arts,AllArticleData.class);
        if (allArts == null || allArts.list.size() == 0)return;
        ArticleData first = allArts.list.get(0);
        if (first.cover_pic_url != null && !first.cover_pic_url.equals(""))Glide.with(context).load(first.cover_pic_url).into(vh.art_history_first_pic);
        vh.art_history_first_title.setText(first.title);
        vh.art_history_first_read.setText("阅读 " + first.readnums);
        vh.art_history_first_love.setText("赞 " + first.collectnums);
        vh.history_del_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DelAhDialog delAhDialog = new DelAhDialog(context);
                delAhDialog.show();
                delAhDialog.setOnDelAhListener(new DelAhDialog.OnDelAhListener() {
                    @Override
                    public void delAh() {
                        if (onDelAhAllListener != null)onDelAhAllListener.onDelAh(articleHistory);
                    }
                });
            }
        });

        if (allArts.list.size() < 2)return;
        for (int i = 1; i<allArts.list.size();i++){
            ArtHistoryItemView itemView = new ArtHistoryItemView(context,allArts.list.get(i));
            vh.item_art_history_list.addView(itemView.getRootView());
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 0;
        return mDatas.size();
    }

    class ArtHistoryVH extends RecyclerView.ViewHolder{

        TextView art_history_time;
        ImageButton history_del_all;
        TextView art_send_info;
        RelativeLayout art_history_first;
        ImageView art_history_first_pic;
        TextView art_history_first_title;
        TextView art_history_first_read;
        TextView art_history_first_love;
        TextView art_history_first_del;
        LinearLayout item_art_history_list;

        public ArtHistoryVH(View itemView) {
            super(itemView);
            art_history_time = (TextView) itemView.findViewById(R.id.art_history_time);
            history_del_all = (ImageButton) itemView.findViewById(R.id.history_del_all);
            art_send_info = (TextView) itemView.findViewById(R.id.art_send_info);
            art_history_first = (RelativeLayout) itemView.findViewById(R.id.art_history_first);
            art_history_first_pic = (ImageView) itemView.findViewById(R.id.art_history_first_pic);
            art_history_first_title = (TextView) itemView.findViewById(R.id.art_history_first_title);
            art_history_first_read = (TextView) itemView.findViewById(R.id.art_history_first_read);
            art_history_first_love = (TextView) itemView.findViewById(R.id.art_history_first_love);
            art_history_first_del = (TextView) itemView.findViewById(R.id.art_history_first_del);
            item_art_history_list = (LinearLayout) itemView.findViewById(R.id.item_art_history_list);
        }
    }

    private OnDelAhAllListener onDelAhAllListener;
    public interface OnDelAhAllListener{
        void onDelAh(ArticleHistory articleHistory);
    }
    public void setOnDelAhAllListener(OnDelAhAllListener onDelAhAllListener){
        this.onDelAhAllListener = onDelAhAllListener;
    }
}

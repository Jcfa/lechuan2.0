package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/10/24.
 */

public class ArticleSearchHistoryAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<String> mHistorys;

    public ArticleSearchHistoryAdapter(Context context, ArrayList<String> mHistorys) {
        this.context = context;
        this.mHistorys = mHistorys;
    }

    public void notifyAdapter(ArrayList<String> mHistorys){
        this.mHistorys = mHistorys;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article_search,parent,false);
        return new ArtSearchVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String his = mHistorys.get(position);
        if (his == null)return;
        ArtSearchVH vh = (ArtSearchVH) holder;
        vh.article_search_history.setText(his);
        vh.article_search_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onHistoryListener != null)onHistoryListener.onHistoryClick(his);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mHistorys == null)return 0;
        return mHistorys.size();
    }

    class ArtSearchVH extends RecyclerView.ViewHolder{

        TextView article_search_history;

        public ArtSearchVH(View itemView) {
            super(itemView);
            article_search_history = (TextView) itemView.findViewById(R.id.article_search_history);
        }
    }

    private OnHistoryListener onHistoryListener;
    public interface OnHistoryListener{
        void onHistoryClick(String keyword);
    }
    public void setOnHistoryListener(OnHistoryListener onHistoryListener){
        this.onHistoryListener = onHistoryListener;
    }
}

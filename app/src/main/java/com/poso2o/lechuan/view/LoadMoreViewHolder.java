package com.poso2o.lechuan.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/8/15.
 * 加载更多
 */

public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

    public TextView load_tips;
    public ProgressBar load_progress;

    public LoadMoreViewHolder(View itemView) {
        super(itemView);
        load_tips = (TextView) itemView.findViewById(R.id.load_tips);
        load_progress = (ProgressBar) itemView.findViewById(R.id.load_progress);
    }
}

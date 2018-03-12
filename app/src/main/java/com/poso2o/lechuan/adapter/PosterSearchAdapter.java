package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.poster.MyFansDTO;
import com.poso2o.lechuan.bean.poster.PosterHistoryDTO;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

/**
 * 广告搜索历史适配器
 * Created by Luo on 2017/02/10.
 */
public class PosterSearchAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    public ArrayList<PosterHistoryDTO> fansBeen = new ArrayList<>();
    private String mKey = "";

    public PosterSearchAdapter(Context context, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.fansBeen = new ArrayList<>();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_poster_search_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return fansBeen.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;
//            posterHolder.poster_search_item_text.setText("" + fansBeen.get(position).keywords);
            setKeywordTextColor(posterHolder.poster_search_item_text, fansBeen.get(position).keywords);

            posterHolder.poster_search_item_group.setTag(fansBeen.get(position));
            posterHolder.poster_search_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickItem(0, (PosterHistoryDTO) (v.getTag()));
                    }
                }
            });

            posterHolder.poster_search_item_close.setTag(fansBeen.get(position));
            posterHolder.poster_search_item_close.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickItem(1, (PosterHistoryDTO) (v.getTag()));
                    }
                }
            });

        }
    }

    private void setKeywordTextColor(TextView textView, String content) {
        Print.println("setKeywordTextColor_content:"+content);
        if (!TextUtil.isEmpty(content)) {
            if (!TextUtil.isEmpty(mKey)) {
                int start = content.indexOf(mKey);
                if (start != -1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textView.setText(Html.fromHtml(content.substring(0, start) + "<font color='#F17604'>" + mKey + "</font>" + content.substring(start + mKey.length()), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        textView.setText(Html.fromHtml(content.substring(0, start) + "<font color='#F17604'>" + mKey + "</font>" + content.substring(start + mKey.length())));
                    }
                }
                return;
            }
            textView.setText(content);
        }
    }

    public void notifyData(ArrayList<PosterHistoryDTO> fansBeen, String key) {
        this.fansBeen.clear();
        this.mKey = key;
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<PosterHistoryDTO> posterData) {
        if (posterData != null) {
            this.fansBeen.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(int type, PosterHistoryDTO fansBeen);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {
        View poster_search_item_group;
        TextView poster_search_item_text;
        ImageView poster_search_item_close;

        PosterHolder(View itemView) {
            super(itemView);
            poster_search_item_group = itemView.findViewById(R.id.poster_search_item_group);
            poster_search_item_text = (TextView) itemView.findViewById(R.id.poster_search_item_text);
            poster_search_item_close = (ImageView) itemView.findViewById(R.id.poster_search_item_close);
        }
    }
}

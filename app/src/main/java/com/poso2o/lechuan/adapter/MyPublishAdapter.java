package com.poso2o.lechuan.adapter;

import android.content.Context;
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
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberUtils;

import java.util.ArrayList;

/**
 * 我的发布列表适配器
 * Created by Luo on 2017/02/10.
 */
public class MyPublishAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    private ArrayList<PosterDTO> fansBeen;
    private RequestManager glideRequest;

    public MyPublishAdapter(Context context, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.fansBeen = new ArrayList<>();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        glideRequest = Glide.with(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_my_publish_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return fansBeen.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;

            glideRequest.load(fansBeen.get(position).news_img).placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into(posterHolder.my_publish_item_img);

            posterHolder.my_publish_item_name.setText("" + fansBeen.get(position).news_title);
            posterHolder.my_publish_item_red_bag_amount.setText(NumberUtils.format2(fansBeen.get(position).red_envelopes_amount));
            posterHolder.my_publish_item_red_bag_amount_b.setText(fansBeen.get(position).getSurplusAmount());
            posterHolder.my_publish_item_commission_amount.setText(NumberUtils.format2(fansBeen.get(position).goods_commission_amount));

            posterHolder.my_publish_item_group.setTag(fansBeen.get(position));
            posterHolder.my_publish_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItem(0, (PosterDTO)v.getTag());
                    }
                }
            });
            posterHolder.my_publish_item_group.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemListener!=null){
                        onItemListener.onClickItem(1, (PosterDTO)view.getTag());
                    }
                    return false;
                }
            });
            posterHolder.my_publish_item_red_bag.setTag(fansBeen.get(position));
            posterHolder.my_publish_item_red_bag.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItemRedBag((PosterDTO)v.getTag());
                    }
                }
            });
            posterHolder.my_publish_item_commission.setTag(fansBeen.get(position));
            posterHolder.my_publish_item_commission.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickItemCommission((PosterDTO)v.getTag());
                    }
                }
            });
            posterHolder.my_publish_item_red_bag.setVisibility(fansBeen.get(position).has_red_envelopes==1?View.VISIBLE:View.INVISIBLE);//有红包的显示，没红包的不显示
            posterHolder.my_publish_item_commission.setVisibility(fansBeen.get(position).has_goods==1?View.VISIBLE:View.INVISIBLE);//有佣金的显示，没佣金的不显示
        }
    }

    public void notifyData(ArrayList<PosterDTO> fansBeen) {
        this.fansBeen.clear();
        if (fansBeen != null) {
            this.fansBeen.addAll(fansBeen);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<PosterDTO> posterData) {
        if (posterData != null) {
            this.fansBeen.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除tem
     */
    public void delItem(PosterDTO posterData) {
        if (fansBeen != null) {
            fansBeen.remove(posterData);
            notifyDataSetChanged();
        }
    }

    public interface OnItemListener {
        void onClickItem(int type, PosterDTO fansBeen);
        void onClickItemRedBag(PosterDTO fansBeen);
        void onClickItemCommission(PosterDTO fansBeen);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View my_publish_item_group;
        LinearLayout my_publish_item_red_bag;
        LinearLayout my_publish_item_commission;
        ImageView my_publish_item_img;
        TextView my_publish_item_name;
        TextView my_publish_item_red_bag_amount;
        TextView my_publish_item_red_bag_amount_b;
        TextView my_publish_item_commission_amount;

        PosterHolder(View itemView) {
            super(itemView);
            my_publish_item_group = itemView.findViewById(R.id.my_publish_item_group);
            my_publish_item_red_bag = (LinearLayout) itemView.findViewById(R.id.my_publish_item_red_bag);
            my_publish_item_commission = (LinearLayout) itemView.findViewById(R.id.my_publish_item_commission);

            my_publish_item_img = (ImageView) itemView.findViewById(R.id.my_publish_item_img);
            my_publish_item_name = (TextView) itemView.findViewById(R.id.my_publish_item_name);
            my_publish_item_red_bag_amount = (TextView) itemView.findViewById(R.id.my_publish_item_red_bag_amount);
            my_publish_item_red_bag_amount_b = (TextView) itemView.findViewById(R.id.my_publish_item_red_bag_amount_b);
            my_publish_item_commission_amount = (TextView) itemView.findViewById(R.id.my_publish_item_commission_amount);
        }
    }
}

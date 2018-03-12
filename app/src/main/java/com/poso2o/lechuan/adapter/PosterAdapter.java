package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.TextUtil;

import java.util.ArrayList;

/**
 * 广告列表适配器
 * Created by Luo on 2017/02/10.
 */
public class PosterAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    public ArrayList<PosterDTO> posterData;
    private RequestManager glideRequest;

    public PosterAdapter(Context context, OnItemListener onItemListener) {
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
        return new PosterHolder(LayoutInflater.from(context).inflate(R.layout.view_poster_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return posterData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PosterHolder) {
            PosterHolder posterHolder = (PosterHolder) holder;
            final PosterDTO posterDTO = posterData.get(position);
            if (!posterDTO.logo.isEmpty()) {
                glideRequest.load("" + posterDTO.logo).transform(new GlideCircleTransform(context)).into(posterHolder.poster_item_logo);
            } else {
                posterHolder.poster_item_logo.setImageResource(R.mipmap.logo_e);
            }
            if (!posterDTO.nick.isEmpty()) {
                posterHolder.poster_item_user_name.setText("" + posterDTO.nick);
            } else {
                posterHolder.poster_item_user_name.setText("用户名称");
            }
            //posterHolder.poster_item_type;0=商家,1=分销商
            if (posterDTO.user_type == 0) {
                //posterHolder.poster_item_type.setImageDrawable(context.getResources().getDrawable(R.mipmap.poster_seller_icon));
                posterHolder.poster_item_type.setVisibility(View.VISIBLE);
                posterHolder.poster_item_type.setImageResource(R.mipmap.poster_seller_icon);
                posterHolder.poster_item_type.setTag(R.mipmap.poster_seller_icon);
            } else if (posterDTO.user_type == 1) {
                posterHolder.poster_item_type.setVisibility(View.VISIBLE);
                posterHolder.poster_item_type.setImageResource(R.mipmap.poster_seller_icon_b);
                posterHolder.poster_item_type.setTag(R.mipmap.poster_seller_icon_b);
            } else {
                posterHolder.poster_item_type.setVisibility(View.GONE);
            }

//            Print.println("has_red_envelopes" + posterData.get(position).has_red_envelopes);
//            Print.println("has_myget_red_envelopes" + posterData.get(position).has_myget_red_envelopes);
            if (posterDTO.has_red_envelopes > 0) {//0=没有红包,1=有红包
                posterHolder.poster_item_reward_layout.setVisibility(View.VISIBLE);
                posterHolder.poster_item_reward_lable.setText("红包");
                posterHolder.poster_item_reward.setText("" + posterDTO.red_envelopes_surplus_number);
                if (posterDTO.red_envelopes_surplus_number > 0 && //红包剩余个数
                        posterDTO.has_myget_red_envelopes == 0) {//1=已抢红包,0=未抢红包
                    posterHolder.poster_item_reward.setTextColor(Color.parseColor("#FF6537"));
                    posterHolder.poster_item_reward_type.setImageResource(R.mipmap.ic_red_envelope_on);
                } else {
                    posterHolder.poster_item_reward.setTextColor(Color.parseColor("#5E5E5E"));
                    posterHolder.poster_item_reward_type.setImageResource(R.mipmap.ic_red_envelope_off);
                }
            } else {
                if (posterDTO.has_commission > 0) {//0=没有佣金,1=有佣金
                    posterHolder.poster_item_reward_layout.setVisibility(View.VISIBLE);
                    posterHolder.poster_item_reward_type.setImageResource(R.mipmap.poster_commission);
                    posterHolder.poster_item_reward_lable.setText("佣金");
                    posterHolder.poster_item_reward.setText("" + posterDTO.goods_commission_amount);
                } else {
                    posterHolder.poster_item_reward_layout.setVisibility(View.GONE);
                }
            }
            if (posterDTO.news_img != null && !TextUtil.isEmpty(posterDTO.news_img) || posterDTO.news_img.length() > 0) {
                Print.println("news_img="+posterDTO.news_img.toLowerCase());
                if (posterDTO.news_img.toLowerCase().contains(".gif")) {
                    glideRequest.load(posterDTO.news_img).placeholder(R.mipmap.logo_c).error(R.mipmap.logo_c).into(posterHolder.poster_item_img);
                } else {
                    glideRequest.load(posterDTO.news_img).asBitmap().centerCrop().thumbnail(0.1f).placeholder(R.mipmap.logo_c).error(R.mipmap.logo_c).into(posterHolder.poster_item_img);
                }
                posterHolder.poster_item_img.setVisibility(View.VISIBLE);
            } else {
                posterHolder.poster_item_img.setVisibility(View.GONE);
            }
            posterHolder.poster_item_title.setText(posterDTO.news_title);
            posterHolder.poster_item_content.setText(posterDTO.news_describe);//news_describe

            posterHolder.poster_item_browse.setText("" + posterDTO.read_num);
            posterHolder.poster_item_like.setText("" + posterDTO.like_num);
            posterHolder.poster_item_comment.setText("" + posterDTO.comment_num);

            posterHolder.poster_item_group.setTag(posterDTO);
            posterHolder.poster_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickItem(position, (PosterDTO) v.getTag());
                        return;
                    }
                }
            });
            posterHolder.poster_item_user_layout.setTag(posterDTO);
            posterHolder.poster_item_user_layout.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickUser(posterDTO);
                        return;
                    }
                }
            });
            posterHolder.poster_item_reward_layout.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickReward(position, posterDTO);
                        return;
                    }
                }
            });

            //浏览
            /*posterHolder.poster_item_browse_layout.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener!=null){
                        onItemListener.onClickBrowse(posterData.get(position));
                        return;
                    }
                }
            });*/
            //点赞
            if (posterDTO.has_like == 1) {
                posterHolder.poster_item_like_iv.setImageResource(R.mipmap.ic_praise_selected);
            } else {
                posterHolder.poster_item_like_iv.setImageResource(R.mipmap.ic_praise);
            }
            posterHolder.poster_item_like_layout.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickLike(posterDTO, position);
                        return;
                    }
                }
            });
            //评论
            posterHolder.poster_item_comment_layout.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickComment(position, posterDTO);
                        return;
                    }
                }
            });

        }
    }

    public void notifyData(ArrayList<PosterDTO> posterData) {
        this.posterData.clear();
        if (posterData != null) {
            this.posterData.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<PosterDTO> posterData) {
        if (posterData != null) {
            this.posterData.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onClickItem(int position, PosterDTO posterData);

        void onClickUser(PosterDTO posterData);

        void onClickReward(int position, PosterDTO posterData);

        void onClickBrowse(PosterDTO posterData);

        void onClickLike(PosterDTO posterData, int position);

        void onClickComment(int position, PosterDTO posterData);
    }

    public class PosterHolder extends RecyclerView.ViewHolder {

        View poster_item_group;

        ImageView poster_item_logo;
        TextView poster_item_user_name;
        ImageView poster_item_type;

        TextView poster_item_reward;
        ImageView poster_item_reward_type;
        TextView poster_item_reward_lable;

        TextView poster_item_title;
        TextView poster_item_content;

        TextView poster_item_browse;
        TextView poster_item_like;
        TextView poster_item_comment;

        ImageView poster_item_like_iv;

        ImageView poster_item_img;
        View poster_item_user_layout;
        View poster_item_reward_layout;
        View poster_item_browse_layout;
        View poster_item_like_layout;
        View poster_item_comment_layout;

        PosterHolder(View itemView) {
            super(itemView);
            poster_item_group = itemView.findViewById(R.id.poster_item_group);

            poster_item_logo = (ImageView) itemView.findViewById(R.id.poster_item_logo);
            poster_item_user_name = (TextView) itemView.findViewById(R.id.poster_item_user_name);
            poster_item_type = (ImageView) itemView.findViewById(R.id.poster_item_type);

            poster_item_reward = (TextView) itemView.findViewById(R.id.poster_item_reward);
            poster_item_reward_type = (ImageView) itemView.findViewById(R.id.poster_item_reward_type);
            poster_item_reward_lable = (TextView) itemView.findViewById(R.id.poster_item_reward_lable);

            poster_item_title = (TextView) itemView.findViewById(R.id.poster_item_title);
            poster_item_content = (TextView) itemView.findViewById(R.id.poster_item_content);

            poster_item_browse = (TextView) itemView.findViewById(R.id.poster_item_browse);
            poster_item_like = (TextView) itemView.findViewById(R.id.poster_item_like);
            poster_item_comment = (TextView) itemView.findViewById(R.id.poster_item_comment);

            poster_item_like_iv = (ImageView) itemView.findViewById(R.id.poster_item_like_iv);

            poster_item_img = (ImageView) itemView.findViewById(R.id.poster_item_img);
            poster_item_user_layout = itemView.findViewById(R.id.poster_item_user_layout);
            poster_item_reward_layout = itemView.findViewById(R.id.poster_item_reward_layout);
            poster_item_browse_layout = itemView.findViewById(R.id.poster_item_browse_layout);
            poster_item_like_layout = itemView.findViewById(R.id.poster_item_like_layout);
            poster_item_comment_layout = itemView.findViewById(R.id.poster_item_comment_layout);
        }
    }


}

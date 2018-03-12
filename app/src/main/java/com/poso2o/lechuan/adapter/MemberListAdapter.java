package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.member.Member;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.view.LoadMoreViewHolder;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/28.
 */

public class MemberListAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Member> mDatas;

    public MemberListAdapter(Context context) {
        this.context = context;
    }

    public void notifyAdapter(ArrayList<Member> members){
        mDatas = members;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.item_load_more,parent,false);
            return new LoadMoreViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_member,parent,false);
        return new MemberVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1 && onMemberClickListener != null){
            LoadMoreViewHolder vh = (LoadMoreViewHolder) holder;
            onMemberClickListener.onLoadMore(vh.load_tips,vh.load_progress);
        }else {
            final Member member = mDatas.get(position);
            if (member == null)return;
            MemberVH vh = (MemberVH) holder;
//            vh.member_name.setText(member.member_name);
//            vh.member_buy_count.setText(member.member_purchase_number + "");
//            vh.member_buy_amount.setText(NumberFormatUtils.format(member.member_consume));
//            vh.member_balance.setText(NumberFormatUtils.format(member.member_balance));
//            vh.member_integral.setText(member.member_integral + "");
            vh.member_name.setText(member.nick);
            vh.member_buy_count.setText(member.ordernum);
            vh.member_buy_amount.setText(NumberFormatUtils.format(member.orderamount));
            vh.member_balance.setText(NumberFormatUtils.format(member.amounts));
            vh.member_integral.setText(member.points);
            vh.member_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMemberClickListener != null)
                        onMemberClickListener.onMemberClick(member);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 1;
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || position == mDatas.size())return 1;
        return 0;
    }

    class MemberVH extends RecyclerView.ViewHolder{

        LinearLayout member_root;
        TextView member_name;
        TextView member_buy_count;
        TextView member_buy_amount;
        TextView member_balance;
        TextView member_integral;

        public MemberVH(View itemView) {
            super(itemView);
            member_root = (LinearLayout) itemView.findViewById(R.id.member_root);
            member_name = (TextView)itemView.findViewById(R.id.member_name);
            member_buy_count = (TextView) itemView.findViewById(R.id.member_buy_count);
            member_buy_amount = (TextView) itemView.findViewById(R.id.member_buy_amount);
            member_balance = (TextView) itemView.findViewById(R.id.member_balance);
            member_integral = (TextView) itemView.findViewById(R.id.member_integral);
        }
    }

    private OnMemberClickListener onMemberClickListener;
    public interface OnMemberClickListener{
        void onMemberClick(Member member);
        void onLoadMore(TextView textView, ProgressBar progressBar);
    }
    public void setOnMemberClickListener(OnMemberClickListener onMemberClickListener){
        this.onMemberClickListener = onMemberClickListener;
    }

}

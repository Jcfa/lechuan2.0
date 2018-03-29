package com.poso2o.lechuan.orderInfoAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.orderInfo.FidEventBus;
import com.poso2o.lechuan.bean.orderInfo.QueryDirBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/28 0028.
 */

public class DialogQueryDirAdapter extends RecyclerView.Adapter<DialogQueryDirAdapter.Vhloder> {
    private List<QueryDirBean.ListBean> data;

    /**
     * 初始化构造方法
     */
    public DialogQueryDirAdapter(List<QueryDirBean.ListBean> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @Override
    public DialogQueryDirAdapter.Vhloder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_query_sell_dir, parent, false);
        return new Vhloder(view);
    }

    @Override
    public void onBindViewHolder(DialogQueryDirAdapter.Vhloder holder, int position) {
        final QueryDirBean.ListBean bean = data.get(position);
        holder.tvQueryNameCount.setText(bean.getDirectory() + "(" + bean.getProductNum() + ")");
        /**
         * 此方法主要是做参数传递的  根据两边fid是否相同进行展示界面
         * */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FidEventBus(bean.getFid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Vhloder extends RecyclerView.ViewHolder {

        private final TextView tvQueryNameCount;

        public Vhloder(View itemView) {
            super(itemView);
            tvQueryNameCount = (TextView) itemView.findViewById(R.id.tv_sell_name_count);
        }
    }
}

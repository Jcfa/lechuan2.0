package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.wopenaccountdata.ServiceOrderingTrial;
import com.poso2o.lechuan.util.NumberUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/3/22.
 *
 * 公众号服务套餐适配器
 */

public class OaServiceAdapter extends BaseAdapter<OaServiceAdapter.ServiceVH, ServiceOrderingTrial> {

    private Context context;
    private int selected;

    public OaServiceAdapter(Context context, ArrayList<ServiceOrderingTrial> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    public void initItemView(ServiceVH holder, final ServiceOrderingTrial item, final int position) {
        holder.wopen_trial_gao.setText(item.service_name);
        holder.wopen_trial_money.setText(NumberUtils.format2(item.original_amount));
        holder.wopen_trial_quan.setText(item.remark.replace("\\n","\n"));

        //单选
        if (selected==position){
            holder.wopen_trial_one.setImageResource(R.drawable.checked_true);
        }else {
            holder.wopen_trial_one.setImageResource(R.drawable.checked_false);
        }
        holder.ll_wopen_trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemAddClick.onItemClick(position,item);
            }
        });
    }

    @Override
    public ServiceVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_service, parent, false);
        return new ServiceVH(view);
    }

    class ServiceVH extends BaseViewHolder {

        TextView wopen_trial_gao;
        TextView wopen_trial_money;
        TextView wopen_trial_quan;
        ImageView wopen_trial_one;
        View ll_wopen_trial;

        public ServiceVH(View itemView) {
            super(itemView);

            wopen_trial_gao = (TextView) itemView.findViewById(R.id.wopen_trial_gao);
            wopen_trial_money = (TextView) itemView.findViewById(R.id.wopen_trial_money);
            wopen_trial_quan = (TextView) itemView.findViewById(R.id.wopen_trial_quan);
            wopen_trial_one = (ImageView) itemView.findViewById(R.id.wopen_trial_one);
            ll_wopen_trial = itemView.findViewById(R.id.ll_wopen_trial);
        }
    }

    //回调接口
    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    private OnAddClickListener onItemAddClick;
    public interface OnAddClickListener {
        void onItemClick(int position,ServiceOrderingTrial item); //传递boolean类型数据给activity
    }
    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}

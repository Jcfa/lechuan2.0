package com.poso2o.lechuan.adapter.wopenaccountadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.wopenaccountdata.ServiceOrderingTrial;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15 0015.
 */

public class ServiceOrderingTrialAdapter extends BaseAdapter {

    private Context context;
    private List<ServiceOrderingTrial> data;

    public ServiceOrderingTrialAdapter(Context context, List<ServiceOrderingTrial> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wopen_trial, null);
            viewHolder.tv_wopen_trial_gao = (TextView) convertView.findViewById(R.id.tv_wopen_trial_gao);
            viewHolder.tv_wopen_trial_money = (TextView) convertView.findViewById(R.id.tv_wopen_trial_money);
            viewHolder.tv_wopen_trial_quan = (TextView) convertView.findViewById(R.id.tv_wopen_trial_quan);
            viewHolder.rb_wopen_trial_one = (RadioButton) convertView.findViewById(R.id.rb_wopen_trial_one);
            viewHolder.ll_wopen_trial = convertView.findViewById(R.id.ll_wopen_trial);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_wopen_trial_gao.setText(data.get(position).getService_name());
        viewHolder.tv_wopen_trial_money.setText(data.get(position).getAmount());
        viewHolder.tv_wopen_trial_quan.setText(data.get(position).getRemark().replace("\\n", "\n"));

        // 单选
        if (selected == position) {
            viewHolder.rb_wopen_trial_one.setChecked(true);
        } else {
            viewHolder.rb_wopen_trial_one.setChecked(false);
        }
        viewHolder.ll_wopen_trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemAddClick.onItemClick(position);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView tv_wopen_trial_gao, tv_wopen_trial_money, tv_wopen_trial_quan;
        private RadioButton rb_wopen_trial_one;
        private View ll_wopen_trial;
    }

    private int selected;


    //回调接口
    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public static interface OnAddClickListener {
        // true add; false cancel
        public void onItemClick(int position); //传递boolean类型数据给activity
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}

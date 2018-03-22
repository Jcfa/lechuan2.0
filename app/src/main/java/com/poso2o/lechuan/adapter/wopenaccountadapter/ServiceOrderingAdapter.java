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

public class ServiceOrderingAdapter extends BaseAdapter {

    private Context context;
    private List<ServiceOrderingTrial> data;

    public ServiceOrderingAdapter(Context context, List<ServiceOrderingTrial> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_service_ordering, null);
            viewHolder.service_ordering_gao = (TextView) convertView.findViewById(R.id.service_ordering_gao);
            viewHolder.service_ordering_money = (TextView) convertView.findViewById(R.id.service_ordering_money);
            viewHolder.service_ordering_quan = (TextView) convertView.findViewById(R.id.service_ordering_quan);
            viewHolder.service_ordering_one = (RadioButton) convertView.findViewById(R.id.service_ordering_one);
            viewHolder.service_ordering_group = convertView.findViewById(R.id.service_ordering_group);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.service_ordering_gao.setText(data.get(position).getService_name());
        viewHolder.service_ordering_money.setText(data.get(position).getAmount());
        viewHolder.service_ordering_quan.setText(data.get(position).getRemark().replace("\\n", "\n"));

        // 单选
        if (selected == position) {
            viewHolder.service_ordering_one.setChecked(true);
        } else {
            viewHolder.service_ordering_one.setChecked(false);
        }
        viewHolder.service_ordering_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemAddClick.onItemClick(position);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView service_ordering_gao, service_ordering_money, service_ordering_quan;
        private RadioButton service_ordering_one;
        private View service_ordering_group;
    }

    private int selected;

    /**
     * 回调接口
     */
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

    // add click callback在别人表明自己是某样东西的粉丝之后，还去当面diss这样东西，
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}

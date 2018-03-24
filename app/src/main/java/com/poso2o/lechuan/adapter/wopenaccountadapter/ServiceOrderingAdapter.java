package com.poso2o.lechuan.adapter.wopenaccountadapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.vdian.ServiceOrderingTrial;
import com.poso2o.lechuan.util.NumberUtils;

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
            viewHolder.service_ordering_one = (ImageView) convertView.findViewById(R.id.service_ordering_one);
            viewHolder.service_ordering_group = convertView.findViewById(R.id.service_ordering_group);
            viewHolder.service_ordering_renew = (LinearLayout) convertView.findViewById(R.id.service_ordering_renew);
            viewHolder.service_ordering_renew_1 = (TextView) convertView.findViewById(R.id.service_ordering_renew_1);
            viewHolder.service_ordering_renew_2 = (TextView) convertView.findViewById(R.id.service_ordering_renew_2);
            viewHolder.service_ordering_renew_3 = (TextView) convertView.findViewById(R.id.service_ordering_renew_3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ServiceOrderingTrial serviceOrderingTrial = data.get(position);
        viewHolder.service_ordering_gao.setText(serviceOrderingTrial.service_name);
        viewHolder.service_ordering_money.setText(NumberUtils.format2(serviceOrderingTrial.original_amount));
        viewHolder.service_ordering_quan.setText(serviceOrderingTrial.remark.replace("\\n", "\n"));
        // 单选
        if (selected == position) {
            viewHolder.service_ordering_money.setSelected(true);
            viewHolder.service_ordering_one.setSelected(true);
            viewHolder.service_ordering_renew.setVisibility(View.VISIBLE);
            setRenewText1(viewHolder.service_ordering_renew_1, serviceOrderingTrial);
            setRenewText2(viewHolder.service_ordering_renew_2, serviceOrderingTrial);
            setRenewText3(viewHolder.service_ordering_renew_3, serviceOrderingTrial);
        } else {
            viewHolder.service_ordering_money.setSelected(false);
            viewHolder.service_ordering_one.setSelected(false);
            viewHolder.service_ordering_renew.setVisibility(View.GONE);
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
        private ImageView service_ordering_one;
        private View service_ordering_group;
        private LinearLayout service_ordering_renew;
        private TextView service_ordering_renew_1;
        private TextView service_ordering_renew_2;
        private TextView service_ordering_renew_3;
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

    /**
     * 续费套餐现价
     */
    private void setRenewText1(TextView textView, ServiceOrderingTrial serviceOrderingTrial) {
        textView.setText("现套餐价：" + NumberUtils.format2(serviceOrderingTrial.original_amount) + "元");
    }

    /**
     * 折后余额
     */
    private void setRenewText2(TextView textView, ServiceOrderingTrial serviceOrderingTrial) {
        double balanceAmount = serviceOrderingTrial.original_amount - serviceOrderingTrial.deduction_amount;
        StringBuilder builder = new StringBuilder();
        builder.append("余额：" + NumberUtils.format2(balanceAmount));
        builder.append("元=原套餐 " + NumberUtils.format2(serviceOrderingTrial.original_amount) + "元/年");
        builder.append("-已用 " + NumberUtils.format2(serviceOrderingTrial.deduction_amount) + "元");
        textView.setText(builder.toString());
    }

    /**
     * 续费应补金额
     */
    private void setRenewText3(TextView textView, ServiceOrderingTrial serviceOrderingTrial) {
//        textView.setText("应补金额：" + NumberUtils.format2(serviceOrderingTrial.amount) + "元");
        Spannable span = new SpannableString("应补金额：" + NumberUtils.format2(serviceOrderingTrial.amount) + "元");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF6537"));
        span.setSpan(colorSpan, 5, span.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }
}

package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.GoodsNameActivity;
import com.poso2o.lechuan.bean.goodsdata.GoodsNameData;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/12/19.
 */
public class GoodsNameAdapter extends BaseAdapter<GoodsNameAdapter.GoodsNameHolder, GoodsNameData> {

    public GoodsNameAdapter(Context context, ArrayList<GoodsNameData> data) {
        super(context, data);
    }

    @Override
    public GoodsNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsNameHolder(getItemView(R.layout.recycle_write_goods_name_item, parent));
    }

    @Override
    public void initItemView(GoodsNameHolder holder, GoodsNameData item, int position) {
        if (((GoodsNameActivity) context).isEdit()) {
            holder.write_item_goods_name_tv.setText(Html.fromHtml(item.goodsNameLight));
        } else {
            holder.write_item_goods_name_tv.setText(item.goodsName);
        }

        holder.itemView.setTag(item.goodsName);
        holder.itemView.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                ((GoodsNameActivity) context).inPut(v.getTag().toString());
            }
        });
    }

    class GoodsNameHolder extends BaseViewHolder {
        public View itemView;
        public TextView write_item_goods_name_tv;
        public LinearLayout write_item_group;

        public GoodsNameHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            write_item_goods_name_tv = (TextView) itemView.findViewById(R.id.write_item_goods_name_tv);
            write_item_group = (LinearLayout) itemView.findViewById(R.id.write_item_group);
        }
    }
}

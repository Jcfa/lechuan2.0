package com.poso2o.lechuan.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.bean.goodsdata.OldSpec;
import com.poso2o.lechuan.util.NumberFormatUtils;


/**
 * Created by lenovo on 2016/12/21.
 */

public class NumsDataView extends BaseView {
    private int lineNumber;
    private GoodsSpec spec;
    private LinearLayout goods_details_nums_item_group;
    private TextView goods_details_nums_item_color_size;
    private TextView goods_details_nums_item_barcode;
    private TextView goods_details_nums_item_price;
    private TextView goods_details_nums_item_cost;
    private TextView goods_details_nums_item_salesnum;
    private TextView goods_details_nums_item_stock;
    private View view;

    // 实体店规格
    private OldSpec oldSpec;

    public NumsDataView(Context context, GoodsSpec spec, int lineNumber) {
        super(context);
        this.lineNumber = lineNumber;
        this.spec = spec;
    }

    public NumsDataView(Context context, OldSpec oldSpec, int lineNumber) {
        super(context);
        this.oldSpec = oldSpec;
        this.lineNumber = lineNumber;
    }

    @Override
    public View initGroupView() {
        return view = View.inflate(context, R.layout.view_nums_data, null);
    }

    @Override
    public void initView() {
        goods_details_nums_item_group = (LinearLayout) view.findViewById(R.id.goods_details_nums_item_group);
        goods_details_nums_item_color_size = (TextView) view.findViewById(R.id.goods_details_nums_item_color_size);
        goods_details_nums_item_barcode = (TextView) view.findViewById(R.id.goods_details_nums_item_barcode);
        goods_details_nums_item_price = (TextView) view.findViewById(R.id.goods_details_nums_item_price);
        goods_details_nums_item_cost = (TextView) view.findViewById(R.id.goods_details_nums_item_cost);
        goods_details_nums_item_salesnum = (TextView) view.findViewById(R.id.goods_details_nums_item_salesnum);
        goods_details_nums_item_stock = (TextView) view.findViewById(R.id.goods_details_nums_item_stock);
    }

    @Override
    public void initData() {
        if (lineNumber % 2 == 0) {
            goods_details_nums_item_group.setBackgroundColor(Color.parseColor("#ffffffff"));
        } else {
            goods_details_nums_item_group.setBackgroundColor(Color.parseColor("#ffffffff"));
        }

        if (spec != null) {
            goods_details_nums_item_color_size.setText(spec.goods_spec_name + "\n" + spec.spec_bar_code);
            goods_details_nums_item_price.setText(NumberFormatUtils.format(spec.spec_price));
            goods_details_nums_item_cost.setText(NumberFormatUtils.format(spec.spec_cost));
            goods_details_nums_item_salesnum.setText(Integer.toString(spec.spec_sale_number));
            goods_details_nums_item_stock.setText(Integer.toString(spec.spec_number));
        } else if (oldSpec != null) {
            goods_details_nums_item_price.setVisibility(View.GONE);
            goods_details_nums_item_cost.setVisibility(View.GONE);
            goods_details_nums_item_barcode.setVisibility(View.VISIBLE);
            goods_details_nums_item_color_size.setText(oldSpec.colorid + "/" + oldSpec.sizeid);
            goods_details_nums_item_barcode.setText(oldSpec.barcode);
            goods_details_nums_item_salesnum.setText(oldSpec.salesnum);
            goods_details_nums_item_stock.setText(oldSpec.num);
        }
    }
}

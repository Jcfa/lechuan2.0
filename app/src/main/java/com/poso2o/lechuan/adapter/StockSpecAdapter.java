package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.dialog.StockGoodsDetailDialog;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/4.
 */

public class StockSpecAdapter extends RecyclerView.Adapter {

    private Context context ;
    private ArrayList<GoodsSpec> mDatas;
    private StockGoodsDetailDialog stockGoodsDetailDialog;

    //总售出
    private int total_sale = 0;
    //总库存
    private int total_stock = 0;
    //是否刷新
    private boolean is_fresh = false;

    public StockSpecAdapter(Context context, ArrayList<GoodsSpec> specs,StockGoodsDetailDialog stockGoodsDetailDialog) {
        this.context = context;
        mDatas = specs;
        this.stockGoodsDetailDialog = stockGoodsDetailDialog;
        is_fresh = true;
        total_sale = 0;
        total_stock = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stock_spec_list,parent,false);
        return new StockSpecVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StockSpecVH vh = (StockSpecVH) holder;
        if (mDatas == null){
            vh.stock_spec_name.setText("合计");
            vh.stock_spec_sale.setText("0");
            vh.stock_spec_stock.setText("0");
            vh.stock_spec_cost.setText("0.00");
            vh.stock_spec_cost.setTextColor(0xFFFF6537);
        }else if (position == mDatas.size()){
            Goods goods = stockGoodsDetailDialog.getGoods();
            vh.stock_spec_name.setText("合计");
            vh.stock_spec_sale.setText(total_sale + "");
            vh.stock_spec_stock.setText(total_stock + "");
            if (goods.lists != null && goods.lists.size() > 0){
                //实体店数据
                vh.stock_spec_cost.setText(NumberFormatUtils.format(ArithmeticUtils.mul(goods.fprice,total_stock + "")));
            }else {
                //微店数据
                vh.stock_spec_cost.setText(NumberFormatUtils.format(goods.goods_cost_amount));
            }
            vh.stock_spec_cost.setTextColor(0xFFFF6537);
            is_fresh = false;
        }else {
            GoodsSpec spec = mDatas.get(position);
            if (spec == null)return;
            if (is_fresh){
                total_sale = total_sale + spec.spec_sale_number;
                total_stock = total_stock + spec.spec_number;
            }
            vh.stock_spec_name.setText(spec.goods_spec_name);
            vh.stock_spec_sale.setText(spec.spec_sale_number + "");
            vh.stock_spec_stock.setText(spec.spec_number + "");
            vh.stock_spec_cost.setText(NumberFormatUtils.format(ArithmeticUtils.mul(spec.spec_cost,spec.spec_number)));
            vh.stock_spec_cost.setTextColor(0xFF5E5E5e);
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas == null)return 1;
        return mDatas.size()+1;
    }

    class StockSpecVH extends RecyclerView.ViewHolder{

        LinearLayout item_stock_spec_root;
        TextView stock_spec_name;
        TextView stock_spec_sale;
        TextView stock_spec_stock;
        TextView stock_spec_cost;

        public StockSpecVH(View itemView) {
            super(itemView);
            item_stock_spec_root = (LinearLayout) itemView.findViewById(R.id.item_stock_spec_root);
            stock_spec_name = (TextView) itemView.findViewById(R.id.stock_spec_name);
            stock_spec_sale = (TextView) itemView.findViewById(R.id.stock_spec_sale);
            stock_spec_stock = (TextView) itemView.findViewById(R.id.stock_spec_stock);
            stock_spec_cost = (TextView) itemView.findViewById(R.id.stock_spec_cost);
        }
    }
}

package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.marketdata.DiscountGoodsData;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/11/7.
 */

public class MDGPromotionAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<DiscountGoodsData> discountGoodsDatas;

    public MDGPromotionAdapter(Context context) {
        this.context = context;
    }

    public void notifyData(ArrayList<DiscountGoodsData> discountGoodsDatas){
        this.discountGoodsDatas = discountGoodsDatas;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mdg_promotion,parent,false);
        return new MDGPVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DiscountGoodsData goodsData = discountGoodsDatas.get(position);
        if (goodsData == null)return;
        final MDGPVH vh = (MDGPVH) holder;
        if (goodsData.main_picture != null && !goodsData.main_picture.equals("")) Glide.with(context).load(goodsData.main_picture).into(vh.mdg_promotion_pic);
        vh.mdg_promotion_name.setText(goodsData.goods_name);
        vh.mdg_promotion_price.setText("¥ " + goodsData.goods_price_section);
        vh.mdg_promotion_discount_price.setHint(goodsData.goods_price_section);
        if (goodsData.goods_discount != null && !goodsData.goods_discount.equals(""))vh.mdg_promotion_discount.setText(goodsData.goods_discount);
        vh.mdg_promotion_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMDGPListener != null)onMDGPListener.onMDGPDel(goodsData);
            }
        });

        vh.mdg_promotion_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String str = vh.mdg_promotion_discount.getText().toString();
                if (str == null || str.equals("")){
                    str = "100";
                }else if (str.equals(".")){
                    str = "0";
                }else if (str.startsWith(".")){
                    str = str.substring(1,str.length());
                }else if (str.endsWith(".")){
                    str = str.substring(0,str.length()-1);
                }
                goodsData.goods_discount = str;
                double mix = ArithmeticUtils.div(ArithmeticUtils.mul(Double.parseDouble(goodsData.goods_price_section_mix),Double.parseDouble(str)),100);
                double max = ArithmeticUtils.div(ArithmeticUtils.mul(Double.parseDouble(goodsData.goods_price_section_max),Double.parseDouble(str)),100);
                if (mix == max){
                    goodsData.goods_price_section = NumberFormatUtils.format(max);
                }else {
                    goodsData.goods_price_section = NumberFormatUtils.format(mix) + "-" + NumberFormatUtils.format(max);
                }
                vh.mdg_promotion_discount_price.setText(goodsData.goods_price_section);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (discountGoodsDatas == null)return 0;
        return discountGoodsDatas.size();
    }

    class MDGPVH extends RecyclerView.ViewHolder{

        RelativeLayout mdg_promotion_root;
        ImageView mdg_promotion_pic;
        TextView mdg_promotion_name;
        TextView mdg_promotion_price;
        TextView mdg_promotion_discount_price;
        EditText mdg_promotion_discount;
        ImageButton mdg_promotion_del;

        public MDGPVH(View itemView) {
            super(itemView);
            mdg_promotion_root = (RelativeLayout) itemView.findViewById(R.id.mdg_promotion_root);
            mdg_promotion_pic = (ImageView) itemView.findViewById(R.id.mdg_promotion_pic);
            mdg_promotion_name = (TextView) itemView.findViewById(R.id.mdg_promotion_name);
            mdg_promotion_price = (TextView) itemView.findViewById(R.id.mdg_promotion_price);
            mdg_promotion_discount_price = (TextView) itemView.findViewById(R.id.mdg_promotion_discount_price);
            mdg_promotion_discount = (EditText) itemView.findViewById(R.id.mdg_promotion_discount);
            mdg_promotion_del = (ImageButton) itemView.findViewById(R.id.mdg_promotion_del);
        }
    }

    private OnMDGPListener onMDGPListener;
    public interface OnMDGPListener{
        void onMDGPDel(DiscountGoodsData discountGoodsData);
    }
    public void setOnMDGPListener(OnMDGPListener onMDGPListener){
        this.onMDGPListener = onMDGPListener;
    }
}

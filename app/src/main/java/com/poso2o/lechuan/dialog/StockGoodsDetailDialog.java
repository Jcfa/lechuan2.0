package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.StockSpecAdapter;
import com.poso2o.lechuan.bean.goodsdata.Goods;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.bean.goodsdata.OldSpec;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/4.
 * 库存商品详情弹窗
 */

public class StockGoodsDetailDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context context;

    //图片
    private ImageView goods_spec_picture;
    //名称
    private TextView goods_name;
    //货号
    private TextView goods_no;
    //价格
    private TextView goods_price;
    //成本
    private TextView goods_cost;
    //关闭
    private ImageView spec_detail_close;
    //列表
    private RecyclerView goods_spec_recycler;

    //列表适配器
    private StockSpecAdapter specAdapter;
    //商品信息
    private Goods mGoods;

    //是否微店
    private boolean is_online = false;

    public StockGoodsDetailDialog(Context context,Goods goods,boolean is_online) {
        super(context);
        this.context = context;
        this.is_online = is_online;
        mGoods = goods;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_stock_goods_detail, null);
        return view;
    }

    @Override
    public void initView() {
        goods_spec_picture = (ImageView) view.findViewById(R.id.goods_spec_picture);
        goods_name = (TextView) view.findViewById(R.id.goods_name);
        goods_no = (TextView) view.findViewById(R.id.goods_no);
        goods_price = (TextView) view.findViewById(R.id.goods_price);
        goods_cost = (TextView) view.findViewById(R.id.goods_cost);
        spec_detail_close = (ImageView) view.findViewById(R.id.spec_detail_close);
        goods_spec_recycler = (RecyclerView) view.findViewById(R.id.goods_spec_recycler);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM);
        setWindowDispalay(1.0f,0.7f);
        this.getWindow().setWindowAnimations(R.style.BottomInAnimation);

        if (mGoods == null)return;

        if (is_online){
            Glide.with(context).load(mGoods.main_picture).into(goods_spec_picture);
            goods_name.setText(mGoods.goods_name);
            goods_no.setText(mGoods.goods_no);
            goods_price.setText("¥" + mGoods.goods_price_section);
            goods_cost.setText("成本：" + mGoods.goods_cost_section);
        }else {
            Glide.with(context).load(mGoods.image222).into(goods_spec_picture);
            goods_name.setText(mGoods.name);
            goods_no.setText(mGoods.bh);
            goods_price.setText("¥" + mGoods.price);
            goods_cost.setText("成本：" + mGoods.fprice);
        }

        initAdapter();
    }

    @Override
    public void initListener() {
        spec_detail_close.setOnClickListener(this);
    }

    private void initAdapter() {

        if (!is_online){
            mGoods.goods_spec = new ArrayList<>();
            if (mGoods.lists == null || mGoods.lists.size() == 0){
                GoodsSpec goodsSpec = new GoodsSpec();
                goodsSpec.goods_spec_name = "无规格";
                goodsSpec.spec_number = Integer.parseInt(mGoods.kcnum);
                goodsSpec.spec_sale_number = Integer.parseInt(mGoods.salesnum);
                goodsSpec.spec_bar_code = "--";
                goodsSpec.goods_id = mGoods.guid;

                mGoods.goods_spec.add(goodsSpec);
            }else {
                for (OldSpec spec : mGoods.lists){
                    GoodsSpec goodsSpec = new GoodsSpec();
                    goodsSpec.goods_spec_name = spec.colorid + "/" + spec.sizeid;
                    goodsSpec.spec_number = Integer.parseInt(spec.num);
                    goodsSpec.spec_sale_number = Integer.parseInt(spec.sales_num);
                    goodsSpec.spec_bar_code = spec.barcode;
                    goodsSpec.goods_id = spec.guid;
                    goodsSpec.spec_cost = Double.parseDouble(mGoods.fprice);

                    mGoods.goods_spec.add(goodsSpec);
                }
            }
        }

        specAdapter = new StockSpecAdapter(context,mGoods.goods_spec,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        goods_spec_recycler.setLayoutManager(linearLayoutManager);
        goods_spec_recycler.setAdapter(specAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.spec_detail_close:
                dismiss();
                break;
        }
    }

    public Goods getGoods(){
        return mGoods;
    }
}

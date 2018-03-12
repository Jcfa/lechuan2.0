package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.HotSaleProduct;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;

/**
 * Created by mr zhang on 2017/8/3.
 */

public class HotSaleDetailDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context content;

    //商品图片
    private ImageView hot_sale_detail_picture;
    //商品名称
    private TextView hot_sale_detail_name;
    //商品价格
    private TextView hot_sale_detail_price;
    //商品成本价
    private TextView hot_sale_detail_cost;
    //商品货号
    private TextView hot_sale_goods_no;
    //商品规格
    private TextView hot_sale_goods_spec;
    //销售数量
    private TextView goods_sale_number;
    //销售金额
    private TextView goods_sale_amount;
    //成本金额
    private TextView goods_cost;
    //利润
    private TextView goods_gain;
    //库存
    private TextView goods_number;
    //弹窗关闭
    private ImageView hot_sale_detail_close;

    public HotSaleDetailDialog(Context context) {
        super(context);
        this.content = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(content, R.layout.dialog_hot_sale_detail,null);
        return view;
    }

    @Override
    public void initView() {

        hot_sale_detail_picture = (ImageView) view.findViewById(R.id.hot_sale_detail_picture);

        hot_sale_detail_name = (TextView) view.findViewById(R.id.hot_sale_detail_name);

        hot_sale_detail_price = (TextView) view.findViewById(R.id.hot_sale_detail_price);

        hot_sale_detail_cost = (TextView) view.findViewById(R.id.hot_sale_detail_cost);

        hot_sale_goods_no = (TextView) view.findViewById(R.id.hot_sale_goods_no);

        hot_sale_goods_spec = (TextView) view.findViewById(R.id.hot_sale_goods_spec);

        goods_sale_number = (TextView) view.findViewById(R.id.goods_sale_number);

        goods_sale_amount = (TextView) view.findViewById(R.id.goods_sale_amount);

        goods_cost = (TextView) view.findViewById(R.id.goods_cost);

        goods_gain = (TextView) view.findViewById(R.id.goods_gain);

        goods_number = (TextView) view.findViewById(R.id.goods_number);

        hot_sale_detail_close = (ImageView) view.findViewById(R.id.hot_sale_detail_close);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM);
        setWindowDispalay(1.0f,0.7f);
        this.getWindow().setWindowAnimations(R.style.BottomInAnimation);
    }

    @Override
    public void initListener() {
        hot_sale_detail_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.hot_sale_detail_close:
                dismiss();
                break;
        }
    }

    public void setData(HotSaleProduct hotSaleProduct,boolean isOnline){
        if (isOnline){
            if (hotSaleProduct.main_picture != null && !hotSaleProduct.main_picture.equals(""))
                Glide.with(content).load(hotSaleProduct.main_picture).placeholder(R.mipmap.background_image).into(hot_sale_detail_picture);
            hot_sale_detail_name.setText(hotSaleProduct.goods_name);
            hot_sale_detail_price.setText("¥" + NumberFormatUtils.format(hotSaleProduct.spec_price) + "/" + hotSaleProduct.goods_unit);
            hot_sale_detail_cost.setText("成本：" + NumberFormatUtils.format(hotSaleProduct.spec_cost));
            hot_sale_goods_no.setText(hotSaleProduct.goods_no);
            hot_sale_goods_spec.setText(hotSaleProduct.goods_spec_name + "");
            goods_sale_number.setText(hotSaleProduct.sale_munber + "");
            goods_sale_amount.setText(NumberFormatUtils.format(hotSaleProduct.sale_amount));
            goods_cost.setText(NumberFormatUtils.format(hotSaleProduct.cost_amount));
            goods_gain.setText(NumberFormatUtils.format(hotSaleProduct.profit_amount));
            goods_number.setText(hotSaleProduct.spec_stock_munber + "");
        }else {
            if (hotSaleProduct.image222 != null && !hotSaleProduct.image222.equals(""))
                Glide.with(content).load(hotSaleProduct.image222).placeholder(R.mipmap.background_image).into(hot_sale_detail_picture);
            hot_sale_detail_name.setText(hotSaleProduct.name);
            hot_sale_detail_price.setText("¥" + NumberFormatUtils.format(hotSaleProduct.price) + "/件");
            hot_sale_detail_cost.setText("成本：" + NumberFormatUtils.format(hotSaleProduct.fprice));
            hot_sale_goods_no.setText(hotSaleProduct.bh);
            hot_sale_goods_spec.setText(hotSaleProduct.colorid + "/" + hotSaleProduct.sizeid);
            goods_sale_number.setText(hotSaleProduct.total_num);
            goods_sale_amount.setText(NumberFormatUtils.format(hotSaleProduct.total_amount));
            goods_cost.setText(NumberFormatUtils.format(ArithmeticUtils.mul(hotSaleProduct.fprice,hotSaleProduct.total_num)));
            findViewById(R.id.layout_profit).setVisibility(View.GONE);
            goods_number.setText(hotSaleProduct.kcnum);
        }
    }
}

package com.poso2o.lechuan.activity.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.order.OrderGoodsDTO;
import com.poso2o.lechuan.dialog.OrderModifyFreightDialog;
import com.poso2o.lechuan.dialog.OrderModifyPriceDialog;
import com.poso2o.lechuan.dialog.OrderModifyTotalDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * 修改订单价格页面
 */
public class OrderPriceActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    private RequestManager glideRequest;
    //订单数据
    private OrderDTO mOrderDTO;
    //返回
    private ImageView order_price_back;
    //一键改价、确定
    private TextView order_price_change_price_btn,order_price_confirm_btn;
    private TextView order_price_no,order_price_dat,order_price_receipts,order_item_address;
    private LinearLayout order_price_item_groups;
    private TextView order_price_freight;
    private CheckBox order_price_freight_free,order_price_freight_other;
    //修改售价
    private OrderModifyPriceDialog mOrderModifyPriceDialog;
    //修改运费窗口
    private OrderModifyFreightDialog mOrderModifyFreightDialog;
    //修改一键改价窗口
    private OrderModifyTotalDialog mOrderModifyTotalDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_price;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        order_price_back = (ImageView) findViewById(R.id.order_price_back);
        //一键改价、确定
        order_price_change_price_btn = (TextView) findViewById(R.id.order_price_change_price_btn);
        order_price_confirm_btn = (TextView) findViewById(R.id.order_price_confirm_btn);

        order_price_no = (TextView) findViewById(R.id.order_price_no);
        order_price_dat = (TextView) findViewById(R.id.order_price_dat);
        order_price_receipts = (TextView) findViewById(R.id.order_price_receipts);
        order_item_address = (TextView) findViewById(R.id.order_item_address);

        order_price_item_groups = (LinearLayout) findViewById(R.id.order_price_item_groups);

        order_price_freight = (TextView) findViewById(R.id.order_price_freight);
        order_price_freight_free = (CheckBox) findViewById(R.id.order_price_freight_free);
        order_price_freight_other = (CheckBox) findViewById(R.id.order_price_freight_other);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mOrderDataManager = OrderDataManager.getOrderDataManager();
        glideRequest = Glide.with(activity);
        mOrderModifyPriceDialog = new OrderModifyPriceDialog(activity);
        mOrderModifyFreightDialog = new OrderModifyFreightDialog(activity);
        mOrderModifyTotalDialog = new OrderModifyTotalDialog(activity);

        // 上一级页面传来的数据
        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("orderDTOs");

        showDatas();

    }

    /**
     * 设置商品数据
     */
    private void showDatas(){

        order_price_no.setText("" + mOrderDTO.order_id);
        String date = DateTimeUtil.LongToData(mOrderDTO.build_time, "yyyy-MM-dd HH:mm:ss");
        order_price_dat.setText("" + date);
        order_price_receipts.setText("" + NumberFormatUtils.format(mOrderDTO.order_payable_amount));
        order_item_address.setText("" + mOrderDTO.receipt_province_name + mOrderDTO.receipt_city_name +
                mOrderDTO.receipt_area_name + mOrderDTO.receipt_address);

        if (mOrderDTO.freight == 0){
            order_price_freight_free.setChecked(true);
            order_price_freight_other.setChecked(false);
            order_price_freight.setText("");
        }else{
            order_price_freight.setText("" + mOrderDTO.freight);
            order_price_freight_free.setChecked(false);
            order_price_freight_other.setChecked(true);
        }

        // 清空列表布局
        order_price_item_groups.removeAllViews();
        // 数据不为空，填充View到列表
        if (ListUtils.isNotEmpty(mOrderDTO.order_goods)) {
            // 第二种列表项的差异化可以在这里面完成，选择性的隐藏一些布局

            for (int i = 0; i < mOrderDTO.order_goods.size(); i++) {
                GoodsItemView itemView = new GoodsItemView(activity);

                // 将View添加到列表布局里面
                order_price_item_groups.addView(itemView.getRootView());
                if(i%2==0){
                    itemView.order_goods_item_group.setBackgroundColor(Color.parseColor("#FCFCFC"));
                }else{
                    itemView.order_goods_item_group.setBackgroundColor(Color.parseColor("#00000000"));
                }
                itemView.order_goods_item_name.setText("" + mOrderDTO.order_goods.get(i).goods_name);
                itemView.order_goods_item_size.setText("" + mOrderDTO.order_goods.get(i).goods_spec_name);
                itemView.order_goods_item_colour.setText("" + mOrderDTO.order_goods.get(i).goods_spec_name);
                itemView.order_goods_item_price.setText("¥" + NumberFormatUtils.format(mOrderDTO.order_goods.get(i).goods_amount));
                itemView.order_goods_item_old_price.setText("¥" + NumberFormatUtils.format(mOrderDTO.order_goods.get(i).spec_price * mOrderDTO.order_goods.get(i).purchase_munber));
                itemView.order_goods_item_num.setText("X" + mOrderDTO.order_goods.get(i).purchase_munber);

                itemView.order_item_price_modify_btn.setTag(mOrderDTO.order_goods.get(i));
                itemView.order_item_price.setText("¥" + NumberFormatUtils.format(mOrderDTO.order_goods.get(i).goods_amount));
                itemView.order_goods_item_old_price_b.setText("¥" + NumberFormatUtils.format(mOrderDTO.order_goods.get(i).spec_price * mOrderDTO.order_goods.get(i).purchase_munber));


                // 设置图片
                if (!mOrderDTO.order_goods.get(i).main_picture.isEmpty()) {
                    glideRequest.load(mOrderDTO.order_goods.get(i).main_picture).asBitmap().centerCrop().placeholder(R.mipmap.logo_c).error(R.mipmap.logo_c).into(itemView.order_goods_item_img);
                } else {
                    itemView.order_goods_item_img.setImageResource(R.mipmap.logo_e);
                }

            }
        }

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        order_price_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finishActivityX();
            }
        });
        //一键改价
        order_price_change_price_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mOrderModifyTotalDialog != null){
                    mOrderModifyTotalDialog.show(mOrderDTO, new OrderModifyTotalDialog.OnModifyPriceListener() {
                        @Override
                        public void onConfirm(String price, String discount) {
                            editOrderAmount(price,discount);
                        }
                    });
                }
            }
        });
        //确定
        order_price_confirm_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finishActivityX();
            }
        });
        //运费
        order_price_freight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showOrderModifyFreightDialog();
            }
        });
        order_price_freight_free.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                editFreight("0");
                order_price_freight_other.setChecked(false);
            }
        });
        order_price_freight_other.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showOrderModifyFreightDialog();
            }
        });
    }

    public class GoodsItemView extends BaseView {
        View itemView;
        View order_goods_item_group;
        ImageView order_goods_item_img;
        TextView order_goods_item_name;
        TextView order_goods_item_size;
        TextView order_goods_item_colour;
        TextView order_goods_item_price;
        TextView order_goods_item_old_price;
        TextView order_goods_item_num;
        LinearLayout order_item_price_modify_btn;
        TextView order_item_price;
        TextView order_goods_item_old_price_b;

        public GoodsItemView(Context context) {
            super(context);
        }

        @Override
        public View initGroupView() {
            itemView = View.inflate(context, R.layout.view_order_price_goods_item, null);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return itemView;
        }

        @Override
        public void initView() {
            order_goods_item_group = itemView.findViewById(R.id.order_price_goods_item_group);
            order_goods_item_img = (ImageView) itemView.findViewById(R.id.order_goods_item_img);
            order_goods_item_name = (TextView) itemView.findViewById(R.id.order_goods_item_name);
            order_goods_item_size = (TextView) itemView.findViewById(R.id.order_goods_item_size);
            order_goods_item_colour = (TextView) itemView.findViewById(R.id.order_goods_item_colour);
            order_goods_item_price = (TextView) itemView.findViewById(R.id.order_goods_item_price);
            order_goods_item_old_price = (TextView) itemView.findViewById(R.id.order_goods_item_old_price);
            order_goods_item_num = (TextView) itemView.findViewById(R.id.order_goods_item_num);
            order_item_price_modify_btn = (LinearLayout) itemView.findViewById(R.id.order_item_price_modify_btn);
            order_item_price = (TextView) itemView.findViewById(R.id.order_item_price);
            order_goods_item_old_price_b = (TextView) itemView.findViewById(R.id.order_goods_item_old_price_b);
        }

        @Override
        public void initData() {
            //添加删除线
            order_goods_item_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            order_goods_item_old_price_b.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        @Override
        public void initListener() {
            order_item_price_modify_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (mOrderModifyPriceDialog != null){
                        OrderGoodsDTO orderGoodsDTO = (OrderGoodsDTO) v.getTag();
                        mOrderModifyPriceDialog.show(orderGoodsDTO, new OrderModifyPriceDialog.OnModifyPriceListener() {
                            @Override
                            public void onConfirm(int serial, String price, String discount) {
                                editGoodsPrice (serial, price, discount);
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * 修改订单商品价格
     */
    private void editGoodsPrice (int serial, String price, String discount){
        showLoading();

        /** @param order_id                订单号（必填）
        * @param serial                  订单商品序号
        * @param goods_discount          商品折扣
        * @param goods_discount_price  商品折扣单价*/

        mOrderDataManager.editGoodsPrice(activity, "" + mOrderDTO.order_id, "" + serial, discount, price, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                dismissLoading();
                mOrderDTO = orderDTO;
                Toast.show(activity, " 修改成功");
                showDatas();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
        });
    }

    /**
     * 一键改价
     */
    private void editOrderAmount (String price, String discount){
        showLoading();

        mOrderDataManager.editOrderAmount(activity, "" + mOrderDTO.order_id, "" + discount, price, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                dismissLoading();
                Toast.show(activity, " 修改成功");
                mOrderDTO = orderDTO;
                showDatas();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
        });
    }

    /**
     * 打开修改订单运费窗口
     */
    private void showOrderModifyFreightDialog(){
        mOrderModifyFreightDialog.show(order_price_freight.getText().toString(),new OrderModifyFreightDialog.OnModifyFreightListener() {
            @Override
            public void onConfirm(String freight) {
                editFreight(freight);
            }

            @Override
            public void onCancel(String freight) {
                if (mOrderDTO.freight == 0){
                    order_price_freight_free.setChecked(true);
                    order_price_freight_other.setChecked(false);
                }else{
                    order_price_freight.setText("" + mOrderDTO.freight);
                    order_price_freight_free.setChecked(false);
                    order_price_freight_other.setChecked(true);
                }
            }
        });
    }

    /**
     * 修改订单运费
     */
    private void editFreight (String price){
        showLoading();

        mOrderDataManager.editFreight(activity, "" + mOrderDTO.order_id, price, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                dismissLoading();
                Toast.show(activity, " 修改成功");
                mOrderDTO = orderDTO;
                showDatas();
                mOrderModifyFreightDialog.dismiss();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }
        });
    }

    private void finishActivityX() {
        //添加给第一个Activity的返回值，并设置resultCode
        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        //bundle.putString("news_id",posterData.news_id.toString());
        bundle.putSerializable("orderDTOs",mOrderDTO);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("按下了back键   onKeyDown()");
            finishActivityX();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

}

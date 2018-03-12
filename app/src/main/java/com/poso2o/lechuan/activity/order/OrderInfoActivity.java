package com.poso2o.lechuan.activity.order;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.printer.PrinterBean;
import com.poso2o.lechuan.dialog.AgreeRefundDialog;
import com.poso2o.lechuan.dialog.CloseOrderDialog;
import com.poso2o.lechuan.dialog.ContactBuyerDialog;
import com.poso2o.lechuan.dialog.RefuseRefundDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.OrderDataManager;
import com.poso2o.lechuan.printer.PrinterBluetoothActivity;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 订单详情页面
 */
public class OrderInfoActivity extends BaseActivity {
    //网络管理
    private OrderDataManager mOrderDataManager;
    private RequestManager glideRequest;

    //订单数据
    private OrderDTO mOrderDTO;
    private String mOrderFlag = "-";

    //返回
    private ImageView order_info_back;
    private TextView order_info_title;

    private TextView order_list_no;
    private TextView order_list_dat;
    private TextView order_item_total_num;
    private TextView order_item_total_amount;
    private TextView order_item_freight;
    private TextView order_item_state;

    private TextView order_item_address;

    private TextView order_list_contact_buyer_btn;
    private TextView order_list_close_btn;
    private TextView order_list_change_price_btn;

    private TextView order_list_print_btn;
    private TextView order_list_deliver_goods_btn;

    private TextView order_list_agree_refund_btn;
    private TextView order_list_refuse_refund_btn;

    private LinearLayout order_item_groups;

    private LinearLayout order_item_address_layout;
    private ImageView order_item_address_iv;

    private LinearLayout order_list_remark_buyer_layout;//买家备注
    private TextView order_list_buyer_remark;

    private LinearLayout order_list_remark_layout;//卖家备注
    private TextView order_list_remark;

    private LinearLayout order_list_logistics_layout;//物流
    private TextView order_list_logistics_name;
    private TextView order_list_logistics_info;

    private static final int REQUEST_CODE = 11;
    private static final int DELIVERY_REQUEST_CODE = 22;
    private static final int REMARK_REQUEST_CODE = 33;
    private static final int LOGISTICS_REQUEST_CODE = 44;
    private static final int BATCH_DELIVERY_REQUEST_CODE = 55;
    private static final int ADDRESS_REQUEST_CODE = 66;
    private static final int ORDER_PRICE_REQUEST_CODE = 88;
    private static final int PRINT_REQUEST_CODE = 99;

    private final int CALL_PHONE = 1001;
    private final int SEND_SMS = 1002;
    private String contact_buyer_phone;

    //联系买家窗口
    private ContactBuyerDialog mContactBuyerDialog;
    //关闭订单窗口
    private CloseOrderDialog mCloseOrderDialog;
    //同意退款窗口
    private AgreeRefundDialog mAgreeRefundDialog;
    //拒绝退款窗口
    private RefuseRefundDialog mRefuseRefundDialog;

    //退款
    private LinearLayout order_list_refund_layout,order_list_refund_layout_a,order_list_refund_layout_b;
    private TextView order_list_refund_type,order_list_refund_reason,order_list_refund_amount,order_list_refund_dat,
            order_list_refund_after_lable,order_list_refund_after_text;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_info;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        order_info_back = (ImageView) findViewById(R.id.order_info_back);

        order_info_title = (TextView) findViewById(R.id.order_info_title);

        order_list_no = (TextView) findViewById(R.id.order_list_no);
        order_list_dat = (TextView) findViewById(R.id.order_list_dat);
        order_item_total_num = (TextView) findViewById(R.id.order_item_total_num);
        order_item_total_amount = (TextView) findViewById(R.id.order_item_total_amount);
        order_item_freight = (TextView) findViewById(R.id.order_item_freight);
        order_item_state = (TextView) findViewById(R.id.order_item_state);

        order_item_address = (TextView) findViewById(R.id.order_item_address);

        order_list_contact_buyer_btn = (TextView) findViewById(R.id.order_list_contact_buyer_btn);
        order_list_close_btn = (TextView) findViewById(R.id.order_list_close_btn);
        order_list_change_price_btn = (TextView) findViewById(R.id.order_list_change_price_btn);

        order_list_print_btn = (TextView) findViewById(R.id.order_list_print_btn);
        order_list_deliver_goods_btn = (TextView) findViewById(R.id.order_list_deliver_goods_btn);

        order_list_agree_refund_btn = (TextView) findViewById(R.id.order_list_agree_refund_btn);
        order_list_refuse_refund_btn = (TextView) findViewById(R.id.order_list_refuse_refund_btn);

        order_item_groups = (LinearLayout) findViewById(R.id.order_item_groups);

        order_item_address_layout = (LinearLayout) findViewById(R.id.order_item_address_layout);
        order_item_address_iv = (ImageView) findViewById(R.id.order_item_address_iv);

        order_list_remark_layout = (LinearLayout) findViewById(R.id.order_list_remark_layout);
        order_list_remark = (TextView) findViewById(R.id.order_list_remark);

        order_list_remark_buyer_layout = (LinearLayout) findViewById(R.id.order_list_remark_buyer_layout);
        order_list_buyer_remark = (TextView) findViewById(R.id.order_list_buyer_remark);

        order_list_logistics_layout = (LinearLayout) findViewById(R.id.order_list_logistics_layout);
        order_list_logistics_name = (TextView) findViewById(R.id.order_list_logistics_name);
        order_list_logistics_info = (TextView) findViewById(R.id.order_list_logistics_info);

        //退款
        order_list_refund_layout = (LinearLayout) findViewById(R.id.order_list_refund_layout);
        order_list_refund_layout_a = (LinearLayout) findViewById(R.id.order_list_refund_layout_a);
        order_list_refund_layout_b = (LinearLayout) findViewById(R.id.order_list_refund_layout_b);
        order_list_refund_type = (TextView) findViewById(R.id.order_list_refund_type);
        order_list_refund_reason = (TextView) findViewById(R.id.order_list_refund_reason);
        order_list_refund_amount = (TextView) findViewById(R.id.order_list_refund_amount);
        order_list_refund_dat = (TextView) findViewById(R.id.order_list_refund_dat);
        order_list_refund_after_lable = (TextView) findViewById(R.id.order_list_refund_after_lable);
        order_list_refund_after_text = (TextView) findViewById(R.id.order_list_refund_after_text);

    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        mOrderDataManager = OrderDataManager.getOrderDataManager();
        glideRequest = Glide.with(activity);

        // 上一级页面传来的数据
        mOrderDTO = (OrderDTO) getIntent().getSerializableExtra("orderDTOs");

        if (mOrderDTO.order_state == 4){
            order_info_title.setText("退款详情");
            if (mOrderDTO.orderRefund.refund_code == 0){
                order_list_refund_type.setText("不需要退货");//退款类型
            }else{
                order_list_refund_type.setText("需要退货");
            }
            order_list_refund_reason.setText("" + mOrderDTO.orderRefund.refund_remark);//退款原因
            order_list_refund_amount.setText("" + mOrderDTO.orderRefund.refund_amount);//实退金额
            String date = DateTimeUtil.LongToData(mOrderDTO.orderRefund.refund_time, "yyyy-MM-dd HH:mm:ss");
            order_list_refund_dat.setText("" + date);
            order_list_refund_after_text.setText("" + mOrderDTO.orderRefund.reply_remark);

            if (mOrderDTO.order_refund_state == 2){
                order_list_refund_after_lable.setText("拒绝退款：");
            }else if (mOrderDTO.order_refund_state == 3){
                order_list_refund_after_lable.setText("同意退款");
                order_list_refund_after_text.setVisibility(View.GONE);
            }

        }else{
            order_list_refund_layout.setVisibility(View.GONE);
        }

        //联系买家窗口
        mContactBuyerDialog = new ContactBuyerDialog(activity);
        //关闭订单窗口
        mCloseOrderDialog = new CloseOrderDialog(activity);
        //同意退款窗口
        mAgreeRefundDialog = new AgreeRefundDialog(activity);
        //拒绝退款窗口
        mRefuseRefundDialog = new RefuseRefundDialog(activity);

        setOrderDatas();
    }

    /**
     * 设置数据
     */
    private void setOrderDatas(){
        order_list_no.setText("" + mOrderDTO.order_id);
        String date = DateTimeUtil.LongToData(mOrderDTO.build_time, "yyyy-MM-dd HH:mm:ss");
        order_list_dat.setText("" + date);

        order_item_total_num.setText("" + mOrderDTO.order_total_goods_munber);
        order_item_total_amount.setText("" + mOrderDTO.order_payable_amount);
        order_item_freight.setText("" + mOrderDTO.freight);

        order_item_address.setText("" + mOrderDTO.receipt_province_name + mOrderDTO.receipt_city_name +
                mOrderDTO.receipt_area_name + mOrderDTO.receipt_address);

        //买家留言
        order_list_buyer_remark.setText("" + mOrderDTO.member_remark);
        if (mOrderDTO.member_remark.isEmpty()){
            order_list_remark_buyer_layout.setVisibility(View.GONE);
        }else {
            order_list_remark_buyer_layout.setVisibility(View.VISIBLE);
        }

        //订单备注
        order_list_remark.setText("" + mOrderDTO.order_remark);

        order_list_logistics_name.setText("" + mOrderDTO.express_company + " " + mOrderDTO.express_order_id);


        /** 订单状态 1 待付款   2.已付款   3.已发货   4.退款代码（1.申请退款   2.拒绝退款  3.已退款) 7.投诉中   8.已完成 */
        if (mOrderDTO.order_state == 1) {
            mOrderFlag = "待付款";
            order_list_close_btn.setVisibility(View.VISIBLE);
            order_list_change_price_btn.setVisibility(View.VISIBLE);
            order_list_print_btn.setVisibility(View.GONE);
            order_list_deliver_goods_btn.setVisibility(View.GONE);
            order_list_agree_refund_btn.setVisibility(View.GONE);
            order_list_refuse_refund_btn.setVisibility(View.GONE);
            order_list_remark_layout.setVisibility(View.GONE);
            order_item_address_iv.setVisibility(View.GONE);
            order_item_address_layout.setClickable(false);
            order_list_logistics_layout.setVisibility(View.GONE);
        } else if (mOrderDTO.order_state == 2) {
            mOrderFlag = "待发货";
            order_list_close_btn.setVisibility(View.GONE);
            order_list_change_price_btn.setVisibility(View.GONE);
            order_list_print_btn.setVisibility(View.VISIBLE);
            order_list_deliver_goods_btn.setVisibility(View.VISIBLE);
            order_list_agree_refund_btn.setVisibility(View.GONE);
            order_list_refuse_refund_btn.setVisibility(View.GONE);
            order_list_remark_layout.setVisibility(View.VISIBLE);
            order_item_address_iv.setVisibility(View.VISIBLE);
            order_item_address_layout.setClickable(true);
            order_list_logistics_layout.setVisibility(View.GONE);
        } else if (mOrderDTO.order_state == 3) {
            mOrderFlag = "已发货";
            order_list_close_btn.setVisibility(View.GONE);
            order_list_change_price_btn.setVisibility(View.GONE);
            order_list_print_btn.setVisibility(View.GONE);
            order_list_deliver_goods_btn.setVisibility(View.GONE);
            order_list_agree_refund_btn.setVisibility(View.GONE);
            order_list_refuse_refund_btn.setVisibility(View.GONE);
            order_list_remark_layout.setVisibility(View.VISIBLE);
            order_item_address_iv.setVisibility(View.GONE);
            order_item_address_layout.setClickable(false);
            order_list_logistics_layout.setVisibility(View.VISIBLE);
        } else if (mOrderDTO.order_state == 4) {
            mOrderFlag = "退款中";
            order_list_close_btn.setVisibility(View.GONE);
            order_list_change_price_btn.setVisibility(View.GONE);
            order_list_print_btn.setVisibility(View.GONE);
            order_list_deliver_goods_btn.setVisibility(View.GONE);
            order_list_agree_refund_btn.setVisibility(View.VISIBLE);
            order_list_refuse_refund_btn.setVisibility(View.VISIBLE);
            order_list_refuse_refund_btn.setText("拒绝退款");
            order_list_refuse_refund_btn.setEnabled(true);
            order_list_refuse_refund_btn.setTextColor(activity.getResources().getColor(R.color.color_5E5E5E));
            order_list_remark_layout.setVisibility(View.VISIBLE);
            order_item_address_iv.setVisibility(View.GONE);
            order_item_address_layout.setClickable(false);
            order_list_logistics_layout.setVisibility(View.GONE);
            /**退款代码:1.申请退款   2.拒绝退款  3.已退款*/
            if (mOrderDTO.order_refund_state == 1) {
                mOrderFlag = "申请退款";
            } else if (mOrderDTO.order_refund_state == 2) {
                mOrderFlag = "拒绝退款";
                order_list_refuse_refund_btn.setText("已拒绝");
                order_list_refuse_refund_btn.setEnabled(false);
                order_list_refuse_refund_btn.setTextColor(activity.getResources().getColor(R.color.color_FF6537));
            } else if (mOrderDTO.order_refund_state == 3) {
                mOrderFlag = "退款成功";
                order_list_agree_refund_btn.setVisibility(View.GONE);
                order_list_refuse_refund_btn.setVisibility(View.GONE);
            }
        } else if (mOrderDTO.order_state == 7) {
            mOrderFlag = "投诉中";
            order_list_close_btn.setVisibility(View.GONE);
            order_list_change_price_btn.setVisibility(View.GONE);
            order_list_print_btn.setVisibility(View.GONE);
            order_list_deliver_goods_btn.setVisibility(View.GONE);
            order_list_agree_refund_btn.setVisibility(View.GONE);
            order_list_refuse_refund_btn.setVisibility(View.GONE);
            order_list_remark_layout.setVisibility(View.VISIBLE);
            order_item_address_iv.setVisibility(View.GONE);
            order_item_address_layout.setClickable(false);
            order_list_logistics_layout.setVisibility(View.GONE);
        } else if (mOrderDTO.order_state == 8) {
            mOrderFlag = "已完成";
            order_list_close_btn.setVisibility(View.GONE);
            order_list_change_price_btn.setVisibility(View.GONE);
            order_list_print_btn.setVisibility(View.GONE);
            order_list_deliver_goods_btn.setVisibility(View.GONE);
            order_list_agree_refund_btn.setVisibility(View.GONE);
            order_list_refuse_refund_btn.setVisibility(View.GONE);
            order_list_remark_layout.setVisibility(View.VISIBLE);
            order_item_address_iv.setVisibility(View.GONE);
            order_item_address_layout.setClickable(false);
            order_list_logistics_layout.setVisibility(View.VISIBLE);
        } else if (mOrderDTO.order_state == 9) {
            mOrderFlag = "已关闭";
            order_list_close_btn.setVisibility(View.GONE);
            order_list_change_price_btn.setVisibility(View.GONE);
            order_list_print_btn.setVisibility(View.GONE);
            order_list_deliver_goods_btn.setVisibility(View.GONE);
            order_list_agree_refund_btn.setVisibility(View.GONE);
            order_list_refuse_refund_btn.setVisibility(View.GONE);
            order_list_remark_layout.setVisibility(View.GONE);
            order_item_address_iv.setVisibility(View.GONE);
            order_item_address_layout.setClickable(false);
            order_list_logistics_layout.setVisibility(View.GONE);
        } else {
            mOrderFlag = "其他:" + mOrderDTO.order_state;
        }
        order_item_state.setText("" + mOrderFlag);


        // 清空列表布局
        order_item_groups.removeAllViews();
        // 数据不为空，填充View到列表
        if (ListUtils.isNotEmpty(mOrderDTO.order_goods)) {
            // 第二种列表项的差异化可以在这里面完成，选择性的隐藏一些布局

            for (int i = 0; i < mOrderDTO.order_goods.size(); i++) {
                GoodsItemView itemView = new GoodsItemView(activity);

                // 将View添加到列表布局里面
                order_item_groups.addView(itemView.getRootView());
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

                // 设置图片
                if (!mOrderDTO.order_goods.get(i).main_picture.isEmpty()) {
                    glideRequest.load(mOrderDTO.order_goods.get(i).main_picture).asBitmap().centerCrop().placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into(itemView.order_goods_item_img);

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
        order_info_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finishActivityX();
            }
        });
        //联系买家
        order_list_contact_buyer_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mContactBuyerDialog != null) {
                    mContactBuyerDialog.show(new ContactBuyerDialog.OnContactBuyerListener() {
                        @Override
                        public void onConfirm(int type) {
                            if (type == 0){
                                //call(posterData.receipt_mobile);
                                contact_buyer_phone = mOrderDTO.receipt_mobile;
                                call();
                            }else if (type == 1){
                                contact_buyer_phone = mOrderDTO.receipt_mobile;
                                doSendSMSTo();
                            }else {
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                //创建ClipData对象
                                ClipData clipData = ClipData.newPlainText("copy", "" + mOrderDTO.receipt_province_name + mOrderDTO.receipt_city_name +
                                        mOrderDTO.receipt_area_name + mOrderDTO.receipt_address);
                                //添加ClipData对象到剪切板中
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.show(activity, "复制成功，可以发给买家了。");
                            }
                        }
                    });
                }
            }
        });
        //关闭
        order_list_close_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //关闭订单窗口
                if (mCloseOrderDialog != null){
                    mCloseOrderDialog.show(mOrderDTO, new CloseOrderDialog.OnContactBuyerListener() {
                        @Override
                        public void onConfirm(OrderDTO orderDTOs, String remark) {
                            delOrder(orderDTOs, remark);
                        }
                    });
                }
            }
        });
        //改价
        order_list_change_price_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {//改价
                Bundle bundle=new Bundle();
                //bundle.putString("news_id",posterData.news_id.toString());
                bundle.putSerializable("orderDTOs",mOrderDTO);
                //startActivity(new Intent(activity, OrderPriceActivity.class).putExtras(bundle));
                startActivityForResult(new Intent(activity, OrderPriceActivity.class).putExtras(bundle),ORDER_PRICE_REQUEST_CODE);
            }
        });
        //打印
        order_list_print_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {

                ArrayList<OrderDTO> orderDTOs = new ArrayList<OrderDTO>();//订单数据
                orderDTOs.add(mOrderDTO);

                PrinterBean printerBean = new PrinterBean();
                printerBean.printer_type = 0;// 打印机类型，0：票据，1：标签
                printerBean.ticket_type = 1;// 小票类型：0:打印测试，1：销售单，2：退货单，3：交接班，4:会员充值，5:标签
                printerBean.printer_num = 1;// 打印张数
                printerBean.print_message = "";// 打印内容
                printerBean.open_casher = 1;// 1/2:开钱箱,3:检测打印机状态
                printerBean.lablePrinterBeans = null;
                printerBean.orderDTOs = orderDTOs;

                Intent intent = new Intent();
                intent.putExtra("printerData",printerBean);//打印数据
                intent.setClass(activity, PrinterBluetoothActivity.class);
                ((BaseActivity) activity).startActivityForResult(intent, PRINT_REQUEST_CODE);

            }
        });
        //发货
        order_list_deliver_goods_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("orderDTOs",mOrderDTO);
                startActivityForResult(new Intent(activity, OrderDeliveryActivity.class).putExtras(bundle),DELIVERY_REQUEST_CODE);
            }
        });
        //拒绝退款
        order_list_refuse_refund_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mRefuseRefundDialog != null){
                    mRefuseRefundDialog.show(mOrderDTO, new RefuseRefundDialog.OnRefuseRefundListener() {
                        @Override
                        public void onConfirm(String remark) {
                            OrderRefundFail("" + mOrderDTO.order_id, remark);
                        }
                    });
                }
            }
        });
        //同意退款
        order_list_agree_refund_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mAgreeRefundDialog != null){
                    mAgreeRefundDialog.show(mOrderDTO, new AgreeRefundDialog.OnAgreeRefundListener() {
                        @Override
                        public void onConfirm() {
                            OrderRefundSuccess("" + mOrderDTO.order_id);
                        }
                    });
                }
            }
        });
        //收货地址
        order_item_address_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {//收货地址
                if (mOrderDTO.order_state == 2) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderDTOs", mOrderDTO);
                    startActivityForResult(new Intent(activity, OrderAddressActivity.class).putExtras(bundle), ADDRESS_REQUEST_CODE);
                }
            }
        });
        //修改卖家备注
        order_list_remark_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {//修改订单备注页面
                Bundle bundle=new Bundle();
                bundle.putSerializable("orderDTOs",mOrderDTO);
                startActivityForResult(new Intent(activity, OrderRemarkActivity.class).putExtras(bundle),REMARK_REQUEST_CODE);
            }
        });
        //查看物流
        order_list_logistics_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //物流信息页面
                Bundle bundle=new Bundle();
                bundle.putSerializable("orderDTOs",mOrderDTO);
                startActivityForResult(new Intent(activity, LogisticsInfoActivity.class).putExtras(bundle),LOGISTICS_REQUEST_CODE);
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

        public GoodsItemView(Context context) {
            super(context);
        }

        @Override
        public View initGroupView() {
            itemView = View.inflate(context, R.layout.view_order_list_goods_item, null);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return itemView;
        }

        @Override
        public void initView() {
            order_goods_item_group = itemView.findViewById(R.id.order_goods_item_group);
            order_goods_item_img = (ImageView) itemView.findViewById(R.id.order_goods_item_img);
            order_goods_item_name = (TextView) itemView.findViewById(R.id.order_goods_item_name);
            order_goods_item_size = (TextView) itemView.findViewById(R.id.order_goods_item_size);
            order_goods_item_colour = (TextView) itemView.findViewById(R.id.order_goods_item_colour);
            order_goods_item_price = (TextView) itemView.findViewById(R.id.order_goods_item_price);
            order_goods_item_old_price = (TextView) itemView.findViewById(R.id.order_goods_item_old_price);
            order_goods_item_num = (TextView) itemView.findViewById(R.id.order_goods_item_num);
        }

        @Override
        public void initData() {
            //添加删除线
            order_goods_item_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        @Override
        public void initListener() {

        }
    }

    /**
     * 卖家【作废订单】
     */
    public void delOrder(final OrderDTO orderDTOs, String order_remark){
        showLoading();
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.delOrder(activity, shop_id, "" + orderDTOs.order_id, order_remark, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
                if (mCloseOrderDialog != null){
                    mCloseOrderDialog.dismiss();
                }
            }

            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                dismissLoading();
                Toast.show(activity, "关闭订单成功！");
                if (mCloseOrderDialog != null){
                    mCloseOrderDialog.dismiss();
                }
                mOrderDTO.order_state = 9;
                setOrderDatas();
            }
        });
    }

    /**
     * 卖家【拒绝退款】
     */
    public void OrderRefundFail(String order_id,String remark){
        showLoading();
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.OrderRefundFail(activity, shop_id, order_id, remark, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                Toast.show(activity, "拒绝退款成功！");
                dismissLoading();
                if (mRefuseRefundDialog != null){
                    mRefuseRefundDialog.dismiss();
                }
                mOrderDTO.order_refund_state = 2;
                setOrderDatas();
                /*if (order_refund_state.equals("0")){//所有订单状态
                    *//**退款代码   :  1.申请退款   2.拒绝退款  3.已退款*//*
                    mOrderListAdapter.setRefundOrderFlag(2, current_order_no);
                }else{
                    mOrderListAdapter.delOrderItem(current_order_no);
                }
                mOrderListAdapter.notifyItemChanged(current_order_no, "order");*/
            }
        });
    }

    /**
     * 卖家【同意退款】
     */
    public void OrderRefundSuccess(String order_id){
        showLoading();
        String shop_id = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
        mOrderDataManager.OrderRefundSuccess(activity, shop_id, order_id, new IRequestCallBack<OrderDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, OrderDTO orderDTO) {
                Toast.show(activity, "同意退款成功！");
                dismissLoading();
                if (mAgreeRefundDialog != null){
                    mAgreeRefundDialog.dismiss();
                }
                mOrderDTO.order_refund_state = 3;
                setOrderDatas();
                /*if (order_refund_state.equals("0")){//所有订单状态
                    *//**退款代码   :  1.申请退款   2.拒绝退款  3.已退款*//*
                    mOrderListAdapter.setRefundOrderFlag(3, current_order_no);
                }else{
                    mOrderListAdapter.delOrderItem(current_order_no);
                }
                mOrderListAdapter.notifyItemChanged(current_order_no, "order");*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DELIVERY_REQUEST_CODE:
                    // 发货返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        mOrderDTO.order_state = 3;
                        setOrderDatas();
                        /*if (order_state.equals("0")){//所有订单状态
                            String order_id = data.getStringExtra("order_id");
                            mOrderListAdapter.setOrderFlag(3, current_order_no);
                        }else{
                            mOrderListAdapter.delOrderItem(current_order_no);
                        }
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");*/
                    }
                    break;
                case REMARK_REQUEST_CODE:// 修改订单备注返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        String order_remark = data.getStringExtra("order_remark");
                        mOrderDTO.order_remark = order_remark;
                        setOrderDatas();
                        /*mOrderListAdapter.editOrderRemarkItem(order_remark, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");*/
                    }
                    break;
                case LOGISTICS_REQUEST_CODE:// 物流信息页面返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }

                        String express_company_id = data.getStringExtra("express_company_id");
                        String express_company = data.getStringExtra("express_company");
                        String express_order_id = data.getStringExtra("express_order_id");

                        mOrderDTO.express_company_id = express_company_id;
                        mOrderDTO.express_company = express_company;
                        mOrderDTO.express_order_id = express_order_id;
                        setOrderDatas();

                        /*mOrderListAdapter.editOrderLogisticsItem(express_company_id, express_company, express_order_id, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");*/
                    }
                    break;
                case ADDRESS_REQUEST_CODE:// 修改订单地址页面
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        String provinceName = data.getStringExtra("provinceName");
                        String cityName = data.getStringExtra("cityName");
                        String areaName = data.getStringExtra("areaName");
                        String address = data.getStringExtra("address");

                        mOrderDTO.receipt_province_name = provinceName;
                        mOrderDTO.receipt_city_name = cityName;
                        mOrderDTO.receipt_area_name = areaName;
                        mOrderDTO.receipt_address = address;
                        setOrderDatas();

                        /*mOrderListAdapter.editOrderAddressItem(provinceName, cityName, areaName, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");*/
                    }
                    break;
                case ORDER_PRICE_REQUEST_CODE:// 修改订单价格页面返回
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        OrderDTO orderDTO = (OrderDTO) data.getSerializableExtra("orderDTOs");
                        mOrderDTO = orderDTO;
                        setOrderDatas();

                        /*mOrderListAdapter.editOrderAddressItem(provinceName, cityName, areaName, current_order_no);
                        mOrderListAdapter.notifyItemChanged(current_order_no, "order");*/
                    }
                    break;

            }
        }
    }

    /**
     * 调用拨号功能
     */
    private void call() {
        if(PhoneNumberUtils.isGlobalPhoneNumber(contact_buyer_phone)) {
            //系统会弹出需要请求权限的对话框
            if (android.os.Build.VERSION.SDK_INT > 22 && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE);
            } else {
                Intent intent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:" + contact_buyer_phone));
                startActivity(intent);
            }
        }else {
            Toast.show(activity, "买家填写电话有误");
        }
    }

    /**
     * 调起系统发短信功能
     */
    public void doSendSMSTo(){
        if(PhoneNumberUtils.isGlobalPhoneNumber(contact_buyer_phone)){
            //系统会弹出需要请求权限的对话框
            if (android.os.Build.VERSION.SDK_INT > 22 && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{Manifest.permission.SEND_SMS }, SEND_SMS);
            }else{
                Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, Uri.parse("smsto:"+contact_buyer_phone));
                intent.putExtra("sms_body", "");
                startActivity(intent);
            }
        }else {
            Toast.show(activity, "买家填写电话有误");
        }
    }

    //接收权限是否请求的请求状态
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Print.println("requestCode:=========================================:"+requestCode);
        switch (requestCode) {
            case CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.show(activity,"没有权限");
                    mContactBuyerDialog.dismiss();
                }
            }
            case SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSendSMSTo();
                } else {
                    Toast.show(activity,"没有权限");
                    mContactBuyerDialog.dismiss();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

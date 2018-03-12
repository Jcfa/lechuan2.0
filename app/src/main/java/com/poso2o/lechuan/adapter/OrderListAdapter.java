package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.time.DateTimeUtil;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * 订单列表适配器
 * Created by Luo on 2017/02/10.
 */
public class OrderListAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemListener onItemListener;
    private ArrayList<OrderDTO> orderDTOs;
    private RequestManager glideRequest;
    private String mOrderFlag = "-";

    public OrderListAdapter(Context context, OnItemListener onItemListener) {
        this.context = context;
        this.onItemListener = onItemListener;
        this.orderDTOs = new ArrayList<>();
        if (orderDTOs != null) {
            this.orderDTOs.addAll(orderDTOs);
        }
        glideRequest = Glide.with(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderHolder(LayoutInflater.from(context).inflate(R.layout.view_order_list_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return orderDTOs.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OrderHolder) {
            OrderHolder orderHolder = (OrderHolder) holder;

            final OrderDTO item = orderDTOs.get(position);

            orderHolder.order_list_no.setText("" + item.order_id);
            String date = DateTimeUtil.LongToData(item.build_time, "yyyy-MM-dd HH:mm:ss");
            orderHolder.order_list_dat.setText("" + date);

            orderHolder.order_item_total_num.setText("" + item.order_total_goods_munber);
            orderHolder.order_item_total_amount.setText("" + item.order_payable_amount);
            orderHolder.order_item_freight.setText("" + item.freight);

            orderHolder.order_item_address.setText(item.receipt_name + " " + item.receipt_mobile + " " + item.receipt_province_name + item.receipt_city_name +
                    item.receipt_area_name + item.receipt_address);

            //买家留言
            orderHolder.order_list_buyer_remark.setText("" + item.member_remark);
            if (item.member_remark.isEmpty()){
                orderHolder.order_list_remark_buyer_layout.setVisibility(View.GONE);
            }else {
                orderHolder.order_list_remark_buyer_layout.setVisibility(View.VISIBLE);
            }

            //订单备注
            orderHolder.order_list_remark.setText("" + item.order_remark);

            orderHolder.order_list_logistics_name.setText("" + item.express_company + " " + item.express_order_id);

            orderHolder.order_item_group.setTag(item);
            orderHolder.order_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickItem((OrderDTO) v.getTag(), position);
                    }
                }
            });
            //联系买家
            orderHolder.order_list_contact_buyer_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickBtn(1, item, position);
                    }
                }
            });
            //关闭
            orderHolder.order_list_close_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickBtn(2, item, position);
                    }
                }
            });
            //改价
            orderHolder.order_list_change_price_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickBtn(3, item, position);
                    }
                }
            });
            //打印
            orderHolder.order_list_print_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickBtn(4, item, position);
                    }
                }
            });
            //发货
            orderHolder.order_list_deliver_goods_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickBtn(5, item, position);
                    }
                }
            });
            //拒绝退款
            orderHolder.order_list_refuse_refund_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickBtn(6, item, position);
                    }
                }
            });
            //同意退款
            orderHolder.order_list_agree_refund_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onItemListener != null) {
                        onItemListener.onClickBtn(7, item, position);
                    }
                }
            });
            //收货地址
            orderHolder.order_item_address_layout.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    onItemListener.onClickBtn(8, item, position);
                }
            });
            //修改卖家备注
            orderHolder.order_list_remark_layout.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    onItemListener.onClickBtn(9, item, position);
                }
            });
            //查看物流
            orderHolder.order_list_logistics_layout.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    onItemListener.onClickBtn(10, item, position);
                }
            });

            /** 订单状态 1 待付款   2.已付款   3.已发货   4.退款代码（1.申请退款   2.拒绝退款  3.已退款) 7.投诉中   8.已完成 */
            if (item.order_state == 1) {
                mOrderFlag = "待付款";
                orderHolder.order_list_close_btn.setVisibility(View.VISIBLE);
                orderHolder.order_list_change_price_btn.setVisibility(View.VISIBLE);
                orderHolder.order_list_print_btn.setVisibility(View.GONE);
                orderHolder.order_list_deliver_goods_btn.setVisibility(View.GONE);
                orderHolder.order_list_agree_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_refuse_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_remark_layout.setVisibility(View.GONE);
                orderHolder.order_item_address_iv.setVisibility(View.GONE);
                orderHolder.order_item_address_layout.setClickable(false);
                orderHolder.order_list_logistics_layout.setVisibility(View.GONE);
            } else if (item.order_state == 2) {
                mOrderFlag = "待发货";
                orderHolder.order_list_close_btn.setVisibility(View.GONE);
                orderHolder.order_list_change_price_btn.setVisibility(View.GONE);
                orderHolder.order_list_print_btn.setVisibility(View.VISIBLE);
                orderHolder.order_list_deliver_goods_btn.setVisibility(View.VISIBLE);
                orderHolder.order_list_agree_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_refuse_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_remark_layout.setVisibility(View.VISIBLE);
                orderHolder.order_item_address_iv.setVisibility(View.VISIBLE);
                orderHolder.order_item_address_layout.setClickable(true);
                orderHolder.order_list_logistics_layout.setVisibility(View.GONE);
            } else if (item.order_state == 3) {
                mOrderFlag = "已发货";
                orderHolder.order_list_close_btn.setVisibility(View.GONE);
                orderHolder.order_list_change_price_btn.setVisibility(View.GONE);
                orderHolder.order_list_print_btn.setVisibility(View.GONE);
                orderHolder.order_list_deliver_goods_btn.setVisibility(View.GONE);
                orderHolder.order_list_agree_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_refuse_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_remark_layout.setVisibility(View.VISIBLE);
                orderHolder.order_item_address_iv.setVisibility(View.GONE);
                orderHolder.order_item_address_layout.setClickable(false);
                orderHolder.order_list_logistics_layout.setVisibility(View.VISIBLE);
            } else if (item.order_state == 4) {
                mOrderFlag = "退款中";
                orderHolder.order_list_close_btn.setVisibility(View.GONE);
                orderHolder.order_list_change_price_btn.setVisibility(View.GONE);
                orderHolder.order_list_print_btn.setVisibility(View.GONE);
                orderHolder.order_list_deliver_goods_btn.setVisibility(View.GONE);
                orderHolder.order_list_agree_refund_btn.setVisibility(View.VISIBLE);
                orderHolder.order_list_refuse_refund_btn.setVisibility(View.VISIBLE);
                orderHolder.order_list_refuse_refund_btn.setText("拒绝退款");
                orderHolder.order_list_refuse_refund_btn.setEnabled(true);
                orderHolder.order_list_refuse_refund_btn.setTextColor(context.getResources().getColor(R.color.color_5E5E5E));
                orderHolder.order_list_remark_layout.setVisibility(View.VISIBLE);
                orderHolder.order_item_address_iv.setVisibility(View.GONE);
                orderHolder.order_item_address_layout.setClickable(false);
                orderHolder.order_list_logistics_layout.setVisibility(View.GONE);
                /**退款代码:1.申请退款   2.拒绝退款  3.已退款*/
                if (item.order_refund_state == 1) {
                    mOrderFlag = "申请退款";
                } else if (item.order_refund_state == 2) {
                    mOrderFlag = "拒绝退款";
                    orderHolder.order_list_refuse_refund_btn.setText("已拒绝");
                    orderHolder.order_list_refuse_refund_btn.setEnabled(false);
                    orderHolder.order_list_refuse_refund_btn.setTextColor(context.getResources().getColor(R.color.color_FF6537));
                } else if (item.order_refund_state == 3) {
                    mOrderFlag = "退款成功";
                    orderHolder.order_list_agree_refund_btn.setVisibility(View.GONE);
                    orderHolder.order_list_refuse_refund_btn.setVisibility(View.GONE);
                }
            } else if (item.order_state == 7) {
                mOrderFlag = "投诉中";
                orderHolder.order_list_close_btn.setVisibility(View.GONE);
                orderHolder.order_list_change_price_btn.setVisibility(View.GONE);
                orderHolder.order_list_print_btn.setVisibility(View.GONE);
                orderHolder.order_list_deliver_goods_btn.setVisibility(View.GONE);
                orderHolder.order_list_agree_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_refuse_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_remark_layout.setVisibility(View.VISIBLE);
                orderHolder.order_item_address_iv.setVisibility(View.GONE);
                orderHolder.order_item_address_layout.setClickable(false);
                orderHolder.order_list_logistics_layout.setVisibility(View.GONE);
            } else if (item.order_state == 8) {
                mOrderFlag = "已完成";
                orderHolder.order_list_close_btn.setVisibility(View.GONE);
                orderHolder.order_list_change_price_btn.setVisibility(View.GONE);
                orderHolder.order_list_print_btn.setVisibility(View.GONE);
                orderHolder.order_list_deliver_goods_btn.setVisibility(View.GONE);
                orderHolder.order_list_agree_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_refuse_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_remark_layout.setVisibility(View.VISIBLE);
                orderHolder.order_item_address_iv.setVisibility(View.GONE);
                orderHolder.order_item_address_layout.setClickable(false);
                orderHolder.order_list_logistics_layout.setVisibility(View.VISIBLE);
            } else if (item.order_state == 9) {
                mOrderFlag = "已关闭";
                orderHolder.order_list_close_btn.setVisibility(View.GONE);
                orderHolder.order_list_change_price_btn.setVisibility(View.GONE);
                orderHolder.order_list_print_btn.setVisibility(View.GONE);
                orderHolder.order_list_deliver_goods_btn.setVisibility(View.GONE);
                orderHolder.order_list_agree_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_refuse_refund_btn.setVisibility(View.GONE);
                orderHolder.order_list_remark_layout.setVisibility(View.GONE);
                orderHolder.order_item_address_iv.setVisibility(View.GONE);
                orderHolder.order_item_address_layout.setClickable(false);
                orderHolder.order_list_logistics_layout.setVisibility(View.GONE);
            } else {
                mOrderFlag = "其他:" + item.order_state;
            }
            orderHolder.order_item_state.setText("" + mOrderFlag);

            // 清空列表布局
            orderHolder.order_item_groups.removeAllViews();
            // 数据不为空，填充View到列表
            if (ListUtils.isNotEmpty(item.order_goods)) {
                // 第二种列表项的差异化可以在这里面完成，选择性的隐藏一些布局

                for (int i = 0; i < item.order_goods.size(); i++) {
                    GoodsItemView itemView = new GoodsItemView(context);

                    // 将View添加到列表布局里面
                    orderHolder.order_item_groups.addView(itemView.getRootView());

                    if(i%2==0){
                        itemView.order_goods_item_group.setBackgroundColor(Color.parseColor("#FCFCFC"));
                    }else{
                        itemView.order_goods_item_group.setBackgroundColor(Color.parseColor("#00000000"));
                    }

                    itemView.order_goods_item_name.setText("" + item.order_goods.get(i).goods_name);
                    itemView.order_goods_item_size.setText("" + item.order_goods.get(i).goods_spec_name);
                    itemView.order_goods_item_colour.setText("" + item.order_goods.get(i).goods_spec_name);
                    itemView.order_goods_item_price.setText("¥" + NumberFormatUtils.format(item.order_goods.get(i).goods_amount));
                    itemView.order_goods_item_old_price.setText("¥" + NumberFormatUtils.format(item.order_goods.get(i).spec_price * item.order_goods.get(i).purchase_munber));
                    itemView.order_goods_item_num.setText("X" + item.order_goods.get(i).purchase_munber);

                    // 设置图片
                    if (!item.order_goods.get(i).main_picture.isEmpty()) {
                        glideRequest.load(item.order_goods.get(i).main_picture).asBitmap().centerCrop().placeholder(R.mipmap.logo_e).error(R.mipmap.logo_e).into(itemView.order_goods_item_img);
                    } else {
                        itemView.order_goods_item_img.setImageResource(R.mipmap.logo_e);
                    }

                }
            }
            

        }
    }

    public void notifyData(ArrayList<OrderDTO> orderDTOs) {
        this.orderDTOs.clear();
        if (orderDTOs != null) {
            this.orderDTOs.addAll(orderDTOs);
        }
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<OrderDTO> posterData) {
        if (posterData != null) {
            this.orderDTOs.addAll(posterData);
        }
        notifyDataSetChanged();
    }

    /**
     * 修改订单状态item
     */
    public void setOrderFlag(int order_state, int current_order_no) {
        if (orderDTOs != null && orderDTOs.size() >= current_order_no) {
            orderDTOs.get(current_order_no).order_state = order_state;
        }
    }

    /**
     * 删除订单item
     */
    public void delOrderItem(int current_order_no) {
        if (orderDTOs != null && orderDTOs.size() >= current_order_no) {
            orderDTOs.remove(current_order_no);
            notifyDataSetChanged();
        }
    }

    /**
     * 修改订单备注
     */
    public void editOrderRemarkItem(String order_remark, int current_order_no) {
        if (orderDTOs != null && orderDTOs.size() >= current_order_no) {
            orderDTOs.get(current_order_no).order_remark = order_remark;
        }
    }

    /**
     * 修改订单物流
     */
    public void editOrderLogisticsItem(String express_company_id, String express_company, String express_order_id, int current_order_no) {
        if (orderDTOs != null && orderDTOs.size() >= current_order_no) {
            orderDTOs.get(current_order_no).express_company_id = express_company_id;
            orderDTOs.get(current_order_no).express_company = express_company;
            orderDTOs.get(current_order_no).express_order_id = express_order_id;
        }
    }

    /**
     * 修改订单地址
     */
    public void editOrderAddressItem(String provinceName, String cityName, String areaName, String address, int current_order_no) {
        if (orderDTOs != null && orderDTOs.size() >= current_order_no) {
            orderDTOs.get(current_order_no).receipt_province_name = provinceName;
            orderDTOs.get(current_order_no).receipt_city_name = cityName;
            orderDTOs.get(current_order_no).receipt_area_name = areaName;
            orderDTOs.get(current_order_no).receipt_address = address;
        }
    }

    /**
     * 修改退款订单状态item
     */
    public void setRefundOrderFlag(int order_state, int current_order_no) {
        if (orderDTOs != null && orderDTOs.size() >= current_order_no) {
            orderDTOs.get(current_order_no).order_refund_state = order_state;
        }
    }

    /**
     * 修改订单对象
     */
    public void editOrderItem(OrderDTO orderDTO, int current_order_no) {
        if (orderDTOs != null && orderDTOs.size() >= current_order_no) {
            //orderDTOs.get(current_order_no) = orderDTO;
            orderDTOs.set(current_order_no,orderDTO);
        }
    }

    /**
     * 获取所有订单数据
     */
    public ArrayList<OrderDTO> getAllOrderDatas(){
        return orderDTOs;
    }

    public interface OnItemListener {
        void onClickItem(OrderDTO orderDTOs, int position);
        void onClickBtn(int type, OrderDTO orderDTOs,int position);
    }

    public class OrderHolder extends RecyclerView.ViewHolder {

        View order_item_group;
        TextView order_list_no;
        TextView order_list_dat;
        TextView order_item_total_num;
        TextView order_item_total_amount;
        TextView order_item_freight;
        TextView order_item_state;

        TextView order_item_address;

        TextView order_list_contact_buyer_btn;
        TextView order_list_close_btn;
        TextView order_list_change_price_btn;
        
        TextView order_list_print_btn;
        TextView order_list_deliver_goods_btn;

        TextView order_list_agree_refund_btn;
        TextView order_list_refuse_refund_btn;

        LinearLayout order_item_groups;

        LinearLayout order_item_address_layout;
        ImageView order_item_address_iv;

        LinearLayout order_list_remark_buyer_layout;//买家备注
        TextView order_list_buyer_remark;

        LinearLayout order_list_remark_layout;//卖家备注
        TextView order_list_remark;

        LinearLayout order_list_logistics_layout;//物流
        TextView order_list_logistics_name;
        TextView order_list_logistics_info;

        OrderHolder(View itemView) {
            super(itemView);
            order_item_group = itemView.findViewById(R.id.order_item_group);

            order_list_no = (TextView) itemView.findViewById(R.id.order_list_no);
            order_list_dat = (TextView) itemView.findViewById(R.id.order_list_dat);
            order_item_total_num = (TextView) itemView.findViewById(R.id.order_item_total_num);
            order_item_total_amount = (TextView) itemView.findViewById(R.id.order_item_total_amount);
            order_item_freight = (TextView) itemView.findViewById(R.id.order_item_freight);
            order_item_state = (TextView) itemView.findViewById(R.id.order_item_state);

            order_item_address = (TextView) itemView.findViewById(R.id.order_item_address);

            order_list_contact_buyer_btn = (TextView) itemView.findViewById(R.id.order_list_contact_buyer_btn);
            order_list_close_btn = (TextView) itemView.findViewById(R.id.order_list_close_btn);
            order_list_change_price_btn = (TextView) itemView.findViewById(R.id.order_list_change_price_btn);

            order_list_print_btn = (TextView) itemView.findViewById(R.id.order_list_print_btn);
            order_list_deliver_goods_btn = (TextView) itemView.findViewById(R.id.order_list_deliver_goods_btn);

            order_list_agree_refund_btn = (TextView) itemView.findViewById(R.id.order_list_agree_refund_btn);
            order_list_refuse_refund_btn = (TextView) itemView.findViewById(R.id.order_list_refuse_refund_btn);

            order_item_groups = (LinearLayout) itemView.findViewById(R.id.order_item_groups);

            order_item_address_layout = (LinearLayout) itemView.findViewById(R.id.order_item_address_layout);
            order_item_address_iv = (ImageView) itemView.findViewById(R.id.order_item_address_iv);

            order_list_remark_layout = (LinearLayout) itemView.findViewById(R.id.order_list_remark_layout);
            order_list_remark = (TextView) itemView.findViewById(R.id.order_list_remark);

            order_list_remark_buyer_layout = (LinearLayout) itemView.findViewById(R.id.order_list_remark_buyer_layout);
            order_list_buyer_remark = (TextView) itemView.findViewById(R.id.order_list_buyer_remark);

            order_list_logistics_layout = (LinearLayout) itemView.findViewById(R.id.order_list_logistics_layout);
            order_list_logistics_name = (TextView) itemView.findViewById(R.id.order_list_logistics_name);
            order_list_logistics_info = (TextView) itemView.findViewById(R.id.order_list_logistics_info);

        }
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

}

package com.poso2o.lechuan.manager.poster;

import com.google.gson.Gson;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseManager;
import com.poso2o.lechuan.bean.system.AddressDTO;
import com.poso2o.lechuan.bean.order.ExpressLogisticsQueryDTO;
import com.poso2o.lechuan.bean.order.GoodsAppraiseQueryDTO;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.order.OrderDeliveryDTO;
import com.poso2o.lechuan.bean.order.OrderQueryDTO;
import com.poso2o.lechuan.bean.order.TodayOrderSaleReportDTO;
import com.poso2o.lechuan.http.HttpAPI;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.tool.print.Print;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.ArrayList;

/**
 * 订单管理类
 * Created by Luo on 2017-12-11.
 */

public class OrderDataManager<T> extends BaseManager {
    public static OrderDataManager mOrderDataManager;
    /**
     * 订单 - 当天订单销售统计
     */
    private final int TAG_ORDER_ID = 1;
    private final int TAG_ADDRESS_ID = 2;

    public static OrderDataManager getOrderDataManager() {
        if (mOrderDataManager == null) {
            mOrderDataManager = new OrderDataManager();
        }
        return mOrderDataManager;
    }

    /**
     * 当天订单销售统计
     *
     * @param baseActivity
     * @param shop_id      商家店铺ID
     * @param callBack     回调
     */
    public void loadTodayOrderSaleReport(final BaseActivity baseActivity, final String shop_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_TODAY_ORDER_SALE_REPORT_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                TodayOrderSaleReportDTO datas = gson.fromJson(response, TodayOrderSaleReportDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 订单查找+分页
     *
     * @param baseActivity
     * @param shop_id               商家店铺ID
     * @param order_state           1 待付款   2.已付款   3.已发货   4.申请退款   5.拒绝退款  6.已退款  7.投诉中   8.已完成
     * @param order_refund_state  退款状态:0=没有退款，1.申请退款 2.拒绝退款 3.已退款  （必填）
     * @param keywords              关键字
     * @param begin_time            开始时间（必填）
     * @param end_time              结束时间（必填）
     * @param currPage               查看页码（必填）
     * @param callBack              回调
     */
    public void loadOrderQuery(final BaseActivity baseActivity, final String shop_id, final String order_state, final String order_refund_state,
                               final String keywords, final String begin_time, final String end_time,
                               final String currPage, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_QUERY_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("order_state", order_state);
        request.add("order_refund_state", order_refund_state);
        request.add("keywords", keywords);
        request.add("begin_time", begin_time);
        request.add("end_time", end_time);
        request.add("currPage", currPage);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderQueryDTO datas = gson.fromJson(response, OrderQueryDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }

            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 卖家【查看订单详情】
     *
     * @param baseActivity
     * @param shop_id         商家店铺ID
     * @param order_id        订单号（必填）
     * @param callBack        回调
     */
    public void OrderInfo(final BaseActivity baseActivity, final String shop_id, final String order_id,final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_INFO_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("order_id", order_id);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 卖家【作废订单】
     *
     * @param baseActivity
     * @param shop_id         商家店铺ID
     * @param order_id        订单号（必填）
     * @param order_remark    作废原因 （必填）
     * @param callBack        回调
     */
    public void delOrder(final BaseActivity baseActivity, final String shop_id, final String order_id, final String order_remark, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_DEL_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("order_id", order_id);
        request.add("order_remark", order_remark);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 卖家【发货确认】
     *
     * @param baseActivity
     * @param shop_id                     商家店铺ID
     * @param has_express                     是否需要发物流  1=需要,0=不需要
     * @param express_company_id         快递公司ID（必填）
     * @param datas                        订单号（必填）
     * @param callBack                    回调
     */
    public void OrderShippingConfrim(final BaseActivity baseActivity, final String shop_id, final String has_express, final String express_company_id, final ArrayList<OrderDeliveryDTO> datas, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_SHIPPING_CONFRIM_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("has_express", has_express);
        request.add("express_company_id", express_company_id);
        request.add("datas",new Gson().toJson(datas));
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 卖家【拒绝退款】
     *
     * @param baseActivity
     * @param shop_id         商家店铺ID
     * @param order_id        订单号（必填）
     * @param order_remark   拒绝原因（必填）
     * @param callBack        回调
     */
    public void OrderRefundFail(final BaseActivity baseActivity, final String shop_id, final String order_id, String order_remark, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_REFUND_FAIL_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("order_id", order_id);
        request.add("order_remark", order_remark);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 卖家【同意退款】
     *
     * @param baseActivity
     * @param shop_id         商家店铺ID
     * @param order_id        订单号（必填）
     * @param callBack        回调
     */
    public void OrderRefundSuccess(final BaseActivity baseActivity, final String shop_id, final String order_id,final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_REFUND_SUCCESS_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("order_id", order_id);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 卖家【按时间统计订单个数】
     *
     * @param baseActivity
     * @param shop_id         商家店铺ID
     * @param begin_time      统计时间（必填）
     * @param callBack        回调
     */
    public void OrderTotalOrderCount(final BaseActivity baseActivity, final String shop_id, final String begin_time, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_TOTAL_ORDER_COUNT_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("begin_time", begin_time);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 卖家 购买评论+分页
     *
     * @param baseActivity
     * @param shop_id         商家店铺ID
     * @param keywords        关键字
     * @param currPage        查看页码（必填）
     * @param callBack        回调
     */
    public void GoodsAppraiseQuery(final BaseActivity baseActivity, final String shop_id, final String keywords,
                                   final String currPage,String appraise_grade, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_GOODS_APPRAISE_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("keywords", keywords);
        request.add("currPage", currPage);
        request.add("appraise_grade", appraise_grade);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                GoodsAppraiseQueryDTO datas = gson.fromJson(response, GoodsAppraiseQueryDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 修改订单备注信息
     *
     * @param baseActivity
     * @param shop_id           商家店铺ID
     * @param order_id          订单号（必填）
     * @param order_remark     备注信息
     * @param callBack          回调
     */
    public void editOrderRemark(final BaseActivity baseActivity, final String shop_id, final String order_id, final String order_remark, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_EDIT_REMARK_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("order_id", order_id);
        request.add("order_remark", order_remark);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Gson gson = new Gson();
                //OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, null);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 修改物流信息
     *
     * @param baseActivity
     * @param shop_id                     商家店铺ID
     * @param order_id                    订单号（必填）
     * @param has_express                 是否需要发物流  1=需要,0=不需要
     * @param express_company_id         快递公司ID（必填）
     * @param express_order_id           快递单号（必填）
     * @param callBack                    回调
     */
    public void editExpress(final BaseActivity baseActivity, final String shop_id, final String order_id, final String has_express, final String express_company_id, final String express_order_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_EDIT_EXPRESS_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("order_id", order_id);
        request.add("has_express", has_express);
        request.add("express_company_id", express_company_id);
        request.add("express_order_id",express_order_id);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                //Gson gson = new Gson();
                //OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, null);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 订单物流信息
     *
     * @param baseActivity
     * @param shop_id                商家店铺ID
     * @param express_company_id    物流公司ID（必填）
     * @param express_order_id      快递单号
     * @param callBack               回调
     */
    public void expressLogistics (final BaseActivity baseActivity, final String shop_id, final String express_company_id, final String express_order_id, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_EXPRESS_LOGISTICS_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("express_company_id", express_company_id);
        request.add("express_order_id", express_order_id);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                ExpressLogisticsQueryDTO datas = gson.fromJson(response, ExpressLogisticsQueryDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 修改收货信息
     *
     * @param baseActivity
     * @param shop_id                     商家店铺ID
     * @param order_id                    订单号（必填）
     * @param receipt_name			    收货姓名
     * @param receipt_province_name		收货地址-省份名称
     * @param receipt_city_name			收货地址-市名称
     * @param receipt_area_name			收货地址-区名称
     * @param receipt_address			    收货地址-地址
     * @param receipt_mobile			    联系手机
     * @param receipt_tel			        联系电话
     * @param receipt_zipcode			    联系编号
     * @param callBack                    回调
     */
    public void editReceiptAddress(final BaseActivity baseActivity, final String shop_id, final String order_id, final String receipt_name,
                                   final String receipt_province_name, final String receipt_city_name, final String receipt_area_name,
                                   final String receipt_mobile, final String receipt_tel, final String receipt_zipcode, final String receipt_address,
                                   final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_EDIT_ADDRESS_API);
        request = defaultParam(request);
        request.add("shop_id", shop_id);
        request.add("order_id", order_id);
        request.add("receipt_name", receipt_name);
        request.add("receipt_province_name", receipt_province_name);
        request.add("receipt_city_name",receipt_city_name);
        request.add("receipt_area_name",receipt_area_name);
        request.add("receipt_mobile",receipt_mobile);
        request.add("receipt_tel",receipt_tel);
        request.add("receipt_zipcode",receipt_zipcode);
        request.add("receipt_address",receipt_address);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 修改订单商品价格
     *
     * @param baseActivity
     * @param order_id                订单号（必填）
     * @param serial                  订单商品序号
    * @param goods_discount          商品折扣
     * @param goods_discount_price  商品折扣单价
     * @param callBack                回调
     */
    public void editGoodsPrice (final BaseActivity baseActivity, final String order_id, final String serial,
                                final String goods_discount,final String goods_discount_price, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_EDIT_GOODS_PRICE_API);
        request = defaultParam(request);
        request.add("order_id", order_id);
        request.add("serial", serial);
        request.add("goods_discount", goods_discount);
        request.add("goods_discount_price", goods_discount_price);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 一键改价
     *
     * @param baseActivity
     * @param order_id                订单号（必填）
     * @param order_discount         整单折扣
     * @param order_amount           整单金额
     * @param callBack                回调
     */
    public void editOrderAmount (final BaseActivity baseActivity, final String order_id,final String order_discount,final String order_amount,
                                 final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_EDIT_ORDER_AMOUNT_API);
        request = defaultParam(request);
        request.add("order_id", order_id);
        request.add("order_discount", order_discount);
        request.add("order_amount", order_amount);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

    /**
     * 修改订单运费
     *
     * @param baseActivity
     * @param order_id         订单号（必填）
     * @param freight           单运费
     * @param callBack         回调
     */
    public void editFreight (final BaseActivity baseActivity, final String order_id,final String freight, final IRequestCallBack callBack) {
        Request<String> request = getStringRequest(HttpAPI.ORDER_EDIT_FREIGHT_API);
        request = defaultParam(request);
        request.add("order_id", order_id);
        request.add("freight", freight);
        baseActivity.request(TAG_ORDER_ID, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, String response) {
                Gson gson = new Gson();
                OrderDTO datas = gson.fromJson(response, OrderDTO.class);
                callBack.onResult(TAG_ORDER_ID, datas);
            }
            @Override
            public void onFailed(int what, String response) {
                callBack.onFailed(TAG_ORDER_ID, response);
            }
        }, true, true);
    }

}

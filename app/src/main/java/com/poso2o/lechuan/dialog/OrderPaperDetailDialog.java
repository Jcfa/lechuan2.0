package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderMemberDetailBean;
import com.poso2o.lechuan.bean.orderInfo.OrderMothsDetailBean;
import com.poso2o.lechuan.bean.orderInfo.OrderPaperDetailBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderPaperDetailManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderMemberDetailAdapter;
import com.poso2o.lechuan.orderInfoAdapter.OrderMothsDetailAdapter;
import com.poso2o.lechuan.orderInfoAdapter.OrderPaperDetailAdapter;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/16 0016.
 * 设置库存管理界面弹框
 * 自下而上的弹出 点击关闭时 自上而下的关闭
 */

public class OrderPaperDetailDialog extends BaseDialog {

    private View view;
    private ImageView ivHead, ivClose;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tv_member_num, tv_member_money;
    private RelativeLayout rl_member_vis;
    private RecyclerView rlvDialog;
    private TextView tv_public_frist, tv_public_second, tv_public_thrid, tv_public_fourth, tv_public_fifth;
    private TextView tvTotalshouc, tvTotalKc, tvTotalPrice;
    private TextView tvQchu, tv_spg, tv_paper_detail_vip;

    public OrderPaperDetailDialog(Context context) {
        super(context);
    }

    @Override
    public View setDialogContentView() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_order_paper, null);
        return view;
    }

    @Override
    public void initView() {
        ivHead = (ImageView) findViewById(R.id.iv_paper_detail_head);
        tvName = (TextView) findViewById(R.id.tv_paper_detail_name);
        tvPrice = (TextView) findViewById(R.id.tv_paper_detail_price);

        tv_public_frist = (TextView) findViewById(R.id.tv_public_frist);
        tv_public_second = (TextView) findViewById(R.id.tv_public_second);
        tv_public_thrid = (TextView) findViewById(R.id.tv_public_thrid);
        tv_public_fourth = (TextView) findViewById(R.id.tv_public_fourth);
        tv_public_fifth = (TextView) findViewById(R.id.tv_public_fifth);
        tvQchu = (TextView) findViewById(R.id.tv_qchu);
        rlvDialog = (RecyclerView) findViewById(R.id.rlv_dalog);
        ivClose = (ImageView) findViewById(R.id.iv_paper_click_close);

        tv_member_num = (TextView) findViewById(R.id.tv_kc);
        tv_member_money = (TextView) findViewById(R.id.tv_price);
        rl_member_vis = (RelativeLayout) findViewById(R.id.rl_member_vis);
        tvTotalshouc = (TextView) findViewById(R.id.tv_shouc);
        tvTotalKc = (TextView) findViewById(R.id.tv_kc);
        tvTotalPrice = (TextView) findViewById(R.id.tv_price);
        tv_spg = (TextView) findViewById(R.id.tv_spg);
        tv_paper_detail_vip = (TextView) findViewById(R.id.tv_paper_detail_vip);

    }

    /**
     * 设置属性 自下而上
     */
    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        setWindowDispalay(1.0f, 0.7f);
        setCancelable(true);
        getWindow().setWindowAnimations(R.style.BottomInAnimation);
        rlvDialog.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void initListener() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /**
     * 根据库存列表的id来查询库存详情界面
     * 因为很多界面的之间的弹框都是一样的  这里我按照type类型来进行判断进行展示不同几面
     */
    public void setData(String guid, String begin, final int type) {
        if (type == 1) {
            ((BaseActivity) context).showLoading();
            OrderPaperDetailManager.getOrderInfo().orderPaperDetailApi((BaseActivity) context, guid,
                    new IRequestCallBack<OrderPaperDetailBean>() {
                        @Override
                        public void onResult(int tag, OrderPaperDetailBean detailBean) {
                            ((BaseActivity) context).dismissLoading();
                            Glide.with(context).load(detailBean.getImage222()).error(R.drawable.expicture).into(ivHead);
                            tvName.setText(detailBean.getName());
                            String fprice = detailBean.getFprice();
                            tvPrice.setText(detailBean.getPrice() + "     成本" + fprice);
                            int profit = 0;
                            int income = 0;
                            double totalPrice = 0;
                            for (int i = 0; i < detailBean.getLists().size(); i++) {
                                profit += Integer.parseInt(detailBean.getLists().get(i).getSales_num());
                                income += Integer.parseInt(detailBean.getLists().get(i).getNum());
                            }
                            totalPrice += Double.parseDouble(detailBean.getFprice());
                            BigDecimal bg3 = new BigDecimal(totalPrice);
                            double value3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            double moeny = value3 * income;
                            tvTotalshouc.setText(profit + "");
                            tvTotalKc.setText(income + "");
                            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
                            String format = df.format(moeny);
                            if (moeny == 0) {
                                tvTotalPrice.setText("0.00");
                            } else
                                tvTotalPrice.setText(format + "");
                            OrderPaperDetailAdapter adapter = new OrderPaperDetailAdapter(detailBean.getLists(), fprice, type);
                            rlvDialog.setAdapter(adapter);

                        }

                        @Override
                        public void onFailed(int tag, String msg) {
                            ((BaseActivity) context).dismissLoading();
                            Toast.show(context, msg);

                        }
                    });
        } else if (type == 2) {
            rl_member_vis.setVisibility(View.VISIBLE);
            tv_public_second.setVisibility(View.VISIBLE);
            tv_public_frist.setText("时间");
            tv_public_second.setText("营业收入");
            tv_public_thrid.setText("销售成本");
            tv_public_fourth.setText("日常支出");
            tv_public_fifth.setText("净利润");
            tvPrice.setVisibility(View.GONE);
            tvQchu.setVisibility(View.VISIBLE);
            ((BaseActivity) context).showLoading();
            OrderPaperDetailManager.getOrderInfo().orderMothsDetailApi((BaseActivity) context, begin, new IRequestCallBack<OrderMothsDetailBean>() {
                @Override
                public void onResult(int tag, OrderMothsDetailBean detailBean) {
                    ((BaseActivity) context).dismissLoading();
                    Glide.with(context)
                            .load(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_LOGO))
                            .error(R.drawable.expicture)
                            .into(ivHead);
                    tvName.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK));
                    List<OrderMothsDetailBean.ListBean> list = detailBean.getList();
                    double profit = 0;
                    double income = 0;
                    double spend = 0;
                    double fprice = 0;
                    for (int i = 0; i < list.size(); i++) {
                        profit += Double.parseDouble(list.get(i).getClear_profit());
                        income += Double.parseDouble(list.get(i).getPrimecost_amount());
                        spend += Double.parseDouble(list.get(i).getSales_amount());
                        fprice += Double.parseDouble(list.get(i).getDel_amount());
                    }
                    BigDecimal bg = new BigDecimal(profit);
                    BigDecimal bg2 = new BigDecimal(income);
                    BigDecimal bg3 = new BigDecimal(spend);
                    double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    double value2 = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    double value3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    tvQchu.setText(value3 + "");
                    tvTotalshouc.setText(fprice + "");
                    tv_member_num.setText(value2 + "");
                    tv_member_money.setText(value + "");
                    tvTotalshouc.setTextColor(context.getResources().getColor(R.color.color_FF6537));
                    tv_member_num.setTextColor(context.getResources().getColor(R.color.color_FF6537));
                    tv_member_money.setTextColor(context.getResources().getColor(R.color.color_FF6537));
//                    tvPrice.setText("净利润" + value + "收入 " + value2 + " 支出 " + value3);
                    OrderMothsDetailAdapter adapter = new OrderMothsDetailAdapter(detailBean.getList(), type);
                    rlvDialog.setAdapter(adapter);

                }

                @Override
                public void onFailed(int tag, String msg) {
                    ((BaseActivity) context).dismissLoading();
                    Toast.show(context, msg);

                }
            });

        } else if (type == 3) {
            tv_paper_detail_vip.setVisibility(View.VISIBLE);
            tv_public_frist.setText("时间");
            tv_public_second.setVisibility(View.GONE);
            tv_public_second.setWidth(2);
            tv_public_thrid.setText("单号");
            tv_public_fourth.setText("数量");
            tv_public_fifth.setText("成交额");
            rl_member_vis.setVisibility(View.VISIBLE);
            tvTotalshouc.setVisibility(View.GONE);
            tvQchu.setVisibility(View.GONE);
            tv_spg.setVisibility(View.GONE);
            ((BaseActivity) context).showLoading();
            OrderPaperDetailManager.getOrderInfo().orderMemberDetailApi((BaseActivity) context, guid, new IRequestCallBack<OrderMemberDetailBean>() {
                @Override
                public void onResult(int tag, OrderMemberDetailBean memberDetailBean) {
                    ((BaseActivity) context).dismissLoading();
                    Glide.with(context)
                            .load("")
                            .error(R.mipmap.employee_manage)
                            .placeholder(R.mipmap.employee_manage)
                            .into(ivHead);
                    tvName.setText(memberDetailBean.getNick());
                    tv_paper_detail_vip.setText("(" + memberDetailBean.getGroupname() + ")");
                    tvPrice.setText(memberDetailBean.getMobile());
                    tvPrice.setTextColor(context.getResources().getColor(R.color.textBlack));
                    List<OrderMemberDetailBean.ListsBean> lists = memberDetailBean.getLists();
                    int countNum = 0;
                    double countMoney = 0.00;
                    for (int i = 0; i < lists.size(); i++) {
                        List<OrderMemberDetailBean.ListsBean> data = memberDetailBean.getLists();
                        countNum += Integer.parseInt(data.get(i).getOrder_num());
                        countMoney += Float.parseFloat(data.get(i).getPayment_amount());
                        BigDecimal bg = new BigDecimal(countMoney);
                        double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        java.text.DecimalFormat dfs = new java.text.DecimalFormat("#.00");
                        String format = dfs.format(value);
                        Spanned html = Html.fromHtml("成交额:<font color=\"#FF4F01\">" + format + "</font>");

                        if (value == 0.0) {
                            tv_member_money.setText("0.00");
                        } else
                            tv_member_money.setText(html);
                        tv_member_money.setTextSize(14);
                        tv_member_money.setTextColor(context.getResources().getColor(R.color.textBlack));
                    }
                    Spanned html = Html.fromHtml("共<font color=\"#FF4F01\">" + lists.size() + "</font>" + "笔订单");
                    tv_member_num.setText(html);
                    tv_member_num.setTextSize(14);
                    tv_member_num.setTextColor(context.getResources().getColor(R.color.textBlack));
                    OrderMemberDetailAdapter adapter = new OrderMemberDetailAdapter(lists);
                    rlvDialog.setAdapter(adapter);

                }

                @Override
                public void onFailed(int tag, String msg) {
                    ((BaseActivity) context).dismissLoading();
                    Toast.show(context, msg);

                }
            });
        }


    }
}

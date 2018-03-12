package com.poso2o.lechuan.fragment.oa;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.official.OaServiceActivity;
import com.poso2o.lechuan.activity.realshop.OAHelperActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.oa.OaServiceInfo;
import com.poso2o.lechuan.dialog.OaTimeSelectDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.OaServiceManager;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.DateTimeUtil;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by mr zhang on 2018/2/2.
 *
 * 公众号续费界面
 */

public class OaServiceInfoFragment extends BaseFragment {

    private View view;

    //选择篇数
    private TextView oa_set_off_article_num;
    //价格
    private TextView oa_set_off_article_price;

    //自主运营
    private TextView oa_off_self_running;
    //免费
    private TextView off_self_running_price;

    //代运营
    private TextView oa_off_supper_running;
    //费用
    private TextView off_supper_running_price;

    //总费用
    private TextView oa_off_total_pay;
    //计算公式
    private TextView oa_off_total_formula;
    //到期时间
    private TextView packages_time_out;
    //剩余天数
    private TextView oa_time_less;

    //定时发布布局
    private LinearLayout layout_auto_send;
    //选择的时间
    private TextView oa_auto_send_time;
    //时间设置按钮
    private TextView set_auto_send_time;

    //续费
    private TextView continue_fee;

    //时间选择弹窗
    private OaTimeSelectDialog timeSelectDialog;

    private OaServiceInfo oaServiceInfo;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(context, R.layout.fragment_oa_continue_fee,null);
        return view;
    }

    @Override
    public void initView() {
        oa_set_off_article_num = (TextView) view.findViewById(R.id.oa_set_off_article_num);
        oa_set_off_article_price = (TextView) view.findViewById(R.id.oa_set_off_article_price);

        oa_off_self_running = (TextView) view.findViewById(R.id.oa_off_self_running);
        off_self_running_price = (TextView) view.findViewById(R.id.off_self_running_price);

        oa_off_supper_running = (TextView) view.findViewById(R.id.oa_off_supper_running);
        off_supper_running_price = (TextView) view.findViewById(R.id.off_supper_running_price);

        oa_off_total_pay = (TextView) view.findViewById(R.id.oa_off_total_pay);
        oa_off_total_formula = (TextView) view.findViewById(R.id.oa_off_total_formula);
        packages_time_out = (TextView) view.findViewById(R.id.packages_time_out);
        oa_time_less = (TextView) view.findViewById(R.id.oa_time_less);

        layout_auto_send = (LinearLayout) view.findViewById(R.id.layout_auto_send);
        oa_auto_send_time = (TextView) view.findViewById(R.id.oa_auto_send_time);
        set_auto_send_time = (TextView) view.findViewById(R.id.set_auto_send_time);

        continue_fee = (TextView) view.findViewById(R.id.continue_fee);
    }

    @Override
    public void initData() {
        timeSelectDialog = new OaTimeSelectDialog(context,onTimeSelect);
        oaServiceData();
    }

    @Override
    public void initListener() {
        //设置
        set_auto_send_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] times = oa_auto_send_time.getText().toString().split(":");
                timeSelectDialog.setTime(Integer.parseInt(times[0]),Integer.parseInt(times[1]));
                timeSelectDialog.show();
            }
        });

        //续费
        continue_fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent toBuy = new Intent();
                toBuy.setClass(context, OaServiceActivity.class);
                toBuy.putExtra(OaServiceActivity.OA_RUNNING_TYPE,oaServiceInfo.news_operation_mode);
                toBuy.putExtra(OaServiceActivity.OA_SERVICE_DATA,oaServiceInfo.systemService);
                toBuy.putExtra(OaServiceActivity.OA_SERVICE_PRICE,oa_off_total_pay.getText().toString());
                getActivity().startActivityForResult(toBuy,8001);*/
                ((OAHelperActivity)getActivity()).toRenewals();
            }
        });
    }

    private OaTimeSelectDialog.OnTimeSelect onTimeSelect = new OaTimeSelectDialog.OnTimeSelect() {
        @Override
        public void onTimeSelect(int hour, int minute) {
            String h = hour + "";
            if (hour < 10){
                h = "0" + hour;
            }
            String m = minute + "";
            if (minute < 10){
                m = "0" + minute;
            }
            oa_auto_send_time.setText(h + ":" + m);
        }
    };

    private void oaServiceData(){
        showLoading();
        OaServiceManager.getOaServiceManager().oaServiceInfo((BaseActivity) getActivity(), new IRequestCallBack() {

            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                oaServiceInfo = (OaServiceInfo) result;
                setServiceData();
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(getActivity(),msg);
            }
        });
    }

    private void setServiceData(){
        if (oaServiceInfo == null || oaServiceInfo.systemService == null)return;

        oa_set_off_article_num.setText(oaServiceInfo.news_send_num + "");
        Drawable selectLeft = getResources().getDrawable(R.mipmap.item_select);

        if (oaServiceInfo.news_operation_mode == 0){

            oa_off_self_running.setCompoundDrawablesWithIntrinsicBounds(selectLeft,null,null,null);

            off_self_running_price.setVisibility(View.VISIBLE);
            off_supper_running_price.setVisibility(View.INVISIBLE);

            oa_off_total_pay.setText("¥" + NumberFormatUtils.format(oaServiceInfo.systemService.amount));

            oa_off_total_formula.setText("可选文章（" + oaServiceInfo.news_send_num + "篇）*一个月（" +  NumberFormatUtils.format(oaServiceInfo.systemService.news_amount) + "）");
        }else {

            off_self_running_price.setVisibility(View.INVISIBLE);
            off_supper_running_price.setVisibility(View.VISIBLE);

            oa_off_supper_running.setCompoundDrawablesWithIntrinsicBounds(selectLeft,null,null,null);

            off_supper_running_price.setText("¥" + NumberFormatUtils.format(oaServiceInfo.systemService.operation_amount));
            oa_off_total_pay.setText("¥" + NumberFormatUtils.format(ArithmeticUtils.add(Double.parseDouble(oaServiceInfo.systemService.amount),Double.parseDouble(oaServiceInfo.systemService.operation_amount))));

            oa_off_total_formula.setText("可选文章（" + oaServiceInfo.news_send_num + "篇）*一个月（" +  NumberFormatUtils.format(oaServiceInfo.systemService.news_amount) + "）+代运营（" +  NumberFormatUtils.format(oaServiceInfo.systemService.operation_amount) + "）");
        }

        oa_set_off_article_price.setText("¥" + oaServiceInfo.systemService.amount);
        if (TextUtil.isEmpty(oaServiceInfo.news_service_date))oaServiceInfo.news_service_date = "0";
        packages_time_out.setText(oaServiceInfo.news_service_date);
//        packages_time_out.setText(DateTimeUtil.StringToData(oaServiceInfo.news_service_date,"yyyy-MM-dd"));
        oa_time_less.setText("" + oaServiceInfo.news_service_days);
    }

    //提供获取购买信息的方法
    public OaServiceInfo getServiceInfo(){
        return oaServiceInfo;
    }
}

package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.OrderIOnfoStaffDetailBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoPoplStaffManager;
import com.poso2o.lechuan.orderInfoAdapter.OrderInfoStaffDetailAdapter;
import com.poso2o.lechuan.util.Toast;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ${cbf} on 2018/3/19 0019.
 */

public class OrderInfoStaffDetailDialog extends BaseDialog {
    private View view;
    //名字 销售  达成
    private TextView tvPopleName, tvStaffxs, tvStaffdc, tvstaffRw;
    private ImageView ivClose;
    private RecyclerView rlv;

    public OrderInfoStaffDetailDialog(Context context) {
        super(context);
    }

    @Override
    public View setDialogContentView() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_order_staff, null);
        return view;
    }

    @Override
    public void initView() {
        ivClose = (ImageView) findViewById(R.id.iv_paper_click_close);
        tvPopleName = (TextView) findViewById(R.id.tv_staff_name);
        tvStaffxs = (TextView) findViewById(R.id.tv_staff_xs);
        tvStaffdc = (TextView) findViewById(R.id.tv_staff_dc);
        tvstaffRw = (TextView) findViewById(R.id.tv_staff_rw);
        rlv = (RecyclerView) findViewById(R.id.rlv_staff_detail);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        setWindowDispalay(1.0f, 0.7f);
        setCancelable(true);
        getWindow().setWindowAnimations(R.style.BottomInAnimation);
        rlv.setLayoutManager(new LinearLayoutManager(context));
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

    //名字  销售员id
    public void setDiaLogData(final String realname, String czyId) {
        Log.v("cbf", "realname = " + realname + " czyId= " + czyId);
        OrderInfoPoplStaffManager.getsInstance().poplStaffDetailApi((BaseActivity) context, czyId, new IRequestCallBack<OrderIOnfoStaffDetailBean>() {
            @Override
            public void onResult(int tag, OrderIOnfoStaffDetailBean staffDetailBean) {
                ((BaseActivity) context).dismissLoading();
                tvPopleName.setText(realname);
                List<OrderIOnfoStaffDetailBean.ListsBean> list = staffDetailBean.getLists();
                double staff_xs = 0;
                double staff_rw = 0;
                double staff_dc = 0;
                for (int i = 0; i < list.size(); i++) {
                    staff_xs += Double.parseDouble(list.get(i).getOrder_amounts());
                    staff_rw += Double.parseDouble(list.get(i).getAssignments());
                    staff_dc += Double.parseDouble(list.get(i).getCompletion_rate());
                }
                BigDecimal bg1 = new BigDecimal(staff_xs);
                BigDecimal bg2 = new BigDecimal(staff_rw);
                BigDecimal bg3 = new BigDecimal(staff_dc);
                double value1 = bg1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                double value2 = bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                double value3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                tvStaffxs.setText("销售额:" + value1 + "");
                tvstaffRw.setText("任务额:" + value2);
                tvStaffdc.setText("达成率:" + value3 + "%");
                OrderInfoStaffDetailAdapter adapter = new OrderInfoStaffDetailAdapter(list);
                rlv.setAdapter(adapter);

            }

            @Override
            public void onFailed(int tag, String msg) {
                ((BaseActivity) context).dismissLoading();
                Toast.show(context, msg);

            }
        });

    }
}

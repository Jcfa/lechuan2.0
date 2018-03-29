package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.orderInfo.QueryDirBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.orderInfomanager.OrderInfoGoodsManager;
import com.poso2o.lechuan.orderInfoAdapter.DialogQueryDirAdapter;

import java.util.List;

/**
 * Created by ${cbf} on 2018/3/28 0028.
 * 查询所有的畅销商品目录
 */

public class DialogQuerySellDir extends BaseDialog {

    private View view;
    private RecyclerView rlvQ;

    public DialogQuerySellDir(Context context) {
        super(context);
    }

    @Override
    public View setDialogContentView() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_query_sell_dir, null);
        return view;
    }

    @Override
    public void initView() {
        rlvQ = (RecyclerView) findViewById(R.id.rlv_query_dir);

    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM);
        setWindowDispalay(1.0f, 0.7f);
        this.getWindow().setWindowAnimations(R.style.BottomInAnimation);
        rlvQ.setLayoutManager(new LinearLayoutManager(context));
        OrderInfoGoodsManager.getOrderInfo().orderInfoQueryDirApi((BaseActivity) context, new IRequestCallBack<QueryDirBean>() {
            @Override
            public void onResult(int tag, QueryDirBean dirBean) {
                ((BaseActivity) context).dismissLoading();
                List<QueryDirBean.ListBean> list = dirBean.getList();
                DialogQueryDirAdapter adapter = new DialogQueryDirAdapter(list);
                rlvQ.setAdapter(adapter);
            }

            @Override
            public void onFailed(int tag, String msg) {
                ((BaseActivity) context).dismissLoading();

            }
        });
    }

    @Override
    public void initListener() {

    }

    /**
     * 传参使用
     */
    public void setData() {

    }
}

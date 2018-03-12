package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.SelectBankAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.supplier.SupplierBank;
import com.poso2o.lechuan.bean.supplier.SupplierBankBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.VdianSupplierManager;
import com.poso2o.lechuan.util.Toast;

/**
 * 选择银行对话框
 * Created by tenac on 2017/1/5.
 */
public class SelectBankDialog extends BaseDialog {

    private Context context;

    private View view;

    private RecyclerView goodsBankSelectRecycle;
    private SelectBankAdapter selectBankAdapter;

    private View goods_bank_select_exit;
    private View goods_bank_select_cancel;

    /**
     * 单选回调
     */
    private OnBankSelect onBankSelect;

    /**
     * 选中的银行
     */
    private String selectBank;

    /**
     * 单选对话框
     */
    public SelectBankDialog(Context context, String selectBank, OnBankSelect onBankDelete) {
        super(context);
        this.context = context;
        this.selectBank = selectBank;
        this.onBankSelect = onBankDelete;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_goods_bank_select, null);
        return view;
    }

    @Override
    public void initView() {
        setWindowDisplay(0.8, 0.6);
        goods_bank_select_exit = view.findViewById(R.id.goods_bank_select_exit);
        goods_bank_select_cancel = view.findViewById(R.id.goods_bank_select_cancel);
        goodsBankSelectRecycle = (RecyclerView) view.findViewById(R.id.goods_bank_select_recycle);
        RecyclerView.LayoutManager listLayoutManager = new LinearLayoutManager(context);
        goodsBankSelectRecycle.setLayoutManager(listLayoutManager);
        initData();
    }

    public void initData() {
        selectBankAdapter = new SelectBankAdapter(context, this, null);
        goodsBankSelectRecycle.setAdapter(selectBankAdapter);
        selectBankAdapter.setSelectBank(selectBank);
        loadBankDatas();
    }

    private void loadBankDatas() {
        ((BaseActivity) context).showLoading();
        VdianSupplierManager.getInstance().loadBanks((BaseActivity) context, new IRequestCallBack<SupplierBankBean>() {

            @Override
            public void onResult(int tag, SupplierBankBean result) {
                selectBankAdapter.notifyDataSetChanged(result.list);
                ((BaseActivity) context).dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, msg);
                ((BaseActivity) context).dismissLoading();
            }
        });
    }

    @Override
    public void initListener() {
        goods_bank_select_exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        goods_bank_select_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void selectName(SupplierBank item) {
        onBankSelect.onItemClick(item);
        dismiss();
    }

    public interface OnBankSelect {

        void onItemClick(SupplierBank item);
    }
}

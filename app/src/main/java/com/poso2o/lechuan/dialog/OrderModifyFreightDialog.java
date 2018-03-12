package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

/**
 * 修改运费窗口
 * Created by Luo on 2017/12/11.
 */
public class OrderModifyFreightDialog extends BaseDialog {
    private Context context;
    private View view;
    private String freight;
    private EditText dialog_modify_freight_et;
    private TextView dialog_modify_freight_cancel,dialog_modify_freight_confirm;

    /**
     * 回调
     */
    private OnModifyFreightListener onModifyFreightListener;

    public void show(String freight, OnModifyFreightListener onModifyFreightListener) {
        this.onModifyFreightListener = onModifyFreightListener;
        super.show();
        dialog_modify_freight_et.setText(freight);
    }

    public OrderModifyFreightDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_modify_freight, null);
    }

    @Override
    public void initView() {
        setWindowDispalay(0.8f, OUT_TO);
        setCanceledOnTouchOutside(false);

        dialog_modify_freight_et = (EditText) view.findViewById(R.id.dialog_modify_freight_et);
        dialog_modify_freight_cancel = (TextView) view.findViewById(R.id.dialog_modify_freight_cancel);
        dialog_modify_freight_confirm = (TextView) view.findViewById(R.id.dialog_modify_freight_confirm);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //取消
        dialog_modify_freight_cancel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onModifyFreightListener!=null){
                    String freight = dialog_modify_freight_et.getText().toString();
                    if (freight.isEmpty()){
                        freight = "0.00";
                    }
                    onModifyFreightListener.onCancel(freight);
                }
                dismiss();
            }
        });
        //确定
        dialog_modify_freight_confirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onModifyFreightListener!=null){
                    String freight = dialog_modify_freight_et.getText().toString();
                    if (freight.isEmpty()){
                        freight = "0.00";
                    }
                    onModifyFreightListener.onConfirm(freight);
                }
                dismiss();
            }
        });

    }

    public interface OnModifyFreightListener {
        void onConfirm(String freight);
        void onCancel(String freight);
    }
}

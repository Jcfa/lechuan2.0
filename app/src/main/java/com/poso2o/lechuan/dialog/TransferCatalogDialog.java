package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.util.TextUtil;

/**
 * 商品移动到新目录对话框
 * Created by Jaydon on 2017/1/11.
 */
public class TransferCatalogDialog extends BaseDialog {

    private View view;

    private Context context;

    private Catalog catalog;

    private int number;

    private OnTransferListener onTransferListener;

    private TextView move_to_group_number;

    private TextView move_to_group_name;

    private View move_to_group_cancel;

    private View move_to_group_confirm;

    private boolean isVdian;

    public TransferCatalogDialog(Context context, int number, Catalog catalog, boolean isVdian) {
        super(context);
        this.context = context;
        this.number = number;
        this.catalog = catalog;
        this.isVdian = isVdian;
    }

    public void show(OnTransferListener onTransferListener) {
        this.onTransferListener = onTransferListener;
        super.show();
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_move_to_group, null);
    }

    @Override
    public void initView() {
        move_to_group_number = (TextView) view.findViewById(R.id.move_to_group_number);
        move_to_group_name = (TextView) view.findViewById(R.id.move_to_group_name);
        move_to_group_cancel = view.findViewById(R.id.move_to_group_cancel);
        move_to_group_confirm = view.findViewById(R.id.move_to_group_confirm);
        move_to_group_number.setText(Integer.toString(number));
        if (isVdian) {
            move_to_group_name.setText(TextUtil.trimToEmpty(catalog.catalog_name));
        } else {
            move_to_group_name.setText(TextUtil.trimToEmpty(catalog.directory));
        }
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDisplay(0.7, OUT_TO);
    }

    @Override
    public void initListener() {
        // 取消
        move_to_group_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // 确认
        move_to_group_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onTransferListener.onConfirm();
                dismiss();
            }
        });
    }

    public interface OnTransferListener {

        void onConfirm();
    }
}
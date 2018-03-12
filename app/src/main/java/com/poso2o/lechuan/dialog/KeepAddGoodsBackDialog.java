package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

/**
 * Created by lenovo on 2016/12/21.
 */
public class KeepAddGoodsBackDialog extends BaseDialog {
    private View view;
    private Context context;
    private OnKeepAddGoodsBack onKeepAddGoodsBack;

    private TextView add_goods_keep_cancel;
    private TextView add_goods_keep_confirm;

    public KeepAddGoodsBackDialog(Context context, OnKeepAddGoodsBack onKeepAddGoodsBack) {
        super(context);
        this.context = context;
        this.onKeepAddGoodsBack = onKeepAddGoodsBack;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_keep_add_goods_back, null);
    }

    @Override
    public void initView() {
        add_goods_keep_cancel = (TextView) view.findViewById(R.id.add_goods_keep_cancel);
        add_goods_keep_confirm = (TextView) view.findViewById(R.id.add_goods_keep_confirm);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void initListener() {
        add_goods_keep_cancel.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (onKeepAddGoodsBack != null) {
                    onKeepAddGoodsBack.onCancel();
                }
            }
        });
        add_goods_keep_confirm.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (onKeepAddGoodsBack != null) {
                    onKeepAddGoodsBack.onConfirm();
                }
            }
        });

    }

    public interface OnKeepAddGoodsBack {

        void onCancel();

        void onConfirm();
    }
}

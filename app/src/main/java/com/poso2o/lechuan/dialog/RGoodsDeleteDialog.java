package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;


/**
 * 删除商品对话框
 * Created by zhang on 2017/11/24.
 */
public class RGoodsDeleteDialog extends BaseDialog {

    private Context context;
    private View view;

    protected TextView home_delete_number;
    private TextView home_goods_delete_cancel;
    private TextView home_goods_delete_confirm;

    private OnDeleteGoodsListener onDeleteGoodsListener;

    private int deleteNumber;

    public RGoodsDeleteDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_r_goods_delete,null);
        return view;
    }

    @Override
    public void initView() {
        home_delete_number = (TextView) view.findViewById(R.id.home_delete_number);

        home_goods_delete_cancel = (TextView) view.findViewById(R.id.home_goods_delete_cancel);
        home_goods_delete_confirm = (TextView) view.findViewById(R.id.home_goods_delete_confirm);

        home_delete_number.setText(Html.fromHtml("请确认" + "<font color=\"#ff0000\">删除</font>" + "已选中的" + "<font color=\"#ff0000\">" + Integer.toString(deleteNumber) + "</font>" + "件商品"));
    }

    public void show(OnDeleteGoodsListener onDeleteGoodsListener, int deleteNumber) {
        this.onDeleteGoodsListener = onDeleteGoodsListener;
        this.deleteNumber = deleteNumber;
        if (home_delete_number != null) {
            home_delete_number.setText(Html.fromHtml("请确认" + "<font color=\"#ff0000\">删除</font>" + "已选中的" + "<font color=\"#ff0000\">" + Integer.toString(deleteNumber) + "</font>" + "件商品"));
        }
        super.show();
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.7,0.3);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void initListener() {
        home_goods_delete_cancel.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });

        home_goods_delete_confirm.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (onDeleteGoodsListener != null) {
                    onDeleteGoodsListener.onDelete();
                }
                dismiss();
            }
        });
    }

    public interface OnDeleteGoodsListener {

        void onDelete();
    }
}

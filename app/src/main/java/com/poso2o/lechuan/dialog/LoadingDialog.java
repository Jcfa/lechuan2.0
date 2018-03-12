package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;


/**
 * 加载对话框
 * Created by Fynner on 2016/12/30.
 */
public class LoadingDialog extends BaseDialog {

    private Context context;

    private View view;

    private TextView read_message;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_loading, null);
    }

    @Override
    public void initView() {
        setWindowDispalay(0.6, 0.1);
        setDialogGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        read_message = (TextView) view.findViewById(R.id.read_message);
    }

    @Override
    public void initData() {

    }

    /**
     * 设置加载框文本
     */
    public void setMessage(int resId) {
        if (read_message != null) {
            read_message.setText(resId);
        }
    }

    /**
     * 设置加载框文本
     */
    public void setMessage(String text) {
        if (read_message != null && text != null) {
            read_message.setText(text);
        }
    }

    @Override
    public void initListener() {

    }
}

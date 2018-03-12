package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/12/6.
 * 绑定成功弹窗
 */

public class BindSuccessDialog extends BaseDialog {

    private View view;
    private Context context;

    private TextView authority_to_login;

    private OnLoginListener onLoginListener;

    public BindSuccessDialog(Context context,OnLoginListener onLoginListener) {
        super(context);
        this.context = context;
        this.onLoginListener = onLoginListener;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_tips_wc,null);
        return view;
    }

    @Override
    public void initView() {
        authority_to_login = (TextView) view.findViewById(R.id.authority_to_login);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.7,0.4);
    }

    @Override
    public void initListener() {
        authority_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginListener.onLogin();
            }
        });
    }

    public interface OnLoginListener{
        void onLogin();
    }
}

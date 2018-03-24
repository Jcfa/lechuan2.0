package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.util.AppUtil;

/**
 * Created by ${cbf} on 2018/3/23 0023.
 * 主要做退出登录 取消登录的
 */

public class OrderInfoExitApp extends BaseDialog {

    private View view;
    private TextView cancel;
    private TextView confirm;

    public OrderInfoExitApp(Context context) {
        super(context);
    }

    @Override
    public View setDialogContentView() {
        view = LayoutInflater.from(context).inflate(R.layout.popupwindow_tip, null);
        return view;
    }

    @Override
    public void initView() {
        cancel = (TextView) view.findViewById(R.id.cancel);
        confirm = (TextView) view.findViewById(R.id.confirm);

    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM | Gravity.CENTER);
        setWindowDispalay(1.0f, 0.7f);
        setCancelable(true);
        getWindow().setWindowAnimations(R.style.BottomInAnimation);

    }

    @Override
    public void initListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.exitApp((BaseActivity) context, false);
            }
        });

    }
}

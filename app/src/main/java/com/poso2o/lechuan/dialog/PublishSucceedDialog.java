package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;


/**
 * 发布成功对话框
 * Created by lenovo on 2016/12/22.
 */
public class PublishSucceedDialog extends BaseDialog {

    public PublishSucceedDialog(Context context) {
        super(context);
    }

    public void show(OnDismissListener listener) {
        setOnDismissListener(listener);
        super.show();
    }

    @Override
    public View setDialogContentView() {
        return View.inflate(context, R.layout.dialog_publish_succeed, null);
    }

    @Override
    public void initView() {
        setDialogGravity(Gravity.CENTER);
        setWindowDisplay(0.45f, OUT_TO);
        setCanceledOnTouchOutside(false);
        new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((BaseActivity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }
        }.start();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
    }
}
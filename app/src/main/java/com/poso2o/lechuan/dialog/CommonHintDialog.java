package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by Administrator on 2017-12-12.
 */

public class CommonHintDialog extends BaseDialog {
    private Context mContext;
    private View mView;
    private InvitationDistributionDialog.DialogClickCallBack callback;
    private String mMessage = "";

    public CommonHintDialog(Context context, String message, InvitationDistributionDialog.DialogClickCallBack callback) {
        super(context);
        mContext = context;
        mMessage = message;
        this.callback = callback;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_hint_view, null);
        return mView;
    }

    @Override
    public void initView() {
        TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
        tvHint.setText(mMessage);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mView.findViewById(R.id.tv_affirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.setAffirm();
                }
                dismiss();
            }
        });
        mView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}

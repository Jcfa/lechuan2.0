package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.TextUtil;

/**
 * Created by Administrator on 2017-12-12.
 */

public class SettleAccountsDayDialog extends BaseDialog {
    private View mView;
    private Context mContext;
    private DialogClickCallBack callback;
    private String mTitle = "", mHint = "";

    public SettleAccountsDayDialog(Context context, String title, String hint, DialogClickCallBack callback) {
        super(context);
        mContext = context;
        mTitle = title;
        mHint = hint;
        this.callback = callback;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_settle_account_day, null);
        return mView;
    }

    @Override
    public void initView() {
        TextView tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        if (!TextUtil.isEmpty(mTitle)) {
            tvTitle.setText(mTitle);
        }
        TextView tvHint = (TextView) mView.findViewById(R.id.tv_hint);
        if (!TextUtil.isEmpty(mHint)) {
            tvHint.setText(mHint);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mView.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.setAffirm();
                }
                dismiss();
            }
        });
    }

    public interface DialogClickCallBack {
        public void setAffirm();
    }
}

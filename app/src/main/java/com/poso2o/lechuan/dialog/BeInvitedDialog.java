package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * 被邀请分销提示dialog
 * Created by Administrator on 2017-12-11.
 */

public class BeInvitedDialog extends BaseDialog {
    private View mView;
    private Context mContext;
    private DialogClickCallBack callback;
    //    private TextView tvNick, tvRate, tvDiscount;
    private String mNick, mRate, mDiscount;

    public BeInvitedDialog(Context context, String nick, String rate, String discount, DialogClickCallBack callback) {
        super(context);
        mContext = context;
        mNick = nick;
        mRate = rate;
        mDiscount = discount;
        this.callback = callback;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_be_invited_view, null);
        return mView;
    }

    @Override
    public void initView() {
        TextView tvNick = (TextView) mView.findViewById(R.id.tv_nick);
        tvNick.setText(mNick);
        TextView tvRate = (TextView) mView.findViewById(R.id.tv_rate);
        tvRate.setText(mRate);
        TextView tvDiscount = (TextView) mView.findViewById(R.id.tv_discount);
        tvDiscount.setText(mDiscount);
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

    public interface DialogClickCallBack {
        public void setAffirm();
    }
}

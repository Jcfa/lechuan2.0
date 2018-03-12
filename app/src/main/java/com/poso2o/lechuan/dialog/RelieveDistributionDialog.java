package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by Administrator on 2018-01-02.
 */

public class RelieveDistributionDialog extends BaseDialog {
    private View mView;
    private Context mContext;
    private InvitationDistributionDialog.DialogClickCallBack callback;
    private String mNick = "";

    public RelieveDistributionDialog(Context context, String nick, InvitationDistributionDialog.DialogClickCallBack callback) {
        super(context);
        mContext = context;
        mNick = nick;
        this.callback = callback;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_relieve_distribution, null);
        return mView;
    }

    @Override
    public void initView() {
        TextView tvNick = (TextView) mView.findViewById(R.id.tv_nick);
        TextView tvMessage = (TextView) mView.findViewById(R.id.tv_message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvNick.setText(Html.fromHtml("<font color='#F17604'>" + mNick + "</font>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvNick.setText(Html.fromHtml("<font color='#F17604'>" + mNick + "</font>"));
        }
        tvMessage.setText("解除了与您的分销关系。");
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
//        mView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
    }
}

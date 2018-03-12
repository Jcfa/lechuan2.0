package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.NumberUtils;

/**
 * 主动邀请分销提示dialog
 * Created by Administrator on 2017-12-11.
 */

public class InvitationDistributionDialog extends BaseDialog {
    private View mView;
    private Context mContext;
    private DialogClickCallBack callback;
    private String mNick = "";

    public InvitationDistributionDialog(Context context, String nick, DialogClickCallBack callback) {
        super(context);
        mContext = context;
        mNick = nick;
        this.callback = callback;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_invitation_distribution_view, null);
        return mView;
    }

    @Override
    public void initView() {
        TextView tvNick = (TextView) mView.findViewById(R.id.tv_nick);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvNick.setText(Html.fromHtml("是否邀请<font color='#F17604'>" + mNick + "</font>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvNick.setText(Html.fromHtml("是否邀请<font color='#F17604'>" + mNick + "</font>"));
        }
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

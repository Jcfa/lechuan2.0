package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

/**
 * 邀请确认窗口
 * Created by Luo on 2017/1/18.
 */
public class InvitingDialog extends BaseDialog {
    private Context context;
    private View view;
    private TextView dialog_inviting_title,dialog_inviting_close,dialog_inviting_confirm;

    /**
     * 回调
     */
    private OnInvitingListener onInvitingListener;

    public void show(OnInvitingListener onInvitingListener, String title) {
        this.onInvitingListener = onInvitingListener;
        super.show();
        dialog_inviting_title.setText("" + title);
    }

    public InvitingDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_inviting, null);
    }

    @Override
    public void initView() {
        setWindowDispalay(0.8f, OUT_TO);

        dialog_inviting_title = (TextView) view.findViewById(R.id.dialog_inviting_title);
        dialog_inviting_close = (TextView) view.findViewById(R.id.dialog_inviting_close);
        dialog_inviting_confirm = (TextView) view.findViewById(R.id.dialog_inviting_confirm);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //关闭
        dialog_inviting_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //确认
        dialog_inviting_confirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onInvitingListener!=null){
                    onInvitingListener.onConfirm();
                }
                dismiss();
            }
        });

    }

    public interface OnInvitingListener {
        void onConfirm();
    }
}

package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/10/28.
 */

public class DelPublishingDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context context;

    //取消
    private Button del_publishing_cancel;
    //确定
    private Button del_publishing_confirm;

    public DelPublishingDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_tips_del_publishing,null);
        return view;
    }

    @Override
    public void initView() {
        del_publishing_cancel = (Button) view.findViewById(R.id.del_publishing_cancel);
        del_publishing_confirm = (Button) view.findViewById(R.id.del_publishing_confirm);
    }

    @Override
    public void initData() {
        setWindowDispalay(0.7,0.3);
        setDialogGravity(Gravity.CENTER);
    }

    @Override
    public void initListener() {
        del_publishing_cancel.setOnClickListener(this);
        del_publishing_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.del_publishing_cancel:
                dismiss();
                break;
            case R.id.del_publishing_confirm:
                if (onDelPublishingListener != null)onDelPublishingListener.onDelPublishing();
                dismiss();
                break;
        }
    }

    private OnDelPublishingListener onDelPublishingListener;
    public interface OnDelPublishingListener{
        void onDelPublishing();
    }
    public void setOnDelPublishingListener(OnDelPublishingListener onDelPublishingListener){
        this.onDelPublishingListener = onDelPublishingListener;
    }
}

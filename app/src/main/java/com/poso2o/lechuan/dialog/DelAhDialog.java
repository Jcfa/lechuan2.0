package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/10/27.
 */

public class DelAhDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context context;

    //取消
    private Button del_ah_cancel;
    //确定
    private Button del_ah_confirm;

    public DelAhDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_del_ah_tips,null);
        return view;
    }

    @Override
    public void initView() {
        del_ah_cancel = (Button) view.findViewById(R.id.del_ah_cancel);
        del_ah_confirm = (Button) view.findViewById(R.id.del_ah_confirm);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.7,0.3);
    }

    @Override
    public void initListener() {
        del_ah_cancel.setOnClickListener(this);
        del_ah_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.del_ah_cancel:
                dismiss();
                break;
            case R.id.del_ah_confirm:
                if (onDelAhListener != null)onDelAhListener.delAh();
                dismiss();
                break;
        }
    }

    private OnDelAhListener onDelAhListener;
    public interface OnDelAhListener{
        void delAh();
    }
    public void setOnDelAhListener(OnDelAhListener onDelAhListener){
        this.onDelAhListener = onDelAhListener;
    }
}

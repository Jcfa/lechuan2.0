package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/8/21.
 * 删除员工弹窗
 */

public class DelTipsDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context context;

    //提示语
    private TextView del_position_tips;
    //取消
    private TextView del_tips_cancel;
    //确定
    private TextView del_tips_confirm;

    public DelTipsDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_del_position_tips,null);
        return view;
    }

    @Override
    public void initView() {
        del_position_tips = (TextView) view.findViewById(R.id.del_position_tips);
        del_tips_cancel = (TextView) view.findViewById(R.id.del_tips_cancel);
        del_tips_confirm = (TextView) view.findViewById(R.id.del_tips_confirm);
    }

    @Override
    public void initData() {
        setWindowDispalay(0.7,0.24);
        setDialogGravity(Gravity.CENTER);
    }

    @Override
    public void initListener() {
        del_tips_cancel.setOnClickListener(this);
        del_tips_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.del_tips_cancel:
                dismiss();
                break;
            case R.id.del_tips_confirm:
                if (onDelListener != null)onDelListener.onDelClick();
                break;
        }
    }

    public void setTips(String tips){
        del_position_tips.setText(tips);
    }

    private OnDelListener onDelListener;
    public interface OnDelListener{
        void onDelClick();
    }
    public void setOnDelListener(OnDelListener onDelListener){
        this.onDelListener = onDelListener;
    }
}

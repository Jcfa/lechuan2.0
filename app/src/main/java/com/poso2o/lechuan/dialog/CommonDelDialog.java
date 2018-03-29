package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/11/2.
 * 一般提示弹窗
 */

public class CommonDelDialog extends BaseDialog implements View.OnClickListener {

    private View view;
    private Context context;

    //提示框标题
    private TextView tips_title;
    //提示语
    private TextView common_tips;
    //取消
    private Button common__cancel;
    //确定
    private Button common_confirm;

    public CommonDelDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_tips_del_common, null);
        return view;
    }

    @Override
    public void initView() {
        tips_title = (TextView) view.findViewById(R.id.tips_title);

        common_tips = (TextView) view.findViewById(R.id.common_tips);

        common__cancel = (Button) view.findViewById(R.id.common__cancel);

        common_confirm = (Button) view.findViewById(R.id.common_confirm);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDisplay(0.7, OUT_TO);
    }

    @Override
    public void initListener() {
        common__cancel.setOnClickListener(this);
        common_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common__cancel:
                dismiss();
                break;
            case R.id.common_confirm:
                if (onCommonOkListener == null) dismiss();
                onCommonOkListener.onOkClick();
                break;
        }
    }

    public void setTips(String tips) {
        common_tips.setText(tips);
    }

    public void setTips(String title,int title_size,String tips,int tips_size){
        tips_title.setText(title);
        tips_title.setTextSize(title_size);

        common_tips.setText(tips);
        common_tips.setTextSize(tips_size);
    }

    private OnCommonOkListener onCommonOkListener;

    public interface OnCommonOkListener {
        void onOkClick();
    }

    public void setOnCommonOkListener(OnCommonOkListener onCommonOkListener) {
        this.onCommonOkListener = onCommonOkListener;
    }
}

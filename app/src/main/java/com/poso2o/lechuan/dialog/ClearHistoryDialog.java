package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2017/10/24.
 */

public class ClearHistoryDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context context;

    //取消
    private TextView cancel_clear_history;
    //确定
    private TextView confirm_clear_history;

    public ClearHistoryDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_clear_art_history,null);
        return view;
    }

    @Override
    public void initView() {
        cancel_clear_history = (TextView) view.findViewById(R.id.cancel_clear_history);
        confirm_clear_history = (TextView) view.findViewById(R.id.confirm_clear_history);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.7,0.3);
    }

    @Override
    public void initListener() {
        cancel_clear_history.setOnClickListener(this);
        confirm_clear_history.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_clear_history:
                dismiss();
                break;
            case R.id.confirm_clear_history:
                if (onClearHistoryListener != null){
                    onClearHistoryListener.onClearHistory();
                }
                dismiss();
                break;
        }
    }

    private OnClearHistoryListener onClearHistoryListener;
    public interface OnClearHistoryListener{
        void onClearHistory();
    }
    public void setOnClearHistoryListener(OnClearHistoryListener onClearHistoryListener){
        this.onClearHistoryListener = onClearHistoryListener;
    }
}

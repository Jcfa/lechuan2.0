package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by mr zhang on 2017/12/6.
 */

public class TipsNoAuthorDialog extends BaseDialog {

    private View view;
    private Context context;

    private TextView tips_i_see;

    public TipsNoAuthorDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_tips_no_author,null);
        return view;
    }

    @Override
    public void initView() {
        tips_i_see = (TextView) view.findViewById(R.id.tips_i_see);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDispalay(0.8,0.3);
    }

    @Override
    public void initListener() {
        tips_i_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}

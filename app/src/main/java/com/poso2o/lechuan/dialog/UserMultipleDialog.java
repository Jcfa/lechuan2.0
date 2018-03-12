package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;

import com.poso2o.lechuan.R;

/**
 * 用户类型选择，选择商家还是分销员登录
 * Created by Administrator on 2017-12-08.
 */

public class UserMultipleDialog extends BaseDialog {
    private Context mContext;
    private View mView;
    private IChooseCallback iChooseCallback;

    public UserMultipleDialog(Context context, IChooseCallback iChooseCallback) {
        super(context);
        mContext = context;
        this.iChooseCallback = iChooseCallback;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_multiple_view, null);
        return mView;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        findViewById(R.id.layout_merchant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iChooseCallback != null) {
                    iChooseCallback.merchant();
                }
            }
        });
        findViewById(R.id.layout_distribution).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iChooseCallback != null) {
                    iChooseCallback.distribution();
                }
            }
        });
    }

    public interface IChooseCallback {
        public void merchant();

        public void distribution();
    }
}

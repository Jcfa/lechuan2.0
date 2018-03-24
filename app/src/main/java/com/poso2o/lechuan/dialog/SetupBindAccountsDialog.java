package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.shopdata.ShopData;
import com.poso2o.lechuan.util.Toast;

/**
 * 设置绑定收款银行帐号
 * <p>
 * Created by Jaydon on 2018/3/15.
 */
public class SetupBindAccountsDialog extends BaseDialog {
//    private Context mContext;
    private ShopData shopData;

    private Callback callback;

    public SetupBindAccountsDialog(Context context, ShopData shopData) {
        super(context);
//        this.mContext = context;
        this.shopData = shopData;
    }

    public void show(Callback callback) {
        super.show();
        this.callback = callback;
    }

    @Override
    public View setDialogContentView() {
        return View.inflate(context, R.layout.dialog_setup_bind_accounts, null);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        findView(R.id.bind_account_open_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callback.onResult();
            }
        });
        findView(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface Callback {

        void onResult();

        void onCancel();
    }
}

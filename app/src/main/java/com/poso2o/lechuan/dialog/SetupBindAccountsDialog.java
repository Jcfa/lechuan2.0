package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.shopdata.ShopData;

/**
 * 设置绑定收款银行帐号
 *
 * Created by Jaydon on 2018/3/15.
 */
public class SetupBindAccountsDialog extends BaseDialog {

    private ShopData shopData;

    private Callback callback;

    public SetupBindAccountsDialog(Context context, ShopData shopData) {
        super(context);
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

    }

    public interface Callback {

        void onResult();

        void onCancel();
    }
}

package com.poso2o.lechuan.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.views.CountDownTimerButton;

/**
 * Created by Administrator on 2017-12-02.
 */

public class EditPhoneActivity extends BaseActivity {
    public static final int PHONE_CODE = 1;
    public static final String KEY_PHONE = "phone";
    private EditText etAccount, etVerificationCode;
    private CountDownTimerButton btnGet;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_phone;
    }

    @Override
    protected void initView() {
        setTitle("更改手机号");
        etAccount = findView(R.id.et_account);
        etVerificationCode = findView(R.id.et_verification_code);
        btnGet = findView(R.id.tv_get_code);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            etAccount.setText(bundle.getString(KEY_PHONE));
        }
    }

    @Override
    protected void initListener() {
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerificationCode();
            }
        });
        findView(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePhone();
            }
        });
    }


    /**
     * 获取验证码
     */
    private void getVerificationCode() {
        if (checkPhone()) {
            String account = etAccount.getText().toString().trim();
            WaitDialog.showLoaddingDialog(activity, "正在发送...");
            btnGet.setEnabled(false);
            UserDataManager.getUserDataManager().getVerificationCode(activity, account, new IRequestCallBack<String>() {
                @Override
                public void onFailed(int tag, String object) {
                    btnGet.stopTimer();
                    btnGet.setEnabled(true);
                }

                @Override
                public void onResult(int tag, String object) {
                    btnGet.startTimer();//发送验证码成功，开始倒计时
                }
            });
        }
    }

    /**
     * 保存修改手机号
     */
    private void savePhone() {
        if (checkPhone()) {
            String verificationCode = etVerificationCode.getText().toString().trim();
            if (TextUtil.isEmpty(verificationCode)) {
                etVerificationCode.setError("请输入认证码！");
                etVerificationCode.requestFocus();
                return;
            }
            final String account = etAccount.getText().toString().trim();
            WaitDialog.showLoaddingDialog(activity);
            MineDataManager.getMineDataManager().editPhone(activity, account, verificationCode, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, object.toString());
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_MOBILE, account);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    private boolean checkPhone() {
        String account = etAccount.getText().toString().trim();
        if (TextUtil.isEmpty(account)) {
            etAccount.setError("请输入手机号码！");
            etAccount.requestFocus();
            return false;
        }
        if (!TextUtil.isMobile(account)) {
            etAccount.setError("请输入正确手机号码！");
            etAccount.requestFocus();
            return false;
        }
        return true;
    }
}

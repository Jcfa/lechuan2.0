package com.poso2o.lechuan.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.login.LoginBean;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.views.CountDownTimerButton;

/**
 * 忘记密码
 * Created by Administrator on 2017-12-18.
 */

public class ForgetPasswordActivity extends BaseActivity {
    private EditText etAccount, etVerificationCode, etPassword;
    private CountDownTimerButton btnGet;
    public static final String KEY_PHONE = "phone";
    private ImageView ivShow;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forget;
    }

    @Override
    protected void initView() {
        setTitle("忘记密码");
        ivShow = findView(R.id.iv_show);
        etAccount = findView(R.id.et_account);
        etPassword = findView(R.id.et_password);
        etVerificationCode = findView(R.id.et_verification_code);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String phone = bundle.getString(KEY_PHONE);
            etAccount.setText(phone);
        }
        findView(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        btnGet = findView(R.id.tv_get_code);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerificationCode();
            }
        });

        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ivShow.isSelected()) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivShow.setSelected(false);
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShow.setSelected(true);
                }
                etPassword.setSelection(etPassword.length());
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    /**
     * 重置密码
     */
    private void resetPassword() {
        if (verifyRegister()) {
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String verificationCode = etVerificationCode.getText().toString().trim();
            WaitDialog.showLoaddingDialog(activity, "正在重置密码...");
            UserDataManager.getUserDataManager().resetPassowrd(activity, account, password, verificationCode, new IRequestCallBack<LoginBean>() {

                @Override
                public void onFailed(int tag, String object) {

                }

                @Override
                public void onResult(int tag, LoginBean object) {
                    /**
                     * 重置成功
                     */
                    startActivity(new Intent(activity, LoginActivity.class));

                }
            });
        }
    }

    /**
     * 验证注册信息
     */
    private boolean verifyRegister() {
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
        String verificationCode = etVerificationCode.getText().toString().trim();
        if (TextUtil.isEmpty(verificationCode)) {
            etVerificationCode.setError("请输入验证码！");
            etVerificationCode.requestFocus();
            return false;
        }
        String password = etPassword.getText().toString().trim();
        if (TextUtil.isEmpty(password)) {
            etPassword.setError("请输入登录密码！");
            etPassword.requestFocus();
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            etPassword.setError("请输入6-16位密码！");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 获取验证码
     */
    private void getVerificationCode() {
        String account = etAccount.getText().toString().trim();
        if (TextUtil.isEmpty(account)) {
            etAccount.setError("请输入手机号码！");
            etAccount.requestFocus();
            return;
        }
        if (!TextUtil.isMobile(account)) {
            etAccount.setError("请输入正确手机号码");
            etAccount.requestFocus();
            return;
        }
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

package com.poso2o.lechuan.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.login.LoginBean;
import com.poso2o.lechuan.bean.login.WXUserInfo;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.views.CountDownTimerButton;

/**
 * Created by Administrator on 2017-11-27.
 */

public class RegisterActivity extends BaseActivity {
    private EditText etAccount, etVerificationCode, etPassword;
    private WXUserInfo mWXInfo;
    private CountDownTimerButton btnGet;
    private String mOpenId = "";
    public static final String KEY_OPENID = "openId";
    private ImageView ivShow;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        setTitle("注册");
        if (bundle != null) {
            mWXInfo = (WXUserInfo) bundle.getSerializable(KEY_OPENID);
            if (mWXInfo != null)
                mOpenId = mWXInfo.getOpenid();
            setTitle("微信绑定");
        }
        ivShow = findView(R.id.iv_show);
        etAccount = findView(R.id.et_account);
        etPassword = findView(R.id.et_password);
        etVerificationCode = findView(R.id.et_verification_code);
        findView(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });
        btnGet = findView(R.id.tv_get_code);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerificationCode();
            }
        });
        findView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(StartActivity.class);
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
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

    /**
     * 注册
     */
    private void doRegister() {
        if (verifyRegister()) {
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String verificationCode = etVerificationCode.getText().toString().trim();
            WaitDialog.showLoaddingDialog(activity, "正在注册...");
            UserDataManager.getUserDataManager().doRegister(activity, account, password, verificationCode, mWXInfo, new IRequestCallBack<LoginBean>() {

                @Override
                public void onFailed(int tag, String object) {

                }

                @Override
                public void onResult(int tag, LoginBean object) {
                    /**
                     * 注册成功返回登录信息
                     */
                    startActivity(new Intent(activity, MainActivity.class));

                }
            });
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            startActivity(StartActivity.class);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}

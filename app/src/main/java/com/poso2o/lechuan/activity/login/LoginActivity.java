package com.poso2o.lechuan.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.activity.orderinfo.OrderInfoMainActivity;
import com.poso2o.lechuan.activity.realshop.RShopMainActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.login.LoginBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.UserMultipleDialog;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.TextUtil;

/**
 * 登录页面
 * Created by Administrator on 2017-11-28.
 */

public class LoginActivity extends BaseActivity {
    private EditText etAccount, etPassword;
    private CheckBox checkBoxRemember;
    private ImageView ivClear, ivShow;
    private boolean mExist = false;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mExist = getIntent().getBooleanExtra(SharedPreferencesUtils.TAG_EXIT, false);
        setTitle("登录");
        ivClear = findView(R.id.iv_clear);
        ivShow = findView(R.id.iv_show);
        etAccount = findView(R.id.et_account);
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etAccount.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_MOBILE));
        etAccount.setSelection(etAccount.length());
        etPassword = findView(R.id.et_password);
        etPassword.setText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_REMEMBER_PASSWORD));
        checkBoxRemember = (CheckBox) findViewById(R.id.checkbox_remember);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAccount.setText("");
                etPassword.setText("");
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
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
//        findView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(StartActivity.class);
//                finish();
//            }
//        });
        findView(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(ForgetPasswordActivity.KEY_PHONE, etAccount.getText().toString().trim());
                Intent intent = new Intent(activity, ForgetPasswordActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        String uid = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_ID);
//        String token = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_TOKEN);
        String token = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_PASSWORD);
        if (!TextUtil.isEmpty(uid) && !TextUtil.isEmpty(token) && !mExist) {//已经登录过直接去首页
//            if (SharedPreferencesUtils.getInt(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE) {
            toRShopMainActivity();
//            } else {
//                toMainActivity();
//            }
        }
    }

    /**
     * 登录
     */
    private void doLogin() {
        if (verifyLogin()) {
            boolean remember = checkBoxRemember.isChecked();
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            WaitDialog.showLoaddingDialog(activity, "正在登录...");
            UserDataManager.getUserDataManager().doLogin(this, account, password, remember, new IRequestCallBack<LoginBean>() {
                @Override
                public void onFailed(int tag, String object) {

                }

                @Override
                public void onResult(int tag, LoginBean object) {
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, object.user_type);
//                    switch (object.user_type) {
//                        case Constant.MERCHANT_TYPE://商家
                    toRShopMainActivity();
//                            break;
//                        case Constant.DISTRIBUTION_TYPE://分销员
//                            toMainActivity();
//                            break;
//                        case Constant.COMMON_TYPE://普通用户
//                            toMainActivity();
//                            break;
//                    }
                }
            });
        }
    }

    /**
     * 乐传首页
     */
    private void toMainActivity() {
//        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, Constant.DISTRIBUTION_TYPE);//保存当前登录身份为分销员
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    /**
     * 老板管理首页
     */
    private void toRShopMainActivity() {
//        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_SELECTED_TYPE, Constant.MERCHANT_TYPE);//保存当前登录身份为商家
//        startActivity(new Intent(activity, RShopMainActivity.class));
        startActivity(new Intent(activity, OrderInfoMainActivity.class));
        finish();
    }

    /**
     * 选择商家或分销员
     */
    private void showMultipleDialog() {
        UserMultipleDialog dialog = new UserMultipleDialog(activity, new UserMultipleDialog.IChooseCallback() {
            @Override
            public void merchant() {
                toRShopMainActivity();
            }

            @Override
            public void distribution() {
                toMainActivity();
            }
        });
        dialog.show();
    }

    /**
     * 验证登录信息
     */
    private boolean verifyLogin() {
        String account = etAccount.getText().toString().trim();
        if (TextUtil.isEmpty(account)) {
            etAccount.setError("请输入登录帐号！");
            etAccount.requestFocus();
            return false;
        }
        if (!TextUtil.isMobile(account)) {
            etAccount.setError("请输入正确帐号！");
            etAccount.requestFocus();
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            startActivity(StartActivity.class);
//            finish();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}

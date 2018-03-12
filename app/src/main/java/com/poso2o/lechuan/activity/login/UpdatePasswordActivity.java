package com.poso2o.lechuan.activity.login;

import android.os.UserManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.main.UserDataManager;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by Administrator on 2017-12-23.
 */

public class UpdatePasswordActivity extends BaseActivity {
    private EditText etOldPassword, etNewPassword, etAgainPassword;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void initView() {
        setTitle("修改密码");
        etOldPassword = findView(R.id.et_password_old);
        etNewPassword = findView(R.id.et_password_new);
        etAgainPassword = findView(R.id.et_password_again);
        final ImageView ivShow = findView(R.id.iv_show);
        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassword(ivShow, etOldPassword);
            }
        });
        final ImageView ivShow2 = findView(R.id.iv_show2);
        ivShow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassword(ivShow2, etNewPassword);
            }
        });
        final ImageView ivShow3 = findView(R.id.iv_show3);
        ivShow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPassword(ivShow3, etAgainPassword);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        findView(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    updatePassword();
                }
            }
        });
    }

    private void updatePassword() {
        String oldPw = etOldPassword.getText().toString().trim();
        String newPw = etNewPassword.getText().toString().trim();
        String phone = SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_MOBILE);
        UserDataManager.getUserDataManager().updatePassowrd(activity, phone, oldPw, newPw, new IRequestCallBack<String>() {
            @Override
            public void onResult(int tag, String result) {
                finish();
            }

            @Override
            public void onFailed(int tag, String msg) {

            }
        });
    }

    private void showPassword(ImageView ivShow, EditText editText) {
        if (ivShow.isSelected()) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivShow.setSelected(false);
        } else {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivShow.setSelected(true);
        }
    }

    private boolean check() {
        String newPw = etNewPassword.getText().toString().trim();
        String againPw = etAgainPassword.getText().toString().trim();
        if (etOldPassword.length() < 6 || etOldPassword.length() > 16) {
            etOldPassword.setError("输入6-16位密码");
            etOldPassword.requestFocus();
            return false;
        }
        if (etNewPassword.length() < 6 || etNewPassword.length() > 16) {
            etNewPassword.setError("输入6-16位密码");
            etNewPassword.requestFocus();
            return false;
        }
        if (!newPw.equals(againPw)) {
            etAgainPassword.setError("2次密码输入不同！");
            etAgainPassword.requestFocus();
            return false;
        }
        return true;
    }

}

package com.poso2o.lechuan.activity.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.dialog.WaitDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.MineDataManager;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

/**
 * Created by Administrator on 2017-12-02.
 */

public class EditTextActivity extends BaseActivity {
    public static final String KEY_TITLE = "title";
    public static final String KEY_VALUE = "value";
    public static final String KEY_TYPE = "type";
    private int mType = 0;
    private String mValue = "";
    public static final int NICK_RESULT = 2, DESCRIPTION_RESULT = 3;
    private EditText etInput;
    private int maxLength = 12;//最大输入长度限制
    private CharSequence chars;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edittext;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setTitle(bundle.getString(KEY_TITLE));
            mType = bundle.getInt(KEY_TYPE);
            mValue = bundle.getString(KEY_VALUE);
        }
        etInput = findView(R.id.et_input);
        etInput.setText(mValue);
        etInput.setSelection(etInput.length());
        TextView tvRight = findView(R.id.tv_title_right);
        tvRight.setText("提交");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });

    }

    @Override
    protected void initData() {
        if (mType == NICK_RESULT) {
            etInput.setLines(1);
            maxLength = 12;
        } else if (mType == DESCRIPTION_RESULT) {
            etInput.setMaxLines(5);
            maxLength = 36;
        }
    }

    @Override
    protected void initListener() {
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chars = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (chars.length() > maxLength) {
                    editable.delete(maxLength, chars.length());
//                    Toast.show(activity, "最多输入" + maxLength + "个字符");
                }
            }
        });
    }

    private void saveInfo() {
        final String content = etInput.getText().toString().trim();
        if (mType == NICK_RESULT) {
            WaitDialog.showLoaddingDialog(activity);
            MineDataManager.getMineDataManager().editNick(activity, content, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, object.toString());
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_NICK, content);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else if (mType == DESCRIPTION_RESULT) {
            WaitDialog.showLoaddingDialog(activity);
            MineDataManager.getMineDataManager().editDescription(activity, content, new IRequestCallBack() {
                @Override
                public void onFailed(int tag, String msg) {
                    Toast.show(activity, msg);
                }

                @Override
                public void onResult(int tag, Object object) {
                    Toast.show(activity, object.toString());
                    SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_DESCRIPTION, content);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }
}

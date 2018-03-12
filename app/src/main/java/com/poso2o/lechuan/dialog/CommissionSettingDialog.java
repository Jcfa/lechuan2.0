package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.NumberUtils;

/**
 * 佣金设置dialog，通用和单品设置
 * Created by Administrator on 2017-12-06.
 */

public class CommissionSettingDialog extends BaseDialog {
    private Context mContext;
    private SettingCallBack callBack;
    private View mView;
    private int mType;
    public static int COMMON_TYPE = 1;//通用佣金
    public static int SINGLE_TYPE = 2;//单品佣金
    private EditText etRate, etDiscount;
    private TextView tvTitle, tvRate;

    public CommissionSettingDialog(Context context, int type, SettingCallBack callBack) {
        super(context);
        mContext = context;
        mType = type;
        this.callBack = callBack;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_commission_setting, null);
        return mView;
    }

    @Override
    public void initView() {
        setWindowDisplay(0.9f, OUT_TO);
        etRate = (EditText) mView.findViewById(R.id.et_rate);
        etDiscount = (EditText) mView.findViewById(R.id.et_discount);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        tvRate = (TextView) mView.findViewById(R.id.tv_rate);
        etRate.addTextChangedListener(rateWatcher);
        etDiscount.addTextChangedListener(discountWatcher);
    }

    @Override
    public void initData() {
        if (mType == COMMON_TYPE) {
            tvTitle.setText("佣金设置");
            tvRate.setText("通用佣金：");
        } else {
            tvTitle.setText("单品佣金");
            tvRate.setText("佣金比例：");
        }
    }

    @Override
    public void initListener() {
        mView.findViewById(R.id.tv_affirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null) {
                    String rate = etRate.getText().toString().trim();
                    String discount = etDiscount.getText().toString().trim();
                    float f_rate = NumberUtils.toFloat(rate);
                    float f_discount = NumberUtils.toFloat(discount);
                    callBack.setFinish(f_rate, f_discount);
                }
                dismiss();
            }
        });
        mView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void init() {

    }

    private TextWatcher rateWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int num = NumberUtils.toInt(charSequence.toString());
            if (num > 100) {
                etRate.setText("100");
                etRate.setSelection(etRate.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher discountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text = charSequence.toString();
            int num = NumberUtils.toInt(text);
            if (num > 100) {
                etDiscount.setText("100");
                etDiscount.setSelection(etDiscount.length());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public interface SettingCallBack {
        public void setFinish(float rate, float discount);
    }
}

package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 通用编辑对话框
 * Created by lenovo on 2016/12/19.
 */
public class UnitSetupDialog extends BaseDialog {
    private Context context;
    private View view;
    private OnUnitSetupListener onUnitSetupListener;

    private EditText unit_setup_base;
    private EditText unit_setup_assist;
    private EditText unit_setup_number;
    private TextView unit_setup_cancel;
    private TextView unit_setup_confirm;

    private String unit;
    private String auxiliary_unit;
    private int auxiliary_unit_number;

    public UnitSetupDialog(Context context, String unit, String auxiliary_unit, int auxiliary_unit_number) {
        super(context);
        this.context = context;
        this.unit = unit;
        this.auxiliary_unit = auxiliary_unit;
        this.auxiliary_unit_number = auxiliary_unit_number;
    }

    public void show(OnUnitSetupListener onUnitSetupListener) {
        this.onUnitSetupListener = onUnitSetupListener;
        super.show();
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_unit_setup, null);
    }

    @Override
    public void initView() {
        unit_setup_base = (EditText) view.findViewById(R.id.unit_setup_base);
        unit_setup_assist = (EditText) view.findViewById(R.id.unit_setup_assist);
        unit_setup_number = (EditText) view.findViewById(R.id.unit_setup_number);
        unit_setup_cancel = (TextView) view.findViewById(R.id.unit_setup_cancel);
        unit_setup_confirm = (TextView) view.findViewById(R.id.unit_setup_confirm);

        unit_setup_base.setText(TextUtil.trimToEmpty(unit));
        unit_setup_assist.setText(TextUtil.trimToEmpty(auxiliary_unit));
        unit_setup_number.setText(Integer.toString(auxiliary_unit_number));
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDisplay(0.9, OUT_TO);
        setCanceledOnTouchOutside(false);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) unit_setup_base.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(unit_setup_base, 0);
                }
            }
        }, 200);
    }

    @Override
    public void initListener() {
        unit_setup_cancel.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });

        unit_setup_confirm.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                unit = unit_setup_base.getText().toString();
                auxiliary_unit = unit_setup_assist.getText().toString();
                auxiliary_unit_number = NumberUtils.toInt(unit_setup_number.getText().toString());

                if (TextUtil.isEmpty(unit)) {
                    Toast.show(context, "请输入单位");
                    return;
                }

                if (auxiliary_unit_number < 1) {
                    Toast.show(context, "数量不能小于1");
                    return;
                }

                if (onUnitSetupListener != null) {
                    onUnitSetupListener.onConfirm(unit, auxiliary_unit, auxiliary_unit_number);
                }
                dismiss();
            }
        });

    }

    public interface OnUnitSetupListener {

        void onConfirm(String unit, String auxiliary_unit, int auxiliary_unit_number);
    }
}

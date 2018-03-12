package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;

/**
 * 编辑目录
 * Created by lenovo on 2016/12/15.
 */
public class EditCatalogDialog extends BaseDialog {

    private Context context;

    private EditText catalog_edit_name;
    private EditText catalog_edit_discount;
    private TextView catalog_edit_cancel;
    private TextView catalog_edit_confirm;

    private OnGroupEditListener onGroupEditListener;

    private Catalog catalog;

    private boolean isVdian;

    public EditCatalogDialog(Context context, boolean isVdian, OnGroupEditListener onGroupEditListener) {
        super(context);
        this.context = context;
        this.isVdian = isVdian;
        this.onGroupEditListener = onGroupEditListener;
    }

    public void show(Catalog catalog) {
        try {
            this.catalog = catalog.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        super.show();
    }

    @Override
    public void dismiss() {
        catalog = new Catalog();
        catalog_edit_name.setText("");
        catalog_edit_discount.setText("100.00");
        catalog_edit_name.clearFocus();
        catalog_edit_discount.clearFocus();
        super.dismiss();
    }

    @Override
    public View setDialogContentView() {
        return View.inflate(context, R.layout.dialog_catalog_edit, null);
    }

    @Override
    public void initView() {
        catalog_edit_name = findView(R.id.catalog_edit_name);
        catalog_edit_discount = findView(R.id.catalog_edit_discount);
        catalog_edit_cancel = findView(R.id.catalog_edit_cancel);
        catalog_edit_confirm = findView(R.id.catalog_edit_confirm);
        View catalog_edit_discount_group = findView(R.id.catalog_edit_discount_group);
        if (!isVdian) {
            catalog_edit_discount_group.setVisibility(GONE);
        }
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);

        if (catalog != null) {
            if (isVdian){
                catalog_edit_name.setText(catalog.catalog_name);
                if (catalog.catalog_name.length() > 0)catalog_edit_name.setSelection(catalog.catalog_name.length());
            }else {
                catalog_edit_name.setText(catalog.directory);
                if (catalog.directory.length() > 0)catalog_edit_name.setSelection(catalog.directory.length());
            }
            catalog_edit_discount.setText(NumberFormatUtils.format(catalog.catalog_discount));
            catalog_edit_confirm.setText(R.string.save);
        } else {
            catalog = new Catalog();
        }

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) catalog_edit_name.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(catalog_edit_name, 0);
                }
            }
        }, 200);
    }

    @Override
    public void initListener() {
        catalog_edit_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        catalog_edit_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                catalog.directory = catalog_edit_name.getText().toString();
                catalog.catalog_name = catalog_edit_name.getText().toString();
                catalog.catalog_discount = NumberUtils.toDouble(catalog_edit_discount.getText().toString());
                if (catalog.directory.equals("")){
                    Toast.show(context,"请输入目录名称");
                    return;
                }
                if (onGroupEditListener != null) {
                    onGroupEditListener.onConfirm(catalog);
                }
            }
        });
    }

    public interface OnGroupEditListener {

        void onCancel();

        void onConfirm(Catalog catalog);
    }
}

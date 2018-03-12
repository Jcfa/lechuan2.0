package com.poso2o.lechuan.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.supplier.Supplier;
import com.poso2o.lechuan.bean.supplier.SupplierBank;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.SelectAddressedDialog;
import com.poso2o.lechuan.dialog.SelectBankDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.vdian.VdianSupplierManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

/**
 * 编辑供应商
 * Created by Jaydon on 2017/8/24.
 */
public class EditSupplierActivity extends BaseActivity {

    private Context context;

    private View edit_supplier_back;

    private TextView edit_supplier_title;

    private View edit_supplier_save;

    /**
     * 编号
     */
    private EditText edit_supplier_code;

    /**
     * 全称
     */
    private EditText edit_supplier_name;

    /**
     * 简称
     */
    private EditText edit_supplier_shortname;

    /**
     * 手机
     */
    private EditText edit_supplier_mobile;

    /**
     * 联系人
     */
    private EditText edit_supplier_contact;

    /**
     * 联系人手机
     */
    private EditText edit_supplier_contact_mobile;

    /**
     * 联系人邮箱
     */
    private EditText edit_supplier_contact_mail;

    /**
     * 地址
     */
    private TextView edit_supplier_address;

    /**
     * 详细地址
     */
    private EditText edit_supplier_address_detail;

    /**
     * 开户银行
     */
    private TextView edit_supplier_bank;

    /**
     * 账户名
     */
    private EditText edit_supplier_bank_name;

    /**
     * 银行卡号
     */
    private EditText edit_supplier_bank_code;

    private Supplier supplier;

    private int type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_supplier;
    }

    @Override
    public void initView() {
        context = this;

        edit_supplier_back = findViewById(R.id.edit_supplier_back);
        edit_supplier_title = (TextView) findViewById(R.id.edit_supplier_title);
        edit_supplier_save = findViewById(R.id.edit_supplier_save);
        edit_supplier_code = (EditText) findViewById(R.id.edit_supplier_code);
        edit_supplier_name = (EditText) findViewById(R.id.edit_supplier_name);
        edit_supplier_shortname = (EditText) findViewById(R.id.edit_supplier_shortname);
        edit_supplier_mobile = (EditText) findViewById(R.id.edit_supplier_phone);

        edit_supplier_contact = (EditText) findViewById(R.id.edit_supplier_contact);
        edit_supplier_contact_mobile = (EditText) findViewById(R.id.edit_supplier_mobile);
        edit_supplier_contact_mail = (EditText) findViewById(R.id.edit_supplier_mail);

        edit_supplier_address = (TextView) findViewById(R.id.edit_supplier_address);
        edit_supplier_address_detail = (EditText) findViewById(R.id.edit_supplier_address_detail);

        edit_supplier_bank = (TextView) findViewById(R.id.edit_supplier_bank);
        edit_supplier_bank_name = (EditText) findViewById(R.id.edit_supplier_bank_name);
        edit_supplier_bank_code = (EditText) findViewById(R.id.edit_supplier_bank_code);

    }

    @Override
    public void initData() {
        type = getIntent().getIntExtra(Constant.TYPE, -1);
        switch (type) {
            case Constant.ADD:
                edit_supplier_title.setText("添加供应商");
                supplier = new Supplier();
                break;

            case Constant.UPDATE:
                supplier = (Supplier) getIntent().getSerializableExtra(Constant.DATA);
                refreshView();
                break;
        }
    }

    private void refreshView() {
        if (supplier != null) {
            edit_supplier_code.setText(TextUtil.trimToEmpty(supplier.supplier_number));
            edit_supplier_name.setText(TextUtil.trimToEmpty(supplier.supplier_name));
            edit_supplier_shortname.setText(TextUtil.trimToEmpty(supplier.supplier_shortname));
            edit_supplier_mobile.setText(TextUtil.trimToEmpty(supplier.supplier_telephone));

            edit_supplier_contact.setText(TextUtil.trimToEmpty(supplier.supplier_contacts));
            edit_supplier_contact_mobile.setText(TextUtil.trimToEmpty(supplier.supplier_contacts_mobile));
            edit_supplier_contact_mail.setText(TextUtil.trimToEmpty(supplier.supplier_mail));
            edit_supplier_address.setText(supplier.province_name + " " + supplier.city_name + " " + supplier.area_name);
            edit_supplier_address_detail.setText(TextUtil.trimToEmpty(supplier.supplier_address));

            edit_supplier_bank.setText(TextUtil.trimToEmpty(supplier.supplier_bank_name));
            edit_supplier_bank_name.setText(TextUtil.trimToEmpty(supplier.supplier_bank_account_id));
            edit_supplier_bank_code.setText(TextUtil.trimToEmpty(supplier.supplier_bank_account_name));
        }
    }

    @Override
    public void initListener() {
        // 返回
        edit_supplier_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 保存
        edit_supplier_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveSupplier();
            }
        });

        // 地址
        edit_supplier_address.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                showSelectAddressDialog();
            }
        });

        // 开户银行
        edit_supplier_bank.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                showSelectBankDialog();
            }
        });
    }

    private void showSelectAddressDialog() {
        SelectAddressedDialog selectAddressedDialog = new SelectAddressedDialog(context, new SelectAddressedDialog.OnSelectAddressListener() {

            @Override
            public void confirm(String provinceId, String provincename, String cityId, String cityname, String areaId, String areaname) {
                supplier.province_id = provinceId;
                supplier.province_name = provincename;
                supplier.city_id = cityId;
                supplier.city_name = cityname;
                supplier.area_id = areaId;
                supplier.area_name = areaname;

                edit_supplier_address.setText(provincename + " " + cityname + " " + areaname);
            }
        });
        selectAddressedDialog.show(supplier.province_id, supplier.province_name, supplier.city_id, supplier.city_name, supplier.area_id, supplier.area_name);
    }

    private void showSelectBankDialog() {
        SelectBankDialog dialog = new SelectBankDialog(context, edit_supplier_bank.getText().toString(), new SelectBankDialog.OnBankSelect() {

            @Override
            public void onItemClick(SupplierBank item) {
                edit_supplier_bank.setText(TextUtil.trimToEmpty(item.bank_name));
            }
        });
        dialog.show();
    }

    /**
     * 保存供应商
     */
    public void saveSupplier() {
        supplier.supplier_number = edit_supplier_code.getText().toString();
        supplier.supplier_name = edit_supplier_name.getText().toString();
        supplier.supplier_shortname = edit_supplier_shortname.getText().toString();
        supplier.supplier_telephone = edit_supplier_mobile.getText().toString();
        supplier.supplier_contacts = edit_supplier_contact.getText().toString();
        supplier.supplier_contacts_mobile = edit_supplier_contact_mobile.getText().toString();
        supplier.supplier_mail = edit_supplier_contact_mail.getText().toString();
        supplier.supplier_address = edit_supplier_address_detail.getText().toString();
        supplier.supplier_bank_name = edit_supplier_bank.getText().toString();
        supplier.supplier_bank_account_id = edit_supplier_bank_code.getText().toString();
        supplier.supplier_bank_account_name = edit_supplier_bank_name.getText().toString();

        // 数据判断
        if (TextUtil.isEmpty(supplier.supplier_shortname)) {
            Toast.show(context, "请输入简称");
            return;
        }
        if (TextUtil.isEmpty(supplier.supplier_name)) {
            Toast.show(context, "请输入全称");
            return;
        }
        if (TextUtil.isEmpty(supplier.supplier_contacts)) {
            Toast.show(context, "请输入联系人");
            return;
        }
        if (TextUtil.isEmpty(supplier.supplier_contacts_mobile)) {
            Toast.show(context, "请输入手机号码");
            return;
        }

        // 判断业务类型
        if (type == Constant.ADD) {
            add();
        } else {
            update();
        }
    }

    private void add() {
        showLoading();
        VdianSupplierManager.getInstance().add(this, supplier, new IRequestCallBack<Supplier>() {

            @Override
            public void onResult(int tag, Supplier result) {
                Intent intent = new Intent();
                intent.putExtra(Constant.DATA, result);
                setResult(RESULT_OK, intent);
                finish();
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }

    private void update() {
        showLoading();
        VdianSupplierManager.getInstance().edit(this, supplier, new IRequestCallBack<Supplier>() {

            @Override
            public void onResult(int tag, Supplier result) {
                Intent intent = new Intent();
                intent.putExtra(Constant.DATA, result);
                setResult(RESULT_OK, intent);
                finish();
                dismissLoading();
            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(activity, msg);
                dismissLoading();
            }
        });
    }
}

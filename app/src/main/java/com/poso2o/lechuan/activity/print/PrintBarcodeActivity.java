package com.poso2o.lechuan.activity.print;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.printer.GoodsBarcodePrintData;
import com.poso2o.lechuan.bean.printer.PrintData;
import com.poso2o.lechuan.bean.printer.PrinterBean;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.printer.PrinterBluetoothActivity;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.PrintBarcodeActivityP;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.PrintGoodsBarcodeView;

import java.util.ArrayList;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by mr zhang on 2017/11/30.
 */
public class PrintBarcodeActivity extends BaseActivity {

    private static final int TO_BLUE_TOOTH_ACTIVITY = 162;
    private static final int PRINT_PERMISSIONS_REQUEST_READ_CONTACTS = 1010;
    private static final int REQUEST_LOCATION_PERMISSION = 11;

    private PrintBarcodeActivityP printBarcodeActivityP;
    private ArrayList<PrintGoodsBarcodeView> printGoodsBarcodeViews; // 商品展示视图

    private Context context;

    /**
     * 返回
     */
    private ImageView print_barcode_back;

    /**
     * 商品详情信息
     */
    private ArrayList<GoodsBarcodePrintData> mGoodsBarcodePrintDatas;

    /**
     * 商品打印信息
     */
    private LinearLayout print_barcode_goods_muns_group;

    /**
     * 打印数量
     */
    private TextView print_barcode_goods_print_total;
    private ImageView print_barcode_goods_print_sub;
    private ImageView print_barcode_goods_print_add;
    private EditText print_barcode_goods_all_print_num;

    private String numEdit;

    //选择货币符号
    private LinearLayout print_barcode_currency_group;
    private TextView print_barcode_currency_group_name;
    private String currency_id;

    /**
     * 全选
     */
    private LinearLayout print_barcode_goods_all_select;
    private ImageView print_barcode_goods_is_select;
    private boolean isAllSelect;

    /**
     * 打印
     */
    private TextView print_barcode_goods_print_config;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_print_barcode;
    }

    @Override
    protected void initView() {
        context = this;

        // 判断sdk版本
        if (SDK_INT > 22) {
            // 判断是否授权了调用相册权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PRINT_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        // 返回
        print_barcode_back = (ImageView) findViewById(R.id.print_barcode_back);

        // 打印数量
        print_barcode_goods_print_total = (TextView) findViewById(R.id.print_barcode_goods_print_total);
        print_barcode_goods_print_sub = (ImageView) findViewById(R.id.print_barcode_goods_print_sub);
        print_barcode_goods_print_add = (ImageView) findViewById(R.id.print_barcode_goods_print_add);
        print_barcode_goods_all_print_num = (EditText) findViewById(R.id.print_barcode_goods_all_print_num);

        // 全选
        print_barcode_goods_all_select = (LinearLayout) findViewById(R.id.print_barcode_goods_all_select);
        print_barcode_goods_is_select = (ImageView) findViewById(R.id.print_barcode_goods_select);

        // 商品打印信息
        print_barcode_goods_muns_group = (LinearLayout) findViewById(R.id.print_barcode_goods_muns_group);

        // 打印
        print_barcode_goods_print_config = (TextView) findViewById(R.id.print_barcode_goods_print_config);

        //选择货币符号
        print_barcode_currency_group = (LinearLayout) findViewById(R.id.print_barcode_currency_group);
        print_barcode_currency_group_name = (TextView) findViewById(R.id.print_barcode_currency_group_name);

    }

    @Override
    protected void initData() {
        printBarcodeActivityP = new PrintBarcodeActivityP(context);
        printGoodsBarcodeViews = new ArrayList<>();

        isAllSelect = false;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getSerializable(Constant.DATA) != null) {
                mGoodsBarcodePrintDatas = (ArrayList<GoodsBarcodePrintData>) bundle.getSerializable(Constant.DATA);
            } else {
                mGoodsBarcodePrintDatas = new ArrayList<>();
            }
        }

        // 获取货币符号
        currency_id = SharedPreferencesUtils.getString("print_currency_id");
        if (currency_id.isEmpty()) currency_id = "￥";
        //currency_id = new SharedPreferencesUtil(context, GprinterBluetoothMemory.GPRINTER_BLUETOOTH).getString(GprinterBluetoothMemory.PRINT_CURRENCY, "￥");
        print_barcode_currency_group_name.setText(currency_id);
        Print.println("currency:"+currency_id);

        initGoodsMunsData();
    }

    @Override
    protected void initListener() {
        // 打印
        print_barcode_goods_print_config.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                // 判断Android版本
                if (SDK_INT > 22) {
                    //判断是否授权了调用定位权限
                    if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                    } else {
                        toBluetoothActivity();
                    }
                } else {
                    toBluetoothActivity();
                }
            }
        });

        // 全选功能
        print_barcode_goods_all_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isAllSelect) {
                    allSelect(false);
                    isAllSelect = false;
                    print_barcode_goods_is_select.setSelected(false);
                } else {
                    isAllSelect = true;
                    allSelect(true);
                    print_barcode_goods_is_select.setSelected(true);
                }
                notifyAllPrintNum();

            }
        });
        // 数量
        print_barcode_goods_all_print_num.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (print_barcode_goods_all_print_num.length() > 2) {
                    print_barcode_goods_all_print_num.setText(numEdit);
                    Toast.show(context, "单个数量不能超过99");
                } else {
                    numEdit = print_barcode_goods_all_print_num.getText().toString().trim();
                }
                if (print_barcode_goods_all_print_num.getText() == null || print_barcode_goods_all_print_num.getText().toString().equals("")) {
                    allSelect(false);
                    isAllSelect = false;
                    print_barcode_goods_is_select.setSelected(false);
                    notifyNum(0);
                } else {
                    int num = Integer.parseInt(print_barcode_goods_all_print_num.getText().toString().trim());
                    if (num > 0) {
                        isAllSelect = true;
                        allSelect(true);
                        print_barcode_goods_is_select.setSelected(true);
                    } else {
                        allSelect(false);
                        isAllSelect = false;
                        print_barcode_goods_is_select.setSelected(false);
                    }
                    notifyNum(num);
                }
                notifyAllPrintNum();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (print_barcode_goods_all_print_num.getText() == null || print_barcode_goods_all_print_num.getText().toString().equals("")) {
                    print_barcode_goods_all_print_num.setText(0 + "");
                }
            }
        });

        print_barcode_goods_print_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i;
                if (print_barcode_goods_all_print_num.getText() == null || print_barcode_goods_all_print_num.getText().toString().trim().equals("")) {
                    i = 0;
                } else {
                    i = Integer.parseInt(print_barcode_goods_all_print_num.getText().toString().trim());
                }

                if (i == 99) {
                    print_barcode_goods_all_print_num.setText(99 + "");
                } else {
                    print_barcode_goods_all_print_num.setText(i + 1 + "");
                    notifyNum(i + 1);
                }

                notifyAllPrintNum();
            }
        });

        print_barcode_goods_print_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i;
                if (print_barcode_goods_all_print_num.getText() == null || print_barcode_goods_all_print_num.getText().toString().trim().equals("")) {
                    i = 0;
                } else {
                    i = Integer.parseInt(print_barcode_goods_all_print_num.getText().toString().trim());
                }
                if (i == 0) {
                    print_barcode_goods_all_print_num.setText(0 + "");
                    Toast.show(context, "数量不能小于0");
                } else {
                    print_barcode_goods_all_print_num.setText(i - 1 + "");
                    notifyNum(i - 1);
                }
                notifyAllPrintNum();
            }
        });

        // 返回
        print_barcode_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        //选择货币符号
        print_barcode_currency_group.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(context, CurrencySelectActivity.class);
                intent.putExtra("currency_id", "" + currency_id);
                startActivityForResult(intent, 2000);
            }
        });
    }

    // 商品条码信息
    private void initGoodsMunsData() {
        if (printGoodsBarcodeViews == null) {
            printGoodsBarcodeViews = new ArrayList<>();
        } else {
            printGoodsBarcodeViews.clear();
        }
        print_barcode_goods_muns_group.removeAllViews();
        for (GoodsBarcodePrintData g : mGoodsBarcodePrintDatas) {
            PrintGoodsBarcodeView printGoodsBarcodeView = new PrintGoodsBarcodeView(context, g);
            printGoodsBarcodeViews.add(printGoodsBarcodeView);
            print_barcode_goods_muns_group.addView(printGoodsBarcodeView.getRootView());
        }
    }

    /**
     * 进入蓝牙选择页面
     */
    private void toBluetoothActivity() {

        if (printBarcodeActivityP.notifyAllPrintNum(printGoodsBarcodeViews) > 0) {

            /*Intent intent = new Intent();
            intent.setClass(context, PortBluetoothActivity.class);
            boolean[] state = getConnectState();
            intent.putExtra(PortBluetoothActivity.CONNECT_STATUS, state);
            startActivityForResult(intent, TO_BLUE_TOOTH_ACTIVITY);*/

            ArrayList<PrintData> printDatas = new ArrayList<PrintData>();
            printDatas = printBarcodeActivityP.initPrintData(printGoodsBarcodeViews, printDatas);

            PrinterBean printerBean = new PrinterBean();
            printerBean.printer_type = 1;// 打印机类型，0：票据，1：标签
            printerBean.ticket_type = 5;// 小票类型：0:打印测试，1：销售单，2：退货单，3：交接班，4:会员充值，5:标签
            printerBean.printer_num = 1;// 打印张数
            printerBean.print_message = "欢迎使用日进斗金店铺管理系统！";// 打印内容
            printerBean.open_casher = 1;// 1/2:开钱箱,3:检测打印机状态
            printerBean.lablePrinterBeans = printDatas;

            Intent intent = new Intent();
            intent.putExtra("printerData",printerBean);//打印数据
            intent.setClass(context, PrinterBluetoothActivity.class);
            ((BaseActivity) context).startActivityForResult(intent, TO_BLUE_TOOTH_ACTIVITY);

        } else {
            Toast.show(context, "请先选择打印项目");
        }


    }

    /**
     * 更新合计的数量
     */
    public void notifyAllPrintNum() {
        print_barcode_goods_print_total.setText(printBarcodeActivityP.notifyAllPrintNum(printGoodsBarcodeViews) + "");
    }

    /**
     * item数量响应变化
     */
    private void notifyNum(int i) {
        printBarcodeActivityP.notifyNum(printGoodsBarcodeViews, i);
    }


    /**
     * 全选功能
     */
    private void allSelect(boolean b) {
        printBarcodeActivityP.allSelect(printGoodsBarcodeViews, b);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == 2001) {
            String currency_id = data.getStringExtra("currency_id");
            this.currency_id = currency_id;
            // 设置货币符号
            SharedPreferencesUtils.put("print_currency_id", currency_id);
            print_barcode_currency_group_name.setText(currency_id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PRINT_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.show(context, "没有权限退出页面");
                    finish();
                }
                break;

            case REQUEST_LOCATION_PERMISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    toBluetoothActivity();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

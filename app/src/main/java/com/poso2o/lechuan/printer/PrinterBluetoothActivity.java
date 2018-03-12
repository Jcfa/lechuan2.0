package com.poso2o.lechuan.printer;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.printer.PrintData;
import com.poso2o.lechuan.bean.printer.PrinterBean;
import com.poso2o.lechuan.printer.command.EscCommand;
import com.poso2o.lechuan.printer.command.GpUtils;
import com.poso2o.lechuan.printer.command.LabelCommand;
import com.poso2o.lechuan.printer.manager.BluetoothManager;
import com.poso2o.lechuan.printer.utils.PrinterUtil;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.CalendarUtil;
import com.poso2o.lechuan.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Vector;

public class PrinterBluetoothActivity extends BaseActivity {

    private PrinterUtil mPrinterUtil;

    private static final int REQUEST_CODE_BLUETOOTH_ON = 666;
    public static final int REQUEST_BLUETOOTH_DEVICE = 2001;
    private PrinterBluetoothActivity activity;
    private PrinterBean mPrinterBean = null;// 打印机打印内容
    private int printer_type = 0;// 打印机类型，0：票据，1：标签

    private LinearLayout printer_bluetooth_activity;
    private Button print_esc_btn, print_lable_btn;

    private TextView printer_message;

    //订单数据
    private ArrayList<OrderDTO> mOrderDTOs;
    //条码数据
    private ArrayList<PrintData> mPrinterData = new ArrayList<PrintData>();

    private BluetoothManager mBluetoothManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_printer_bluetooth;
    }

    @Override
    public void initView() {
        printer_bluetooth_activity = (LinearLayout) findViewById(R.id.printer_bluetooth_activity);
        printer_message = (TextView) findViewById(R.id.printer_message);
        print_esc_btn = (Button) findViewById(R.id.print_esc_btn);
        print_lable_btn = (Button) findViewById(R.id.print_lable_btn);
    }

    @Override
    public void initData() {

        activity = PrinterBluetoothActivity.this;
        mBluetoothManager = BluetoothManager.getInstance();

        // 上一级页面传来的数据
        Intent intent = getIntent();
        this.mPrinterBean = (PrinterBean) intent.getSerializableExtra("printerData");// 打印信息
        this.printer_type = mPrinterBean.printer_type;// 打印机类型，0：票据，1：标签
        this.mOrderDTOs = mPrinterBean.orderDTOs;
        this.mPrinterData = mPrinterBean.lablePrinterBeans;

        mPrinterUtil = new PrinterUtil();

        printer_message.setText("正在打印...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                readyPrint(printer_type);
            }
        }, 100);
    }

    @Override
    public void initListener() {
        printer_bluetooth_activity.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                toBluetoothDeviceActivity();
                return false;
            }
        });
        print_esc_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ///toPrinterTestctivity();
                printer_message.setText("正在打印...");
                //readyPrint(0);
                /*activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此时已在主线程中，可以更新UI了
                        readyPrint(0);
                    }
                });*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readyPrint(0);
                    }
                }, 100);
            }
        });
        print_esc_btn.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                printer_type = 0;
                toBluetoothDeviceActivity();
                return false;
            }
        });

        print_lable_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                printer_message.setText("正在打印...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readyPrint(1);
                    }
                }, 500);
            }
        });
        print_lable_btn.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                printer_type = 1;
                toBluetoothDeviceActivity();
                return false;
            }
        });
    }

    /**
     * 准备打印
     *
     * @param printer_type
     */
    private void readyPrint(int printer_type) {
        if (printer_type == 0) {
            print_esc(0, mOrderDTOs.get(0));
        } else if (printer_type == 1) {
            print_lable(0, mPrinterData.get(0));
        }
        System.out.println("==打印结束==" + CalendarUtil.getDate());
        //closeLoadingDialog();

    }

    private void print_esc(final int num, OrderDTO orderDTO) {
        EscCommand esc = new EscCommand();

        //esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
        //esc.addText(SharedPreferencesUtils.getString(SharedPreferencesUtils.KEY_USER_NICK) + "销售单\n");

        esc = mPrinterUtil.printerCashText(activity,esc, orderDTO);

        // 切割
        esc.addCutPaper();

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        byte[] decode_datas = Base64.decode(str, Base64.DEFAULT);

        //System.out.println("===========-------:"+decode_datas);

        print_esc_btn.setText("正在连接...");

        //byte[] command = new byte[]{(byte)29, (byte)114, (byte)1};
        mBluetoothManager.initPrinter(activity, printer_type, decode_datas, new BluetoothManager.OnPrinterListenner() {
            @Override
            public void onSuccess(String text) {
                Print.println("num:" + num);
                //Print.println("num:" + mOrderDTOs.size());
                if (num + 1 >= mOrderDTOs.size()){
                    messageBox("打印成功");
                    finish();
                }else{
                    print_esc(num+1, mOrderDTOs.get(num+1));
                }
            }
            @Override
            public void onFail(final String text) {
                messageBox(text);
                if (text.equals("系统蓝牙已关闭,点击开启") || text.equals("请配对打印机")){

                }else if(text.equals("请选择票据打印机")) {
                    printer_type = 0;
                    toBluetoothDeviceActivity();
                }else if(text.equals("请选择标签打印机")) {
                    printer_type = 1;
                    toBluetoothDeviceActivity();
                }else{
                    finish();
                }
            }
            @Override
            public void onTips(String text) {
                printer_message.setText(text);
            }
        });

        print_esc_btn.setText("打印票据");

    }

    // 打印标签
    public void print_lable() {

        int print_num = 0;
        for (int i = 0; i < mPrinterData.size(); i++) {
            print_num++;

            Print.println("print_num:"+print_num);
            Print.println("print_num:"+mPrinterData.size());

            //sendLabelX(mPrinterData.get(i),print_num,mPrinterData.size());
        }

    }

    // 重打打印标签
    public void print_lable(final int num, PrintData printData) {

        LabelCommand tsc = new LabelCommand();
        tsc.addCls();// 清除打印缓冲区
        tsc.addSize(40, 30); // 设置标签尺寸，按照实际尺寸设置 设置标签尺寸的宽和高
        tsc.addGap(2); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启

        String currency_id = SharedPreferencesUtils.getString("print_currency_id");// 获取货币符号
        if (currency_id.isEmpty()) currency_id = "￥";

        tsc.addText(20, 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                printData.name);
        tsc.addText(20, 60, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "货号:" + printData.bh);
        tsc.add1DBarcode(20, 90, LabelCommand.BARCODETYPE.CODE128, 80, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0,
                printData.barcode);
        if (printData.spec_name == null){
            tsc.addText(20, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    printData.colorid + "/"+ printData.sizeid + "  " + currency_id + printData.price);
        }else{
            tsc.addText(20, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    printData.spec_name + "  " + currency_id + printData.price);
        }
        for (int i = 0; i < printData.print_num; i++) {
            tsc.addPrint(1, 1); // 打印标签
        }

        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        byte[] decode_datas = Base64.decode(str, Base64.DEFAULT);

        mBluetoothManager.initPrinter(activity, printer_type, decode_datas, new BluetoothManager.OnPrinterListenner() {
            @Override
            public void onSuccess(String text) {
                Print.println("num:" + num);
                //Print.println("num:" + mOrderDTOs.size());
                if (num + 1 >= mPrinterData.size()){
                    messageBox("打印成功");
                    finish();
                }else{
                    print_lable(num+1, mPrinterData.get(num+1));
                }
            }
            @Override
            public void onFail(final String text) {
                messageBox(text);
                if (text.equals("系统蓝牙已关闭,点击开启") || text.equals("请配对打印机")){

                }else if(text.equals("请选择票据打印机")) {
                    printer_type = 0;
                    toBluetoothDeviceActivity();
                }else if(text.equals("请选择标签打印机")) {
                    printer_type = 1;
                    toBluetoothDeviceActivity();
                }else{
                    finish();
                }
            }
            @Override
            public void onTips(String text) {
                printer_message.setText(text);
            }
        });

    }

    /**
     * 提示信息
     */
    private void messageBox(String tips) {
        Toast.makeText(activity, tips, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("===========-------requestCode:" + requestCode);
        System.out.println("===========-------resultCode:" + resultCode);
        // 选择蓝牙设置返回执行事件
        if (requestCode == REQUEST_BLUETOOTH_DEVICE && resultCode == 1000) {
            String printer_name = data.getStringExtra("printer_name");
            String printer_address = data.getStringExtra("printer_address");
            if (!printer_name.isEmpty() && !printer_address.isEmpty()) {
                System.out.println("正在连接打印机：" + printer_name);
                messageBox("正在连接打印机：" + printer_name);

                BluetoothManager.getInstance().stopPrinter(new BluetoothManager.OnStopPrinterListenner() {
                    @Override
                    public void onSuccess(String text) {
                        messageBox("stopPrinter：" + text);
                        readyPrint(printer_type);
                    }

                    @Override
                    public void onFail(String text) {
                        messageBox("stopPrinter：" + text);
                        readyPrint(printer_type);
                    }
                });
            } else {
                messageBox("未选择打印机");
                finish();
            }
        }else if (requestCode == REQUEST_CODE_BLUETOOTH_ON) {
            switch (resultCode) {
                case Activity.RESULT_OK: {// 点击确认按钮 - 平板不执行
                    // 用户选择开启 Bluetooth，Bluetooth 会被开启
                    messageBox("蓝牙已开启");
                    print_esc_btn.setText("正在打印...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            readyPrint(printer_type);
                        }
                    }, 100);
                }
                break;
                // 点击取消按钮或点击返回键
                case Activity.RESULT_CANCELED: {
                    // 用户拒绝打开 Bluetooth, Bluetooth 不会被开启
                    messageBox("拒绝开启蓝牙,无法打印");
                    finish();
                }
                break;
                default:
                    break;
            }
        }
    }

    private void toBluetoothDeviceActivity() {
        //PrinterSPU.setPrinterAddress(activity,"-1","");
        Intent intent = new Intent(activity, BluetoothDeviceActivity.class);
        intent.putExtra("printer_type", "" + printer_type);// 打印类型
        startActivityForResult(intent, REQUEST_BLUETOOTH_DEVICE);
    }

    //返回主界面
    @Override
    public void onBackPressed() {
        Print.println("=======================================================");
        mBluetoothManager.aotoConnectPrinterNum = 10;
        mBluetoothManager.shutdownClient();
        activity.dismissLoading();
        finish();
    }

}

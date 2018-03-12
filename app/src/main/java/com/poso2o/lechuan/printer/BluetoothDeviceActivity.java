package com.poso2o.lechuan.printer;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.printer.bean.BluetoothDeviceBean;
import com.poso2o.lechuan.printer.utils.BluetoothUtil;
import com.poso2o.lechuan.printer.utils.PrinterSPU;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDeviceActivity extends BaseActivity {

    public static final int REQUEST_SelfPermission = 3001;
    private BluetoothDeviceActivity activity;
    private BluetoothAdapter mBluetoothAdapter;

    private String printer_type = "0";// 打印类型：0:收银小票，1:标签/条码，2:厨房小票
    // Debugging
    private static final String DEBUG_TAG = "BluetoothDeviceActivity";
    // Member fields
    private ImageView bluetooth_device_close;
    private TextView bluetooth_device_search;
    private LinearLayout bluetooth_device_groups,bluetooth_device_groups_b;
    private TextView bluetooth_device_cancel;
    private ArrayList<BluetoothDeviceBean> noPairedDevices = new ArrayList<BluetoothDeviceBean>();


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bluetooth_list;
    }

    @Override
    public void initView() {
        bluetooth_device_close = (ImageView) findViewById(R.id.bluetooth_device_close);
        bluetooth_device_search = (TextView) findViewById(R.id.bluetooth_device_search);
        bluetooth_device_groups = (LinearLayout) findViewById(R.id.bluetooth_device_groups);
        bluetooth_device_groups_b = (LinearLayout) findViewById(R.id.bluetooth_device_groups_b);
        bluetooth_device_cancel = (TextView) findViewById(R.id.bluetooth_device_cancel);
    }

    @Override
    protected void initListener() {
        bluetooth_device_close.setOnClickListener(new OnClickListener() {//关闭
            @Override
            public void onClick(View v) {
                finishViewX("","");
            }
        });
        bluetooth_device_search.setOnClickListener(new OnClickListener() {//搜索
            @Override
            public void onClick(View v) {
                BluetoothDevice_Search();
            }
        });
        bluetooth_device_cancel.setOnClickListener(new OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                finishViewX("","");
            }
        });
    }

    @Override
    protected void initData() {

        activity = BluetoothDeviceActivity.this;

        // 上一级页面传来的数据
        Intent intent = getIntent();
        if (intent.getStringExtra("printer_type")!=null){
            printer_type = intent.getStringExtra("printer_type").trim();
        }
        if (printer_type.equals("0")){
            ((TextView) findViewById(R.id.tvTitle)).setText("选择打印机");//选择小票打印机
        }else if (printer_type.equals("1")) {
            ((TextView) findViewById(R.id.tvTitle)).setText("选择标签打印机");
        }else if (printer_type.equals("2")) {
            ((TextView) findViewById(R.id.tvTitle)).setText("选择厨房打印机");
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 获取所有已经绑定的蓝牙设备
        getPairedBluetoothDevice();

        if (!BluetoothUtil.isBluetoothOn()){
            //弹出系统对话框，请求打开蓝牙
            BluetoothUtil.openBluetooth(activity);
        }
        //获取权限
        getPermissionBLUETOOTH();

        IntentFilter intentX = new IntentFilter();
        intentX.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intentX.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intentX.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intentX.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        intentX.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//动作状态发生了变化
        registerReceiver(mFindBlueToothReceiver, intentX);

        //搜索蓝牙设备
        BluetoothDevice_Search();

    }

    // 广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    private BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice btDevice = null; // 创建一个蓝牙device对象
            // 从Intent中获取设备对象
            btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    boolean address_state = true;
                    for (int i = 0; i < noPairedDevices.size(); i++) {
                        if (device.getAddress().equals(noPairedDevices.get(i).deviceAddress)) {
                            address_state = false;
                            break;
                        }
                    }
                    if (address_state) {
                        BluetoothDeviceBean bluetoothDeviceBean = new BluetoothDeviceBean();
                        bluetoothDeviceBean.deviceName = device.getName();
                        bluetoothDeviceBean.deviceAddress = device.getAddress();
                        noPairedDevices.add(bluetoothDeviceBean);
                    }
                    getNoPairedBluetoothDevice();
                }
                // 搜索完成
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                bluetooth_device_search.setText("搜索");
                // 获取所有已经绑定的蓝牙设备
                getPairedBluetoothDevice();
            }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch(blueState){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(DEBUG_TAG, "STATE_OFF 手机蓝牙关闭");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(DEBUG_TAG, "STATE_TURNING_OFF 手机蓝牙正在关闭");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(DEBUG_TAG, "STATE_ON 手机蓝牙开启");
                        //搜索蓝牙设备
                        BluetoothDevice_Search();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(DEBUG_TAG, "STATE_TURNING_ON 手机蓝牙正在开启");
                        break;
                }
                //状态改变时
            }else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (btDevice.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        Log.e(DEBUG_TAG, "******正在配对*****"+btDevice.getName());
                        //onRegisterBltReceiver.onBltIng(btDevice);
                        showLoading("正在配对："+btDevice.getName());
                        setLoadingMessage("正在配对："+btDevice.getName());
                        break;
                    case BluetoothDevice.BOND_BONDED://配对结束
                        Log.e(DEBUG_TAG, "******完成配对*****"+btDevice.getName());
                        //onRegisterBltReceiver.onBltEnd(btDevice);
                        if (PrinterSPU.getPrinterAddress(activity,"pairPrinterAddress").equals(btDevice.getAddress())){
                            PrinterSPU.setPrinterAddress(activity,"pairPrinterAddress","");
                        }
                        showLoading("完成配对");
                        Toast.show(activity,"完成配对");
                        dismissLoading();
                        finishViewX(btDevice.getName(),""+btDevice.getAddress());
                        break;
                    case BluetoothDevice.BOND_NONE://取消配对/未配对/无法配对
                        Log.e(DEBUG_TAG, "******取消配对*****"+btDevice.getName());
                        //onRegisterBltReceiver.onBltNone(btDevice);
                        showLoading("取消配对");
                        dismissLoading();
                        Toast.show(activity,"无法配对");
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 获取所有已经绑定的蓝牙设备
     */
    protected void getPairedBluetoothDevice() {
        // 清空列表布局
        bluetooth_device_groups.removeAllViews();
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        // 数据不为空，填充View到列表
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                BluetoothDeviceView itemView = new BluetoothDeviceView(activity);
                // 将View添加到列表布局里面
                bluetooth_device_groups.addView(itemView.getRootView());
                itemView.bluetooth_device_item_group.setTag(bluetoothDevice);
                itemView.bluetooth_device_item_name.setText(bluetoothDevice.getName());
            }
        }

    }

    /**
     * 获取所有未绑定的蓝牙设备
     */
    protected void getNoPairedBluetoothDevice() {
        // 清空列表布局
        bluetooth_device_groups_b.removeAllViews();
        // 数据不为空，填充View到列表
        if (ListUtils.isNotEmpty(noPairedDevices)) {
            for (BluetoothDeviceBean bluetoothDeviceBean : noPairedDevices) {
                BluetoothDeviceView itemView = new BluetoothDeviceView(activity);
                // 将View添加到列表布局里面
                bluetooth_device_groups_b.addView(itemView.getRootView());
                itemView.bluetooth_device_item_group.setTag(bluetoothDeviceBean);
                itemView.bluetooth_device_item_name.setText(bluetoothDeviceBean.deviceName);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除注册
        unregisterReceiver(mFindBlueToothReceiver);
    }

    // 搜索蓝牙设备
    public void BluetoothDevice_Search() {
        // 如果正在搜索，就先取消搜索
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        bluetooth_device_search.setText("正在扫描....");
        // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
        mBluetoothAdapter.startDiscovery();
    }

    //系统会弹出需要请求权限的对话框
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionBLUETOOTH() {
        //判断sdk版本
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        //Android6.0需要动态申请权限
        if (sdkVersion > 22) {
            //判断是否授权了调用定位权限
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_SelfPermission);
            }
        }
    }

    //接收权限是否请求的请求状态
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SelfPermission:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //finishViewX("","");
                }else{
                    //搜索蓝牙设备
                    BluetoothDevice_Search();
                }
                break;
        }
    }

    public class BluetoothDeviceView extends BaseView {
        View itemView;
        View bluetooth_device_item_group;
        TextView bluetooth_device_item_name;

        public BluetoothDeviceView(Context context) {
            super(context);
        }

        @Override
        public View initGroupView() {
            itemView = View.inflate(context, R.layout.view_bluetooth_device_item, null);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return itemView;
        }

        @Override
        public void initView() {
            bluetooth_device_item_group = itemView.findViewById(R.id.bluetooth_device_item_group);
            bluetooth_device_item_name = (TextView) itemView.findViewById(R.id.bluetooth_device_item_name);
        }

        @Override
        public void initData() {

        }

        @Override
        public void initListener() {
            bluetooth_device_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    mBluetoothAdapter.cancelDiscovery();
                    //已配对
                    if (v.getTag().toString().contains("BluetoothDeviceBean")){
                        //未配对
                        BluetoothDeviceBean bluetoothDeviceBean = (BluetoothDeviceBean) v.getTag();
                        PrinterSPU.setPrinterAddress(activity,"pairPrinterAddress",bluetoothDeviceBean.deviceAddress);
                        Toast.show(activity,"未配对:" + bluetoothDeviceBean.deviceName);
                        noPairedDevices.clear();
                        getNoPairedBluetoothDevice();
                        //搜索蓝牙设备
                        BluetoothDevice_Search();
                    }else{
                        BluetoothDevice bluetoothDevice = (BluetoothDevice) v.getTag();
                        finishViewX(bluetoothDevice.getName(), bluetoothDevice.getAddress());
                        Toast.show(activity,"已配对:" + bluetoothDevice.getName());
                    }
                }
            });
        }
    }

    //返回主界面
    @Override
    public void onBackPressed() {
        Print.println("=======================================================");
        finishViewX("","");
    }

    // 关闭或返回父级页面
    public void finishViewX(String printer_name, String printer_address) {

        //设置配对打印机名称
        if (!printer_name.isEmpty()) {
            BluetoothDevice_Search();
        }

        System.out.println("X:-printer_name-"+printer_name);
        System.out.println("X:-printer_address-"+printer_address);

        PrinterSPU.setPrinterAddress(activity,""+printer_type,printer_address);
        PrinterSPU.setPrinterAddress(activity,"-1",printer_name);

        Intent data=new Intent();
        data.putExtra("printer_name", printer_name);
        data.putExtra("printer_address", printer_address);
        setResult(1000, data);
        activity.finish();

    }

}

package com.poso2o.lechuan.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.poso2o.lechuan.printer.utils.PrinterSPU;
import com.poso2o.lechuan.util.ClsUtils;


public class BluetoothReceiver extends BroadcastReceiver {

    String pin = "0000"; // 此处为你要连接的蓝牙设备的初始密钥，一般为1234或0000
    private final static String DEBUG_TAG = "BluetoothReceiver";

    public BluetoothReceiver() {

    }

    // 广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction(); // 得到action
        Log.e(DEBUG_TAG, "action1="+action);
        BluetoothDevice btDevice = null; // 创建一个蓝牙device对象

        // 获取配对打印机地址
        String pairPrinterAddress = PrinterSPU.getPrinterAddress(context, "pairPrinterAddress");
        Log.e(DEBUG_TAG, "配对的打印机名称pairPrinterAddress："+pairPrinterAddress);

        // 从Intent中获取设备对象
        btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        //判断是否是打印机
        if (btDevice == null || (btDevice.getName() == null) || (btDevice.getName().equals("null")) || (btDevice.getName().equals(""))) {
            return;
        }

        if (BluetoothDevice.ACTION_FOUND.equals(action)) { // 发现设备
            Log.e(DEBUG_TAG, "发现设备:"+"[" + btDevice.getName() + "]" + ":" + btDevice.getAddress());
            try {

                // Gprinter设备如果有多个，第一个搜到的那个会被尝试。
                if (btDevice.getAddress().equals(pairPrinterAddress)) {
                    Log.e(DEBUG_TAG, "BluetoothAddress="+"" + "[" + btDevice.getName() + "-" + btDevice.getAddress() + "]");

                    Log.e(DEBUG_TAG, "BluetoothAddress：ret："+btDevice.getBondState());
                    Log.e(DEBUG_TAG, "BluetoothAddress：ret："+BluetoothDevice.BOND_NONE);

                    if (btDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                        Log.e(DEBUG_TAG, "attemp to bond："+"[" + btDevice.getName() + "-" + btDevice.getAddress() + "]");
                        // 通过工具类ClsUtils,调用createBond方法
                        boolean ret = ClsUtils.createBond(btDevice.getClass(), btDevice);
                        Log.e(DEBUG_TAG, "发现设备：ret："+ret);
                    }


                } else {
                    Log.e(DEBUG_TAG, "提示信息：这个设备不是目标蓝牙设备-发现");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {// 再次得到的action，会等于PAIRING_REQUEST

            Log.e(DEBUG_TAG, "再次得到的action="+action);
            try {
                if (btDevice.getAddress().equals(pairPrinterAddress)) {
                    // if (btDevice.getName().contains("Gprinter_") ||
                    // btDevice.getName().contains("Printer_")) {
                    //Log.e("再次得到的:", "Gprinter");
                    Log.e(DEBUG_TAG, "再次得到的：Gprinter"+action);

                    // 1.确认配对
                    ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);
                    // 2.终止有序广播
                    Log.e(DEBUG_TAG, "isOrderedBroadcast:" + isOrderedBroadcast() + ",isInitialStickyBroadcast:"
                            + isInitialStickyBroadcast());
                    abortBroadcast();// 如果没有将广播终止，则会出现一个一闪而过的配对框。
                    // 3.调用setPin方法进行配对...
                    boolean ret = ClsUtils.setPin(btDevice.getClass(), btDevice, pin);
                    Log.e(DEBUG_TAG, "再次得到的：ret："+ret);

                } else {
                    Log.e(DEBUG_TAG, "提示信息：这个设备不是目标蓝牙设备-再次");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
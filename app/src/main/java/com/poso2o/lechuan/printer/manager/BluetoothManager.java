package com.poso2o.lechuan.printer.manager;

/**
 * Created by Luo on 2017/09/07 0007.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.printer.BluetoothDeviceActivity;
import com.poso2o.lechuan.printer.bean.HadBluetoothDevice;
import com.poso2o.lechuan.printer.command.EscCommand;
import com.poso2o.lechuan.printer.utils.BluetoothUtil;
import com.poso2o.lechuan.printer.utils.PrinterSPU;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * 负责蓝牙相关的业务逻辑
 */
public class BluetoothManager {
    private static BluetoothManager sBluetoothManager; // 防止创建多次，设置为单例
    public static final int REQUEST_BLUETOOTH_DEVICE = 2001;
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//00001101-0000-1000-8000-00805F9B34FB
    private final String tag = "BluetoothXManager";
    private BluetoothAdapter mBluetoothAdapter = null;

    private BluetoothDevice mBluetoothDevice = null;
    private BluetoothSocket mBluetoothSocket = null;
    private OutputStream mOutputStream = null;

    //连接上的打印机集合
    private ArrayList<HadBluetoothDevice> mHadBluetoothDevice;
    //当前连接的打印机
    private HadBluetoothDevice hadBluetoothDevice;

    // --线程类-----------------
    private ClientThread mClientThread;
    private ReadThread mReadThread;

    private OnPrinterListenner mOnPrinterListenner;
    private Activity activity;
    private int printer_type;
    private byte[] command;
    private boolean successBackListenner = false;

    //检测打印机
    private boolean checkPrinterStatus = true;

    //当前打印机状态
    private boolean printerStatus = false;

    //断开打印机监听
    private OnStopPrinterListenner mOnStopPrinterListenner;

    // 自动连接
    private boolean aotoConnectPrinterStatus = false;
    //自动连接打印机次数
    public int aotoConnectPrinterNum = 1;
    // 打印机出错提示
    private String printer_error_tips = "打印出错啦";

    private BluetoothManager() {
        // 通过getInstance()方法获取实例
        mHadBluetoothDevice = new ArrayList<HadBluetoothDevice>();
    }

    /**
     * 获取当前类示例
     */
    public synchronized static BluetoothManager getInstance() {
        if (sBluetoothManager == null) {
            sBluetoothManager = new BluetoothManager();
        }
        return sBluetoothManager;
    }

    /**
     * 初始化打印机
     */
    public synchronized void initPrinter(Activity activity, int printer_type, byte[] command, OnPrinterListenner onPrinterListenner) {
        this.activity = activity;
        this.printer_type = printer_type;
        this.command = command;
        this.mOnPrinterListenner = onPrinterListenner;
        if (!aotoConnectPrinterStatus) {
            this.aotoConnectPrinterNum = 1;
        }
        //TODO 检测蓝牙是否开启 - 弹出系统对话框，请求打开蓝牙
        if (!BluetoothUtil.isBluetoothOn()) {
            BluetoothUtil.openBluetooth(activity);
            if (mOnPrinterListenner != null) {
                showPrinterInfo("系统蓝牙已关闭,点击开启");
                mOnPrinterListenner.onFail(printer_error_tips);
            }
            return;
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.startDiscovery()) {
            Log.d(tag, "启动蓝牙扫描设备...");
        }

        //TODO 检测是否配对蓝牙打印机 - 获取配对打印机
        Log.d(tag, "printer_type:"+printer_type);
        String printerAddress = PrinterSPU.getPrinterAddress(activity, "" + printer_type);
        Log.d(tag, "printerAddress:"+printerAddress);
        if (printerAddress.isEmpty()) {
            showPrinterInfo("请配对打印机");
            toBluetoothDeviceActivity(activity, "" + printer_type);
            if (mOnPrinterListenner != null) {
                //未绑定蓝牙打印机器
                mOnPrinterListenner.onFail(printer_error_tips);
            }
            return;
        }

        hadBluetoothDevice = getHadBluetoothDevice(printerAddress);
        if (hadBluetoothDevice == null) {
            Log.e("BluetoothXManager", "开始连接打印机");

            printerStatus = false;

            mBluetoothDevice = null;
            mBluetoothSocket = null;
            mOutputStream = null;

            //获取所有已配对的设备
            List<BluetoothDevice> devices = BluetoothUtil.getPairedDevices();
            for (BluetoothDevice device : devices) {
                if (device.getAddress().equals(printerAddress)) {
                    mBluetoothDevice = device;
                    break;
                }
            }
            if (mBluetoothDevice == null) {
                showPrinterInfo("请配对打印机");
                toBluetoothDeviceActivity(activity, "" + printer_type);
                if (mOnPrinterListenner != null) {
                    mOnPrinterListenner.onFail(printer_error_tips);
                }
                return;
            }

            //在建立之前调用
            if (mBluetoothAdapter.isDiscovering()) {
                //停止搜索
                mBluetoothAdapter.cancelDiscovery();
            }
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(printerAddress);

            //TODO 连接打印机
            mClientThread = new ClientThread();
            mClientThread.start();

            return;
        } else {
            printerStatus = true;
            mBluetoothDevice = hadBluetoothDevice.bluetoothDevice;
            mBluetoothSocket = hadBluetoothDevice.bluetoothSocket;
            mOutputStream = hadBluetoothDevice.outputStream;
        }

        Log.e("BluetoothXManager", "无需连接打印机");

        //在建立之前调用
        if (mBluetoothAdapter.isDiscovering()) {
            //停止搜索
            mBluetoothAdapter.cancelDiscovery();
        }

        // 发送信息 - 0：不检测打印机状态，1：检测打印机状态
        sendMessageHandle(1);

    }

    /**
     * 获取连接上打印机
     */
    private HadBluetoothDevice getHadBluetoothDevice(String address) {
        HadBluetoothDevice hadBluetoothDevice = null;
        for (int i = 0; i < mHadBluetoothDevice.size(); i++) {
            if (mHadBluetoothDevice.get(i).bluetoothDevice.getAddress().equals(address)) {
                hadBluetoothDevice = new HadBluetoothDevice();
                hadBluetoothDevice.bluetoothDevice = mHadBluetoothDevice.get(i).bluetoothDevice;
                hadBluetoothDevice.bluetoothSocket = mHadBluetoothDevice.get(i).bluetoothSocket;
                hadBluetoothDevice.outputStream = mHadBluetoothDevice.get(i).outputStream;
            }
        }
        return hadBluetoothDevice;
    }

    /**
     * 缓存连接上打印机
     */
    private void addHadBluetoothDevice(BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket, OutputStream outputStream) {
        boolean isNews = true;
        for (int i = 0; i < mHadBluetoothDevice.size(); i++) {
            if (mHadBluetoothDevice.get(i).bluetoothDevice.getAddress().equals(bluetoothDevice.getAddress())) {
                isNews = false;
            }
        }
        if (isNews) {
            HadBluetoothDevice hadBluetoothDevice = new HadBluetoothDevice();
            hadBluetoothDevice.bluetoothDevice = bluetoothDevice;
            hadBluetoothDevice.bluetoothSocket = bluetoothSocket;
            hadBluetoothDevice.outputStream = outputStream;
            mHadBluetoothDevice.add(hadBluetoothDevice);
        }
    }

    /**
     * 删除连接上打印机
     */
    private void delHadBluetoothDevice(String address) {
        for (int i = 0; i < mHadBluetoothDevice.size(); i++) {
            if (mHadBluetoothDevice.get(i).bluetoothDevice.getAddress().equals(address)) {
                mHadBluetoothDevice.remove(i);
            }
        }
    }

    /**
     * TODO 连接打印机 - 客户端线程
     */
    private class ClientThread extends Thread {
        public void run() {
            try {
                showPrinterInfo("正在连接打印机" + aotoConnectPrinterNum + "次");
                //在建立之前调用
                if (mBluetoothAdapter.isDiscovering()) {
                    //停止搜索
                    mBluetoothAdapter.cancelDiscovery();
                }
                if (mBluetoothDevice == null) {
                    printerFail("蓝牙设备为空！");
                    return;
                }
                //通过和服务器协商的uuid来进行连接
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                //Log.e("ClientThread", "请稍候，正在连接打印机:" + mBluetoothDevice.getAddress());
                //如果当前socket处于非连接状态则调用连接
                if (!mBluetoothSocket.isConnected()) {
                    //你应当确保在调用connect()时设备没有执行搜索设备的操作。
                    // 如果搜索设备也在同时进行，那么将会显著地降低连接速率，并很大程度上会连接失败。
                    mBluetoothSocket.connect();
                }
                Log.e("", "已经连接上打印机！可以发送信息。");
            } catch (IOException e) {
                Log.e("ClientThread", e.getMessage());
                showPrinterInfo("正在连接打印机" + aotoConnectPrinterNum + "次");
                try {
                    Log.e("ClientThread", "trying fallback...");
                    mBluetoothSocket = (BluetoothSocket) mBluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mBluetoothDevice, 1);
                    Log.e("ClientThread", "请稍候，正在连接打印机:" + mBluetoothDevice.getAddress());
                    //如果当前socket处于非连接状态则调用连接
                    if (!mBluetoothSocket.isConnected()) {
                        //你应当确保在调用connect()时设备没有执行搜索设备的操作。
                        // 如果搜索设备也在同时进行，那么将会显著地降低连接速率，并很大程度上会连接失败。
                        mBluetoothSocket.connect();
                    }
                    Log.e("ClientThread", "已经连接上打印机！可以发送信息。");
                } catch (Exception e2) {
                    Log.e("ClientThread", "Couldn't establish Bluetooth connection!");
                    printerFail("无法连接蓝牙打印机");// + mBluetoothDevice.getName()
                    return;
                }
            }

            printerStatus = true;

            // 启动接受数据
            mReadThread = new ReadThread();
            mReadThread.start();

            // 发送信息
            Message msg = new Message();
            msg.obj = "";
            msg.what = 3;
            mHandler.sendMessage(msg);

        }
    }

    /**
     * TODO 断开打印机连接 - 停止客户端线程
     */
    public synchronized void shutdownClient() {
        Log.e("shutdownClient", "停止客户端线程shutdownClient()");
        new Thread() {
            public void run() {
                if (mClientThread != null) {
                    mClientThread.interrupt();
                    mClientThread = null;
                }
                if (mReadThread != null) {
                    mReadThread.interrupt();
                    mReadThread = null;
                }
                //mBluetoothAdapter.cancelDiscovery();
                if (mBluetoothSocket != null) {
                    try {
                        mBluetoothSocket.close();
                        Log.e("shutdownClient", "打印机连接已断开mBluetoothSocket.close()");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("shutdownClient", "无法断开打印机连接");
                    }
                }

                if (mOutputStream != null) {
                    try {
                        mOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                printerStatus = false;

                mBluetoothSocket = null;
                mBluetoothDevice = null;

                Message msg = new Message();
                msg.obj = "";
                msg.what = 4;
                mHandler.sendMessage(msg);

            }

            ;
        }.start();
    }

    /**
     * 发送数据
     */
    private synchronized void sendMessageHandle(int type) {
        if (mBluetoothSocket == null) {
            Log.e("server", "打印机没有连接");
            printerFail("打印机没有连接");
            return;
        }
        try {
            if (type == 0) {
                successBackListenner = false;
                mOutputStream = mBluetoothSocket.getOutputStream();
                mOutputStream.write(command);
                mOutputStream.flush();
                onPrinterSuccess("打印成功");
            } else {
                successBackListenner = true;
                queryPrinterStatus();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("server", "PrintFaild:" + e.getMessage());
            printerFail("打印机出错:" + e.getMessage());
        }
    }

    /**
     * 读取数据 - 打印机回调
     */
    private class ReadThread extends Thread {
        public void run() {
            InputStream is = null;
            try {
                if (mBluetoothSocket == null) {
                    Log.e("server", "打印机没有连接");
                    printerFail("打印机没有连接");
                    return;
                }
                is = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
                byte[] e = new byte[64];
                checkPrinterStatus = true;
                connectPrinterOvertime();

                while (printerStatus) {

                    int bytesX = is.read(e);
                    Log.e("ReadThread", "打印机回调:" + bytesX);
                    onPrinterSuccess("打印机回调成功:" + bytesX);

                }

            } catch (IOException e1) {
                //e1.printStackTrace();
                //打印机回调出错
                if (printer_type == 0) {
                    printerFail("请选择票据打印机");
                } else {
                    printerFail("请选择标签打印机");
                }

            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e1) {
                    //e1.printStackTrace();
                    printerFail("打印机回调出错");
                }
            }
        }
    }

    /**
     * 配对打印机界面
     */
    private void toBluetoothDeviceActivity(Activity activity, String printer_type) {
        Intent intent = new Intent(activity, BluetoothDeviceActivity.class);
        intent.putExtra("printer_type", "" + printer_type);// 打印类型
        activity.startActivityForResult(intent, REQUEST_BLUETOOTH_DEVICE);
    }

    /**
     * 检测打印机状态
     */
    public void queryPrinterStatus(Activity activity, int printer_type, OnPrinterListenner onPrinterListenner) {
        byte[] command = new byte[]{(byte) 29, (byte) 114, (byte) 1};
        this.checkPrinterStatus = true;
        initPrinter(activity, printer_type, command, onPrinterListenner);
    }

    public void addSetAutoSatusBack(EscCommand.ENABLE enable) {
        byte[] command = new byte[]{(byte) 29, (byte) 97, (byte) 0};
        if (enable == EscCommand.ENABLE.OFF) {
            command[2] = 0;
        } else {
            command[2] = -1;
        }
        //this.addArrayToCommand(command);
    }

    public void addQueryPrinterType() {
        new String();
        String str = "~!T\r\n";
        //this.addStrToCommand(str);
    }

    public void addQueryPrinterStatus() {
        Vector<Byte> Command = null;
        Command = new Vector(4096, 1024);
        Command.add(Byte.valueOf((byte) 27));
        Command.add(Byte.valueOf((byte) 33));
        Command.add(Byte.valueOf((byte) 63));
    }

    /**
     * 检测打印机状态
     */
    private synchronized void queryPrinterStatus() {
        try {

            checkPrinterStatus = true;
            connectPrinterOvertime();

            mOutputStream = mBluetoothSocket.getOutputStream();
            //byte[] command = new byte[]{(byte) 16, (byte) 4, (byte) 1};
            byte[] command = new byte[]{(byte) 29, (byte) 114, (byte) 1};

            if (printer_type == 1) {
                command = new byte[]{(byte) 27, (byte) 33, (byte) 63};
            }

            mOutputStream.write(command);

            mOutputStream.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //printerFail("检测打印机状态出错:"+e.getMessage());

            Log.e("ReadThread", "检测打印机状态出错:" + e.getMessage());

            showPrinterInfo("检测打印机状态出错");

            Message msg = new Message();
            msg.obj = "检测打印机状态出错";
            msg.what = 5;
            mHandler.sendMessage(msg);

        }
    }

    /**
     * 检测打印机状态超时
     */
    private void connectPrinterOvertime() {

        Log.e("printerFail", "connectPrinterOvertime()");
        /*Message msg = new Message();
        msg.obj = "检测打印机状态超时";
        msg.what = 2;
        mHandler.sendMessage(msg);*/

        if (mOnPrinterListenner == null) return;

        if (delayRun != null) {
            // 每次editText有变化的时候，则移除上次发出的延迟线程
            search_handler.removeCallbacks(delayRun);
        }
        // 延迟800ms，如果不再输入字符，则执行该线程的run方法
        search_handler.postDelayed(delayRun, 3000);

    }

    /**
     * 打印机出错回调
     */
    private void printerFail(String text) {
        Log.e("printerFail", text);
        showPrinterInfo(text);
        this.checkPrinterStatus = false;

        Message msg = new Message();
        msg.obj = text;
        msg.what = 0;
        mHandler.sendMessage(msg);

    }

    /**
     * 打印机成功回调
     *
     * @param text
     */
    private void onPrinterSuccess(String text) {
        Log.e("onPrinterSuccess", text);

        //缓存连接上打印机
        addHadBluetoothDevice(mBluetoothDevice, mBluetoothSocket, mOutputStream);

        showPrinterInfo(text);
        this.checkPrinterStatus = false;
        Message msg = new Message();
        msg.obj = text;
        msg.what = 1;
        mHandler.sendMessage(msg);

    }

    /**
     * 关掉打印机
     */
    public void stopPrinter(OnStopPrinterListenner onStopPrinterListenner) {
        this.mOnStopPrinterListenner = onStopPrinterListenner;
        mOnPrinterListenner = null;
        aotoConnectPrinterNum = 10;
        aotoConnectPrinterStatus = false;

        Message msg = new Message();
        msg.obj = "";
        msg.what = 0;
        mHandler.sendMessage(msg);

    }

    /**
     * 重新连接打印机
     */
    private void aotoConnectPrinter(String info) {
        //Toast.show(activity, "打印机:" + info);
        aotoConnectPrinterNum++;
        Log.e("printerFail", "重新连接打印机:" + aotoConnectPrinterNum + "次");
        if (aotoConnectPrinterNum <= 2) {
            showPrinterInfo("正在重新连接打印机");

            if (delayRunX != null) {
                // 每次editText有变化的时候，则移除上次发出的延迟线程
                search_handlerX.removeCallbacks(delayRunX);
            }
            // 延迟800ms，如果不再输入字符，则执行该线程的run方法
            search_handlerX.postDelayed(delayRunX, 5000);

        } else {
            if (mOnPrinterListenner != null) {
                mOnPrinterListenner.onFail(info);
                mOnPrinterListenner = null;
                mBluetoothDevice = null;
                aotoConnectPrinterStatus = false;
                aotoConnectPrinterNum = 1;
            } else if (mOnStopPrinterListenner != null) {
                mOnStopPrinterListenner.onSuccess(info);
            }
        }
    }

    /**
     * 显示打印机信息
     */
    private void showPrinterInfo(String text) {
        Log.e("printerFail", text);
        printer_error_tips = text;
        Message msg = new Message();
        msg.obj = text;
        msg.what = 6;
        mHandler.sendMessage(msg);

    }

    /**
     * 信息处理
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;
            switch (msg.what) {
                case 0://打印机出错
                    shutdownClient();
                    break;
                case 1:
                    if (successBackListenner) {
                        // 发送信息 - 0：不检测打印机状态，1：检测打印机状态
                        sendMessageHandle(0);
                    }
                    if (mOnPrinterListenner != null) {
                        mOnPrinterListenner.onSuccess(info);
                        //mOnPrinterListenner = null;
                        //mBluetoothDevice = null;
                    }
                    break;
                case 2:
                    if (mOnPrinterListenner == null) return;

                    if (delayRun != null) {
                        // 每次editText有变化的时候，则移除上次发出的延迟线程
                        search_handler.removeCallbacks(delayRun);
                    }
                    // 延迟800ms，如果不再输入字符，则执行该线程的run方法
                    search_handler.postDelayed(delayRun, 5000);

                    break;
                case 3:
                    // 发送信息 - 0：不检测打印机状态，1：检测打印机状态
                    sendMessageHandle(1);
                    break;
                case 4:
                    // 关掉打印机
                    aotoConnectPrinter(printer_error_tips);//"检测到打印机脱机。"
                    break;
                case 5:
                    //连接打印机
                    mClientThread = new ClientThread();
                    mClientThread.start();
                    break;
                case 6:
                    //Toast.show(activity,info);
                    if (mOnPrinterListenner != null) {
                        mOnPrinterListenner.onTips(info);
                    }
                    //((BaseActivity) activity).showLoading(info);
                    ((BaseActivity) activity).setLoadingMessage(info);
                    break;

            }
        }
    };

    /**
     * 打印机回调
     */
    public interface OnPrinterListenner {
        void onSuccess(String text);
        void onFail(String text);
        void onTips(String text);
    }

    /**
     * 关掉打印机回调
     */
    public interface OnStopPrinterListenner {
        void onSuccess(String text);
        void onFail(String text);
    }

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Handler search_handler = new Handler();
    private Runnable delayRun = new Runnable() {
        @Override
        public void run() {

            // 在这里调用服务器的接口，获取数据
            printerStatus = false;

            if (mOnPrinterListenner == null) return;
            Log.e("stopPrinter", "检测打印机状态超时");

            aotoConnectPrinterNum = 10;
            //mOnPrinterListenner.onFail("检测打印机状态超时");

            //搜索
            if (!checkPrinterStatus) return;
            showPrinterInfo("检测打印机状态超时");
            printerFail("检测打印机状态超时");
        }
    };

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Handler search_handlerX = new Handler();
    private Runnable delayRunX = new Runnable() {
        @Override
        public void run() {
            // 在这里调用服务器的接口，获取数据
            /*if (mConnectState) {
                Toast.show(activity, "打印机已连接。");
                return;
            }*/
            aotoConnectPrinterStatus = true;
            //连接打印机
            initPrinter(activity, printer_type, command, mOnPrinterListenner);
        }
    };

}

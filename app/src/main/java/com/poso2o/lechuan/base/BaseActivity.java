package com.poso2o.lechuan.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.event.PayEvent;
import com.poso2o.lechuan.bean.mine.InvitationBean;
import com.poso2o.lechuan.bean.mine.UserInfoBean;
import com.poso2o.lechuan.broadcast.BroadcastManager;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.constant.BroadcastAction;
import com.poso2o.lechuan.dialog.BeInvitedDialog;
import com.poso2o.lechuan.dialog.InvitationDistributionDialog;
import com.poso2o.lechuan.dialog.LoadingDialog;
import com.poso2o.lechuan.dialog.RelieveDistributionDialog;
import com.poso2o.lechuan.http.HttpListener;
import com.poso2o.lechuan.http.HttpResponseListener;
import com.poso2o.lechuan.manager.main.ActivityManager;
import com.poso2o.lechuan.manager.poster.MyFansDataManager;
import com.poso2o.lechuan.version.VersionUpdate;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-24.
 */
public abstract class BaseActivity extends FragmentActivity {

    /**
     * 申请权限
     */
    private static final int REQUEST_PERMISSION = 11;

    public BaseActivity activity;
    private Class goBackActivity;//返回键跳转的activity
    private PhoneOverBroadcast mPhoneOverBroadcast;

    private LoadingDialog loadingDialog;

    /**
     * 广播
     */
    private BroadcastReceiver broadcastReceiver;

    /**
     * 用来标记取消。
     */
    private Object object = new Object();

    /**
     * 请求队列。
     */
    private RequestQueue mQueue;

    /**
     *  请求权限回调
     */
    private OnPermissionListener onPermissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = BaseActivity.this;
        ActivityManager.getActivityManager().addActivity(activity);
        //注册EventBus接收
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // 初始化请求队列，传入的参数是请求并发值。
        mQueue = NoHttp.newRequestQueue(3);
        onCreate2(savedInstanceState);
        setContentView(getLayoutResId());
        initView();
        initData();
        initListener();
    }

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    public void onCreate2(Bundle savedInstanceState) {

    }

    public void onDestroy2() {
    }

    // TODO 广播 =============================================================================

    protected void registerBroadcast(String... actions) {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                onBroadcastReceive(intent);
            }
        };
        BroadcastManager.registerReceiver(broadcastReceiver, actions);
    }

    /**
     * 重写此方法处理广播
     *
     * @param intent
     */
    protected void onBroadcastReceive(Intent intent) {
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPhoneOverBroadcast == null) {
            mPhoneOverBroadcast = new PhoneOverBroadcast();
        }
        //注册广播
        BroadcastManager.registerReceiver(mPhoneOverBroadcast, BroadcastAction.WX_CANCEL_BRODCAST);
    }

    @Override
    public void onStop() {
        super.onStop();
        BroadcastManager.unregisterReceiver(mPhoneOverBroadcast);
    }

    public class PhoneOverBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            switch (intent.getAction()) {
                case BroadcastAction.WX_CANCEL_BRODCAST: //微信分享 - 取消

                    break;
            }
        }

    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        showLoading(R.string.loading_common);
    }

    /**
     * 显示加载框
     */
    public void showLoading(int resId) {
        showLoading(resId, true);
    }

    /**
     * 显示加载框(不可撤销）
     */
    public void showCancelableLoading(int resId) {
        showLoading(resId, false);
    }

    /**
     * 显示加载框
     */
    public void showLoading(int resId, boolean flag) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setCancelable(flag);
        loadingDialog.setMessage(resId);
        loadingDialog.show();
    }

    public void showLoading(String text) {
        showLoading(text, true);
    }

    /**
     * 显示加载框(不可撤销）
     */
    public void showCancelableLoading(String text) {
        showLoading(text, false);
    }

    /**
     * 显示加载框
     */
    public void showLoading(String text, boolean flag) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setCancelable(flag);
        loadingDialog.setMessage(text);
        loadingDialog.show();
    }

    /**
     * 修改Loading的文本
     */
    public void setLoadingMessage(int resId) {
        if (loadingDialog != null) {
            loadingDialog.setMessage(resId);
        }
    }

    /**
     * 修改Loading的文本
     */
    public void setLoadingMessage(String message) {
        if (loadingDialog != null) {
            loadingDialog.setMessage(message);
        }
    }

    /**
     * 隐藏加载框
     */
    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 通过视图ID获取视图引用
     *
     * @param resId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int resId) {
        return (T) findViewById(resId);
    }

    public void setTitle(int resId) {
        setTitle(getString(resId));
    }

    /**
     * 有返回键
     *
     * @param title
     */
    public void setTitle(String title) {
        ((TextView) findView(R.id.tv_title)).setText(title);
        ImageView ivBack = findView(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    public int getColorValue(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 发起请求。
     *
     * @param what      what.
     * @param request   请求对象。
     * @param callback  回调函数。
     * @param canCancel 是否能被用户取消。
     * @param isLoading 实现显示加载框。
     * @param <T>       想请求到的数据类型。
     */
    public <T> void request(int what, com.yanzhenjie.nohttp.rest.Request<T> request, HttpListener<String> callback,
                            boolean canCancel, boolean isLoading) {
        request.setCancelSign(object);
        mQueue.add(what, request, new HttpResponseListener(this, request, callback, canCancel, isLoading));
    }

    private ArrayList<OnDestroyListener> onDestroyListeners = new ArrayList<>();

    public void addOnDestroyListener(OnDestroyListener onDestroyListener) {
        onDestroyListeners.add(onDestroyListener);
    }

    @Override
    protected void onDestroy() {
        // 和声明周期绑定，退出时取消这个队列中的所有请求，当然可以在你想取消的时候取消也可以，不一定和声明周期绑定。
        mQueue.cancelBySign(object);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        // 因为回调函数持有了activity，所以退出activity时请停止队列。
        mQueue.stop();
        // 注销广播
        BroadcastManager.unregisterReceiver(broadcastReceiver);

        for (OnDestroyListener onDestroyListener : onDestroyListeners) {
            onDestroyListener.onDestroy();
        }

        ActivityManager.getActivityManager().finishActivity(activity);
        super.onDestroy();
        onDestroy2();
    }

    /**
     * 回收监听，用来在关闭活动时做一些回收资源的操作
     */
    public interface OnDestroyListener {

        void onDestroy();
    }

    protected void cancelAll() {
        mQueue.cancelAll();
    }

    protected void cancelBySign(Object object) {
        mQueue.cancelBySign(object);
    }

    // TODO 界面跳转

    /**
     * 界面跳转无参数
     */
    public void startActivity(Class<?> cls) {
        startActivity(cls, new Bundle());
    }

    /**
     * 界面跳转单个参数
     */
    public void startActivity(Class<?> cls, Serializable data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, data);
        startActivity(cls, bundle);
    }

    /**
     * 界面跳转带参数
     */
    public void startActivity(Class<?> cls, Bundle data) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(data);
        startActivity(intent);
    }

    /**
     * 界面跳转单个参数
     */
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, new Bundle(), requestCode);
    }

    /**
     * 界面跳转单个参数
     */
    public void startActivityForResult(Class<?> cls, Serializable data, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, data);
        startActivityForResult(cls, bundle, requestCode);
    }

    /**
     * 界面跳转带参数
     */
    public void startActivityForResult(Class<?> cls, Bundle data, int requestCode) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(data);
        startActivityForResult(intent, requestCode);
    }

    // TODO 权限相关

    /**
     * 申请权限，有权限直接返回true
     *
     * @param permission
     * @return
     */
    protected boolean applyForPermission(String permission) {
        return applyForPermission(permission, REQUEST_PERMISSION);
    }

    /**
     * 申请权限，有权限直接返回true
     *
     * @param permission
     * @return
     */
    public boolean applyForPermission(String permission, int requestCode) {
        // 判断Android版本
        if (Build.VERSION.SDK_INT > 22) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{permission}, requestCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 申请权限，有回调
     * @param permission
     * @param permissionListener
     */
    public void applyForPermission(String permission,OnPermissionListener permissionListener){
        onPermissionListener = permissionListener;
        // 判断Android版本
        if (Build.VERSION.SDK_INT > 22) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[]{permission}, REQUEST_PERMISSION);
            } else {
                if (onPermissionListener != null)onPermissionListener.onPermissionResult(true);
            }
        } else {
            if (onPermissionListener != null)onPermissionListener.onPermissionResult(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onRequestPermissionsResult(true);
            } else {
                onRequestPermissionsResult(false);
            }
        }
    }

    /**
     * 调用单参数申请权限方法，重写此方法
     */
    protected void onRequestPermissionsResult(boolean isSucceed) {
        if (onPermissionListener != null)onPermissionListener.onPermissionResult(isSucceed);
    }

    // TODO Fragment管理

    /**
     * 往布局中添加Fragment
     *
     * @param resId
     * @param fragment
     */
    protected void addFragment(int resId, BaseFragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(resId, fragment, fragment.getClass().getName());
        ft.commitAllowingStateLoss();
    }

    /**
     * 替换布局中的Fragment
     *
     * @param resId
     * @param fragment
     */
    protected void replaceFragment(int resId, BaseFragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(resId, fragment, fragment.getClass().getName());
        ft.commitAllowingStateLoss();
    }

    /**
     * 检测新版本
     */
    public void checkNewVersion() {
        new VersionUpdate(activity).checkNewVersion();
    }

    /**
     * 收到邀请相关推送时候会调用
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPushMessageEvent(InvitationBean event) {
        showInvitationDistribution(event);
    }

    /**
     * 支付、充值、提现成功后会调用
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayResult(PayEvent event) {
    }

    /**
     * 广告评论、抢红包成功后会调用
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {
    }

    /**
     *
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUserInfo(UserInfoBean userInfoBean) {
    }


    /**
     * 邀请分销提示
     */
    private void showInvitationDistribution(final InvitationBean event) {
        if (event.code.equals(InvitationBean.INVITATION_BIND_CODE)) {
            BeInvitedDialog dialog = new BeInvitedDialog(this, event.nick, event.commission_rate + "%", event.commission_discount + "", new BeInvitedDialog.DialogClickCallBack() {
                @Override
                public void setAffirm() {
                    MyFansDataManager.getMyFansDataManager().acceptInvitation(activity, event.uid, event.nick);
                }
            });
            dialog.show();
        } else if (event.code.equals(InvitationBean.INVITATION_UNBIND_CODE) || event.code.equals(InvitationBean.INVITATION_UNBIND_AGREE_CODE)) {
            RelieveDistributionDialog dialog = new RelieveDistributionDialog(this, event.nick, new InvitationDistributionDialog.DialogClickCallBack() {
                @Override
                public void setAffirm() {

                }
            });
            dialog.show();
//        } else if (event.code.equals(InvitationBean.INVITATION_UNBIND_AGREE_CODE)) {
        }
    }

    public interface OnPermissionListener{
        void onPermissionResult(boolean b);
    }
}

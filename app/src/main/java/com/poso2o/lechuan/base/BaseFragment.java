package com.poso2o.lechuan.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poso2o.lechuan.configs.Constant;

import java.io.Serializable;

/**
 * Created by Adm
 * inistrator on 2017-11-25.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 申请权限
     */
    private static final int REQUEST_PERMISSION = 11;

    protected Context context;

    protected View view;

    /**
     * 碎片活动类
     */
    private FragmentAction fragmentAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();

        view = initGroupView(inflater, container, savedInstanceState);

        initView();

        initData();

        initListener();

        return view;
    }

    public abstract View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

    /**
     * 通过视图ID获取视图引用
     * @param resId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int resId) {
        return (T) view.findViewById(resId);
    }

    /**
     * 获取颜色值
     * @param resId
     * @return
     */
    protected int getColorValue(int resId) {
        return getResources().getColor(resId);
    }

    public void showLoading() {
        ((BaseActivity) context).showLoading();
    }

    public void showLoading(int resId) {
        ((BaseActivity) context).showLoading(resId);
    }

    public void setLoadingMessage(int resId) {
        ((BaseActivity) context).setLoadingMessage(resId);
    }

    public void showLoading(String text) {
        ((BaseActivity) context).showLoading(text);
    }

    public void setLoadingMessage(String text) {
        ((BaseActivity) context).setLoadingMessage(text);
    }

    public void dismissLoading() {
        ((BaseActivity) context).dismissLoading();
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
        Intent intent = new Intent(context, cls);
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
        Intent intent = new Intent(context, cls);
        intent.putExtras(data);
        startActivityForResult(intent, requestCode);
    }

    // TODO 权限相关

    /**
     * 申请权限，有权限直接返回true
     * @param permission
     * @return
     */
    protected boolean applyForPermission(String permission) {
        return applyForPermission(permission, REQUEST_PERMISSION);
    }

    /**
     * 申请权限，有权限直接返回true
     * @param permission
     * @return
     */
    public boolean applyForPermission(String permission, int requestCode) {
        // 判断Android版本
        if (Build.VERSION.SDK_INT > 22) {
            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
                requestPermissions(new String[] { permission }, requestCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
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
    protected void onRequestPermissionsResult(boolean isSucceed) {}

    // TODO Fragment管理

    /**
     * 往布局中添加Fragment
     * @param resId
     * @param fragment
     */
    protected void addFragment(int resId, BaseFragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(resId, fragment, fragment.getClass().getName());
        ft.commitAllowingStateLoss();
    }

    /**
     * 替换布局中的Fragment
     * @param resId
     * @param fragment
     */
    protected void replaceFragment(int resId, BaseFragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(resId, fragment, fragment.getClass().getName());
        ft.commitAllowingStateLoss();
    }

    /**
     * 设置碎片活动回调
     */
    public void setFragmentAction(FragmentAction fragmentAction) {
        this.fragmentAction = fragmentAction;
    }

    /**
     * 隐藏Fragment
     */
    public void hideFragment(boolean isCancel) {
        if (fragmentAction != null) {
            fragmentAction.hide(isCancel);
        }
    }

    /**
     * 碎片活动回调接口
     *
     * @author 郑洁东
     * @date 创建时间：2016-10-24
     */
    public interface FragmentAction {

        boolean isShow();

        /**
         * 隐藏Fragment
         *
         * @param isCancel 是否被取消
         */
        void hide(boolean isCancel);
    }

}

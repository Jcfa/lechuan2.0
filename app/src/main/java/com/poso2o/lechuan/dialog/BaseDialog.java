package com.poso2o.lechuan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.poso2o.lechuan.R;


/**
 * 自定义dialog
 */
public abstract class BaseDialog extends Dialog {

    public static final int OUT_TO = -1;

    protected Context context;

    private View rootView;

    private Window window = null;

    private boolean isInit = false;

    public BaseDialog(Context context) {
        super(context);
        this.context = context;
        // 去标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    /**
     * @param ，设置弹出View
     */
    public abstract View setDialogContentView();

    /**
     * 初始化View
     *
     * @param
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化监听
     */
    public abstract void initListener();

    /**
     * 通过视图ID获取视图引用
     *
     * @param resId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int resId) {
        return (T) rootView.findViewById(resId);
    }

    private void init() {
        rootView = setDialogContentView();
        if (rootView == null) {
            throw new NullPointerException("视图丢失");
        }
        setContentView(rootView);

        if (window == null)
            window = getWindow();

        window.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置对话框背景为透明

        window.setWindowAnimations(R.style.dialog_anim_style);
        setWindowDisplay(0.8, OUT_TO);// 宽度设置为屏幕的0.8
        initView();
        initData();
        initListener();
        isInit = true;
    }

    /**
     * 配置dialog的宽高比例
     * TODO 单词拼错，已废弃
     */
    @Deprecated
    public void setWindowDispalay(double wPercent, double hPercent) {
        setWindowDisplay(wPercent, hPercent);
    }

    /**
     * 配置dialog的宽高比例
     */
    protected void setWindowDisplay(double wPercent, double hPercent) {
        if (window == null) {
            window = getWindow();
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (wPercent != OUT_TO) {
            lp.width = (int) (d.widthPixels * wPercent);
        }

        if (hPercent != OUT_TO) {
            lp.height = (int) (d.heightPixels * hPercent);
        }
        window.setAttributes(lp);
    }

    /**
     * 配置dialog的弹出位置 例如：setDialogGravity（Gravity.TOP）;
     */
    public void setDialogGravity(int gravity) {
        if (window == null) {
            window = getWindow();
        }
        window.setGravity(gravity);
    }

    /**
     * 背景
     *
     * @return
     */
    public void clearFlagsDialog() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// 去掉这句话，背景会变暗
    }


    public boolean getIsFinishInit() {
        return isInit;
    }
}

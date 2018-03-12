package com.poso2o.lechuan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.poso2o.lechuan.R;

/**
 * Created by Administrator on 2017-12-07.
 */

public class BottomDialog extends Dialog {
    private Context mContext;
    private Window window = null;
    public static final int OUT_TO = -1;
    private IChooseCallBack chooseCallBack;

    public BottomDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.dialog_bottom_view, null);
        setContentView(view);
        setWindowDispalay(0.9, OUT_TO);// 宽度设置为屏幕的0.8
        if (window == null)
            window = getWindow();
        window.setBackgroundDrawableResource(R.color.base_dialog_widow_bg); // 设置对话框背景为透明

        window.setWindowAnimations(R.style.BottomInAnimation);
        view.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chooseCallBack != null) {
                    chooseCallBack.onPhoto();
                }
                dismiss();
            }
        });
        view.findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chooseCallBack != null) {
                    chooseCallBack.onCamera();
                }
                dismiss();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /*
  * 配置dialog的宽高比例
  */
    public void setWindowDispalay(double wPercent, double hPercent) {
        if (window == null)
            window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (wPercent != OUT_TO) {
            lp.width = (int) (d.widthPixels * wPercent);
        }

        if (hPercent != OUT_TO) {
            lp.height = (int) (d.heightPixels * hPercent);
        }
        window.setAttributes(lp);
    }

    public void setChooseCallBack(IChooseCallBack chooseCallBack) {
        this.chooseCallBack = chooseCallBack;
    }

    public interface IChooseCallBack {
        public void onPhoto();

        public void onCamera();
    }
}

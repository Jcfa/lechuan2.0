package com.poso2o.lechuan.util;

import android.content.Context;

import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommissionSettingDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.mine.GoodsDataManager;

import static com.poso2o.lechuan.util.SharedPreferencesUtils.KEY_USER_HAS_COMMISSION;
import static com.poso2o.lechuan.util.SharedPreferencesUtils.KEY_USER_SELECTED_TYPE;

/**
 * Created by Jaydon on 2017/12/26.
 */
public class UserUtils {

    /**
     * 是否未设置佣金
     * @return
     */
    public static boolean isNotCommission() {
        if (SharedPreferencesUtils.getInt(KEY_USER_SELECTED_TYPE) == Constant.MERCHANT_TYPE
                && SharedPreferencesUtils.getInt(KEY_USER_HAS_COMMISSION) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 显示佣金设置对话框
     */
    public static void showCommissionSettingDialog(Context context) {
        showCommissionSettingDialog(context, null);
    }

    /**
     * 显示佣金设置对话框
     */
    public static void showCommissionSettingDialog(final Context context, final OnCommissionSettingListener onCommissionSettingListener) {
        CommissionSettingDialog dialog = new CommissionSettingDialog(context, CommissionSettingDialog.COMMON_TYPE, new CommissionSettingDialog.SettingCallBack() {

            @Override
            public void setFinish(final float rate, final float discount) {
                GoodsDataManager manager = GoodsDataManager.getGoodsDataManager();
                manager.setCommissionRate((BaseActivity) context, rate + "", discount + "", new IRequestCallBack() {

                    @Override
                    public void onResult(int tag, Object object) {
                        Toast.show(context, "设置成功！");
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_COMMISSION_RATE, rate);
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_COMMISSION_DISCOUNT, discount);
                        SharedPreferencesUtils.put(SharedPreferencesUtils.KEY_USER_HAS_COMMISSION, 1);
                        if (onCommissionSettingListener != null) {
                            onCommissionSettingListener.onResult(true);
                        }
                    }

                    @Override
                    public void onFailed(int tag, String msg) {
                        Toast.show(context, msg);
                        if (onCommissionSettingListener != null) {
                            onCommissionSettingListener.onResult(false);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    public interface OnCommissionSettingListener {

        void onResult(boolean isSucceed);
    }
}

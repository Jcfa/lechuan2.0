package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.version.VersionBean;
import com.poso2o.lechuan.util.AppUtil;
import com.poso2o.lechuan.util.NumberUtils;

/**
 * Created by Administrator on 2017-12-21.
 */

public class VersionUpdateDialog extends BaseDialog {
    private BaseActivity mContext;
    private View mView;
    private VersionBean versionBean;
    private boolean local;
    private SettingCallBack callBack;

    /**
     * @param context
     * @param versionBean 最新的版本信息
     * @param local       是否安装本地apk文件
     */
    public VersionUpdateDialog(BaseActivity context, VersionBean versionBean, boolean local, SettingCallBack callBack) {
        super(context);
        mContext = context;
        this.versionBean = versionBean;
        this.local = local;
        this.callBack = callBack;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_version_update_view, null);
        return mView;
    }

    @Override
    public void initView() {
        setCancelable(false);
        TextView tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        tvTitle.setText("发现新版本（" + versionBean.version + ")");
        TextView tvContent = (TextView) mView.findViewById(R.id.tv_content);
        tvContent.setText(versionBean.des);
        TextView tvAffirm = (TextView) mView.findViewById(R.id.tv_affirm);
        tvAffirm.setText(local ? "安装" : "更新");
        tvAffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null) {
                    callBack.confirm();
                }
                dismiss();
            }
        });
        mView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (versionBean.getUpdate() == 1) {
                    AppUtil.exitApp(mContext, false);
                }
                dismiss();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    public interface SettingCallBack {
        public void confirm();
    }


}

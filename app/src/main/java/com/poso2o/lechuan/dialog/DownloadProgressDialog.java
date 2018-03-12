package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.NumberUtils;

/**
 * Created by Administrator on 2017-12-21.
 */

public class DownloadProgressDialog extends BaseDialog {
    private Context mContext;
    private View mView;
    private ProgressBar progressBar;
    private TextView tvSize, tvProgress;

    public DownloadProgressDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View setDialogContentView() {
        mView = View.inflate(mContext, R.layout.dialog_download_progress, null);
        return mView;
    }

    @Override
    public void initView() {
        progressBar = findView(R.id.progressBar3);
        progressBar.setMax(100);
        tvSize = findView(R.id.tv_size);
        tvProgress = findView(R.id.tv_progress);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    public void updateTotal(long totalSize) {
        double m = totalSize / (1000d * 1000d);
        tvSize.setText(NumberUtils.format2(m) + "M");
    }

    public void updateProgress(int progress) {
        tvProgress.setText(progress + "%");
        progressBar.setProgress(progress);
    }
}

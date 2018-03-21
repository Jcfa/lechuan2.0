package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;

/**
 * Created by mr zhang on 2018/3/5.
 *
 * 发布确认弹窗
 */

public class PublishConfirmDialog extends BaseDialog {

    private Context context;
    private View view;
    //提示信息
    private TextView publish_art_info;
    //取消
    private TextView publish_cancel;
    //确定发布
    private TextView publish_ok;

    private OnPublishListener onPublishListener;

    public PublishConfirmDialog(Context context,OnPublishListener onPublishListener) {
        super(context);
        this.context = context;
        this.onPublishListener = onPublishListener;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_confirm_publish,null);
        return view;
    }

    @Override
    public void initView() {
        publish_art_info = (TextView) view.findViewById(R.id.publish_art_info);
        publish_cancel = (TextView) view.findViewById(R.id.publish_cancel);
        publish_ok = (TextView) view.findViewById(R.id.publish_ok);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDisplay(0.8,0.3);
    }

    @Override
    public void initListener() {

        publish_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        publish_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPublishListener != null)onPublishListener.onPublish();
                dismiss();
            }
        });
    }

    public void setPublishInfo(int total,int adNum){
        publish_art_info.append(Html.fromHtml("<font color='#8E8E8E'>本次发布 </font><font color='#FF2222'>" + total+ "</font><font color='#8E8E8E'> 篇文章，包括含广告文章 </font><font color='#FF2222'>" + adNum+ "</font><font color='#8E8E8E'> 篇。</font>"));
    }

    public interface OnPublishListener{
        void onPublish();
    }
}

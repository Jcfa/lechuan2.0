package com.poso2o.lechuan.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017-12-07.
 */
public class CountDownTimerButton extends android.support.v7.widget.AppCompatButton implements View.OnClickListener {

    private Context mContext;
    private OnClickListener mOnClickListener;
    private Timer mTimer;//调度器
    private TimerTask mTask;
    private long duration = 60000;//倒计时时长 设置默认10秒
    private long temp_duration;
    private String clickBeffor = "重新获取";//点击前
    private String clickAfter = "秒";//点击后

    public CountDownTimerButton(Context context) {
        super(context);
        mContext = context;
        setOnClickListener(this);
    }

    public CountDownTimerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnClickListener(this);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setText(temp_duration / 1000 + clickAfter);
            temp_duration -= 1000;
            if (temp_duration < 0) {//倒计时结束
                setEnabled(true);
                setText(clickBeffor);
                stopTimer();
            }
        }
    };

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {//提供外部访问方法
        if (onClickListener instanceof CountDownTimerButton) {
            super.setOnClickListener(onClickListener);
        } else {
            this.mOnClickListener = onClickListener;
        }
    }

    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(view);
        }
    }

    //计时开始
    public void startTimer() {
        temp_duration = duration;
        setEnabled(false);
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0x01);
            }
        };
        mTimer.schedule(mTask, 0, 1000);//调度分配，延迟0秒，时间间隔为1秒
    }

    //计时结束
    public void stopTimer() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}

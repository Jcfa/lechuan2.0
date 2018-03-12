package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;

/**
 * 联系买家窗口
 * Created by Luo on 2017/12/11.
 */
public class ContactBuyerDialog extends BaseDialog {
    private Context context;
    private View view;
    private TextView dialog_contact_buyer_tel,dialog_contact_buyer_message,dialog_contact_buyer_copy,dialog_contact_buyer_close;

    /**
     * 回调
     */
    private OnContactBuyerListener onContactBuyerListener;

    public void show(OnContactBuyerListener onContactBuyerListener) {
        this.onContactBuyerListener = onContactBuyerListener;
        super.show();
    }

    public ContactBuyerDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_contact_buyer, null);
    }

    @Override
    public void initView() {
        setDialogGravity(Gravity.BOTTOM);
        setWindowDispalay(1, OUT_TO);

        dialog_contact_buyer_close = (TextView) view.findViewById(R.id.dialog_contact_buyer_close);
        dialog_contact_buyer_tel = (TextView) view.findViewById(R.id.dialog_contact_buyer_tel);
        dialog_contact_buyer_message = (TextView) view.findViewById(R.id.dialog_contact_buyer_message);
        dialog_contact_buyer_copy = (TextView) view.findViewById(R.id.dialog_contact_buyer_copy);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        //关闭
        dialog_contact_buyer_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //拨打电话
        dialog_contact_buyer_tel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onContactBuyerListener!=null){
                    onContactBuyerListener.onConfirm(0);
                }
            }
        });
        //短信
        dialog_contact_buyer_message.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onContactBuyerListener!=null){
                    onContactBuyerListener.onConfirm(1);
                }
            }
        });
        //复制收货地址
        dialog_contact_buyer_copy.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (onContactBuyerListener!=null){
                    onContactBuyerListener.onConfirm(2);
                }
            }
        });

    }

    public interface OnContactBuyerListener {
        void onConfirm(int type);
    }
}

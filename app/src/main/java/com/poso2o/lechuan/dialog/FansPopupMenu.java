package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.ResourceSetUtil;

/**
 * Created by Administrator on 2017-12-20.
 */

public class FansPopupMenu extends PopupWindow {
    private Context mContext;
    private View mView;
    private OnMenuItemClickListener listener;
    private TextView tvSeleted;

    public FansPopupMenu(Context context, String... items) {
        mContext = context;
        mView = View.inflate(context, R.layout.dialog_fans_menu, null);
        initView(items);
    }

    private void initView(String... items) {
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(mView);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(false);
        setFocusable(true);
        final TextView tv1 = (TextView) mView.findViewById(R.id.tv1);
        tv1.setText(items[0]);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tvSeleted = tv1;
                setTextColor(tv1);
                if (listener != null) {
                    listener.onItemClick(0);
                }
                dismiss();
            }
        });
        final TextView tv2 = (TextView) mView.findViewById(R.id.tv2);
        tv2.setText(items[1]);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tvSeleted = tv2;
                setTextColor(tv2);
                if (listener != null) {
                    listener.onItemClick(1);
                }
                dismiss();
            }
        });
        final TextView tv3 = (TextView) mView.findViewById(R.id.tv3);
        tv3.setText(items[2]);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tvSeleted = tv3;
                setTextColor(tv3);
                if (listener != null) {
                    listener.onItemClick(2);
                }
                dismiss();
            }
        });
        setTextColor(tv1);
    }

    private void setTextColor(TextView tv) {
        if (tvSeleted != null) {
            ResourceSetUtil.setTextColor(mContext, tvSeleted, R.color.textBlack);
        }
        ResourceSetUtil.setTextColor(mContext, tv, R.color.textGreen);
        tvSeleted = tv;
    }

    public void setOnItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnMenuItemClickListener {
        public void onItemClick(int position);
    }

}

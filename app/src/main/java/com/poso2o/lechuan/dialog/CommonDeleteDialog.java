package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.TextUtil;

/**
 * Created by lenovo on 2016/12/15.
 */
public class CommonDeleteDialog extends BaseDialog {
    private Context context;
    private View view;
    private OnDeleteListener onDeleteListener;

    private TextView common_delete_name;
    private TextView common_delete_type;
    private TextView common_delete_cancel;
    private TextView common_delete_confirm;
    private View common_delete_unable;
    private View common_delete_hint;
    private View common_delete_line;

    private String deleteName;
    
    private String type;

    public CommonDeleteDialog(Context context, String deleteName, String type) {
        super(context);
        this.context = context;
        this.deleteName = deleteName;
        this.type = type;
    }
    
    public void show(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
        super.show();
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_common_delete, null);
    }

    @Override
    public void initView() {
        setDialogGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        common_delete_name = (TextView) view.findViewById(R.id.common_delete_name);
        common_delete_type = (TextView) view.findViewById(R.id.common_delete_type);
        common_delete_cancel = (TextView) view.findViewById(R.id.common_delete_cancel);
        common_delete_confirm = (TextView) view.findViewById(R.id.common_delete_confirm);
        common_delete_unable = view.findViewById(R.id.common_delete_unable);
        common_delete_hint = view.findViewById(R.id.common_delete_hint);
        common_delete_line = view.findViewById(R.id.common_delete_line);

        common_delete_name.setText(TextUtil.trimToEmpty(deleteName));
        common_delete_type.setText(TextUtil.trimToEmpty(type));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        common_delete_cancel.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });

        common_delete_confirm.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (onDeleteListener != null) {
                    onDeleteListener.onConfirm();
                }
            }
        });

    }

    public interface OnDeleteListener {

        void onConfirm();
    }


}

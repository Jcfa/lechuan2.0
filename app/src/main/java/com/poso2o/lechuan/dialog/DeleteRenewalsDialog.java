package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oa.RenewalsBean;

/**
 * Created by mr zhang on 2018/3/6.
 *
 * 删除稿件确认弹窗
 */

public class DeleteRenewalsDialog extends BaseDialog {

    private Context context;
    private View view;

    //稿件名称
    private TextView delete_renewals_name;
    //稿件信息
    private RenewalsBean renewalsBean;
    private OnDelRenewalsListener onDelRenewalsListener;

    public DeleteRenewalsDialog(Context context,OnDelRenewalsListener onDelRenewalsListener) {
        super(context);
        this.context = context;
        this.onDelRenewalsListener = onDelRenewalsListener;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_delete_renuewals,null);
        return view;
    }

    @Override
    public void initView() {
        delete_renewals_name = (TextView) view.findViewById(R.id.delete_renewals_name);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setWindowDisplay(0.8,0.3);
    }

    @Override
    public void initListener() {
        //取消
        view.findViewById(R.id.renewals_delete_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //确定
        view.findViewById(R.id.renewals_delete_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDelRenewalsListener != null) onDelRenewalsListener.onDelRenewals(renewalsBean);
            }
        });
    }

    public void setRenewals(RenewalsBean renewals){
        this.renewalsBean = renewals;
        if (renewalsBean != null && renewalsBean.articles != null)delete_renewals_name.setText(renewalsBean.articles.title);
    }

    public interface OnDelRenewalsListener{
        void onDelRenewals(RenewalsBean renewalsBean);
    }
}

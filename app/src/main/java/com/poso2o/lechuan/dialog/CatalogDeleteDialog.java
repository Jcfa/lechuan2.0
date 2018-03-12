package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.goodsdata.Catalog;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by zhang on 2017/11/24.
 */
public class CatalogDeleteDialog extends BaseDialog {
    private Context context;
    private View view;
    private OnGroupDelete onGroupDelete;

    private TextView group_delete_name;
    private TextView group_delete_cancel;
    private TextView group_delete_confirm;
    private View group_delete_unable;
    private View group_delete_hint;
    private View group_delete_line;

    private Catalog catalog;

    private boolean isCanDelete = true;

    private boolean isVdian;

    public CatalogDeleteDialog(Context context, boolean isVdian, OnGroupDelete onGroupDelete) {
        super(context);
        this.context = context;
        this.isVdian = isVdian;
        this.onGroupDelete = onGroupDelete;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_catalog_delete, null);
    }

    @Override
    public void initView() {
        group_delete_name = (TextView) view.findViewById(R.id.group_delete_name);
        group_delete_cancel = (TextView) view.findViewById(R.id.group_delete_cancel);
        group_delete_confirm = (TextView) view.findViewById(R.id.group_delete_confirm);
        group_delete_unable = view.findViewById(R.id.group_delete_unable);
        group_delete_hint = view.findViewById(R.id.group_delete_hint);
        group_delete_line = view.findViewById(R.id.group_delete_line);
    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void initListener() {
        group_delete_cancel.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (onGroupDelete != null) {
                    onGroupDelete.onCancel();
                } else {
                    Toast.show(context,"数据出错");
                }
            }
        });

        group_delete_confirm.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (isCanDelete) {
                    if (onGroupDelete != null) {
                        onGroupDelete.onConfirm(catalog);
                    } else {
                        Toast.show(context,"数据出错");
                    }
                } else {
                    onGroupDelete.onCancel();
                }
            }
        });
    }

    public void setDeleteName(Catalog catalog) {
        this.catalog = catalog;
        int num = 0;
        String name;
        if (isVdian) {
            num = catalog.catalog_goods_number;
            name = catalog.catalog_name;
        } else {
            num = NumberUtils.toInt(catalog.productNum);
            name = catalog.directory;
        }
        if (num == 0) {
            if (getIsFinishInit()) {
                group_delete_name.setText(name);
            }
            group_delete_unable.setVisibility(GONE);
            group_delete_hint.setVisibility(VISIBLE);
            group_delete_cancel.setVisibility(VISIBLE);
            group_delete_line.setVisibility(VISIBLE);
            isCanDelete = true;
        } else {
            group_delete_unable.setVisibility(VISIBLE);
            group_delete_hint.setVisibility(GONE);
            group_delete_cancel.setVisibility(GONE);
            group_delete_line.setVisibility(GONE);
            isCanDelete = false;
        }
    }

    public interface OnGroupDelete {

        void onCancel();

        void onConfirm(Catalog homeTransferItemData);
    }


}

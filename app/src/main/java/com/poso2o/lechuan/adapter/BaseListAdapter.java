package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * 列表视图适配器基类
 * Created by Jaydon on 2017/8/21.
 */
public abstract class BaseListAdapter<D> extends BaseAdapter<BaseListAdapter.ListViewHolder, D> {

    /**
     * 是否编辑模式
     */
    private boolean isEdit;

    /**
     * 是否多选模式
     */
    protected boolean isSelect;

    private ArrayList<D> selects;

    protected OnItemDeleteListener<D> onItemDeleteListener;

    /**
     * 临时数据数量
     */
    private int temporaryDataNum = 0;

    public BaseListAdapter(Context context, boolean isSelect) {
        super(context, null);
        selects = new ArrayList<>();
        this.isSelect = isSelect;
    }

    public D findSelect(D d) {
        for (D select : selects) {
            if (select.equals(d)) {
                return select;
            }
        }
        return null;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setSelects(ArrayList<D> selects) {
        this.selects.clear();
        if (selects != null) {
            this.selects.addAll(selects);
        }
    }

    public ArrayList<D> getSelects() {
        return selects;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener<D> onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final BaseListAdapter.ListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final D item = data.get(position);
        if (isEdit) {
            holder.list_item_select.setVisibility(INVISIBLE);
            // 临时数据不参与编辑
            if (position < temporaryDataNum) {
                holder.list_item_delete.setVisibility(GONE);
                holder.list_item_group.setBackgroundColor(0xfff6f6f6);
            } else {
                holder.list_item_group.setBackgroundResource(R.drawable.selector_common_press);
                holder.list_item_delete.setVisibility(VISIBLE);
                holder.list_item_delete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (onItemDeleteListener != null) {
                            onItemDeleteListener.onItemDelete(item);
                        }
                    }
                });
            }
        } else {
            holder.list_item_group.setBackgroundResource(R.drawable.selector_common_press);
            if (isSelect) {
                holder.list_item_select.setVisibility(VISIBLE);
                holder.list_item_select.setSelected(findSelect(item) != null);
            }
            holder.list_item_delete.setVisibility(GONE);
        }
        holder.list_item_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isEdit && isSelect) {
                    D select = findSelect(item);
                    if (select != null) {
                        selects.remove(select);
                    } else {
                        selects.add(item);
                    }
                    notifyDataSetChanged();
                } else {
                    if (holder.getAdapterPosition() < temporaryDataNum) {
                        Toast.show(context,R.string.toast_temporary_data_not_edit);
                    } else if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(item);
                    }
                }
            }
        });
        if (position == getItemCount() - 1) {// 最后一条
            holder.itemView.setPadding(0, 0, 0, 150);
        } else if (position == 0) {// 第一条
            holder.itemView.setPadding(0, 0, 0, 0);
        } else {
            holder.itemView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 刷新列表数据
     * @param data
     */
    public void notifyDataSetChanged(ArrayList<D> data) {
        temporaryDataNum = 0;
        select:for (D d : selects) {
            for (D itemData : data) {
                if (d.equals(itemData)) {
                    continue select;
                }
            }
            temporaryDataNum++;
            data.add(0, d);
        }
        setItems(data);
        notifyDataSetChanged();
    }

    public interface OnItemDeleteListener<D> {

        void onItemDelete(D item);
    }

    public class ListViewHolder extends BaseViewHolder {
        public LinearLayout list_item_group;
        public TextView list_item_text;
        public ImageView list_item_select;
        public ImageView list_item_delete;

        public ListViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(context).inflate(R.layout.recycle_base_list_item, parent, false));
            if (!isSelect) {
                list_item_select.setVisibility(INVISIBLE);
            }
        }

        public ListViewHolder(View itemView) {
            super(itemView);
            list_item_group = getView(R.id.list_item_group);
            list_item_text = getView(R.id.list_item_text);
            list_item_select = getView(R.id.list_item_select);
            list_item_delete = getView(R.id.list_item_delete);
        }
    }
}
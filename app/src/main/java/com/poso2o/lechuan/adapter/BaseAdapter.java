package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Created by Jaydon on 2017/7/18.
 */
public abstract class BaseAdapter<T extends BaseViewHolder, D> extends RecyclerView.Adapter<T> {

    protected Context context;

    protected ArrayList<D> data;

    protected OnItemClickListener<D> onItemClickListener;
    protected OnItemClickListener2<D> onItemClickListener2;

    public BaseAdapter(Context context, ArrayList<D> data) {
        this.context = context;
        this.data = new ArrayList<>();
        setItems(data);
    }

    public void setOnItemClickListener(OnItemClickListener<D> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListener2(OnItemClickListener2<D> onItemClickListener) {
        this.onItemClickListener2 = onItemClickListener;
    }
    /**
     * 刷新列表数据
     *
     * @param data
     */
    public void notifyDataSetChanged(ArrayList<D> data) {
        setItems(data);
        notifyDataSetChanged();
    }

    public void addItem(D item) {
        if (item != null) {
            data.add(0, item);
            notifyDataSetChanged();
        }
    }

    /**
     * 修改条目
     * @param d
     */
    public void updateItem(D d) {
        for (int i = 0; i < data.size(); i++) {
            D item = data.get(i);
            if (item.equals(d)) {
                data.set(i, d);
                notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * 删除条目
     * @param d
     */
    public void removeItem(D d) {
        for (int i = 0; i < data.size(); i++) {
            D item = data.get(i);
            if (item.equals(d)) {
                data.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void addItems(ArrayList<D> data) {
        if (data != null) {
            this.data.addAll(data);
            notifyItemRangeChanged(this.data.size() - data.size(), this.data.size());
        }
    }

    public void setItems(ArrayList<D> data) {
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
    }

    public ArrayList<D> getItems() {
        return data;
    }

    public View getItemView(int resId, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(resId, parent, false);
    }

    @Override
    public void onBindViewHolder(T holder, final int position) {
        final D item;
        if (data.size() > position) {
            item = getItem(position);
        } else {
            item = null;
        }
        holder.itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(item);
                }
                if (onItemClickListener2 != null) {
                    onItemClickListener2.onItemClick(item,position);
                }
            }
        });
        initItemView(holder, item, position);
    }

    public abstract void initItemView(T holder, D item, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public D getItem(int position) {
        return data.get(position);
    }

    public interface OnItemClickListener<D> {

        void onItemClick(D item);
    }
    public interface OnItemClickListener2<D> {

        void onItemClick(D item,int position);
    }
}

package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用列表适配器
 *
 * @see //http://blog.csdn.net/lmj623565791/article/details/38902805
 * @author 郑洁东
 * @date 创建时间：2016-10-24
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    /** 视图充气泵 */
    protected LayoutInflater inflater;

    /** 环境变量 */
    protected Context context;

    /** 数据集合 */
    private List<T> data;

    /** 列表项布局id */
    private final int itemLayoutId;

    public CommonAdapter(Context context, List<T> datas, int itemLayoutId) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        this.itemLayoutId = itemLayoutId;
        addItems(datas);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        if (data.size() > 0) {
            return data.get(position % data.size());
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(position, viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    /**
     * 刷新适配器
     */
    public void notifyDataSetChanged(List<T> data) {
        this.data.clear();
        addItems(data);
    }

    /**
     * 添加数据集合
     */
    public void addItems(List<T> datas) {
        if (datas != null && datas.size() != 0) {
            this.data.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public abstract void convert(int position, ViewHolder helper, T item);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(context, inflater, convertView, parent, itemLayoutId, position);
    }

}

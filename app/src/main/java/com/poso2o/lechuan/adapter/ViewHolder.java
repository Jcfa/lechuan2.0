package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * 视图持有者
 *
 * @author 郑洁东
 * @date 2016-10-24
 * @see //http://blog.csdn.net/lmj623565791/article/details/38902805
 */
public class ViewHolder {

    /**
     * 环境变量
     */
    private Context context;

    /**
     * 视图集合
     */
    private final SparseArray<View> views;

    /**
     * Item的视图
     */
    private View convertView;

    /**
     * Item的下标
     */
    private int position;

    /**
     * 视图ID
     */
    private int layoutId;

    private ViewHolder(Context context, LayoutInflater inflater, ViewGroup parent, int layoutId, int position) {
        this.context = context;
        this.views = new SparseArray<View>();
        this.layoutId = layoutId;
        this.position = position;
        this.convertView = inflater.inflate(layoutId, parent, false);
        // setTag
        convertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, LayoutInflater inflater, View convertView, ViewGroup parent, int layoutId, int position) {
        ViewHolder holder;
        if (convertView == null) {
            return new ViewHolder(context, inflater, parent, layoutId, position);
        }
        holder = (ViewHolder) convertView.getTag();
        // 如果视图布局不一样，则重新创建视图持有者
        if (holder.getLayoutId() != layoutId) {
            return new ViewHolder(context, inflater, parent, layoutId, position);
        }
        holder.position = position;
        return holder;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public View getConvertView() {
        return convertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public ViewHolder setImageByUrl(int viewId, String url) {
        Glide.with(context).load(url).into((ImageView) getView(viewId));
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public int getPosition() {
        return position;
    }

}

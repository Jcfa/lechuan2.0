package com.poso2o.lechuan.util;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;

import static com.poso2o.lechuan.R.mipmap.poster_seller_icon;

/**
 * Created by Administrator on 2017-12-12.
 */

public class ResourceSetUtil {

    public static void setBackground(Context context, View view, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setBackground(context.getResources().getDrawable(resId, null));
        } else {
            view.setBackground(context.getResources().getDrawable(resId));
        }
    }


    public static void setSrc(Context context, ImageView view, int resId) {
        view.setImageResource(R.mipmap.poster_seller_icon);
    }

    public static void setTextColor(Context context, TextView tv, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tv.setTextColor(context.getResources().getColor(resId, null));
        } else {
            tv.setTextColor(context.getResources().getColor(resId));
        }
    }

}

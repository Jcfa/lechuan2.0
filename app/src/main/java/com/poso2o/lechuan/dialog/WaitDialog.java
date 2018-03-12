/*
 * Copyright 2015 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.poso2o.lechuan.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.util.TextUtil;

/**
 * Created in Oct 23, 2015 1:19:04 PM.
 *
 * @author Yan Zhenjie.
 */
public class WaitDialog {

    //    static LoadingDialog dialog;
    static LoadingDialog2 dialog;
//    static Dialog dialog;

    public static void showLoaddingDialog(Context context, String msg) {
        if (dialog != null) {
            dialog.dismiss();
        }
        LoadingDialog2.Builder builder = new LoadingDialog2.Builder(context);
        if (TextUtil.isEmpty(msg)) {
            builder.setShowMessage(false);
        } else {
            builder.setMessage(msg);
        }
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    private static void startAnimation(ImageView img) {
        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(2000);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        rotate.setStartOffset(10);//执行前的等待时间
        img.setAnimation(rotate);
    }

    public static void showLoaddingDialog(Context context) {
        showLoaddingDialog(context, "");
    }

    public static void dismissLoaddingDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void updateMessage(String msg) {
        if (dialog != null && dialog.isShowing()) {
//            dialog.setMessage(msg);
        }
    }

}

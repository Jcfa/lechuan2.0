/*
 * Copyright © Yan Zhenjie. All Rights Reserved
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
package com.poso2o.lechuan.util;

import android.content.Context;
import android.view.View;

/**
 * Created by Yan Zhenjie on 2016/10/16.
 */
public class Toast {

    public static void show(Context context, String msg) {
        if (!TextUtil.isEmpty(msg)) {
            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show();
        }
    }

    public static void show(Context context, int stringId) {
        android.widget.Toast.makeText(context, stringId, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void show(View view, String msg) {
        show(view.getContext(), msg);
    }

    public static void show(View view, int stringId) {
        show(view.getContext(), stringId);
    }

}

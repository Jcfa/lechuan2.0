/*
 * Copyright © 2015 uerp.net. All rights reserved.
 */
package com.poso2o.lechuan.util;

import android.util.Log;

import com.lidroid.xutils.util.OtherUtils;
import com.poso2o.lechuan.configs.Constant;

/**
 * Utils - 日志
 *
 * @author sunjian
 * @version 1.0
 */
public class LogUtils {

    /** 开启 */
    public static boolean IS_ENABLED = Constant.DEV_MODE;

    /**
     * 不允许实例化
     */
    private LogUtils() {
    }

    public static void v(String message) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.v(tag, message);
        }
    }

    public static void v(String message, Throwable throwable) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.v(tag, message, throwable);
        }
    }

    public static void v(String tag, String message) {
        if (IS_ENABLED) {
            Log.v(tag, message);
        }
    }

    public static void v(String tag, String message, Throwable throwable) {
        if (IS_ENABLED) {
            Log.v(tag, message, throwable);
        }
    }

    public static void d(String message) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.d(tag, message);
        }
    }

    public static void d(String message, Throwable throwable) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.d(tag, message, throwable);
        }
    }

    public static void d(String tag, String message) {
        if (IS_ENABLED) {
            Log.d(tag, message);
        }
    }

    public static void d(String tag, String message, Throwable throwable) {
        if (IS_ENABLED) {
            Log.d(tag, message, throwable);
        }
    }

    public static void i(String message) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.i(tag, message);
        }
    }

    public static void i(String message, Throwable throwable) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.i(tag, message, throwable);
        }
    }

    public static void i(String tag, String message) {
        if (IS_ENABLED) {
            Log.i(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable throwable) {
        if (IS_ENABLED) {
            Log.i(tag, message, throwable);
        }
    }

    public static void w(String message) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.w(tag, message);
        }
    }

    public static void w(String message, Throwable throwable) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.w(tag, message, throwable);
        }
    }

    public static void w(String tag, String message) {
        if (IS_ENABLED) {
            Log.w(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable throwable) {
        if (IS_ENABLED) {
            Log.w(tag, message, throwable);
        }
    }

    public static void e(String message) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.e(tag, message);
        }
    }

    public static void e(String message, Throwable throwable) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.e(tag, message, throwable);
        }
    }

    public static void e(String tag, String message) {
        if (IS_ENABLED) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (IS_ENABLED) {
            Log.e(tag, message, throwable);
        }
    }

    public static void wtf(String message) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.wtf(tag, message);
        }
    }

    public static void wtf(String message, Throwable throwable) {
        if (IS_ENABLED) {
            StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
            String tag = getDefaultTag(caller);
            Log.wtf(tag, message, throwable);
        }
    }

    public static void wtf(String tag, String message) {
        if (IS_ENABLED) {
            Log.wtf(tag, message);
        }
    }

    public static void wtf(String tag, String message, Throwable throwable) {
        if (IS_ENABLED) {
            Log.wtf(tag, message, throwable);
        }
    }

    /**
     * @return 默认Tag
     */
    private static String getDefaultTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClassName = caller.getClass().getSimpleName();
        tag = String.format(tag, new Object[] { callerClassName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber()) });
        return tag;
    }

}

package com.poso2o.lechuan.manager.main;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-12-09.
 */

public class ActivityManager {
    private ArrayList<Activity> activities = new ArrayList<>();
    private static ActivityManager activityManager;

    private ActivityManager() {
    }

    public static ActivityManager getActivityManager() {
        synchronized (ActivityManager.class) {
            if (activityManager == null) {
                activityManager = new ActivityManager();
            }
            return activityManager;
        }
    }

    public synchronized void addActivity(Activity activity) {
        synchronized (activities) {
            activities.add(activity);
        }
    }

    public synchronized void finishActivity(Activity activity) {
        synchronized (activities) {
            activities.remove(activity);
        }
    }

    public synchronized void finishAll() {
        synchronized (activities) {
            for (Activity activity : activities) {
                activity.finish();
            }
        }
    }
}

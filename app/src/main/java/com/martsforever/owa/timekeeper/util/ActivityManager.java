package com.martsforever.owa.timekeeper.util;

import android.app.Activity;
import android.content.Intent;

import com.martsforever.owa.timekeeper.login.LoginActivity;
import com.martsforever.owa.timekeeper.main.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by owa on 2017/1/13.
 */

public class ActivityManager {
    private static Map<String, Activity> map = new HashMap<>();

    /**
     * add to destroy queue
     *
     * @param activity
     */

    public static void addDestoryActivity(Activity activity, String activityName) {
        map.put(activityName, activity);
    }

    /**
     * destroy specific activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            map.get(key).finish();
        }
    }

    public static void entryMainActivity(Activity currentActivity, Class c) {
//        entry MainActivity
        Intent intent = new Intent();
        intent.setClass(currentActivity, c);
        currentActivity.startActivity(intent);
        currentActivity.finish();
        ActivityManager.destoryActivity(LoginActivity.class.getName());
    }

    public static void entryRetrivePasswordActivity(Activity activity,Class c){
        Intent intent = new Intent();
        intent.setClass(activity, c);
        activity.startActivity(intent);
    }
}

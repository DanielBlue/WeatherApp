package com.example.coolweather.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 毛琦 on 2016/7/15.
 */
public class ActivityManager {
    public static List<Activity> list = new ArrayList<>();

    private ActivityManager(){}

    public static void add(Activity activity){
        if(list!=null) {
            list.add(activity);
        }
    }

    public static void remove(Activity activity){
        if(list.contains(activity)) {
            list.remove(activity);
        }
    }

    public static void finishAll(){
        for(Activity activity:list){
            activity.finish();
        }
    }
}

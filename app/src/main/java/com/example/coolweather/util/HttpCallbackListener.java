package com.example.coolweather.util;

/**
 * Created by 毛琦 on 2016/7/13.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}

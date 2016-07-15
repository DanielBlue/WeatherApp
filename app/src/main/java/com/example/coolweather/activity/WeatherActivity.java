package com.example.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.util.ContentValue;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

/**
 * Created by 毛琦 on 2016/7/14.
 */
public class WeatherActivity extends Activity {
    private static final String tag = "WeatherActivity";
    private static final int INIT = 100;
    private static final int LOADING = 101;
    private static final int ERROR = 102;
    /**
     * 显示城市名
     */
    private TextView city_name;
    /**
     * 显示发布时间
     */
    private TextView publish_text;
    /**
     * 显示当前时间
     */
    private TextView current_date;
    /**
     * 显示天气
     */
    private TextView weather_desp;
    /**
     * 显示气温范围
     */
    private TextView temp1;
    private LinearLayout weather;
    private String countyName;
    private String publishText;
    private String today;
    private String weatherDesp;
    private String temp;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INIT:
                    initUI();
                    break;
                case LOADING:
                    publish_text.setText("更新中");
                    weather.setVisibility(View.INVISIBLE);
                    break;
                case ERROR:
                    publish_text.setText("更新失败");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        weather = (LinearLayout) findViewById(R.id.weather_info_layout);
        city_name = (TextView) findViewById(R.id.city_name);
        publish_text = (TextView) findViewById(R.id.publish_text);
        current_date = (TextView) findViewById(R.id.current_date);
        weather_desp = (TextView) findViewById(R.id.weather_desp);
        temp1 = (TextView) findViewById(R.id.temp1);
        initUI();
        initData();
    }

    private void initData() {
        final Message message = new Message();
        countyName = getIntent().getStringExtra("CountyName");
        String address = "http://v.juhe.cn/weather/index?cityname="+countyName+"&key=5d9d8d7431f0b864fa216cb311e5c226";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean init = Utility.saveWeatherInfo(getApplicationContext(),response,countyName);
                if(init){
                    message.what = INIT;
                }else {
                    message.what = LOADING;
                }
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                message.what = ERROR;
                handler.sendMessage(message);
            }
        });
    }

    private void initUI() {
        publishText = Utility.getWeatherInfo(this, ContentValue.PUBLISH_TIME);
        today = Utility.getWeatherInfo(this, ContentValue.TODAY);
        weatherDesp = Utility.getWeatherInfo(this, ContentValue.WEATHER_DES);
        temp = Utility.getWeatherInfo(this, ContentValue.TEMP);
        city_name.setText(countyName);
        publish_text.setText(publishText);
        current_date.setText(today);
        weather_desp.setText(weatherDesp);
        temp1.setText(temp);
    }
}
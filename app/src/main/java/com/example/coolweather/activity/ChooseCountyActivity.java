package com.example.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.model.CoolWeatherDB;
import com.example.coolweather.model.County;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 毛琦 on 2016/7/14.
 */
public class ChooseCountyActivity extends Activity {
    private CoolWeatherDB coolWeatherDB;
    private ListView listView;
    private List<String> countyList = new ArrayList<>();
    private String cityName;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            listView.setAdapter(new MyAdapter());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        cityName = getIntent().getStringExtra("CityName");
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(cityName);
        listView = (ListView) findViewById(R.id.list_city);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),WeatherActivity.class);
                intent.putExtra("CountyName",coolWeatherDB.loadCounty(cityName).get(i).getmCountyName());
                startActivity(intent);
            }
        });
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        query();
    }

    private void query() {
        List<County> list = coolWeatherDB.loadCounty(cityName);
        if(list.size()>0){
            for(County c:list){
                if(c.getmCityName().equals(cityName)){
                    countyList.add(c.getmCountyName());
                }
            }
            handler.sendEmptyMessage(0);
        } else{
            HttpUtil.sendHttpRequest("http://v.juhe.cn/weather/citys?key=5d9d8d7431f0b864fa216cb311e5c226", new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    countyList = Utility.handleCountyResponse(coolWeatherDB,response,countyList,cityName);
                    handler.sendEmptyMessage(0);
                    for(int x=0;x<countyList.size();x++){
                        Log.i("MainActivity",countyList.get(x));
                    }
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return countyList.size();
        }

        @Override
        public String getItem(int i) {
            return countyList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(getApplicationContext(),R.layout.list_item,null);
            TextView text = (TextView) view1.findViewById(R.id.list_text);
            text.setText(getItem(i));
            return view1;
        }
    }
}

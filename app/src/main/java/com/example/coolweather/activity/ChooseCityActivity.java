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
import com.example.coolweather.model.City;
import com.example.coolweather.model.CoolWeatherDB;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 毛琦 on 2016/7/14.
 */
public class ChooseCityActivity extends Activity{
    private CoolWeatherDB coolWeatherDB;
    private ListView listView;
    private List<String> cityList = new ArrayList<>();
    private String provinceName;
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
        provinceName = getIntent().getStringExtra("ProvinceName");
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(provinceName);
        listView = (ListView) findViewById(R.id.list_city);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ChooseCountyActivity.class);
                intent.putExtra("CityName",coolWeatherDB.loadCity(provinceName).get(i).getmCityName());
                startActivity(intent);
            }
        });
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        query();
    }

    private void query() {
        List<City> list = coolWeatherDB.loadCity(provinceName);
        if(list.size()>0){
            for(City c:list){
                if(c.getmProvinceName().equals(provinceName)){
                    cityList.add(c.getmCityName());
                }
            }
            handler.sendEmptyMessage(0);
        } else{
            HttpUtil.sendHttpRequest("http://v.juhe.cn/weather/citys?key=5d9d8d7431f0b864fa216cb311e5c226", new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    cityList = Utility.handleCityResponse(coolWeatherDB,response,cityList,provinceName);
                    handler.sendEmptyMessage(0);
                    for(int x=0;x<cityList.size();x++){
                        Log.i("MainActivity",cityList.get(x));
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
            return cityList.size();
        }

        @Override
        public String getItem(int i) {
            return cityList.get(i);
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

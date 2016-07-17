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
import com.example.coolweather.model.Province;
import com.example.coolweather.util.ActivityManager;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择要查询天气的区域的activity
 */
public class ChooseProvinceActivity extends Activity {
    private CoolWeatherDB coolWeatherDB;
    private ListView listView;
    private List<String> provinceList = new ArrayList<>();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            listView.setAdapter(new MyAdapter());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.add(this);
        setContentView(R.layout.choose_area);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("中国");
        listView = (ListView) findViewById(R.id.list_city);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        query();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChooseCityActivity.class);
                intent.putExtra("ProvinceName",coolWeatherDB.loadProvince().get(i).getmProvinceName());
                startActivity(intent);
            }
        });
    }

    /**
     * 先从数据库中找数据，如果没有就发送网络请求获取数据
     */
    private void query() {
        List<Province> list = coolWeatherDB.loadProvince();
        if(list.size()>0){
            for(Province p:list){
                provinceList.add(p.getmProvinceName());
            }
            handler.sendEmptyMessage(0);
        } else{
            HttpUtil.sendHttpRequest("http://v.juhe.cn/weather/citys?key=e82ade8e346ca24743ee02c320498812", new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    provinceList = Utility.handleProvincesResponse(coolWeatherDB,response,provinceList);
                    handler.sendEmptyMessage(0);
                    for(int x=0;x<provinceList.size();x++){
                        Log.i("MainActivity",provinceList.get(x));
                    }
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return provinceList.size();
        }

        @Override
        public String getItem(int i) {
            return provinceList.get(i);
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

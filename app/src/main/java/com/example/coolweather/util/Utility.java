package com.example.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.model.City;
import com.example.coolweather.model.CoolWeatherDB;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 解析json字符串，并存储的工具类
 */
public class Utility {
    private static SharedPreferences sharedPreferences = null;
    private static final String tag = "Utility";

    /**
     * 解析Province数据并保存至数据库
     * @param coolWeatherDB 数据库操作的工具类对象
     * @param response 服务器返回的json字符串
     * @param provinceList 显示到listview上的数据
     * @return 加载了数据的cityList
     */
    public synchronized static List<String> handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response,List<String> provinceList){
        if(!TextUtils.isEmpty(response)){
            try {
                Province province = new Province();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray all = jsonObject.getJSONArray("result");
                for(int x=0;x<all.length();x++){
                    JSONObject jsonProvince = all.getJSONObject(x);
                    String provinceData = jsonProvince.getString("province");
                    if(!provinceList.contains(provinceData)){
                        provinceList.add(provinceData);
                        province.setmProvinceName(provinceData);
                        coolWeatherDB.saveProvince(province);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return provinceList;
    }

    /**
     * 解析City数据并保存至数据库
     * @param coolWeatherDB 数据库操作的工具类对象
     * @param response  服务器返回的json字符串
     * @param cityList  显示到listview上的数据
     * @param provinceName  对应的省名称
     * @return  加载了数据的cityList
     */
    public synchronized static List<String> handleCityResponse(CoolWeatherDB coolWeatherDB,String response,List<String> cityList,String provinceName){
        if(!TextUtils.isEmpty(response)){
            try {
                City city = new City();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray all = jsonObject.getJSONArray("result");
                for(int x=0;x<all.length();x++){
                    JSONObject jsonProvince = all.getJSONObject(x);
                    String provinceData = jsonProvince.getString("province");
                    if(provinceData.equals(provinceName)) {
                        String cityData = jsonProvince.getString("city");
                        if (!cityList.contains(cityData)) {
                            cityList.add(cityData);
                            city.setmCityName(cityData);
                            city.setmProvinceName(provinceName);
                            coolWeatherDB.saveCity(city);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cityList;
    }

    /**
     * 解析County数据并保存至数据库
     * @param coolWeatherDB 数据库操作的工具类对象
     * @param response  服务器返回的json字符串
     * @param countyList  显示到listview上的数据
     * @param cityName  对应的市名称
     * @return  加载了数据的countyList
     */
    public synchronized static List<String> handleCountyResponse(CoolWeatherDB coolWeatherDB,String response,List<String> countyList,String cityName){
        if(!TextUtils.isEmpty(response)){
            try {
                County county = new County();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray all = jsonObject.getJSONArray("result");
                for(int x=0;x<all.length();x++){
                    JSONObject jsonProvince = all.getJSONObject(x);
                    String cityData = jsonProvince.getString("city");
                    if(cityData.equals(cityName)) {
                        String countyData = jsonProvince.getString("district");
                        if (!countyList.contains(countyData)) {
                            countyList.add(countyData);
                            county.setmCountyName(countyData);
                            county.setmCityName(cityData);
                            coolWeatherDB.saveCounty(county);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return countyList;
    }


    /**
     * 解析服务器返回的天气json字符串，并存储至sp中
     * @param context 上下文环境
     * @param response  服务器返回的json字符串
     * @param areaName  要查询天气的地名
     * @return  是否解析成功
     */
    public synchronized static boolean saveWeatherInfo(Context context, String response,String areaName){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String reason = jsonObject.getString("reason");
                if(reason.equals("successed!")){
                    Log.i(tag,"successed");
                }
                JSONObject all = jsonObject.getJSONObject("result");
                JSONObject sk = all.getJSONObject("sk");
                String publishTime = "更新时间 "+sk.getString("time");
                JSONObject today = all.getJSONObject("today");
                String time = today.getString("date_y")+" "+today.getString("week");
                String weather = today.getString("weather");
                String temperature = today.getString("temperature");
                if(sharedPreferences ==null) {
                    sharedPreferences = context.getSharedPreferences(ContentValue.WEATHER_INFORMATION, Context.MODE_PRIVATE);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ContentValue.AREA_NAME,areaName);
                Log.i(tag,publishTime);
                Log.i(tag,time);
                Log.i(tag,weather);
                Log.i(tag,temperature);
                editor.putString(ContentValue.PUBLISH_TIME,publishTime);
                editor.putString(ContentValue.TODAY,time);
                editor.putString(ContentValue.WEATHER_DES,weather);
                editor.putString(ContentValue.TEMP,temperature);
                editor.commit();
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 从sp中读取天气情况
     * @param context 上下文
     * @param key   数据储存的key
     * @return  得到的字符串数据
     */
    public static String getWeatherInfo(Context context, String key){
        if(sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(ContentValue.WEATHER_INFORMATION, Context.MODE_PRIVATE);
        }
        String result = sharedPreferences.getString(key,null);
        return result;
    }

    public static void setWeatherInfo(Context context,String key,String value){
        if(sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(ContentValue.WEATHER_INFORMATION, Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putString(key,value).commit();
    }
}

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
 * Created by 毛琦 on 2016/7/13.
 */
public class Utility {
    private static SharedPreferences sharedPreferences = null;
    private static final String tag = "Utility";
    /**
     * 将省份信息存入数据库
     * @param coolWeatherDB 数据库工具类对象
     * @param response  服务器返回的json字符串
     * @return  true 保存成功 flase保存失败
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
     * 将城市信息存入数据库
     * @param coolWeatherDB 数据库工具类对象
     * @param response 服务器返回的json字符串
     * @param provinceName 省份的名字
     * @return true 保存成功 flase保存失败
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
     * 将县城，区信息存入数据库
     * @param coolWeatherDB 数据库工具类对象
     * @param response 服务器返回的json字符串
     * @param cityName 城市的名字
     * @return true 保存成功 flase保存失败
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

    public static String getWeatherInfo(Context context, String key){
        if(sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(ContentValue.WEATHER_INFORMATION, Context.MODE_PRIVATE);
        }
        String result = sharedPreferences.getString(key,null);
        return result;
    }
}

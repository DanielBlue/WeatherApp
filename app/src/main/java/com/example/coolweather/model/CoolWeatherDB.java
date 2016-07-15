package com.example.coolweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coolweather.db.CoolWeatherOpenHelper;
import com.example.coolweather.util.ContentValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库的工具类.
 */
public class CoolWeatherDB {
    private static final int mVersion = 1;
    private SQLiteDatabase mDb;
    private static CoolWeatherDB mCoolWeatherDb;

    /**
     * 私有化构造方法
     * @param context 上下文环境
     */
    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context, ContentValue.DB_NAME,null,mVersion);
        mDb = coolWeatherOpenHelper.getWritableDatabase();
    }

    /**
     * 得到DBUtil的实例对象
     * @param context   上下文环境
     * @return  DBUtil实例对象
     */
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(mCoolWeatherDb == null){
            mCoolWeatherDb = new CoolWeatherDB(context);
        }
        return mCoolWeatherDb;
    }

    /**
     * 将Province的数据保存到数据库
     * @param province
     */
    public void saveProvince(Province province){
        ContentValues contentValues = new ContentValues();
        contentValues.put("province_name",province.getmProvinceName());
        mDb.insert("Province",null,contentValues);
    }

    /**
     * 取出数据库中保存的Province数据，每一组数据封装到一个Province对象中
     * @return  List<Province> Province对象的集合
     */
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = mDb.query("Province",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Province province = new Province();
            province.setmId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setmProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            list.add(province);
        }
        cursor.close();
        return list;
    }

    /**
     * 将City的数据保存到数据库
     * @param city
     */
    public void saveCity(City city){
        ContentValues contentValues = new ContentValues();
        contentValues.put("city_name",city.getmCityName());
        contentValues.put("province_name",city.getmProvinceName());
        mDb.insert("City",null,contentValues);
    }

    /**
     * 取出数据库中保存的City数据，每一组数据封装到一个City对象中
     * @return List<City> City对象的集合
     */
    public List<City> loadCity(String provinceName){
        List<City> list = new ArrayList<>();
        Cursor cursor = mDb.query("City",null,"province_name = ?",new String[]{provinceName},null,null,null);
        while (cursor.moveToNext()){
            City city = new City();
            city.setmId(cursor.getInt(cursor.getColumnIndex("id")));
            city.setmCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            city.setmProvinceName(provinceName);
            list.add(city);
        }
        cursor.close();
        return list;
    }

    /**
     * 将County的数据保存到数据库
     * @param county
     */
    public void saveCounty(County county){
        ContentValues contentValues = new ContentValues();
        contentValues.put("county_name",county.getmCountyName());
        contentValues.put("city_name",county.getmCityName());
        mDb.insert("County",null,contentValues);
    }

    /**
     * 取出数据库中保存的County数据，每一组数据封装到一个County对象中
     * @return List<County> County对象的集合
     */
    public List<County> loadCounty(String cityName){
        List<County> list = new ArrayList<>();
        Cursor cursor = mDb.query("County",null,"city_name = ?",new String[]{cityName},null,null,null);
        while (cursor.moveToNext()){
            County county = new County();
            county.setmId(cursor.getInt(cursor.getColumnIndex("id")));
            county.setmCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
            county.setmCityName(cityName);
            list.add(county);
        }
        cursor.close();
        return list;
    }

}

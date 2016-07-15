package com.example.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * 创建省市县的数据表
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    /**
     * 省份的SQL建表语句
     */
    private static final String province = "create table Province(" +
            "id integer primary key autoincrement," +
            "province_name text)";
    /**
     * 市的SQL建表语句
     */
    private static final String city = "create table City(" +
            "id integer primary key autoincrement," +
            "city_name text," +
            "province_name text)";
    /**
     * 县的SQL建表语句
     */
    private static final String county = "create table County(" +
            "id integer primary key autoincrement," +
            "county_name text," +
            "city_name text)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(province);
        sqLiteDatabase.execSQL(city);
        sqLiteDatabase.execSQL(county);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

package com.example.coolweather.model;

/**
 * Created by 毛琦 on 2016/7/12.
 */
public class County {
    private int mId;
    private String mCountyName;
    private String mCityName;

    public String getmCityName() {
        return mCityName;
    }

    public void setmCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getmCountyName() {
        return mCountyName;
    }

    public void setmCountyName(String mCountyName) {
        this.mCountyName = mCountyName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}

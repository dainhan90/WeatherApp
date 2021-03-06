package com.example.weather.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thanhle on 4/12/16.
 */
public class Data {

    @SerializedName("time")
    public long time;

    @SerializedName("summary")
    public String summary;

    @SerializedName("icon")
    public String icon;

    @SerializedName("temperature")
    public String temperature;

    @SerializedName("dewPoint")
    public String dewPoint;

    @SerializedName("humidity")
    public String humidity;

    @SerializedName("windSpeed")
    public String windSpeed;

    @SerializedName("temperatureMin")
    public String temperatureMin;

    @SerializedName("temperatureMax")
    public String temperatureMax;
}

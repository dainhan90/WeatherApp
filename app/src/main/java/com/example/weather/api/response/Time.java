package com.example.weather.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by thanhle on 4/12/16.
 */
public class Time {

    @SerializedName("startPeriodName")
    public ArrayList<String> startPeriodName = new ArrayList<String>();

    @SerializedName("startValidTime")
    public ArrayList<String> startValidTime = new ArrayList<String>();

    @SerializedName("tempLabel")
    public ArrayList<String> tempLabel = new ArrayList<String>();

}

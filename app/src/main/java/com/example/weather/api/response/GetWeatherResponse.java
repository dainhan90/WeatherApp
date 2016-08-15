package com.example.weather.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by thanhle on 4/12/16.
 */
public class GetWeatherResponse extends BaseResponse{

    @SerializedName("currently")
    public CurrentObservation currently;

    @SerializedName("daily")
    public Daily daily;

    public class Daily{
        @SerializedName("summary")
        public String summary;

        @SerializedName("icon")
        public String icon;

        @SerializedName("data")
        public ArrayList<Data> data = new ArrayList<Data>();

    }

}

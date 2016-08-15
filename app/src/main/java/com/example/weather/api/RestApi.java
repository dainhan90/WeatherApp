package com.example.weather.api;

import com.example.weather.api.response.GetWeatherResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by thanhle on 4/12/16.
 */
public interface RestApi {
    @GET("/{api_key}/{lat},{lon}")
    void getWeatherByLocation(@Path("api_key") String apiKey,
                              @Path("lat") double latitude,
                              @Path("lon") double longitude,
                             Callback<GetWeatherResponse> callback);

}

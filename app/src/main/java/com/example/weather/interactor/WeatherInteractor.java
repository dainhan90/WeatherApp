package com.example.weather.interactor;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;
import com.example.weather.api.RestConnector;
import com.example.weather.api.response.CurrentObservation;
import com.example.weather.api.response.GetWeatherResponse;
import com.example.weather.application.MyApplication;

/**
 * Created by thanhle on 4/12/16.
 */
public enum WeatherInteractor {
    INTERACTOR;

    private Bus bus = new Bus(ThreadEnforcer.MAIN);

    private boolean isOpened = false;
    private String token;
    private String deviceID = "";
    private Location lastLocation;
    //"-33.873651", "151.2068896"//lat=41.885575&lon=-87.644408
    public double latFake = 41.885575;
    public double lonFake = -87.644408;
    private GetWeatherResponse.Daily dailyWeather;
    private CurrentObservation currentWeather;

    /*----------------------------- Getter & Setter Methods -------------------------------------*/

    public GetWeatherResponse.Daily getDailyWeather() {
        return dailyWeather;
    }

    public void setDailyWeather(GetWeatherResponse.Daily dailyWeather) {
        this.dailyWeather = dailyWeather;
    }

    public CurrentObservation getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentObservation currentWeather) {
        this.currentWeather = currentWeather;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void register(final Object object) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(MyApplication.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    WeatherInteractor.this.bus.register(object);
                }
            });
        } else {
            WeatherInteractor.this.bus.register(object);
        }
    }

    public void unregister(final Object object) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(MyApplication.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        WeatherInteractor.this.bus.unregister(object);
                    } catch (Throwable ignored) {

                    }
                }
            });
        } else {
            try {
                WeatherInteractor.this.bus.unregister(object);
            } catch (Throwable ignored) {

            }
        }
    }

    /**
     * Broadcast event to all subscriber
     *
     * @param event Object to post
     */
    private void post(final Object event) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(MyApplication.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    WeatherInteractor.this.bus.post(event);
                }
            });
        } else {
            WeatherInteractor.this.bus.post(event);
        }
    }

    public void initialize() {
        try {
            RestConnector.getInstance().register(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void uninitialize() {
        RestConnector.getInstance().unregister(this);
    }

    /*-----------------------------------    Get Weather For Location  -------------------------------*/
    public void getWeatherByLocation() {
        if(lastLocation == null) {
            RestConnector.getInstance().getWeatherByLocation(latFake, lonFake);
        } else
            RestConnector.getInstance().getWeatherByLocation(lastLocation.getLatitude(), lastLocation.getLongitude());
    }

    @Subscribe
    public void onGetWeatherResponse(GetWeatherResponse response) {
        if (response.isSuccessful()) {
            this.setCurrentWeather(response.currently);
            this.setDailyWeather(response.daily);
            post(new GetWeatherEvent(true, ""));
        } else {
            post(new GetWeatherEvent(false, response.message));
        }
    }

    public static class GetWeatherEvent {
        public boolean isSuccessful;
        public String message;

        public GetWeatherEvent(boolean isSuccessful, String message) {
            this.isSuccessful = isSuccessful;
            this.message = message;
        }
    }

}


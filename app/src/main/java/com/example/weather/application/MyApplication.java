package com.example.weather.application;

import android.app.Application;
import android.content.Context;


/**
 * Created by thanhle on 4/12/16.
 */
public class MyApplication extends Application{

    private static Context context;

    public synchronized static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}

package com.example.weather.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thanhle on 4/12/16.
 */
public class SharedPreferencesManager {

    private static final String MY_FILE_NAME = "DealzTapPreference";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String IS_REMEMBER = "is_remember";
    private static final String IS_LOGIN = "is_login";
    private static final String PROFILE = "profile";

    public static boolean isRememberUser(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(IS_REMEMBER, false);
    }

    public static boolean isLogin(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(IS_LOGIN, false);
    }

    public static void saveUser(Context context, String username, String password){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_REMEMBER, true);
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public static void clearUser(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_REMEMBER, false);
        editor.putBoolean(IS_LOGIN, false);
        editor.putString(USERNAME, "");
        editor.putString(PASSWORD, "");
        editor.commit();
    }

    public static String getUsername(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getString(USERNAME, "");
    }

    public static String getPassword(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getString(PASSWORD, "");
    }

    public static void saveProfile(Context context, String profile){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PROFILE, profile);
        editor.commit();
    }

    public static String getProfile(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        return pref.getString(PROFILE, "");
    }

    public static void clearProfile(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PROFILE, "");
        editor.commit();
    }

}

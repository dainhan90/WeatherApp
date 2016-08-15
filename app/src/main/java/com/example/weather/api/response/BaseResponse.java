package com.example.weather.api.response;

/**
 * Created by thanhle on 4/12/16.
 */
public class BaseResponse {

    public boolean isSuccess = true;

    public String message;

    public boolean isSuccessful() {
        return isSuccess;
    }

}
package com.example.weather.api;

import android.os.Handler;
import android.os.Looper;

import com.example.weather.BuildConfig;
import com.example.weather.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.example.weather.api.response.BaseResponse;
import com.example.weather.api.response.GetWeatherResponse;
import com.example.weather.application.MyApplication;
import com.example.weather.common.AppConfig;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by thanhle on 4/12/16.
 */
public class RestConnector {

    private static final String TAG = RestConnector.class.getName();

    private static RestConnector instance;

    private final Bus bus;

    private final RestApi api;

    private RestConnector() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1, TimeUnit.MINUTES);
        okHttpClient.setConnectTimeout(1, TimeUnit.MINUTES);
        okHttpClient.setWriteTimeout(1, TimeUnit.MINUTES);
        okHttpClient.setRetryOnConnectionFailure(true);
        RestAdapter.Builder builder = new RestAdapter.Builder();

        builder.setEndpoint(BuildConfig.API_URL);
        builder.setRequestInterceptor(new RequestInterceptor());
        builder.setClient(new OkClient(okHttpClient));
        if (AppConfig.DEBUG_MODE) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        RestAdapter adapter = builder.build();

        api = adapter.create(RestApi.class);

        bus = new Bus(ThreadEnforcer.MAIN);
    }

    public static synchronized RestConnector getInstance() {
        if (null == instance) {
            instance = new RestConnector();
        }
        return instance;
    }

    public void register(final Object object) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(MyApplication.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    RestConnector.this.bus.register(object);
                }
            });
        } else {
            RestConnector.this.bus.register(object);
        }
    }

    public void unregister(final Object object) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(MyApplication.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        RestConnector.this.bus.unregister(object);
                    } catch (Throwable ignored) {
                    }
                }
            });
        } else {
            RestConnector.this.bus.unregister(object);
        }
    }

    private void post(final Object event) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(MyApplication.getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    RestConnector.this.bus.post(event);
                }
            });
        } else {
            RestConnector.this.bus.post(event);
        }
    }

    public Bus getBus() {
        return bus;
    }

    public RestApi getApi() {
        return api;
    }

    private static class Callback<T extends BaseResponse> implements retrofit.Callback<T> {
        final Class<T> resultClass;

        public Callback(Class<T> resultClass) {

            this.resultClass = resultClass;
        }

        @Override
        public void success(T result, Response response) {
            RestConnector.getInstance().post(result);
        }

        @Override
        public void failure(RetrofitError error) {
            String errorMessage;
            switch (error.getKind()) {
                case NETWORK:
                    errorMessage = MyApplication.getContext().getString(R.string.network_failure);
                    break;
                case HTTP:
                    switch (error.getResponse().getStatus()) {
                        case 404:
                            errorMessage = "Not found";
                            break;
                        default:
                            errorMessage = "OOPS! Something wrong!";
                            break;
                    }
                    break;
                case CONVERSION:
                    errorMessage = "OOPS! Cannot retrieve data from server!";
                    break;
                default:
                    errorMessage = "OOPS! Something wrong!";
                    break;
            }
            try {
                final T failed = resultClass.newInstance();
                failed.message = errorMessage;
                failed.isSuccess = false;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        RestConnector.getInstance().post(failed);
                    }
                }, 1000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class RequestInterceptor implements retrofit.RequestInterceptor {
        private String token;

        public RequestInterceptor() {
        }

        @Override
        public void intercept(RequestFacade request) {

        }

    }

    public void getWeatherByLocation(double latitude, double longitude) {
        api.getWeatherByLocation(AppConfig.apiKey, latitude, longitude,
                new Callback<GetWeatherResponse>(GetWeatherResponse.class));
    }

}

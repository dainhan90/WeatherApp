package com.example.weather.presenter;

import com.example.weather.ui.interfaces.IMainView;
import com.squareup.otto.Subscribe;
import com.example.weather.interactor.WeatherInteractor;
import com.example.weather.presenter.interfaces.IMainPresenter;
import com.example.weather.ui.activity.MainActivity;
import com.example.weather.utils.LocationUtils;

/**
 * Created by thanhle on 4/13/16.
 */
public class MainPresenter implements IMainPresenter{

    private IMainView view;

    public MainPresenter(IMainView view) {
        this.view = view;
    }

    @Override
    public void getWeatherDefault() {

        view.onShowProgress();
        WeatherInteractor.INTERACTOR.getWeatherByLocation();
    }

    @Override
    public void getWeather() {

        view.onShowProgress();
        if(WeatherInteractor.INTERACTOR.getLastLocation() == null){
//            view.onHideProgress();
            view.onShowMessage("Can't get your Location!");
            WeatherInteractor.INTERACTOR.getWeatherByLocation();
        } else
            WeatherInteractor.INTERACTOR.getWeatherByLocation();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        WeatherInteractor.INTERACTOR.uninitialize();
        WeatherInteractor.INTERACTOR.unregister(this);
    }

    @Override
    public void onViewCreated() {
        WeatherInteractor.INTERACTOR.initialize();
        WeatherInteractor.INTERACTOR.register(this);
    }

    @Subscribe
    public void onGetWeatherEvent(WeatherInteractor.GetWeatherEvent event){
        view.onHideProgress();
        if (event.isSuccessful){
            view.updateUI();
        }else {
            view.onShowMessage(event.message);
        }
    }

}

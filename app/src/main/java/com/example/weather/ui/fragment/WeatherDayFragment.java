package com.example.weather.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weather.R;
import com.example.weather.api.response.Data;
import com.example.weather.interactor.WeatherInteractor;
import com.example.weather.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thanhle on 4/13/16.
 */
public class WeatherDayFragment extends Fragment {
    private static final String ARG_POSSITION = "ARG_POSITION";

    private int mPosition;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.tvMaxTemp)
    TextView tvMaxTemp;
    @Bind(R.id.tvMinTemp)
    TextView tvMinTemp;
    @Bind(R.id.tvSummaryWeather)
    TextView tvSummaryWeather;
    @Bind(R.id.tvRelh)
    TextView tvRelh;
    @Bind(R.id.tvWind)
    TextView tvWind;

    public WeatherDayFragment() {
    }

    /**
     * @param mPosition Parameter 1.
     * @return A new instance of fragment WeatherDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherDayFragment newInstance(int mPosition) {
        WeatherDayFragment fragment = new WeatherDayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSSITION, mPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSSITION, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_day, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(mPosition > -1){
            Data data = WeatherInteractor.INTERACTOR.getDailyWeather().data.get(mPosition);
            tvDate.setText(ViewUtils.getDate(data.time));
            tvMaxTemp.setText(data.temperatureMax);
            tvMinTemp.setText(data.temperatureMin);
            tvSummaryWeather.setText(data.summary);
            tvRelh.setText(getString(R.string.lb_relh) + " " + data.humidity);
            tvWind.setText(getString(R.string.lb_wind) + " " + data.windSpeed);
        }
    }
}

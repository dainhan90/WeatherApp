package com.example.weather.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.R;
import com.example.weather.presenter.interfaces.IMainPresenter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.example.weather.api.response.CurrentObservation;
import com.example.weather.api.response.Data;
import com.example.weather.interactor.WeatherInteractor;
import com.example.weather.presenter.MainPresenter;
import com.example.weather.ui.adapter.CustomPagerAdapter;
import com.example.weather.ui.interfaces.IMainView;
import com.example.weather.utils.LocationUtils;
import com.example.weather.utils.MapUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by thanhle on 4/12/16.
 */
public class MainActivity extends BaseActivity implements IMainView, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ViewPager.OnPageChangeListener{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    @Bind(R.id.layout)
    View mLayout;
    @Bind(R.id.pager)
    ViewPager mViewPager;
    @Bind(R.id.tvLocation)
    TextView tvLocation;
    @Bind(R.id.tvCurrentTemp)
    TextView tvCurrentTemp;
    @Bind(R.id.tvSummaryWeather)
    TextView tvSummaryWeather;
    @Bind(R.id.tvRelh)
    TextView tvRelh;
    @Bind(R.id.tvWind)
    TextView tvWind;
    @Bind(R.id.tvDailySumary)
    TextView tvDailySumary;
    @Bind(R.id.pagerIndicator)
    LinearLayout pagerIndicator;
    @Bind(R.id.btnNext)
    ImageButton btnNext;
    @Bind(R.id.btnBack)
    ImageButton btnBack;
    @Bind(R.id.loading)
    View loading;



    private int dotsCount;
    private ImageView[] dots;

    private ProgressDialog progress;

    private IMainPresenter presenter;
    CustomPagerAdapter mCustomPagerAdapter;
    ArrayList<Data> datas = new ArrayList<Data>();

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected int getMainLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindEventHandlers() {

    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initComponents() {
        presenter = new MainPresenter(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        checkLocationService();
        checkRequestPermission();
        presenter.onViewCreated();
        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this, datas);
        mViewPager.setAdapter(mCustomPagerAdapter);

    }

    /**
     * Request permission
     * @return
     */
    private boolean checkRequestPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(mLayout, R.string.permission_read_phone_state_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mLastLocation == null)
            getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("LOCATION SUSPEND : ", "==" + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
        }
        Log.e("LOCATION ERROR : ", "==" + connectionResult.getErrorCode());
    }

    /**
     * Get current location
     */
    private void getLocation() {
        final Location locationMove = LocationServices.FusedLocationApi.getLastLocation (
                mGoogleApiClient);
        mLastLocation = locationMove;
        Log.e("LOCATION Call get: ","==");
        if (locationMove != null) {
            WeatherInteractor.INTERACTOR.setLastLocation(mLastLocation);
            if(!LocationUtils.isConnectingToInternet(MainActivity.this)){
                onShowMessage("Network failure!");
                return;
            }
            presenter.getWeather();
            tvLocation.setText(MapUtil.getAddressFromAddress(this, mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        } else {
//            presenter.getWeather();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("LOCATION Call get: ","==");
        if (location != null) {
            mLastLocation = location;
            WeatherInteractor.INTERACTOR.setLastLocation(mLastLocation);
            if(!LocationUtils.isConnectingToInternet(MainActivity.this)){
                onShowMessage("Network failure!");
                return;
            }
            presenter.getWeather();
            tvLocation.setText(MapUtil.getAddressFromAddress(this, mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
    }

    @Override
    public void updateUI() {
        if(WeatherInteractor.INTERACTOR.getLastLocation() == null)
            tvLocation.setText(MapUtil.getAddressFromAddress(this, WeatherInteractor.INTERACTOR.latFake, WeatherInteractor.INTERACTOR.lonFake));
        if(WeatherInteractor.INTERACTOR.getCurrentWeather() != null){
            loading.setVisibility(View.GONE);
            CurrentObservation currentObservation = WeatherInteractor.INTERACTOR.getCurrentWeather();
            tvCurrentTemp.setText(currentObservation.temperature + getString(R.string.lb_unit));
            tvSummaryWeather.setText(currentObservation.summary);
            tvRelh.setText(getString(R.string.lb_relh) + " " + currentObservation.humidity);
            tvWind.setText(getString(R.string.lb_wind) + " " + currentObservation.windSpeed);
        }

        if(!WeatherInteractor.INTERACTOR.getDailyWeather().data.isEmpty()){
            tvDailySumary.setText(WeatherInteractor.INTERACTOR.getDailyWeather().summary);
            datas.clear();
            datas.addAll(WeatherInteractor.INTERACTOR.getDailyWeather().data);
            mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this, datas);
            mViewPager.setAdapter(mCustomPagerAdapter);
            dotsCount = mCustomPagerAdapter.getCount();
            dots = new ImageView[dotsCount];
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(4, 0, 4, 0);

                pagerIndicator.addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            mViewPager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onShowProgress() {
        onHideProgress();
        progress = ProgressDialog.show(this, null, getString(R.string.loading), true);
        progress.setCancelable(false);
    }

    @Override
    public void onHideProgress() {
        if (progress == null){
            return;
        }
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    @OnClick({R.id.btnBack, R.id.btnNext})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                mViewPager.setCurrentItem((mViewPager.getCurrentItem() < dotsCount)
                        ? mViewPager.getCurrentItem() + 1 : 0);
                break;
            case R.id.btnBack:
                mViewPager.setCurrentItem((mViewPager.getCurrentItem() > 0)
                        ? mViewPager.getCurrentItem() - 1 : dotsCount-1);
                break;
        }
    }

    @Override
    public void onShowMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.nonselecteditem_dot, null));
        }

        dots[position].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.selecteditem_dot, null));

        if (position + 1 == dotsCount) {
            btnNext.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
        } else if(position == 0){
            btnNext.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void checkLocationService() {
        boolean gpsEnabled = false, networkEnabled = false;
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gpsEnabled && !networkEnabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(
                    R.string.gps_network_not_enabled));
            dialog.setPositiveButton(
                    getResources().getString(
                            R.string.open_location_settings),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            Intent myIntent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                            // get gps
                        }
                    });
            dialog.setNegativeButton(
                    getString(R.string.cancel_location),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            if(!LocationUtils.isConnectingToInternet(MainActivity.this)){
                                onShowMessage("Network failure!");
                                return;
                            }
                            presenter.getWeatherDefault();
                        }
                    });
            dialog.show();
        }
    }
}

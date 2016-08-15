package com.example.weather.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import com.example.weather.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by thanhle on 4/13/16.
 */
public class MapUtil {
    public static String getAddressFromAddress(Context context, double lat, double lng) {
        String city = "";
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses!= null && addresses.size() > 0)
            city = (addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());

        return city;
    }


    public static void checkLocationService(final Context context) {
        boolean gpsEnabled = false, networkEnabled = false;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gpsEnabled && !networkEnabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(
                    R.string.gps_network_not_enabled));
            dialog.setPositiveButton(
                    context.getResources().getString(
                            R.string.open_location_settings),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {
                            Intent myIntent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(myIntent);
                            // get gps
                        }
                    });
            dialog.setNegativeButton(
                    context.getString(R.string.cancel_location),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(
                                DialogInterface paramDialogInterface,
                                int paramInt) {

                        }
                    });
            dialog.show();
        }
    }

    public static boolean isLocationEnable(Context context){
        boolean gpsEnabled = false, networkEnabled = false;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gpsEnabled && !networkEnabled){
            return  false;
        }
        return true;
    }

//    public static Location getLocation(Context context) {
//        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
//        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if (location2 != null) {
//            return location2;
//        } else {
//            return location1;
//        }
//    }

    public static void viewLocationOnMap(Context context, String lat, String lng){
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static void showLocationSettingDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("Do you want to enable Location Service?");
        // On pressing Settings button
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}

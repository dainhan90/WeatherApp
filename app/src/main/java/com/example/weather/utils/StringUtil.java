package com.example.weather.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by thanhle on 4/13/16.
 */
public class StringUtil {

    public static Date stringToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = null;
        try {
            dateString = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static String parseDateToString(Date date, String pattern) {
        return date != null && pattern != null ? new SimpleDateFormat(pattern,
                Locale.getDefault()).format(date) : null;
    }

    public static Date parseStringToDate(String data, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * Check string 's empty or no
     *
     * @param string
     * @return true if string's null or length = 0, false otherwise
     */
    public static boolean isEmptyString(String string) {
        if (string == null){
            return true;
        }
        if (string.trim().equals("") || string.trim().length() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateBirthday(String strDate, String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date currentDate = new Date();
        try {
            Date date = dateFormat.parse(strDate);
            return (date.before(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getColorIdList(String str){
        List<String> list = new ArrayList<>();
        if (str != null && !str.equals("")){
            for (int i = 0; i < str.length() ; i++){
                if (str.charAt(i) == ','){
                    list.add(str.substring(0,i));
                    str = str.substring(i+1);
                    i = 0;
                }
                if (i == str.length()-1){
                    list.add(str);
                }
            }
        }
        return list;
    }

    public static String formatPrice(String price){
        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        return formatter.format(Double.valueOf(price));
    }

    public static Map<String, Long> getTimeBetweenDateWithCurrent(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        //TimeZone.getTimeZone()
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date currentDate = calendar.getTime();
//        Date currentDate = new Date();
        Date d2 = null;

        Map<String, Long> result = new HashMap<>();

        try {
            d2 = format.parse(strDate);

            //in milliseconds
            long diff = d2.getTime() - currentDate.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            result.put("days", diffDays );
            result.put("hours", diffHours );
            result.put("minutes", diffMinutes );
            result.put("seconds", diffSeconds );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Map<String, Long> getTimeLeftOfClaimed(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        //TimeZone.getTimeZone()
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Date currentDate = calendar.getTime();
//        Date currentDate = new Date();
        Date d2 = null;

        Map<String, Long> result = new HashMap<>();

        try {
            d2 = format.parse(strDate);

            //in milliseconds
            long diff = d2.getTime() - currentDate.getTime();
            if(diff<0){
                Log.e("CURRENT:",""+currentDate + "||" + d2);
            }
            long leftTime = (24 * 60 * 60 * 1000) + diff;

            long diffSeconds = leftTime / 1000 % 60;
            long diffMinutes = leftTime / (60 * 1000) % 60;
            long diffHours = leftTime / (60 * 60 * 1000) % 24;
            long diffDays = leftTime / (24 * 60 * 60 * 1000);

            result.put("days", diffDays );
            result.put("hours", diffHours );
            result.put("minutes", diffMinutes );
            result.put("seconds", diffSeconds );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String capitalize(String str){
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}


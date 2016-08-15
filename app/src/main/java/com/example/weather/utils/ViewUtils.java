package com.example.weather.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by thanhle on 4/13/16.
 */
public class ViewUtils {

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time*1000);
        String date = DateFormat.format("EEE, MMM d", cal).toString();
        return date;
    }

    public static int getUsableScreenHeight(Context context, View mainView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();

            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            return metrics.heightPixels;

        } else {
            return mainView.getRootView().getHeight();
        }
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null && inputManager != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                inputManager.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public static void setStatusBarBackground(Activity activity, int color) {
        final View decorView = activity.getWindow().getDecorView();
        View view = new View(activity);
        final int statusBarHeight = getStatusBarHeight(activity);
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight));
        view.setBackgroundColor(color);
        ((ViewGroup)decorView).addView(view);
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("") || str.equals("null");
    }

    public static void showToast(Context context, String message) {
        if (context != null && !isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, int idString) {
        if (context != null) {
            String message = context.getString(idString);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static Fragment getCurrentChildFragment(FragmentActivity activity) {
        Fragment currentFragment = getCurrentFragment(activity);
        if (currentFragment != null) {
            FragmentManager manager = currentFragment.getChildFragmentManager();
            List<Fragment> fragmentList = manager.getFragments();
            if (fragmentList != null)
                if (fragmentList.size() > 0)
                    for (Fragment aFragmentList : fragmentList) {
                        if (aFragmentList != null && aFragmentList.isVisible())
                            return aFragmentList;
                    }
        }
        return null;
    }

    public static Boolean isFragmentAdded(FragmentManager manager, String tag) {
        List<Fragment> fragmentList = manager.getFragments();
        if (fragmentList != null) {
            if (fragmentList.size() > 0) {
                for (Fragment fragment : fragmentList) {
                    if (fragment != null) {
                        if (fragment.getClass().getName().equals(tag))
                            return true;
                    }
                }
            }
        }
        return false;
    }



    public static Fragment getCurrentFragment(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        List<Fragment> fragmentList = manager.getFragments();
        if (fragmentList != null)
            if (fragmentList.size() > 0)
                for (Fragment aFragmentList : fragmentList) {
                    if (aFragmentList != null && aFragmentList.isVisible())
                        return aFragmentList;
                }
        return null;
    }

    public static ListPopupWindow initPopup(Activity context, int background, int row, int textViewId,
                                            final List<String> list, final TextView anchor){
        final ListPopupWindow popupWindow = new ListPopupWindow(context);

        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(background));
        popupWindow.setAdapter(new ArrayAdapter(context, row, textViewId, list));
        popupWindow.setAnchorView(anchor);

//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);

//        popupCountry.setWidth((int) getActivity().getResources().getDimension(R.dimen.popup_w));
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setModal(true);

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                anchor.setText(list.get(position));
            }
        });

        return popupWindow;
    }
}

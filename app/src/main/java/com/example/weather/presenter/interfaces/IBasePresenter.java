package com.example.weather.presenter.interfaces;

/**
 * Created by thanhle on 4/12/16.
 */
public interface IBasePresenter {

   /**
    * Call when view resumed
    */
   void onResume();

   /**
    * Call when view paused
    */
   void onPause();

   /**
    * Call when view onDestroy
    */
   void onDestroy();

   /**
    * Call when view created
    */
   void onViewCreated();

}

package com.geekbrains.weather;

import android.app.Application;

import es.dmoral.toasty.Toasty;

/**
 * Created by shkryaba on 24/06/2018.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Настройка всплывающих окон оповещений
        Toasty.Config.getInstance().setSuccessColor(getResources().getColor(R.color.blue)).apply();
    }
}

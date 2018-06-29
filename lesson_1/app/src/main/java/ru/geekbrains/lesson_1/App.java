package ru.geekbrains.lesson_1;

import android.app.Application;
import es.dmoral.toasty.Toasty;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Toasty.Config.getInstance()
                //.setSuccessColor(getResources().getColor(R.color.blue))
                .apply();

    }
}

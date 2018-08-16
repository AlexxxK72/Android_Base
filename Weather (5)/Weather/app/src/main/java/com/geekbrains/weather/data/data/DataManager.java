package com.geekbrains.weather.data.data;

import android.util.Log;

import com.geekbrains.weather.model.weather.WeatherRequest;
import com.geekbrains.weather.retrofit.OpenWeather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shkryaba on 01/08/2018.
 */

public class DataManager implements IDataManager {

    private static final String BASE_URL = "https://samples.openweathermap.org/";
    private static final String TAG = "DataManager";
    private OpenWeather openWeather;

    @Override
    public void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeather = retrofit.create(OpenWeather.class);
    }

    @Override
    public void requestRetrofit(String city, String keyApi) {
        openWeather.loadWeather(city, keyApi).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response != null) {
                    Log.d(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                Log.d(TAG, t.getMessage());

            }
        });
    }
}

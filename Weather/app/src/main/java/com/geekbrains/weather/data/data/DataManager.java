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

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String TAG = "DataManager";
    private CallBackDM wFragment;
    private OpenWeather openWeather;

    @Override
    public void initRetrofit(CallBackDM wFragment) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeather = retrofit.create(OpenWeather.class);

        this.wFragment = wFragment;
    }

    @Override
    public void requestRetrofitToName(String city, String keyApi) {
        if(city != null) {
            openWeather.loadWeatherName(city, "metric", keyApi).enqueue(new Callback<WeatherRequest>() {
                @Override
                public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                    if (response != null) {
                        wFragment.onResponse(response.body());
                    }
                }

                @Override
                public void onFailure(Call<WeatherRequest> call, Throwable t) {
                    Log.d(TAG, t.getMessage());

                }
            });
        }
    }

    @Override
    public void requestRetrofitToCoord(String lat, String lon, String keyApi) {
        openWeather.loadWeatherCoord(lat, lon, "metric", keyApi).enqueue(new Callback<WeatherRequest>() {
            @Override
            public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                if (response != null) {
                    wFragment.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherRequest> call, Throwable t) {
                Log.d(TAG, t.getMessage());

            }
        });
    }

    public interface CallBackDM{
        void onResponse(WeatherRequest response);
    }
}

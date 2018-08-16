package com.geekbrains.weather.retrofit;

import com.geekbrains.weather.model.weather.WeatherRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by shkryaba on 01/08/2018.
 */

public interface OpenWeather {

    @GET("/data/2.5/weather")
    Call<WeatherRequest> loadWeatherName(@Query("q") String cityCountry, @Query("units") String units, @Query("appid") String keyApi);

    @GET("/data/2.5/weather")
    Call<WeatherRequest> loadWeatherCoord(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("appid") String keyApi);
}
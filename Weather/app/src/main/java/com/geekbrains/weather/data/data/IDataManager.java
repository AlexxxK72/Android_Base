package com.geekbrains.weather.data.data;

import com.geekbrains.weather.model.weather.WeatherRequest;

/**
 * Created by shkryaba on 01/08/2018.
 */

public interface IDataManager {
    void initRetrofit(DataManager.CallBackDM wFragment);
    void requestRetrofitToName(String city, String keyApi);
    void requestRetrofitToCoord(String lat, String lon, String keyApi);
}

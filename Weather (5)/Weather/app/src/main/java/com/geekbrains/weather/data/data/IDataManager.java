package com.geekbrains.weather.data.data;

/**
 * Created by shkryaba on 01/08/2018.
 */

public interface IDataManager {
    void initRetrofit();

    void requestRetrofit(String city, String keyApi);
}

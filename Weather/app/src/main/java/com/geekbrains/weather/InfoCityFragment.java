package com.geekbrains.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shkryaba on 24/06/2018.
 */

public class InfoCityFragment extends BaseFragment {

    private static final String NAME = "name";
    private static final String HUMIDITY = "city_humidity";
    private static final String WIND_SPEED = "wind_speed";
    private static final String PRESSURE = "pressure";

    private String name;
    private int humidity;
    private int windSpeed;
    private int pressure;

    TextView txtInfo;

    public static InfoCityFragment newInstance(String name, int cityHumidity, int  windSpeed, int pressure) {

        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putInt(HUMIDITY, cityHumidity);
        args.putInt(WIND_SPEED, windSpeed);
        args.putInt(PRESSURE, pressure);
        InfoCityFragment fragment = new InfoCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            name = getArguments().getString(NAME, null);
            humidity = getArguments().getInt(HUMIDITY, 0);
            windSpeed = getArguments().getInt(WIND_SPEED, 0);
            pressure = getArguments().getInt(PRESSURE, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_city, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        txtInfo = view.findViewById(R.id.city_info);
        writeCityInfo();
    }

    private void writeCityInfo() {
        String cityInfo = name + "\n";
        if (humidity != 0) cityInfo += "Влажность: " + humidity + " %\n";
        if (windSpeed != 0) cityInfo += "Скорость ветра: " + windSpeed + " м/с\n";
        if (pressure != 0) cityInfo += "Давление: " + pressure + " мм.рт.ст.\n";
        txtInfo.setText(cityInfo);
    }
}
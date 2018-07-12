package com.geekbrains.weather;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherFragment extends BaseFragment {

    public static final String CITY_NAME = "city_name";
    public static final String HUMIDITY = "humidity";
    public static final String WIND_SPEED = "wind_speed";
    public static final String PRESSURE = "pressure";

    private String cityName;
    private int humidity;
    private int windSpeed;
    private int pressure;
    private FloatingActionButton fab_send;
    private CallBackWF mBackWF;

    private TextView txtCityName, txtHumidity, txtWindSpeed, txtPressure;

    public static WeatherFragment newInstance(String cityName, Boolean isHumidity, Boolean isWindSpeed, Boolean isPressure) {

        Bundle args = new Bundle();
        args.putString(CITY_NAME, cityName);
        args.putBoolean(HUMIDITY, isHumidity);
        args.putBoolean(WIND_SPEED, isWindSpeed);
        args.putBoolean(PRESSURE, isPressure);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityName = getResources().getString(R.string.country);
        if(savedInstanceState != null){
            cityName = savedInstanceState.getString(CITY_NAME, "");
            humidity = savedInstanceState.getInt(HUMIDITY, 0);
            windSpeed = savedInstanceState.getInt(WIND_SPEED, 0);
            pressure = savedInstanceState.getInt(PRESSURE, 0);
        }
        else if(getArguments() != null){
            cityName = getArguments().getString(CITY_NAME, "");
            initParameters(getArguments().getBoolean(HUMIDITY, false),
                    getArguments().getBoolean(WIND_SPEED, false),
                    getArguments().getBoolean(PRESSURE, false));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CITY_NAME, cityName);
        outState.putInt(HUMIDITY, humidity);
        outState.putInt(WIND_SPEED, windSpeed);
        outState.putInt(PRESSURE, pressure);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        mBackWF = (CallBackWF) getActivity();
        txtCityName = view.findViewById(R.id.tv_country);
        txtCityName.setVisibility(View.VISIBLE);
        if(cityName != null)txtCityName.setText(cityName);
        txtHumidity = view.findViewById(R.id.tv_humidity);
        if(humidity != 0) txtHumidity.setText(humidity + " %");
        txtWindSpeed= view.findViewById(R.id.tv_wind_speed);
        if(windSpeed != 0) txtWindSpeed.setText(windSpeed + " м/с");
        txtPressure = view.findViewById(R.id.tv_pressure);
        if(pressure != 0) txtPressure.setText(pressure + " мм.рт.ст.");


        fab_send = view.findViewById(R.id.fab_send);
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBackWF.fab_history_onClick();
            }
        });
    }

    public String getCityName() {
        return cityName;
    }

    private void initParameters(boolean isHumidity, boolean isWindSpeed, boolean isPressure) {
        humidity = (isHumidity)
                ? 85
                : 0;
        windSpeed = (isWindSpeed)
                ? 10
                : 0;
        pressure = (isPressure)
                ? 745
                : 0;
    }

    public interface CallBackWF {
        void fab_history_onClick();
    }

}

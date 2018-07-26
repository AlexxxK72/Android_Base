package com.geekbrains.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.SENSOR_SERVICE;

public class WeatherFragment extends BaseFragment {

    public static final String CITY_NAME = "city_name";
    public static final String TEMPERATURE = "temperature";
    public static final String HUMIDITY = "humidity";
    public static final String WIND_SPEED = "wind_speed";
    public static final String PRESSURE = "pressure";

    public static final int PERMISSION_REQUEST_CODE = 1111;
    LocationManager locationManager;
    LocationListener locationListener;

    private String cityName, currentCityName;
    private int temperature;
    private int humidity;
    private int windSpeed;
    private int pressure;
    private boolean isHumidity;
    private boolean isWindSpeed;
    private boolean isPressure;

    private SensorManager sensorManager;
    private List<Sensor> listSensors;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;
    private Sensor sensorPressure;

    private FloatingActionButton fab_send;
    private CallBackWF mBackWF;

    private TextView txtCityName, txttemperature, txtHumidity, txtWindSpeed, txtPressure;

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
        setHasOptionsMenu(true);
        initSensor();
        locationManager = (LocationManager) getBaseActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (getArguments() != null) {
            cityName = getArguments().getString(CITY_NAME, "");
            isHumidity = getArguments().getBoolean(HUMIDITY, false);
            isWindSpeed = getArguments().getBoolean(WIND_SPEED, false);
            isPressure = getArguments().getBoolean(PRESSURE, false);

        } else {
            requestForLocationPerm();
            isHumidity = true;
            isWindSpeed = true;
            isPressure = true;
        }
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
        txttemperature = view.findViewById(R.id.tv_temperature);
        txtHumidity = view.findViewById(R.id.tv_humidity);
        txtWindSpeed = view.findViewById(R.id.tv_wind_speed);
        txtPressure = view.findViewById(R.id.tv_pressure);
        fab_send = view.findViewById(R.id.fab_send);
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBackWF.fab_history_onClick();
            }
        });
        initParameters();
    }

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Sensor sensor = sensorEvent.sensor;
            switch (sensor.getType()){
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    temperature = (int) sensorEvent.values[0];
                    break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    humidity = (int) sensorEvent.values[0];
                    break;
                case Sensor.TYPE_PRESSURE:
                    pressure = (int) sensorEvent.values[0];
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void initSensor() {
        sensorManager = (SensorManager) getBaseActivity().getSystemService(SENSOR_SERVICE);
        listSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(listener, sensorTemperature, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, sensorHumidity, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, sensorPressure, SensorManager.SENSOR_DELAY_FASTEST);
        listSensors.get(0).getName();
    }

    private void requestForLocationPerm() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(getBaseActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    private void makeUseOfNewLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(getBaseActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                currentCityName = addresses.get(0).getLocality();
                initParameters();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        locationManager.removeUpdates(locationListener);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.weather_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int action_id = item.getItemId();
        if(action_id == R.id.action_find_location){
            cityName = null;
            requestForLocationPerm();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCityName() {
        if(cityName != null) return cityName;
        if(currentCityName != null) return currentCityName;
        return null;
    }

    //в дальнейшем получение значений погоды через API
    private void initParameters() {
        if(cityName != null){
            txtCityName.setText(cityName);
            //txtCityName.setVisibility(View.VISIBLE);
            txttemperature.setText(String.valueOf(getNetworkParameter(cityName, TEMPERATURE)));
            if (isHumidity) txtHumidity.setText(getNetworkParameter(cityName, HUMIDITY) + " %");
            if (isWindSpeed) txtWindSpeed.setText(getNetworkParameter(cityName, WIND_SPEED) + " м/с");
            if (isPressure) txtPressure.setText(getNetworkParameter(cityName, PRESSURE) + " мм.рт.ст.");
        }
        if(currentCityName != null){
            txtCityName.setText(currentCityName);
//            txtCityName.setVisibility(View.VISIBLE);
            txttemperature.setText(String.valueOf((temperature != 0) ? temperature :getNetworkParameter(cityName, TEMPERATURE)));
            if (isHumidity) txtHumidity.setText(((humidity != 0) ? humidity : getNetworkParameter(currentCityName, HUMIDITY))  + " %");
            if (isWindSpeed) txtWindSpeed.setText(((windSpeed != 0) ? windSpeed : getNetworkParameter(currentCityName, WIND_SPEED))  + " м/с");
            if (isPressure) txtPressure.setText(((pressure != 0) ? pressure : getNetworkParameter(currentCityName, PRESSURE))  + " мм.рт.ст.");

        }

    }

    private int getNetworkParameter(String cityName, String paramName){

        switch (paramName){
            case TEMPERATURE:
                return 18;
            case HUMIDITY:
                return 85;
            case WIND_SPEED:
                return 10;
            case PRESSURE:
                return 745;
            default:
                return 0;
        }
    }


    public interface CallBackWF {
        void fab_history_onClick();
    }

}

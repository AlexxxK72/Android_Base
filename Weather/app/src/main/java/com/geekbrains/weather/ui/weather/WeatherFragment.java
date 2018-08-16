package com.geekbrains.weather.ui.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.geekbrains.weather.Constants;
import com.geekbrains.weather.R;
import com.geekbrains.weather.data.PrefsData;
import com.geekbrains.weather.data.PrefsHelper;
import com.geekbrains.weather.data.data.DataManager;
import com.geekbrains.weather.data.data.IDataManager;
import com.geekbrains.weather.model.weather.Weather;
import com.geekbrains.weather.model.weather.WeatherRequest;
import com.geekbrains.weather.ui.base.BaseFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

public class WeatherFragment extends BaseFragment implements DataManager.CallBackDM {

    public static final String CITY_NAME = "city_name";
    public static final String TEMPERATURE = "temperature";
    public static final String HUMIDITY = "humidity";
    public static final String WIND_SPEED = "wind_speed";
    public static final String PRESSURE = "pressure";

    public static final int PERMISSION_REQUEST_CODE = 1111;
    LocationManager locationManager;
    LocationListener locationListener;
    private PrefsHelper mPrefsHelper;

    private String cityName, currentCityName;
    private int temperature;
    private int humidity;
    private int windSpeed;
    private int pressure;
    private boolean isHumidity = true;
    private boolean isWindSpeed = true;
    private boolean isPressure = true;

    private IDataManager dataManager;
    private String provider;
    private SensorManager sensorManager;
    private List<Sensor> listSensors;
    private Sensor sensorTemperature;
    private Sensor sensorHumidity;
    private Sensor sensorPressure;

    private FloatingActionButton fab_send;
    private CallBackWF mBackWF;

    private TextView txtCityName, txtTemperature, txtHumidity, txtWindSpeed, txtPressure;
    private ImageView ivWeather;

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
        mPrefsHelper = new PrefsData(getBaseActivity());

        initRetrofit();
        initSensor();

        if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // запросим координаты
            requestLocation();
        } else {
            // пермиссии нет, будем запрашивать у пользователя
            requestLocationPermissions();
        }

//        String getSP = mPrefsHelper.getSharedPreferences(Constants.CITY);
//        if (getArguments() != null || !getSP.equals("")) {
//            if(getArguments() != null) {
//                cityName = getArguments().getString(CITY_NAME, "");
//                isHumidity = getArguments().getBoolean(HUMIDITY, false);
//                isWindSpeed = getArguments().getBoolean(WIND_SPEED, false);
//                isPressure = getArguments().getBoolean(PRESSURE, false);
//            }
////            if (cityName == null || cityName.equals("")) cityName = getSP;
//
//        } else
//
//    {
//        isHumidity = true;
//        isWindSpeed = true;
//        isPressure = true;
//    }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_layout, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        mBackWF = (CallBackWF) getActivity();
        ivWeather = view.findViewById(R.id.ivWeather);
        txtCityName = view.findViewById(R.id.tv_country);
        txtTemperature = view.findViewById(R.id.tv_temperature);
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

    private void initRetrofit() {
        dataManager = new DataManager();
        dataManager.initRetrofit(this);
    }

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

    private void requestLocation() {
        // Если пермиссии все таки нет - то просто выйдем, приложение не имеет смысла
        if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager = (LocationManager) getBaseActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        // получим наиболее подходящий провайдер геолокации по критериям
        // Но можно и самому назначать какой провайдер использовать.
        // В основном это LocationManager.GPS_PROVIDER или LocationManager.NETWORK_PROVIDER
        // но может быть и LocationManager.PASSIVE_PROVIDER, это когда координаты уже кто-то недавно получил.
        provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            // Будем получать геоположение через каждые 100 секунд или каждые 100 метров
            locationManager.requestLocationUpdates(provider, 100000, 100, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    makeUseOfNewLocation(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

    // Запрос пермиссии для геолокации
    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getBaseActivity(), Manifest.permission.CALL_PHONE)) {
            // Запросим эти две пермиссии у пользователя
            ActivityCompat.requestPermissions(getBaseActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }


    // Это результат запроса у пользователя пермиссии
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {   // Это та самая пермиссия, что мы запрашивали?
            if (grantResults.length == 2 &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Все препоны пройдены и пермиссия дана
                requestLocation();
            }
        }
    }

    private void makeUseOfNewLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        dataManager.requestRetrofitToCoord(String.valueOf(latitude), String.valueOf(longitude), "b072c8fbe401685cfc1492d1a67b1122");


//        Geocoder geocoder = new Geocoder(getBaseActivity(), Locale.getDefault());
//
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//
//            if (addresses != null) {
//                currentCityName = addresses.get(0).getLocality();
//                initParameters();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
//            requestForLocationPerm();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getCityName() {
        if(cityName != null) return cityName;
        if(currentCityName != null) return currentCityName;
        return null;
    }

    @Override
    public void onResponse(WeatherRequest response) {

        List<Weather> temp = response.getWeather();
        txtCityName.setText(response.getName());
        txtTemperature.setText(response.getMain().getTemp().toString());
        if (isHumidity) txtHumidity.setText(response.getMain().getHumidity() + " %");
        if (isWindSpeed) txtWindSpeed.setText(response.getWind().getSpeed() + " м/с");
        if (isPressure) txtPressure.setText(response.getMain().getPressure() * 0.75 + " мм.рт.ст.");
        Picasso.with(getBaseActivity())
                .load("http://openweathermap.org/img/w/" + response.getWeather().get(0).getIcon() + ".png")
                .resize(250, 250)
                .into(ivWeather);
    }

    //в дальнейшем получение значений погоды через API
    private void initParameters() {
        dataManager.requestRetrofitToName(getCityName(), "b072c8fbe401685cfc1492d1a67b1122");
        if(cityName != null){
            txtCityName.setText(cityName);
            //txtCityName.setVisibility(View.VISIBLE);
            txtTemperature.setText(String.valueOf(getNetworkParameter(cityName, TEMPERATURE)));
            if (isHumidity) txtHumidity.setText(getNetworkParameter(cityName, HUMIDITY) + " %");
            if (isWindSpeed) txtWindSpeed.setText(getNetworkParameter(cityName, WIND_SPEED) + " м/с");
            if (isPressure) txtPressure.setText(getNetworkParameter(cityName, PRESSURE) + " мм.рт.ст.");
        }
        if(currentCityName != null){
            txtCityName.setText(currentCityName);
//            txtCityName.setVisibility(View.VISIBLE);
            txtTemperature.setText(String.valueOf((temperature != 0) ? temperature :getNetworkParameter(cityName, TEMPERATURE)));
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

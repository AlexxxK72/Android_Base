package com.geekbrains.weather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class WeatherFragment extends BaseFragment {

    private EditText txtName;
    private CheckBox checkHumidity, checkWindSpeed, checkPressure;
    private FloatingActionButton fab, fab_send;
    private final String  TAG = "WeatherFragment";
    private CallBackWF mBackWF;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.weather_layout, container, false);
        fab_send = view.findViewById(R.id.fab_send);
        mBackWF = (CallBackWF) getActivity();
        return view;
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        txtName = getBaseActivity().findViewById(R.id.city_name);
        checkHumidity = getBaseActivity().findViewById(R.id.city_humidity);
        checkWindSpeed = getBaseActivity().findViewById(R.id.city_wind_speed);
        checkPressure = getBaseActivity().findViewById(R.id.city_pressure);
        fab = getBaseActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityInfo();
            }
        });
        fab_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBackWF.fab_send_onClick();
            }
        });

    }

    private void startActivityInfo() {
        String name = txtName.getText().toString();
        if(!name.isEmpty()){
            int humidity = (checkHumidity.isChecked())
                    ? 85
                    : 0;
            int windSpeed = (checkWindSpeed.isChecked())
                    ? 10
                    : 0;
            int pressure = (checkPressure.isChecked())
                    ? 745
                    : 0;
            getBaseActivity().startInfoCityFragment(name, humidity, windSpeed, pressure);
        }
        else {
            Toasty
                    .error(getBaseActivity(), getString(R.string.empty_city_name), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public interface CallBackWF {
        void fab_send_onClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}

package com.geekbrains.weather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class InfoCityFragment extends BaseFragment implements RecyclerViewAdapter.OnClickCityListener{

    private CallBackICF mBackICF;
    private EditText txtCityName;
    private Switch swHumidity, swWindSpeed, swPressure;
    private FloatingActionButton btnConfirm;
    private RecyclerView mRecyclerView;
    private List<String> cityList;


    public interface CallBackICF{
        void fab_confirm_onclick(String cityName, Boolean humidity, Boolean windSpeed, Boolean pressure);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBackICF = getBaseActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_city, container, false);
    }

    @Override
    protected void initLayout(View view, Bundle savedInstanceState) {
        initCountryList();
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), this, cityList);
        mRecyclerView.setAdapter(adapter);

        txtCityName = view.findViewById(R.id.city_name);
        swHumidity = view.findViewById(R.id.city_humidity);
        swWindSpeed = view.findViewById(R.id.city_wind_speed);
        swPressure = view.findViewById(R.id.city_pressure);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = txtCityName.getText().toString();
                if(!cityName.isEmpty()){

                    mBackICF.fab_confirm_onclick(cityName, swHumidity.isChecked(), swWindSpeed.isChecked(), swPressure.isChecked());
                }
                else {
                    Toasty
                            .error(getBaseActivity(), getString(R.string.empty_city_name), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @Override
    public void onClickCity(List<String> cityList) {
        String cities = "";
        for (String item : cityList){
           cities += item + ", ";
        }
        cities = cities.substring(0, cities.length() - 2);
        txtCityName.setText(cities);
    }

    private void initCountryList() {
        cityList = Arrays.asList(getResources().getStringArray(R.array.cities));
    }
}
package com.geekbrains.weather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;


public class InfoCityFragment extends BaseFragment implements RecyclerViewAdapter.OnClickCityListener{

    private CallBackICF mBackICF;
    private EditText txtCityName;
    private TextInputLayout txtInput;
    private Switch swHumidity, swWindSpeed, swPressure;
    private FloatingActionButton btnConfirm;
    private RecyclerView mRecyclerView;
    private List<String> cityList;
    private Pattern mPattern;


    public interface CallBackICF{
        void fab_confirm_onclick(String cityName, boolean humidity, boolean windSpeed, boolean pressure);
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
    protected void initLayout(final View view, Bundle savedInstanceState) {
        initCountryList();
        mPattern = Pattern.compile("[-a-zA-Z,\\s\\.]+");
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), this, cityList);
        mRecyclerView.setAdapter(adapter);

        txtInput = view.findViewById(R.id.input_text);
        txtCityName = view.findViewById(R.id.city_name);
//        txtCityName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if(i == EditorInfo.IME_ACTION_DONE){
//                    InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//                return false;
//            }
//        });

        swHumidity = view.findViewById(R.id.city_humidity);
        swWindSpeed = view.findViewById(R.id.city_wind_speed);
        swPressure = view.findViewById(R.id.city_pressure);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = txtCityName.getText().toString().trim();
                Matcher m = mPattern.matcher(cityName);
                if(m.matches() && m.group().equals(cityName)){

                    mBackICF.fab_confirm_onclick(cityName, swHumidity.isChecked(), swWindSpeed.isChecked(), swPressure.isChecked());
                    txtInput.setError("");
                }
                else {
                    txtInput.setError("Неправильно заполнено поле город.");
                }
            }
        });
    }

    @Override
    public void onClickCity(List<String> cityList) {
        String cities = "";
        if(cityList.size() > 0) {
            for (String item : cityList) {
                cities += item + ", ";
            }
            cities = cities.substring(0, cities.length() - 2);
        }
        txtCityName.setText(cities);
    }

    private void initCountryList() {
        cityList = Arrays.asList(getResources().getStringArray(R.array.cities));
    }
}
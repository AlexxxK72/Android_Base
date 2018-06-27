package ru.geekbrains.lesson_1;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText txtName;
    private CheckBox checkHumidity, checkWindSpeed, checkPressure;
    private FloatingActionButton fab;

    private final String NAME = "name";
    private final String HUMIDITY = "city_humidity";
    private final String WIND_SPEED = "wind_speed";
    private final String PRESSURE = "pressure";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtName = findViewById(R.id.city_name);
        checkHumidity = findViewById(R.id.city_humidity);
        checkWindSpeed = findViewById(R.id.city_wind_speed);
        checkPressure = findViewById(R.id.city_pressure);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityInfo();
            }
        });
    }

    private void startActivityInfo() {
        String cityName = txtName.getText().toString();
        if(!cityName.isEmpty()){
            Intent intent = new Intent(this, InfoCityActivity.class);
            intent.putExtra(NAME, cityName);
            int humidity = (checkHumidity.isChecked())
                    ? 85
                    : 0;
            intent.putExtra(HUMIDITY, humidity);
            int windSpeed = (checkWindSpeed.isChecked())
                    ? 10
                    : 0;
            intent.putExtra(WIND_SPEED, windSpeed);
            int pressure = (checkPressure.isChecked())
                    ? 745
                    : 0;
            intent.putExtra(PRESSURE, pressure);
            startActivity(intent);
        }
    }
}

package ru.geekbrains.lesson_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoCityActivity extends AppCompatActivity {

    private final String NAME = "name";
    private final String HUMIDITY = "city_humidity";
    private final String WIND_SPEED = "wind_speed";
    private final String PRESSURE = "pressure";

    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_city);
        txtInfo = findViewById(R.id.city_info);
        writeCityInfo(this.getIntent());
    }

    private void writeCityInfo(Intent intent) {
        String cityInfo = intent.getStringExtra(NAME) + "\n";
        int humidity = intent.getIntExtra(HUMIDITY, 0);
        if(humidity != 0) cityInfo += "Влажность: " + humidity + " %\n";
        int wind_speed = intent.getIntExtra(WIND_SPEED, 0);
        if(wind_speed != 0) cityInfo += "Скорость ветра: " + wind_speed + " м/с\n";
        int pressure = intent.getIntExtra(PRESSURE, 0);
        if(pressure != 0) cityInfo += "Давление: " + pressure + " мм.рт.ст.\n";
        txtInfo.setText(cityInfo);
    }
}

package com.geekbrains.weather;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class WeatherActivity extends BaseActivity {

    private static final String TAG = "WeatherActivity";
    private static final String TEXT = "TEXT";
    private TextView textView;
    private FloatingActionButton fab;
    private Boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.weather_main);
//        String instanceState;
//        if (savedInstanceState == null) {
//            instanceState = "Первый запуск";
//        } else {
//            instanceState = "Повторный запуск";
//        }
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        Toasty.success(getApplicationContext(), instanceState + " - onCreate()", Toast.LENGTH_SHORT).show();
//
//        textView = findViewById(R.id.tv);
//        fab = findViewById(R.id.fab);
//
//        if (getIntent().getExtras() != null) {
//            String text = getIntent().getExtras().getString(TEXT);
//            textView.setText("Добрый день, " + text);
//        }
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isPressed) {
//                    isPressed = true;
//                    startNewActivity();
//                }
//            }
//        });

//        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isPressed = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}

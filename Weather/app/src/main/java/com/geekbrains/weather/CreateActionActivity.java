package com.geekbrains.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by shkryaba on 24/06/2018.
 */

public class CreateActionActivity extends BaseActivity {

    private static final String TEXT = "TEXT";
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_action_activity);

        editText = findViewById(R.id.et);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra(TEXT, editText.getText().toString().trim());
        startActivity(intent);
        finish();
    }
}

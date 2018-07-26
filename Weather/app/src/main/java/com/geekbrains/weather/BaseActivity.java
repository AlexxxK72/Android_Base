package com.geekbrains.weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class BaseActivity extends AppCompatActivity
        implements
        BaseView.View,
        WeatherFragment.CallBackWF,
        InfoCityFragment.CallBackICF,
        HistoryFragment.CallBackHF,
        BaseFragment.Callback,
        NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private static final int PERMISSION_REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initLayout();
    }

    private void initLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInfoCityFragment();
            }
        });
        Fragment weatFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (weatFragment == null)
            addFragment(new WeatherFragment(), R.id.content_frame);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            addInfoCityFragment();
    }

    private void addFragment(Fragment fragment, int idContainer) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(idContainer, fragment)
                .commit();
    }

    public void addInfoCityFragment() {
        Fragment infoCityFragment = new InfoCityFragment();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            addFragment(infoCityFragment, R.id.content_frame);
        } else {
            addFragment(infoCityFragment, R.id.content_frame_info_city);
        }
    }

    @Override
    public void fab_history_onClick() {
        Fragment wFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (wFragment instanceof WeatherFragment) {
            String cityName = ((WeatherFragment) wFragment).getCityName();
            if (cityName != null) {
                cityName = cityName.split(",")[0];
                HistoryFragment hFragment = HistoryFragment.newInstance(cityName);
                addFragment(hFragment, R.id.content_frame);
            }
        }
    }

    @Override
    public void fab_confirm_onclick(String cityName, boolean isHumidity, boolean isWindSpeed, boolean pressure) {

        // добавить код для userName
        Fragment weatherFragment = WeatherFragment.newInstance(cityName, isHumidity, isWindSpeed, pressure);
        addFragment(weatherFragment, R.id.content_frame);
    }

    @Override
    public void fab_back_onClick() {
        Fragment weatherFragment = new WeatherFragment();
        addFragment(weatherFragment, R.id.content_frame);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_info:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+712345678"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(dialIntent);
                } else {
                    requestForCallPerm();
                }
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestForCallPerm() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+712345678"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            }
        }
        if(requestCode == WeatherFragment.PERMISSION_REQUEST_CODE){
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment instanceof WeatherFragment) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
        } else if (id == R.id.nav_info) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Boolean inNetworkAvailable() {
        return true;
    }

    @Override
    public void initDrawer(String username, Bitmap profileImage) {

    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }
}

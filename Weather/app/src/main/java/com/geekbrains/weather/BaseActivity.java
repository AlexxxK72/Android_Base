package com.geekbrains.weather;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
        if(weatFragment == null)
            addFragment(new WeatherFragment(), R.id.content_frame);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            addInfoCityFragment();
    }

    private void addFragment(Fragment fragment, int idContainer){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(idContainer, fragment)
                .commit();
    }

    public void addInfoCityFragment(){
        Fragment infoCityFragment = new InfoCityFragment();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            addFragment(infoCityFragment, R.id.content_frame);
        }
        else{
            addFragment(infoCityFragment, R.id.content_frame_info_city);
        }
    }

    @Override
    public void fab_history_onClick() {
        Fragment wFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(wFragment instanceof WeatherFragment){
            String cityName = ((WeatherFragment)wFragment).getCityName();
            if(cityName != null){
                cityName = cityName.split(",")[0];
                HistoryFragment hFragment = HistoryFragment.newInstance(cityName);
                addFragment(hFragment, R.id.content_frame);
            }
        }
    }

    @Override
    public void fab_confirm_onclick(String cityName, Boolean isHumidity, Boolean isWindSpeed, Boolean pressure) {

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

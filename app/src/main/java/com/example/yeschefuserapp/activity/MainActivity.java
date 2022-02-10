package com.example.yeschefuserapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.fragment.AccountFragment;
import com.example.yeschefuserapp.fragment.AdvancedFilterFragment;
import com.example.yeschefuserapp.fragment.BookmarksFragment;
import com.example.yeschefuserapp.fragment.HomeFragment;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private String country;
    private final static int REQ_LOCATION=44;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationIsEnabledOrNot();
        grantPermission();
    }

    private void checkLocationIsEnabledOrNot() {
        LocationManager lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled=false;
        boolean networkEnabled=false;

        try{
            gpsEnabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            networkEnabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(this)
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel",null).show();
        }
    }

    private void grantPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION);
        else{
            navigationBar();
        }
    }

    private void navigationBar(){
        BottomNavigationView bottonNavigationView=findViewById(R.id.bottom_nav);
        bottonNavigationView.setOnNavigationItemSelectedListener(this);
        int id= getIntent().getIntExtra("nav_item",0);
        checkId(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                navigationBar();
            else
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        checkId(id);
        return true;
    }

    public void checkId(int id){
        Fragment selectedFragment=null;
        switch (id){
            case R.id.nav_home:
                selectedFragment=new HomeFragment();
                break;
            case R.id.nav_bookmarks:
                selectedFragment=new BookmarksFragment();
                break;
            case R.id.nav_advanced_filter:
                selectedFragment=new AdvancedFilterFragment();
                break;
            case R.id.nav_account:
                selectedFragment=new AccountFragment();
                break;
            default:
                selectedFragment=new HomeFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
    }
}
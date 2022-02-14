package com.example.yeschefuserapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.fragment.AccountFragment;
import com.example.yeschefuserapp.fragment.AdvancedFilterFragment;
import com.example.yeschefuserapp.fragment.BookmarksFragment;
import com.example.yeschefuserapp.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private final static int REQ_LOCATION = 44;
    private BottomNavigationView bottomNavigationView;
    private Fragment homeFragment=new HomeFragment();
    private Fragment bookmarksFragment=new BookmarksFragment();
    private Fragment advancedFilterFragment=new AdvancedFilterFragment();
    private Fragment accountFragment=new AccountFragment();
    private FragmentManager fragmentManager=getSupportFragmentManager();
    private Fragment activeFragment=homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFragment();
        checkLocationIsEnabledOrNot();
        grantPermission();
    }

    private void createFragment() {
        fragmentManager.beginTransaction().add(R.id.fragment_container,homeFragment,"HomeFragment").hide(homeFragment).
                add(R.id.fragment_container,bookmarksFragment, "BookmarksFragment").hide(bookmarksFragment).
                add(R.id.fragment_container,advancedFilterFragment, "AdvancedFilterFragment").hide(advancedFilterFragment).
                add(R.id.fragment_container,accountFragment, "AccountFragment").hide(accountFragment).commit();
    }

    private void checkLocationIsEnabledOrNot() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gpsEnabled && !networkEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("Cancel", null).show();
        }
    }

    private void grantPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION);
        else {
            navigationBar();
        }
    }

    private void navigationBar() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
        int id = getIntent().getIntExtra("nav_item", 0);
        bottomNavigationView.setSelectedItemId(id);
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
        int id = item.getItemId();
        checkId(id);
        return true;
    }

    public void checkId(int id) {
        switch (id) {
            case R.id.nav_bookmarks:
                fragmentManager.beginTransaction().hide(activeFragment).show(bookmarksFragment).commit();
                activeFragment=bookmarksFragment;
                break;
            case R.id.nav_advanced_filter:
                fragmentManager.beginTransaction().hide(activeFragment).show(advancedFilterFragment).commit();
                activeFragment=advancedFilterFragment;
                break;
            case R.id.nav_account:
                fragmentManager.beginTransaction().hide(activeFragment).show(accountFragment).commit();
                activeFragment=accountFragment;
                break;
            default:
                SharedPreferences pref = this.getSharedPreferences(this.getResources().getString(R.string.login_preference), Context.MODE_PRIVATE);
                String email = pref.getString("EMAIL", "wrong");
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL", email);
                homeFragment.setArguments(bundle);
                fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                activeFragment=homeFragment;
        }
    }


}
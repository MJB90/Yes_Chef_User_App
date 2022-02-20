package com.example.yeschefuserapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.FragmentManager;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.fragment.AccountFragment;
import com.example.yeschefuserapp.fragment.AdvancedFilterFragment;
import com.example.yeschefuserapp.fragment.BookmarksFragment;
import com.example.yeschefuserapp.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private final static int REQ_LOCATION = 44;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment bookmarksFragment = new BookmarksFragment();
    private final Fragment advancedFilterFragment = new AdvancedFilterFragment();
    private final Fragment accountFragment = new AccountFragment();
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment activeFragment = homeFragment;
    private Context context;
    private UserContext userContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        userContext = new UserContext(this);
    }

    @Override
    public void onResume() {
        checkLocationIsEnabledOrNot();
        super.onResume();
    }

    private void createFragment() {
        fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, "HomeFragment").hide(homeFragment).
                add(R.id.fragment_container, bookmarksFragment, "BookmarksFragment").hide(bookmarksFragment).
                add(R.id.fragment_container, advancedFilterFragment, "AdvancedFilterFragment").hide(advancedFilterFragment).
                add(R.id.fragment_container, accountFragment, "AccountFragment").hide(accountFragment).commit();
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
                    .setPositiveButton("Enable", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("Cancel",
                    (dialogInterface, i) -> {
                        Toast.makeText(context, "You are Logged out", Toast.LENGTH_SHORT).show();
                        userContext.clearLoginPreferences();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }).show();
        } else {
            grantPermission();
        }
    }

    private void grantPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOCATION);
        else {
            createFragment();
            navigationBar();
        }
    }

    private void navigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
        int id = getIntent().getIntExtra("nav_item", 0);
        bottomNavigationView.setSelectedItemId(id);
        checkId(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
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
        if (id == R.id.nav_bookmarks) {
            fragmentManager.beginTransaction().hide(activeFragment).show(bookmarksFragment).commit();
            activeFragment = bookmarksFragment;
        } else if (id == R.id.nav_advanced_filter) {
            fragmentManager.beginTransaction().hide(activeFragment).show(advancedFilterFragment).commit();
            activeFragment = advancedFilterFragment;
        } else if (id == R.id.nav_account) {
            fragmentManager.beginTransaction().hide(activeFragment).show(accountFragment).commit();
            activeFragment = accountFragment;
        } else {
            fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
            activeFragment = homeFragment;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, ((arg0, arg1) -> {
                    finishAffinity();
                    System.exit(0);
                })).create().show();
    }
}
package com.example.yeschefuserapp;

import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBar();
    }

    private void navigationBar(){
        BottomNavigationView bottonNavigationView=findViewById(R.id.bottom_nav);
        bottonNavigationView.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
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
                return false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
        return true;
    }
}
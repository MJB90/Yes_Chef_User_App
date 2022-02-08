package com.example.yeschefuserapp.activity;

import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.fragment.AccountFragment;
import com.example.yeschefuserapp.fragment.AdvancedFilterFragment;
import com.example.yeschefuserapp.fragment.BookmarksFragment;
import com.example.yeschefuserapp.fragment.HomeFragment;
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
        int id= getIntent().getIntExtra("nav_item",0);
        checkId(id);
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
package com.example.yeschefuserapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.adapter.FilteredCustomListAdapter;
import com.example.yeschefuserapp.listener.RecipeClickListener;
import com.example.yeschefuserapp.model.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class FilterResult extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_filter);
        bottomNavigationView.setOnItemSelectedListener(this);

        List<Recipe> recipes = (List<Recipe>) getIntent().getExtras().getSerializable("recipes");

        TextView searchResult = findViewById(R.id.filter_result_size);
        searchResult.setText(String.format("%s recipes found", recipes.size()));

        RecipeClickListener onClickListener = new RecipeClickListener(this);
        FilteredCustomListAdapter adapter = new FilteredCustomListAdapter(R.layout.filter_result_row,
                recipes,
                onClickListener);


        RecyclerView recyclerView = findViewById(R.id.filtered_result_recycler_view);
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(this, MainActivity.class);

        if (id == R.id.nav_home) {
            intent.putExtra("nav_item", R.id.nav_home);
        } else if (id == R.id.nav_bookmarks) {
            intent.putExtra("nav_item", R.id.nav_bookmarks);
        } else if (id == R.id.nav_advanced_filter) {
            intent.putExtra("nav_item", R.id.nav_advanced_filter);
        } else if (id == R.id.nav_account) {
            intent.putExtra("nav_item", R.id.nav_account);
        } else {
            return false;
        }
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("nav_item", R.id.nav_advanced_filter);
        startActivity(intent);
    }
}
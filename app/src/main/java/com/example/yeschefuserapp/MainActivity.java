package com.example.yeschefuserapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private final List<Recipe> recipes = new ArrayList<>();
    BottomNavigationView bottonNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainCustomAdapter adapter = new MainCustomAdapter(this, this.recipes, recipe -> {
            //Click on a recipe
            Intent intent = new Intent(this,ViewRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            startActivity(intent);
        });

        fetchData(adapter);
        initView(adapter);

        navigationBar();
    }

    private void fetchData(MainCustomAdapter adapter) {
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://10.0.2.2:8090/api/user/all_recipes",
                null,
                response -> {
                    Gson gson = new Gson();
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    // The recipes should be the same reference
                    recipes.addAll(Arrays.asList(tmpArray.clone()));

                    // Need to notify the adapter after updating the recipes
                    // ref: https://stackoverflow.com/a/48959184
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show()
        );
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }
    private void initView(MainCustomAdapter adapter){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
    private void navigationBar(){
        bottonNavigationView=findViewById(R.id.bottom_nav);
        bottonNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.nav_home:
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_LONG).show();
                return true;
            case R.id.nav_bookmarks:
                Toast.makeText(MainActivity.this, "Bookmarks", Toast.LENGTH_LONG).show();
                return true;
            case R.id.nav_advanced_filter:
                Toast.makeText(MainActivity.this, "Advanced Filter", Toast.LENGTH_LONG).show();
                return true;
            case R.id.nav_account:
                Toast.makeText(MainActivity.this, "Account", Toast.LENGTH_LONG).show();
                return true;
        }

        return false;
    }
}
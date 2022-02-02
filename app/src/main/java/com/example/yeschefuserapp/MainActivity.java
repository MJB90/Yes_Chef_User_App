package com.example.yeschefuserapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final List<Recipe> recipes = new ArrayList<>();

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

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

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
}
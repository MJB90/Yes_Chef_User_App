package com.example.yeschefuserapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment  {

    private final List<Recipe> recipes = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        MainCustomAdapter adapter = new MainCustomAdapter(view.getContext(), this.recipes, recipe -> {
            //Click on a recipe
            Intent intent = new Intent(view.getContext(),ViewRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            startActivity(intent);
        });

        fetchData(adapter, view);
        initView(adapter, view);

        return view;
    }

    private void fetchData(MainCustomAdapter adapter,View view) {
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
                error -> Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show()
        );
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
    }
    private void initView(MainCustomAdapter adapter, View view){
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }



}
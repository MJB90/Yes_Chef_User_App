package com.example.yeschefuserapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.ViewRecipeActivity;
import com.example.yeschefuserapp.adapter.MainCustomAdapter;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private final List<Recipe> recipes = new ArrayList<>();
    private final List<Recipe> recommendationRecipes = new ArrayList<>();
    private final List<Recipe> likeRecipes = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        int rowItemResourceId = R.layout.main_recycler_row_item;
        Context viewContext = view.getContext();
        MainCustomAdapter adapter = new MainCustomAdapter(rowItemResourceId, viewContext, this.recipes, recipe -> {
            //Click on a recipe
            Intent intent = new Intent(viewContext, ViewRecipeActivity.class);
            intent.putExtra("recipeId", recipe);
            startActivity(intent);
        });

        MainCustomAdapter recommendationAdapter = new MainCustomAdapter(rowItemResourceId, viewContext, this.recommendationRecipes, recipe -> {
            //Click on a recipe
            Intent intent = new Intent(viewContext, ViewRecipeActivity.class);
            intent.putExtra("recipeId", recipe);
            startActivity(intent);
        });

        MainCustomAdapter likeAdapter = new MainCustomAdapter(rowItemResourceId, viewContext, this.likeRecipes, recipe -> {
            //Click on a recipe
            Intent intent = new Intent(viewContext, ViewRecipeActivity.class);
            intent.putExtra("recipeId", recipe);
            startActivity(intent);
        });

        fetchData(adapter, view, "http://10.0.2.2:8090/api/user/all_recipes", this.recipes);
        fetchData(recommendationAdapter, view, "http://10.0.2.2:8090/api/user/all_recipes", this.recommendationRecipes);
        fetchData(likeAdapter, view, "http://10.0.2.2:8090/api/user/all_recipes", this.likeRecipes);
        initView(adapter, view, view.findViewById(R.id.recycler_view));
        initView(recommendationAdapter, view, view.findViewById(R.id.recommendation_view));
        initView(likeAdapter, view, view.findViewById(R.id.like_view));

        return view;
    }

    private void fetchData(MainCustomAdapter adapter, View view, String url, List<Recipe> list) {
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Gson gson = new Gson();
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    // The recipes should be the same reference
                    list.addAll(Arrays.asList(tmpArray.clone()));

                    // Need to notify the adapter after updating the recipes
                    // ref: https://stackoverflow.com/a/48959184
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Log.e("HomeFragment", "FetchData failed", error);
                    Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
        );
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
    }

    private void initView(MainCustomAdapter adapter, View view, RecyclerView recyclerView) {
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
}
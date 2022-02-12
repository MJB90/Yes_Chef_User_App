package com.example.yeschefuserapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.FilterResult;
import com.example.yeschefuserapp.adapter.MainVerticalCustomListAdapter;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.MySingleton;
import com.example.yeschefuserapp.utility.RecommendedRecipes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private MainVerticalCustomListAdapter mainVerticalCustomListAdapter;
    private String country;
    private RecommendedRecipes recommendedRecipes;
    private UserContext userContext;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        int rowItemResourceId = R.layout.main_recycler_column_item;
        Context viewContext = view.getContext();
        this.userContext = new UserContext(viewContext);

        //creating a list of recipe category list
        fetchRecommendedRecipes(viewContext, view, view.findViewById(R.id.recycler_view));


        SearchView searchView = view.findViewById(R.id.search_bar);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Toast.makeText(view.getContext(), "Search", Toast.LENGTH_SHORT).show();

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    List<Recipe> recipeSearch = new ArrayList<>();
                    JsonArrayRequest objectRequest = new JsonArrayRequest(
                            Request.Method.GET,
                            "http://10.0.2.2:8090/api/user/search/" + s,
                            null,
                            response -> {
                                Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                                recipeSearch.addAll(Arrays.asList(tmpArray.clone()));
                                Intent intent = new Intent(view.getContext(), FilterResult.class);
                                intent.putExtra("recipes", (Serializable) recipeSearch);
                                startActivity(intent);
                            },
                            error -> Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show()
                    );
                    MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return true;
                }
            });
        }

        return view;
    }


    private void initView(MainVerticalCustomListAdapter adapter, View view, RecyclerView recyclerView) {
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }

    private void fetchRecommendedRecipes(Context context, View view, RecyclerView recyclerView) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH");//Get the current time
        LocalTime now = LocalTime.now();
        String time = dtf.format(now);
        //Get the user location
        try {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {

                        Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
                        List<Address> address = null;
                        try {
                            address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            country = address.get(0).getCountryName();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        //fetch recipe
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                String.format("http://10.0.2.2:8090/api/user/recommended_recipes/%s/singapore/%s/20", this.userContext.getEmail(), time),
                null,
                response -> {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    recommendedRecipes = gson.fromJson(String.valueOf(response), RecommendedRecipes.class);

                    mainVerticalCustomListAdapter = new MainVerticalCustomListAdapter(context, recommendedRecipes);
                    if (recyclerView != null) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mainVerticalCustomListAdapter);
                    }
                },
                error -> {
                    Log.e("HomeFragment", "FetchData failed", error);
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
        );
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(context).addToRequestQueue(objectRequest);
    }
}
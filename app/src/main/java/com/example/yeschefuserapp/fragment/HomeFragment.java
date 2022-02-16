package com.example.yeschefuserapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.FilterResult;
import com.example.yeschefuserapp.activity.LoginActivity;
import com.example.yeschefuserapp.adapter.MainVerticalCustomListAdapter;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.listener.LogoutListener;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.MySingleton;
import com.example.yeschefuserapp.utility.RecommendedRecipes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class HomeFragment extends Fragment {

    private MainVerticalCustomListAdapter mainVerticalCustomListAdapter;
    private RecyclerView recyclerView;
    private String country;
    private RecommendedRecipes recommendedRecipes;
    private UserContext userContext;
    private String email;
    private String ACCESS_TOKEN;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Context viewContext = view.getContext();
        this.userContext = new UserContext(viewContext);
        recyclerView=view.findViewById(R.id.recycler_view);
        ACCESS_TOKEN=userContext.getToken();

        if (getArguments() != null) email = getArguments().getString("email");

        //creating a list of recipe category list
        getUserLocationAndTime(viewContext);


        SearchView searchView = view.findViewById(R.id.search_bar);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Toast.makeText(viewContext, "Search", Toast.LENGTH_SHORT).show();

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    List<Recipe> recipeSearch = new ArrayList<>();
                    JsonArrayRequest objectRequest = new JsonArrayRequest(
                            Request.Method.GET,
                            getString(R.string.domain_name)+"api/user/search/" + s,
                            null,
                            response -> {
                                Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                                recipeSearch.addAll(Arrays.asList(tmpArray.clone()));
                                Intent intent = new Intent(view.getContext(), FilterResult.class);
                                intent.putExtra("recipes", (Serializable) recipeSearch);
                                startActivity(intent);
                            },
                            error -> {
                                Toast.makeText(view.getContext(), "You are Logged out", Toast.LENGTH_LONG).show();
                                userContext.clearLoginPreferences();
                                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                                view.getContext().startActivity(intent);
                            }
                    ){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headerMap = new HashMap<String, String>();
                            headerMap.put("Content-Type", "application/json");
                            headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                            return headerMap;
                        }
                    };
                    objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
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

    private void getUserLocationAndTime(Context context) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH");
        LocalTime now = LocalTime.now(); //Get the current time
        String time = dtf.format(now);
        //Get the user location
        try {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            if (fusedLocationProviderClient.getLastLocation()!=null) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(task -> {
                    Location location = task;
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(context.getApplicationContext(), Locale.getDefault());
                        try {
                            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            country = address.get(0).getCountryName();
                            fetchDataOrCacheData(context, country, time);
                        } catch (IOException e) {
                            e.printStackTrace();
                            fetchDataOrCacheData(context, "Singapore", time);
                        }
                    }

                });
            }
//            else{
//                fetchDataOrCacheData(context, "Singapore", time);
//            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void fetchDataOrCacheData(Context context, String country, String hour){
        SharedPreferences pref= context.getSharedPreferences("recommended_recipes_cache",Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder().serializeNulls().create();
        //caching
        if (pref.contains("RecommendedRecipes") &&
                pref.contains("email") && email.equals(pref.getString("email","")) &&
                pref.contains("Country") && country.equals(pref.getString("Country","")) &&
                pref.contains("Hour") && hour.equals(pref.getString("Hour",""))){

            String json = pref.getString("RecommendedRecipes", "");
            recommendedRecipes = gson.fromJson(json, RecommendedRecipes.class);
            createVerticalRecycleView(context, recommendedRecipes);
        }
        else{
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    String.format(getString(R.string.domain_name)+"api/user/recommended_recipes/%s/%s/%s/20", this.userContext.getEmail(), country, hour),
                    null,
                    response -> {
                        recommendedRecipes = gson.fromJson(String.valueOf(response), RecommendedRecipes.class);
                        createVerticalRecycleView(context, recommendedRecipes);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.putString("Email",email);
                        editor.putString("Country",country);
                        editor.putString("Hour",hour);
                        editor.putString("RecommendedRecipes",String.valueOf(response));
                        editor.apply();
                    },
                    error -> {
                        Toast.makeText(context, "You are Logged out", Toast.LENGTH_LONG).show();
                        userContext.clearLoginPreferences();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headerMap = new HashMap<String, String>();
                    headerMap.put("Content-Type", "application/json");
                    headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                    return headerMap;
                }
            };
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
            MySingleton.getInstance(context).addToRequestQueue(objectRequest);
        }
    }

    public void createVerticalRecycleView(Context context, RecommendedRecipes object){
        mainVerticalCustomListAdapter = new MainVerticalCustomListAdapter(context, object);
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mainVerticalCustomListAdapter);
        }
    }
}
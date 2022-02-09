package com.example.yeschefuserapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.FilterResult;
import com.example.yeschefuserapp.adapter.MainVerticalCustomListAdapter;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.model.RecipeCategoryList;
import com.example.yeschefuserapp.utility.MySingleton;
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
    private List<RecipeCategoryList> recipeCategoryLists = new ArrayList<>();
    private String country;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        int rowItemResourceId = R.layout.main_recycler_column_item;
        Context viewContext = view.getContext();

        //creating a list of recipe category list
        createRecipeCategoryList(viewContext);

        mainVerticalCustomListAdapter=new MainVerticalCustomListAdapter(viewContext,recipeCategoryLists);
        initView(mainVerticalCustomListAdapter, view, view.findViewById(R.id.recycler_view));

        SearchView searchView=view.findViewById(R.id.search_bar);
        if (searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Toast.makeText(view.getContext(),"Search",Toast.LENGTH_SHORT).show();

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    List<Recipe> recipeSearch = new ArrayList<>();
                    JsonArrayRequest objectRequest = new JsonArrayRequest(
                            Request.Method.GET,
                            "http://10.0.2.2:8090/api/user/search/"+s,
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

    private void createRecipeCategoryList(Context context) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");//To get the current time
        LocalTime now = LocalTime.now();
        String time=dtf.format(now).toString();



        try{
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>(){
                @Override
                public void onComplete(@NonNull Task<Location> task){
                    Location location=task.getResult();
                    if (location!=null){

                        Geocoder geocoder=new Geocoder(context.getApplicationContext(), Locale.getDefault());
                        List<Address> address= null;
                        try {
                            address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                            country = address.get(0).getCountryName();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
        }
        catch(SecurityException e){
            e.printStackTrace();
        }

        recipeCategoryLists.add(new RecipeCategoryList("Malaysian Food", new ArrayList<Recipe>(), "http://10.0.2.2:8090/api/user/all_recipes"));
        recipeCategoryLists.add(new RecipeCategoryList("Singapore Food", new ArrayList<Recipe>(), "http://10.0.2.2:8090/api/user/all_recipes"));
        recipeCategoryLists.add(new RecipeCategoryList("Thailand Food", new ArrayList<Recipe>(), "http://10.0.2.2:8090/api/user/all_recipes"));
    }

    private void getLocation() {

    }
}
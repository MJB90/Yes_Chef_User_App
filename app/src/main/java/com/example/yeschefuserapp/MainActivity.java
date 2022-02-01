package com.example.yeschefuserapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Recipe> recipes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchData();

        MainCustomAdapter adapter=new MainCustomAdapter(this, recipes, recipe -> {
            //Click on a recipe
        });

        RecyclerView recyclerView=findViewById(R.id.recycler_view);

        if (recyclerView!=null)
        {
            LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

    }

    private void fetchData() {
        String URL="http://localhost:8080/api/user/all_recipes";
        //Fetching JSON object
        JsonObjectRequest objectRequest=new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    Gson json = new GsonBuilder().serializeNulls().create();
                    Recipe[] recipesArray=json.fromJson(String.valueOf(response),Recipe[].class);
                    recipes= Arrays.asList(recipesArray);
                },
                error -> Toast.makeText(this,error.toString(),Toast.LENGTH_SHORT).show()
        );
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }
}
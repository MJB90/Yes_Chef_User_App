package com.example.yeschefuserapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Recipe> recipes=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainCustomAdapter adapter=new MainCustomAdapter(this, recipes, recipe -> {
            //Click on a recipe
        });

        fetchData(adapter);

        RecyclerView recyclerView=findViewById(R.id.recycler_view);

        if (recyclerView!=null)
        {
            LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

    }

    private void fetchData(MainCustomAdapter adapter) {
        //Fetching JSON object
        JsonArrayRequest objectRequest=new JsonArrayRequest(
                Request.Method.GET,
                "http://10.0.2.2:8090/api/user/all_recipes",
                null,
                response -> {
                    Gson gson = new Gson();
                    Recipe[] recipesArray = gson.fromJson(response.toString(), Recipe[].class);
                    recipes = Arrays.asList(recipesArray.clone());
                    adapter.setRecipes(this.recipes);
                    Toast.makeText(MainActivity.this, "HiHi", Toast.LENGTH_LONG).show();
                    // update the recipes in the adapter
                },
                error -> Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show()
        );
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }
}
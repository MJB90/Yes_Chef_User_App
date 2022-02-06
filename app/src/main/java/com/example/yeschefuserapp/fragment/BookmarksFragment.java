package com.example.yeschefuserapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class BookmarksFragment extends Fragment {
    private final List<Recipe> bookmarkList = new ArrayList<>();

    public BookmarksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        MainCustomAdapter adapter = new MainCustomAdapter(R.layout.bookmark_item, view.getContext(), bookmarkList, recipe -> {
            //Click on a recipe
            Intent intent = new Intent(view.getContext(), ViewRecipeActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            startActivity(intent);
        });

        // TODO: Change to real email
        fetchData(adapter, view, "xxx@gmail.com");
        initView(adapter, view, view.findViewById(R.id.bookmark_recycler_view));
        return view;
    }

    private void fetchData(MainCustomAdapter adapter, View view, String email) {
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("http://10.0.2.2:8090/api/user/bookmarks/%s", email),
                null,
                response -> {
                    Gson gson = new Gson();
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    // The recipes should be the same reference
                    bookmarkList.addAll(Arrays.asList(tmpArray.clone()));

                    // Need to notify the adapter after updating the recipes
                    // ref: https://stackoverflow.com/a/48959184
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Log.e("BookmarksFragment", "FetchData failed", error);
                    Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
        );
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
    }

    private void initView(MainCustomAdapter adapter, View view, RecyclerView recyclerView) {
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
}
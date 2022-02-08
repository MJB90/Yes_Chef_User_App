package com.example.yeschefuserapp.fragment;

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
import com.example.yeschefuserapp.adapter.MainHorizontalCustomAdapter;
import com.example.yeschefuserapp.listener.RecipeClickListener;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookmarksFragment extends Fragment {
    MainHorizontalCustomAdapter adapter;
    private final List<Recipe> bookmarkList = new ArrayList<>();

    public BookmarksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);


        RecipeClickListener onClickListener = new RecipeClickListener(view.getContext());
        adapter = new MainHorizontalCustomAdapter(R.layout.bookmark_item, view.getContext(), bookmarkList, onClickListener);

        // TODO: Change to real email
        fetchData("xxx@gmail.com");
        initView(view.findViewById(R.id.bookmark_recycler_view));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: Change to real email
        fetchData("xxx@gmail.com");
    }


    private void fetchData(String email) {
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("http://10.0.2.2:8090/api/user/bookmarks/%s", email),
                null,
                response -> {
                    Gson gson = new Gson();
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    // The recipes should be the same reference
                    bookmarkList.clear();
                    bookmarkList.addAll(Arrays.asList(tmpArray.clone()));

                    // Need to notify the adapter after updating the recipes
                    // ref: https://stackoverflow.com/a/48959184
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Log.e("BookmarksFragment", "FetchData failed", error);
                    Toast.makeText(this.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
        );
        MySingleton.getInstance(this.getContext()).addToRequestQueue(objectRequest);
    }

    private void initView(RecyclerView recyclerView) {
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
}
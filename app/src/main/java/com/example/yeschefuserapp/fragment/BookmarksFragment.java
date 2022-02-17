package com.example.yeschefuserapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.LoginActivity;
import com.example.yeschefuserapp.adapter.BookmarkAdapter;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.listener.RecipeClickListener;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarksFragment extends Fragment {
    BookmarkAdapter adapter;
    private final List<Recipe> bookmarkList = new ArrayList<>();
    private UserContext userContext;
    private String ACCESS_TOKEN;

    public BookmarksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        this.userContext = new UserContext(view.getContext());
        ACCESS_TOKEN = userContext.getToken();
        RecipeClickListener onClickListener = new RecipeClickListener(view.getContext());
        adapter = new BookmarkAdapter(R.layout.bookmark_item, bookmarkList, onClickListener);

        fetchData(this.userContext.getEmail());
        initView(view.findViewById(R.id.bookmark_recycler_view));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData(this.userContext.getEmail());
    }


    private void fetchData(String email) {
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format(getString(R.string.domain_name) + "api/user/bookmarks/%s", email),
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
                    Toast.makeText(this.getContext(), "You are Logged out", Toast.LENGTH_LONG).show();
                    userContext.clearLoginPreferences();
                    Intent intent = new Intent(this.getContext(), LoginActivity.class);
                    this.getContext().startActivity(intent);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
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
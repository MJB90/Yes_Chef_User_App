package com.example.yeschefuserapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.FilterResult;
import com.example.yeschefuserapp.activity.LoginActivity;
import com.example.yeschefuserapp.adapter.MyExpandableListAdapter;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.AdvancedFilterTags;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedFilterFragment extends Fragment implements View.OnClickListener {

    private List<String> categoryList;
    private List<String> childList;
    private Map<String, List<String>> categoriesCollection;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private AdvancedFilterTags advancedFilterTags;

    private UserContext userContext;
    private String ACCESS_TOKEN;

    public AdvancedFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advanced_filter, container, false);
        this.userContext = new UserContext(view.getContext());
        ACCESS_TOKEN=userContext.getToken();
        createCategoriesList();
        createProperties(view);


        Button button = view.findViewById(R.id.filter);
        if (button != null) {
            button.setOnClickListener(this);
        }

        return view;
    }

    public void createCategoriesList() {
        categoryList = new ArrayList<>();
        categoryList.add("Tags");
        categoryList.add("Calories");
        categoryList.add("Difficulty");
        categoryList.add("Preparation Time");
        categoryList.add("Course Type");
        categoryList.add("Cuisine Type");
    }

    public void createProperties(View view) {
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.domain_name)+"api/user/filter_properties",
                null,
                response -> {
                    Gson gson = new Gson();
                    AdvancedFilterTags filterTags = gson.fromJson(response.toString(), AdvancedFilterTags.class);
                    fetchCategory(filterTags);

                    expandableListView = view.findViewById(R.id.expanded_filter);
                    expandableListAdapter = new MyExpandableListAdapter(view.getContext(), categoryList, categoriesCollection, advancedFilerTags ->
                        //Update the Advanced Filter Tags
                        this.advancedFilterTags = advancedFilerTags
                    );
                    expandableListView.setAdapter(expandableListAdapter);

                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        int lastExpandedPosition = -1;

                        @Override
                        public void onGroupExpand(int i) {
                            if (lastExpandedPosition != -1 && i != lastExpandedPosition) {
                                expandableListView.collapseGroup(lastExpandedPosition);
                            }
                            lastExpandedPosition = i;
                        }
                    });
                },
                error -> {
                    Toast.makeText(view.getContext(), "You are Logged out", Toast.LENGTH_LONG).show();
                    userContext.clearLoginPreferences();
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    view.getContext().startActivity(intent);
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
    }

    public void fetchCategory(AdvancedFilterTags filterTags) {
        categoriesCollection = new HashMap<>();
        for (String category : categoryList) {
            switch (category) {
                case "Tags":
                    childList = filterTags.getTags();
                    break;
                case "Calories":
                    Double maxCalorie = filterTags.getMaxCalories();
                    childList = new ArrayList<>();
                    childList.add("1 to 100 kCals");
                    int i = 100;
                    for (; i <= maxCalorie; i *= 2) {
                        childList.add(i + " to " + i * 2 + "kCals");
                    }
                    childList.add(i + " to " + i * 2 + "kCals");
                    break;
                case "Difficulty":
                    childList = filterTags.getDifficulty();
                    break;
                case "Preparation Time":
                    childList = new ArrayList<>();
                    childList.add("1 to 10 minutes");
                    childList.add("10 to 30 minutes");
                    childList.add("0.5 to 1 hour");
                    childList.add("1 to 5 hours");
                    childList.add("5 to 10 hours");
                    break;
                case "Course Type":
                    childList = filterTags.getCourseType();
                    break;
                case "Cuisine Type":
                    childList = filterTags.getCuisineType();
                    break;
            }

            categoriesCollection.put(category, childList);
        }
    }

    @Override
    public void onClick(View view) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(advancedFilterTags);

        List<Recipe> recipes = new ArrayList<>();
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.POST,
                this.getString(R.string.domain_name)+"api/user/advanced_filter",
                null,
                response -> {
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    recipes.addAll(Arrays.asList(tmpArray.clone()));
                    Intent intent = new Intent(view.getContext(), FilterResult.class);
                    intent.putExtra("recipes", (Serializable) recipes);
                    startActivity(intent);
                },
                error-> {
                    Toast.makeText(view.getContext(), "You are Logged out", Toast.LENGTH_LONG).show();
                    userContext.clearLoginPreferences();
                    Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    view.getContext().startActivity(intent);
                }
        ){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return json == null ? null : json.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);

    }
}
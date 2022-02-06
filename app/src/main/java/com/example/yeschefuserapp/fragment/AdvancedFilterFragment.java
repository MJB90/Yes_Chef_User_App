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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.FilterResult;
import com.example.yeschefuserapp.adapter.MyExpandableListAdapter;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.AdvancedFilterTags;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

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

    public AdvancedFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advanced_filter, container, false);

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
        categoryList.add("Number of Servings");
        categoryList.add("Course Type");
        categoryList.add("Cuisine Type");
    }

    public void createProperties(View view) {
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://10.0.2.2:8090/api/user/filter_properties",
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
                error -> Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show()


        );
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
    }

    public void fetchCategory(AdvancedFilterTags filterTags) {
        categoriesCollection = new HashMap<>();
        for (String category : categoryList) {
            if (category.equals("Tags")) childList = filterTags.getTags();
            else if (category.equals("Calories")) {
                Double maxCalorie = Math.ceil(filterTags.getMaxCalories() / 100) * 100;
                childList = new ArrayList<>();
                if (maxCalorie % 2 == 0) {
                    for (int i = 200; i <= maxCalorie; i += 200) {
                        childList.add("Under " + i + "kcals");
                    }
                } else {
                    for (int i = 100; i <= maxCalorie; i += 200) {
                        childList.add("Under " + i + "kcals");
                    }
                }
            } else if (category.equals("Difficulty")) childList = filterTags.getDifficulty();
            else if (category.equals("Preparation Time")) {
                childList = new ArrayList<>();
                childList.add("Under 10 minutes");
                childList.add("Under 30 minutes");
                childList.add("Under 1 hour");
                childList.add("Under 4 hour");
                childList.add("Under 9 hour");
            } else if (category.equals("Number of Servings")) {
                childList = new ArrayList<>();
                for (int i = 1; i <= filterTags.getMaxNoServings(); i++) {
                    childList.add(String.valueOf(i));
                }
            } else if (category.equals("Course Type")) childList = filterTags.getCourseType();
            else if (category.equals("Cuisine Type")) childList = filterTags.getCuisineType();

            categoriesCollection.put(category, childList);
        }
    }

    @Override
    public void onClick(View view) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(advancedFilterTags);
        Toast.makeText(view.getContext(), json, Toast.LENGTH_SHORT).show();
        JSONObject object = gson.fromJson(json, JSONObject.class);
        List<Recipe> recipes = new ArrayList<>();
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8090/api/user/filter_properties",
                object,
                response -> {
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    recipes.addAll(Arrays.asList(tmpArray.clone()));
                    Intent intent = new Intent(view.getContext(), FilterResult.class);
                    //intent.putExtra("Recipes", recipes);
                    startActivity(intent);
                },
                error -> Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show()
        );
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);

    }
}
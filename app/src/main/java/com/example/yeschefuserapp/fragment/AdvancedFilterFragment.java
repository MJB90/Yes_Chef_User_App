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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.FilterResult;
import com.example.yeschefuserapp.adapter.MyExpandableListAdapter;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.AdvancedFilterTags;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
    private JSONArray object=null;

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
                Double maxCalorie = filterTags.getMaxCalories();
                childList = new ArrayList<>();
                    int i=100;
                    for (; i <= maxCalorie; i*=2) {
                        childList.add("Under " + i + "kcals");
                    }
                    childList.add("Under " + i + "kcals");
            }
            else if (category.equals("Difficulty")) childList = filterTags.getDifficulty();
            else if (category.equals("Preparation Time")) {
                childList = new ArrayList<>();
                childList.add("Under 10 minutes");
                childList.add("Under 30 minutes");
                childList.add("Under 1 hour");
                childList.add("Under 4 hour");
                childList.add("Under 9 hour");
            } else if (category.equals("Course Type")) childList = filterTags.getCourseType();
            else if (category.equals("Cuisine Type")) childList = filterTags.getCuisineType();

            categoriesCollection.put(category, childList);
        }
    }

    @Override
    public void onClick(View view) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(advancedFilterTags);
        /*try {
            JSONObject jObject = new JSONObject(json);
            object=new JSONArray();
            object.put(jObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        List<Recipe> recipes = new ArrayList<>();
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.POST,
                "http://10.0.2.2:8090/api/user/advanced_filter",
                null,
                response -> {
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    recipes.addAll(Arrays.asList(tmpArray.clone()));
                    Intent intent = new Intent(view.getContext(), FilterResult.class);
                    intent.putExtra("recipes", (Serializable) recipes);
                    startActivity(intent);
                },
                error -> Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show()
        ){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                    return null;
                }
            }

            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization",Token);
                return headers;
            }*/
        };;
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);

    }
}
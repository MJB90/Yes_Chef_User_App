package com.example.yeschefuserapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.FilterResult;
import com.example.yeschefuserapp.adapter.CardAdapter;
import com.example.yeschefuserapp.adapter.MyExpandableListAdapter;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.AdvancedFilterTags;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedFilterFragment extends Fragment implements View.OnClickListener{

    private List<String> categoryList;
    private List<String> childList;
    private Map<String,List<String>> categoriesCollection;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private AdvancedFilterTags advancedFilterTags;
    private ArrayList<Recipe> recipes;

    public AdvancedFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advanced_filter, container, false);

        createCategoriesList();
        createCollection();

        expandableListView=view.findViewById(R.id.expanded_filter);
        expandableListAdapter=new MyExpandableListAdapter(view.getContext(),categoryList,categoriesCollection,advancedFilerTags->
            this.advancedFilterTags=advancedFilerTags
        );
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });

        Button button=view.findViewById(R.id.filter);
        if(button!=null){
            button.setOnClickListener(this);
        }

        return view;
    }

    public void createCategoriesList(){
        categoryList=new ArrayList<>();
        categoryList.add("Tags");
        categoryList.add("Calories");
        categoryList.add("Difficulty");
        categoryList.add("Preparation Time");
        categoryList.add("Number of Servings");
        categoryList.add("Course Type");
        categoryList.add("Cuisine Type");
    }
    public void createCollection(){
        String[] tags={"Fat Free", "Healthy", "Heart Healthy", "High Calcium", "High Carb", "High Fiber",
                "High Iron", "High Potassium", "High Protein", "High Vitamin C", "High Vitamin D",
                "High in Omega-3s", "Low Calorie", "Low Carb", "Low Fat", "Low Saturated Fat", "Low Sodium",
                "Low Sugar", "No Carb", "Source of Omega-3s", "Sugar Free"};
        String[] calories={"Under 200kcals","Under 400kcals","Under 600kcals","Under 800kcals", "Under 1000kcals"};
        String[] difficulty={"Easy", "Quick", "Quick and Easy"};
        String[] preparationTime={"Under 10 minutes","Under 30 minutes","Under 1 hour","Under 5 hour","Under 8 hour"};
        String[] noServings={"1","2","3","4","5","6"};
        String[] courseType={"Appetizers", "Beverages", "Breads", "Breakfast and Brunch", "Cocktails",
                "Desserts", "Main Dishes", "Salads", "Side Dishes", "Soups"};
        String[] cuisineType={"Asian", "Barbecue", "Indian", "Italian", "Japanese", "Korean", "Mexican",
                "Southwestern", "Thai", "Turkish"};
        categoriesCollection=new HashMap<>();
        for(String category:categoryList)
        {
            if (category.equals("Tags")) loadChild(tags);
            else if (category.equals("Calories")) loadChild(calories);
            else if (category.equals("Difficulty")) loadChild(difficulty);
            else if (category.equals("Preparation Time")) loadChild(preparationTime);
            else if (category.equals("Number of Servings")) loadChild(noServings);
            else if (category.equals("Course Type")) loadChild(courseType);
            else if (category.equals("Cuisine Type")) loadChild(cuisineType);

            categoriesCollection.put(category,childList);
        }
    }
    public void loadChild(String[] category){
        childList=new ArrayList<>();
        for (String childCategory : category)
        {
            childList.add(childCategory);
        }
    }

    @Override
    public void onClick(View view) {
        Gson gson = new Gson();
        String json=gson.toJson(advancedFilterTags);
        Toast.makeText(view.getContext(),json,Toast.LENGTH_SHORT).show();
        /*JSONObject object=gson.fromJson(json,JSONObject.class);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8090/api/user/filter_properties",
                object,
                response -> {
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    recipes.addAll(Arrays.asList(tmpArray.clone()));
                },
                error -> Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show()
        );
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
        Intent intent=new Intent(view.getContext(), FilterResult.class);
        intent.putExtra("Recipes",recipes);
        startActivity(intent);*/
    }
}
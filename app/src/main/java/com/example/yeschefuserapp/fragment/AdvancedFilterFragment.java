package com.example.yeschefuserapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.adapter.MyExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedFilterFragment extends Fragment implements AdapterView.OnItemClickListener{

    private List<String> categoryList;
    private List<String> childList;
    private Map<String,List<String>> categoriesCollection;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

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
        expandableListAdapter=new MyExpandableListAdapter(view.getContext(),categoryList,categoriesCollection);
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
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                /*String selected = expandableListAdapter.getChild(i,i1).toString();
                Toast.makeText(view.getContext(), "Selected: " + selected, Toast.LENGTH_SHORT).show();*/
                return true;
            }
        });

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
        String[] calories={"Under 200kcals","201-400kcals","401-600kcals","601-800kcals", "Above 800kcals"};
        String[] difficulty={"Easy", "Quick", "Quick and Easy", "test diff"};
        String[] preparationTime={"Under 10 minutes","10 to 30 minutes","31 to 60 minutes","More than 1 hour"};
        String[] noServings={"1","2","3","4","5","6"};
        String[] courseType={"Appetizers", "Beverages", "Breads", "Breakfast and Brunch", "Cocktails",
                "Desserts", "Main Dishes", "Salads", "Side Dishes", "Soups", "coursetype test"};
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
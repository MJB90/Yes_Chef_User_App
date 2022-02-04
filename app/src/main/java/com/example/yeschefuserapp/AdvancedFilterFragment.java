package com.example.yeschefuserapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdvancedFilterFragment extends Fragment {

    private List<String> categoryList;
    private List<String> childList;
    private Map<String,List<String>> categories;
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

        return view;
    }

    public void createCategoriesList(){
        categoryList=new ArrayList<>();
        categoryList.add("Ingredients");
        categoryList.add("Calories");
    }
}
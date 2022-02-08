package com.example.yeschefuserapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.ViewRecipeActivity;
import com.example.yeschefuserapp.adapter.MainHorizontalCustomAdapter;
import com.example.yeschefuserapp.adapter.MainVerticalCustomListAdapter;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.model.RecipeCategoryList;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private MainVerticalCustomListAdapter mainVerticalCustomListAdapter;
    private List<RecipeCategoryList> recipeCategoryLists=new ArrayList<>();
    private final List<Recipe> recipes = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        int rowItemResourceId = R.layout.main_recycler_column_item;
        Context viewContext = view.getContext();

        //creating a list of recipe category list
        createRecipeCategoryList();

        mainVerticalCustomListAdapter=new MainVerticalCustomListAdapter(viewContext,recipeCategoryLists);
        initView(mainVerticalCustomListAdapter, view, view.findViewById(R.id.recycler_view));

        SearchView searchView=view.findViewById(R.id.search_bar);
        if (searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Toast.makeText(view.getContext(),"Search",Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    Toast.makeText(view.getContext(),s,Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

        return view;
    }



    private void initView(MainVerticalCustomListAdapter adapter, View view, RecyclerView recyclerView) {
        if (recyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }

    private void createRecipeCategoryList(){
        recipeCategoryLists.add(new RecipeCategoryList("Malaysian Food",new ArrayList<Recipe>(),"http://10.0.2.2:8090/api/user/all_recipes"));
        recipeCategoryLists.add(new RecipeCategoryList("Singapore Food",new ArrayList<Recipe>(),"http://10.0.2.2:8090/api/user/all_recipes"));
        recipeCategoryLists.add(new RecipeCategoryList("Thailand Food",new ArrayList<Recipe>(),"http://10.0.2.2:8090/api/user/all_recipes"));
    }
}
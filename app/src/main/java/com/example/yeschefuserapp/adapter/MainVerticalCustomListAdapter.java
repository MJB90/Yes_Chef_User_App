package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.listener.RecipeClickListener;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.RecommendedRecipes;
import com.example.yeschefuserapp.utility.RecommendedRecipesDescription;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainVerticalCustomListAdapter extends RecyclerView.Adapter<MainVerticalCustomListAdapter.MainViewHolder> {
    private Context context;
    private String url;
    private RecommendedRecipes recommendedRecipes;

    public MainVerticalCustomListAdapter(Context context, RecommendedRecipes recommendedRecipes) {
        this.context = context;
        this.recommendedRecipes = recommendedRecipes;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.recipeListTitle.setText(recommendedRecipes.getRecommendedRecipes().get(position).getName());
        setRecipeRecycler(holder.itemRecycler, recommendedRecipes.getRecommendedRecipes().get(position).getRecommendedRecipeData(), position, holder.itemView);

        RecipeClickListener onClickListener = new RecipeClickListener(context);
        MainHorizontalCustomAdapter adapter = new MainHorizontalCustomAdapter(R.layout.main_recycler_column_item, context, recommendedRecipes.getRecommendedRecipes().get(position).getRecommendedRecipeData(), onClickListener);
        holder.itemRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.itemRecycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return recommendedRecipes.getRecommendedRecipes().size();
    }

    public static final class MainViewHolder extends RecyclerView.ViewHolder {
        TextView recipeListTitle;
        RecyclerView itemRecycler;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeListTitle = itemView.findViewById(R.id.recipe_list_name);
            itemRecycler = itemView.findViewById(R.id.recycler_horizontal_view);
        }
    }

    private void setRecipeRecycler(RecyclerView recyclerView, List<Recipe> recipes, int position, View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RecipeClickListener onClickListener = new RecipeClickListener(view.getContext());
                MainHorizontalCustomAdapter adapter = new MainHorizontalCustomAdapter(R.layout.main_recycler_column_item, context, recipes, onClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                recyclerView.setAdapter(adapter);
            }
        }).start();
    }
}

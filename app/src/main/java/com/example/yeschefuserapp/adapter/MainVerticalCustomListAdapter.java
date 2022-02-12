package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.listener.RecipeClickListener;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.RecommendedRecipes;
import java.util.List;

public class MainVerticalCustomListAdapter extends RecyclerView.Adapter<MainVerticalCustomListAdapter.MainViewHolder> {
    private final Context context;
    private final RecommendedRecipes recommendedRecipes;

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
        setRecipeRecycler(holder.itemRecycler, recommendedRecipes.getRecommendedRecipes().get(position).getRecommendedRecipeData(), holder.itemView);
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

    private void setRecipeRecycler(RecyclerView recyclerView, List<Recipe> recipes, View view) {
        new Thread(() -> {
            RecipeClickListener onClickListener = new RecipeClickListener(view.getContext());
            MainHorizontalCustomAdapter adapter = new MainHorizontalCustomAdapter(R.layout.main_recycler_column_item, context, recipes, onClickListener);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
        }).start();
    }
}

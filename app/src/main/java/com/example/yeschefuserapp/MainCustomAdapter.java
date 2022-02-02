package com.example.yeschefuserapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainCustomAdapter extends RecyclerView.Adapter<MainCustomAdapter.MyViewHolder> {
    List<Recipe> recipes;
    Context context;
    private ItemClickListener mItemListener;

    public MainCustomAdapter(Context context, List<Recipe> recipes, ItemClickListener itemClickListener) {
        this.context = context;
        this.recipes = recipes;
        mItemListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_row_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        DownloadImageTask downloadImageTask = new DownloadImageTask(holder.recipeImage);
        // TODO: replace to real photo
        downloadImageTask.execute("https://lh3.googleusercontent.com/Js7QBBDQumvLixXwk7wnmyArHjN7SZbOElZHwzmZrR7mjA_ElR_p2tNGAMqcmr4Ru2ei47Gi8EvX7mDZd3ii=s640-c-rw-v1-e365");
        holder.recipeName.setText(recipe.getRecipeName());
        holder.itemView.setOnClickListener(view -> mItemListener.onItemClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface ItemClickListener {
        void onItemClick(Recipe recipe);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);
        }
    }

}

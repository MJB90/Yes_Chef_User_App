package com.example.yeschefuserapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.listener.ItemClickListener;
import com.example.yeschefuserapp.model.Recipe;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.MyViewHolder> {
    private final int resourceId;
    private final List<Recipe> recipes;
    private final ItemClickListener mItemListener;

    public BookmarkAdapter(int resourceId, List<Recipe> recipes, ItemClickListener itemClickListener) {
        this.resourceId = resourceId;
        this.recipes = recipes;
        mItemListener = itemClickListener;
    }

    public Recipe getRecipe(int i) {
        return this.recipes.get(i);
    }

    public void removeRecipe(int i) {
        this.recipes.remove(i);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.resourceId, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        if (recipe.getResizedImageURL() != null && recipe.getResizedImageURL().size() != 0) {
            Glide.with(holder.itemView)
                    .load(recipe.getResizedImageURL().get(0))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.yes_chef)
                    .into(holder.recipeImage);
        }

        holder.recipeName.setText(recipe.getName());
        holder.itemView.setOnClickListener(view -> mItemListener.onItemClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
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

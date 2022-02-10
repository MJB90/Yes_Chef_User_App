package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yeschefuserapp.listener.ItemClickListener;
import com.example.yeschefuserapp.utility.DownloadImageTask;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.model.Recipe;

import java.util.List;

public class MainHorizontalCustomAdapter extends RecyclerView.Adapter<MainHorizontalCustomAdapter.MyViewHolder> {
    private int resourceId;
    private List<Recipe> recipes;
    private Context context;
    private ItemClickListener mItemListener;

    public MainHorizontalCustomAdapter(int resourceId, Context context, List<Recipe> recipes, ItemClickListener itemClickListener) {
        this.resourceId = resourceId;
        this.context = context;
        this.recipes = recipes;
        mItemListener = itemClickListener;
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
        //DownloadImageTask downloadImageTask = new DownloadImageTask(holder.recipeImage);
        //downloadImageTask.execute(recipe.getImageURL().get(0));
        if (recipe.getImageURL().size()!=0){
            Glide.with(holder.itemView)
                    .load(recipe.getImageURL().get(0))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.recipeImage);
        }



        holder.recipeName.setText(recipe.getName());
        holder.itemView.setOnClickListener(view -> mItemListener.onItemClick(recipe));
        //holder.ratingBar.setRating((float)4.5);
        //holder.recipeCuisineType.setText(recipe.getCuisineType().get(0));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeName;
        TextView recipeCuisineType;
        RatingBar ratingBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeName = itemView.findViewById(R.id.recipe_name);
            //recipeCuisineType=itemView.findViewById(R.id.recipe_cuisineType);
            //ratingBar=itemView.findViewById(R.id.recipe_rating);
        }
    }

}

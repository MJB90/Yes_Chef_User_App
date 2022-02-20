package com.example.yeschefuserapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.MainActivity;
import com.example.yeschefuserapp.activity.ViewRecipeActivity;
import com.example.yeschefuserapp.listener.ItemClickListener;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.model.UserReview;

import java.text.DecimalFormat;
import java.util.List;

public class MoreLikeThisAdapter extends RecyclerView.Adapter<MoreLikeThisAdapter.MyViewHolder> {
    private final int resourceId;
    private final List<Recipe> recipes;
    private final ItemClickListener mItemListener;

    public MoreLikeThisAdapter(int resourceId, List<Recipe> recipes, ItemClickListener itemClickListener) {
        this.resourceId = resourceId;
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
        if (holder.ratingBar != null && holder.recipeCuisineType != null) {
            new Thread(() -> ((ViewRecipeActivity)holder.itemView.getContext()).runOnUiThread(() -> {
                holder.ratingBar.setRating(getAvgRating(recipe));
                if (recipe.getCuisineType().size()>=1){
                    holder.recipeCuisineType.setText(recipe.getCuisineType().get(0));
                }
            })).start();
        }
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
            recipeCuisineType = itemView.findViewById(R.id.recipe_cuisineType);
            ratingBar = itemView.findViewById(R.id.recipe_rating);
        }
    }

    public float getAvgRating(Recipe recipe) {
        float ratingTotal = 0;
        float ratingAvg = 0;
        if (recipe.getUserReviews() != null && recipe.getUserReviews().size() > 0) {
            for (UserReview ur : recipe.getUserReviews()) {
                if (ur.getRating() != null)
                    ratingTotal += ur.getRating();
            }
            int reviewNo = recipe.getUserReviews().size();
            DecimalFormat formatter = new DecimalFormat("#0.0");
            ratingAvg = ratingTotal / reviewNo;
            formatter.format(ratingAvg);
        }
        return ratingAvg;
    }

}

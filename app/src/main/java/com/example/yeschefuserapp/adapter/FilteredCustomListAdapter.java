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
import com.example.yeschefuserapp.listener.ItemClickListener;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.model.UserReview;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.List;


public class FilteredCustomListAdapter extends RecyclerView.Adapter<FilteredCustomListAdapter.MyFilterViewHolder> {
    private final int resourceId;
    private final List<Recipe> recipes;
    private final ItemClickListener mItemListener;

    public FilteredCustomListAdapter(int resourceId, List<Recipe> recipes, ItemClickListener itemClickListener) {
            this.resourceId = resourceId;
        this.recipes = recipes;
            mItemListener = itemClickListener;
    }


    @NonNull
    @Override
    public MyFilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.resourceId, parent, false);
        return new MyFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilteredCustomListAdapter.MyFilterViewHolder holder, int pos) {
        if (recipes!=null && holder.recipeImage!=null)
        {
            if (recipes.get(pos).getResizedImageURL()!=null && recipes.get(pos).getResizedImageURL().size() !=0) {
                Glide.with(holder.itemView)
                        .load(recipes.get(pos).getResizedImageURL().get(0))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.yes_chef)
                        .into(holder.recipeImage);
            }
        }
        if (holder.recipeName!=null && recipes!=null){
            holder.recipeName.setText(recipes.get(pos).getName());
        }
        if (holder.review!=null && recipes!=null){
            holder.review.setRating(getAvgRating(recipes.get(pos)));
        }
        if (holder.cuisineType!=null && recipes!=null){
            String words="";
            for (int i = 0; i< recipes.get(pos).getCuisineType().size(); i++) {
                if (i==(recipes.get(pos).getCuisineType().size()-1)) {
                    words=String.format("%s %s",words,recipes.get(pos).getCuisineType().get(i));
                }
                else {
                    words = String.format("%s %s, ", words, recipes.get(pos).getCuisineType().get(i));
                }
            }
            holder.cuisineType.setText(words);
        }
        if (holder.prepTime!=null && recipes!=null)
        {
            int time=recipes.get(pos).getPrepTime()/60;
            if (time>=60){
                int minute= time%60;
                int hour=time/60;
                if (hour==1 && minute>1) {
                    holder.prepTime.setText(String.format("%s Hour %s Minutes",hour,minute));
                }
                else if (hour>1 && minute>1){
                    holder.prepTime.setText(String.format("%s Hours %s Minutes",hour,minute));
                }
                else if(hour==1 && minute==1){
                    holder.prepTime.setText(String.format("%s Hour %s Minute",hour,minute));
                }
                else if(hour == 1){
                    holder.prepTime.setText(String.format("%s Hour",hour));
                }
                else if(minute == 1){
                    holder.prepTime.setText(String.format("%s Hours %s Minute",hour,minute));
                }
                else {
                    holder.prepTime.setText(String.format("%s Hours",hour));
                }
            }
            else{
                if (time>1){
                    holder.prepTime.setText(String.format("%s Minutes",time));
                }
                else if(time==1){
                    holder.prepTime.setText(String.format("%s Minute",time));
                }
                else if(time==0){
                    holder.prepTime.setText(String.format("%s Minute",time));
                }
            }
        }
        holder.card.setOnClickListener(view -> mItemListener.onItemClick(recipes != null ? recipes.get(pos) : null));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
    public static class MyFilterViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeName;
        RatingBar review;
        TextView cuisineType;
        TextView prepTime;
        MaterialCardView card;

        public MyFilterViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage=itemView.findViewById(R.id.filter_imageView);
            recipeName=itemView.findViewById(R.id.filter_textview_name);
            review=itemView.findViewById(R.id.filter_rating_bar);
            cuisineType=itemView.findViewById(R.id.filter_textview_cuisineType);
            prepTime=itemView.findViewById(R.id.filter_textview_prepTime);
            card=itemView.findViewById(R.id.filter_card);
        }
    }
    public float getAvgRating(Recipe recipe) {
        float ratingTotal = 0;
        float ratingAvg=0;
        if (recipe.getUserReviews()!=null && recipe.getUserReviews().size()>0) {
            for (UserReview ur : recipe.getUserReviews()) {
                if (ur.getRating()!=null)
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

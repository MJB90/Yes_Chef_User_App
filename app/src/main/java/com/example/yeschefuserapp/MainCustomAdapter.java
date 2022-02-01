package com.example.yeschefuserapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainCustomAdapter extends RecyclerView.Adapter<MainCustomAdapter.MyViewHolder> {
    ArrayList<Recipe> recipes;
    Context context;
    private ItemClickListener mItemListener;
    public MainCustomAdapter(Context context, ArrayList<Recipe> recipes, ItemClickListener itemClickListener){
        this.context=context;
        this.recipes=recipes;
        mItemListener=itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_row_item,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recipeImage.setImageResource(recipes.get(position).getRecipePhoto());
        holder.recipeName.setText(recipes.get(position).getRecipeName());
        holder.itemView.setOnClickListener(view->{
            mItemListener.onItemClick(recipes.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public interface ItemClickListener{
        void onItemClick(Recipe recipe);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage=itemView.findViewById(R.id.recipe_image);
            recipeName=itemView.findViewById(R.id.recipe_name);
        }
    }

}

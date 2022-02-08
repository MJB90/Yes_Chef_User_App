package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        DownloadImageTask downloadImageTask = new DownloadImageTask(holder.recipeImage);
        // TODO: replace to real photo
        downloadImageTask.execute(recipe.getImageURL().get(0));
        holder.recipeName.setText(recipe.getName());
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

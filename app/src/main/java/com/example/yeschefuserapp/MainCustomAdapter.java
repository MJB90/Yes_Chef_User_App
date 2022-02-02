package com.example.yeschefuserapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainCustomAdapter extends RecyclerView.Adapter<MainCustomAdapter.MyViewHolder> {
    List<Recipe> recipes;
    Context context;
    private ItemClickListener mItemListener;

    private URL url;
    private Bitmap image;

    public MainCustomAdapter(Context context, List<Recipe> recipes, ItemClickListener itemClickListener){
        this.context=context;
        this.recipes=recipes;
        mItemListener=itemClickListener;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_row_item,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            url = new URL(recipes.get(position).getRecipePhoto());
            image = BitmapFactory.decodeStream(url.openStream());
            holder.recipeImage.setImageBitmap(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //holder.recipeImage.setImageResource(recipes.get(position).getRecipePhoto());
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

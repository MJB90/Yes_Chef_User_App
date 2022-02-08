package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.content.Intent;

import com.example.yeschefuserapp.activity.ViewRecipeActivity;
import com.example.yeschefuserapp.model.Recipe;

public class RecipeClickListener implements ItemClickListener {
    private Context context;

    public RecipeClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(context, ViewRecipeActivity.class);
        intent.putExtra("recipe", recipe);
        context.startActivity(intent);
    }
}

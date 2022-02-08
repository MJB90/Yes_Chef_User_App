package com.example.yeschefuserapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.listener.RecipeClickListener;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.model.RecipeCategoryList;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class MainVerticalCustomListAdapter extends RecyclerView.Adapter<MainVerticalCustomListAdapter.MainViewHolder> {
    private Context context;
    private List<RecipeCategoryList> recipeCategoryList;

    public MainVerticalCustomListAdapter(Context context, List<RecipeCategoryList> recipeCategoryList) {
        this.context = context;
        this.recipeCategoryList = recipeCategoryList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.recipeListTitle.setText(recipeCategoryList.get(position).getName());
        setRecipeRecycler(holder.itemRecycler, recipeCategoryList.get(position).getRecipeList(), position, holder.itemView);
    }

    @Override
    public int getItemCount() {
        return recipeCategoryList.size();
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

    private void setRecipeRecycler(RecyclerView recyclerView, List<Recipe> recipes, int position, View view) {
        RecipeClickListener onClickListener = new RecipeClickListener(view.getContext());
        MainHorizontalCustomAdapter adapter = new MainHorizontalCustomAdapter(R.layout.main_recycler_column_item, context, recipes, onClickListener);

        fetchData(adapter, view, recipeCategoryList.get(position).getUrl(), recipeCategoryList.get(position).getRecipeList());

        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void fetchData(MainHorizontalCustomAdapter adapter, View view, String url, List<Recipe> list) {
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    // The recipes should be the same reference
                    list.addAll(Arrays.asList(tmpArray.clone()));

                    // Need to notify the adapter after updating the recipes
                    // ref: https://stackoverflow.com/a/48959184
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Log.e("HomeFragment", "FetchData failed", error);
                    Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
        );
        MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
    }
}

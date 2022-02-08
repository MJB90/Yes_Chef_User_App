package com.example.yeschefuserapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.adapter.FilteredCustomListAdapter;
import com.example.yeschefuserapp.model.Recipe;

import java.util.List;

public class FilterResult extends AppCompatActivity {
    private List<Recipe> recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);

        recipes=(List<Recipe>) getIntent().getExtras().getSerializable("recipes");

        TextView searchResult=findViewById(R.id.filter_result_size);
        searchResult.setText(recipes.size()+" recipes");

        FilteredCustomListAdapter adapter = new FilteredCustomListAdapter(R.layout.filter_result_row,
                this,
                recipes,
                recipe -> {
                    //Click on a recipe
                    Intent intent = new Intent(this, ViewRecipeActivity.class);
                    intent.putExtra("recipe", recipe);
                    startActivity(intent);
                });


        RecyclerView recyclerView=findViewById(R.id.filtered_result_recycler_view);
        if (recyclerView != null)
        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }

}
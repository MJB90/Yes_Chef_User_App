package com.example.yeschefuserapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.model.Recipe;

import java.util.List;

public class FilterResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_result);
        List<Recipe> recipes=(List<Recipe>) getIntent().getExtras().getSerializable("recipes");
        Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
    }
}
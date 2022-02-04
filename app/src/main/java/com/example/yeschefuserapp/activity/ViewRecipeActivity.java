package com.example.yeschefuserapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.DownloadImageTask;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

public class ViewRecipeActivity extends AppCompatActivity implements View.OnClickListener{
    private String recipeId;
    private Recipe selectedRecipe = new Recipe();
    AlertDialog myPopUpReviewDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeId");
        String uri = String.format("http://10.0.2.2:8090/api/user/all_recipes/%s", recipeId);

        fetchSelectedRecipe(uri);

        ImageView recipeImage = findViewById(R.id.recipe_image);

        DownloadImageTask downloadImageTask = new DownloadImageTask(recipeImage);
        downloadImageTask.execute("https://lh3.googleusercontent.com/Js7QBBDQumvLixXwk7wnmyArHjN7SZbOElZHwzmZrR7mjA_ElR_p2tNGAMqcmr4Ru2ei47Gi8EvX7mDZd3ii=s640-c-rw-v1-e365");
    }

    public void fetchSelectedRecipe(String uri){
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                uri,
                null,
                response -> {
                    Gson gson = new Gson();
                    selectedRecipe = gson.fromJson(response.toString(), Recipe.class);
                },
                error -> Toast.makeText(ViewRecipeActivity.this, error.toString(), Toast.LENGTH_LONG).show()
        );
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }


    public void PopUpWriteReview(){

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View myPopUpReview = inflater.inflate(R.layout.pop_up_review, null, false);

        AlertDialog.Builder popUpBuilder = new AlertDialog.Builder(this);
        popUpBuilder.setView(myPopUpReview);
        popUpBuilder.setCancelable(false);
        myPopUpReviewDialog = popUpBuilder.create();

        TextView header = myPopUpReview.findViewById(R.id.writeReviewHead);
        header.setText("Write A Review");

        EditText inputReview = myPopUpReview.findViewById(R.id.writeReviewText);

        Button submitBtn = myPopUpReview.findViewById(R.id.submitBtn);
        if(submitBtn != null){
            submitBtn.setOnClickListener(this);
        }

        Button cancelBtn = myPopUpReview.findViewById(R.id.cancelBtn);
        if(cancelBtn != null){
            cancelBtn.setOnClickListener(this);
        }

        //if rating!=null, submit enable
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.submitBtn){
            //save the review and the star to the document
        }

        if(id == R.id.cancelBtn){
            myPopUpReviewDialog.cancel();
        }
    }
}
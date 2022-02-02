package com.example.yeschefuserapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewRecipeActivity extends AppCompatActivity implements View.OnClickListener{

    AlertDialog myPopUpReviewDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
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
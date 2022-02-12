package com.example.yeschefuserapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.listener.BookmarkListener;
import com.example.yeschefuserapp.model.Ingredient;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.model.UserReview;
import com.example.yeschefuserapp.utility.MySingleton;
import com.example.yeschefuserapp.utility.ReviewCommunicationModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity
        implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private Recipe selectedRecipe = new Recipe();
    private Integer reviewNo;
    private Double ratingAvg;
    private Integer ratingTotal;
    private String ingredients = "";
    private String preparationSteps = "";
    private int counter = 0;
    private JSONObject reviewJsonObject;
    private UserContext userContext;

    AlertDialog myPopUpReviewDialog;
    EditText inputReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        Intent intent = getIntent();
        selectedRecipe = (Recipe) intent.getSerializableExtra("recipe");
        //String uri = String.format("http://10.0.2.2:8090/api/user/all_recipes/%s", selectedRecipe.getId());

        //fetchSelectedRecipe(uri);

        ImageView recipeImage = findViewById(R.id.recipe_image);
        Glide.with(this)
                .load(selectedRecipe.getImageURL().get(0))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_launcher_background)
                .into(recipeImage);
        //DownloadImageTask downloadImageTask = new DownloadImageTask(recipeImage);
        //downloadImageTask.execute(selectedRecipe.getImageURL().get(0));

        TextView recipeName = findViewById(R.id.recipe_name);
        recipeName.setText(selectedRecipe.getName());

        TextView bookmark = findViewById(R.id.bookmark_this);
        bookmark.setOnClickListener(this);

        TextView prepTime = findViewById(R.id.prep_time);
        prepTime.setText("Prep time : " + String.valueOf(selectedRecipe.getPrepTime() / 60) + "min");

        TextView rating = findViewById(R.id.rating);
        if (selectedRecipe.getUserReviews() != null) {
            getAvgRating();
            rating.setText(String.valueOf(ratingAvg) + "/5.0 (" + reviewNo.toString() + " reviews)");
        } else
            rating.setText("O reviews");

        TextView calories = findViewById(R.id.calories);
        if (selectedRecipe.getCalories() != null) {
            calories.setText(selectedRecipe.getCalories().toString() + " kCal");
        } else
            calories.setText("No calories info found");

        TextView writeAReview = findViewById(R.id.write_a_review);
        writeAReview.setOnClickListener(this);

        TextView ingredientsHeader = findViewById(R.id.ingredients_header);
        TextView ingredientsView = findViewById(R.id.ingredients);
        getIngredients();
        ingredientsView.setText(ingredients);

        TextView stepsHeader = findViewById(R.id.steps_header);
        TextView steps = findViewById(R.id.steps);
        getSteps();
        steps.setText(preparationSteps);

        //downloadImageTask.execute("https://lh3.googleusercontent.com/Js7QBBDQumvLixXwk7wnmyArHjN7SZbOElZHwzmZrR7mjA_ElR_p2tNGAMqcmr4Ru2ei47Gi8EvX7mDZd3ii=s640-c-rw-v1-e365");

        Button bookmarkBtn = findViewById(R.id.add_bookmark_btn);
        this.userContext = new UserContext(this);
        String userEmail = this.userContext.getEmail();
        BookmarkListener bookmarkListener = new BookmarkListener(this, userEmail, selectedRecipe.getId(), bookmarkBtn, false);
        bookmarkBtn.setOnClickListener(bookmarkListener);
        fetchBookmarkData(bookmarkBtn, bookmarkListener, userEmail);

        navigationBar();

    }

    public void getAvgRating() {
        ratingTotal = 0;
        for (UserReview ur : selectedRecipe.getUserReviews()) {
            ratingTotal += ur.getRating();
        }
        reviewNo = selectedRecipe.getUserReviews().size();
        DecimalFormat formatter = new DecimalFormat("#0.0");
        ratingAvg = Double.valueOf(ratingTotal / reviewNo);
        formatter.format(ratingAvg);
    }

    public void fetchSelectedRecipe(String uri) {
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                uri,
                null,
                response -> {
                    Gson gson = new Gson();
                    selectedRecipe = gson.fromJson(response.toString(), Recipe.class);
                },
                error -> {
                    Log.e("ViewRecipeActivity", "FetchSelectedRecipe failed", error);
                    Toast.makeText(ViewRecipeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
        );
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }

    private void reviewToJson() {
        ReviewCommunicationModel review = new ReviewCommunicationModel();
        review.setRecipeId(selectedRecipe.getId());
        review.setRating(5);
        review.setUserEmail(this.userContext.getEmail());
        review.setDescription(inputReview.getText().toString());

        Gson gson = new Gson();
        String json = gson.toJson(review);
        try {
            reviewJsonObject = new JSONObject(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitReview() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                String.format("http://10.0.2.2:8090/api/user/post_review"),
                reviewJsonObject,
                response -> {
                    //TODO go to review page
//                    Intent intent = new Intent(view.getContext(), FilterResult.class);
//                    startActivity(intent);
                },
                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()

        );
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }

    private void fetchBookmarkData(Button bookmarkBtn, BookmarkListener bookmarkListener, String email) {
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("http://10.0.2.2:8090/api/user/bookmarks/%s", email),
                null,
                response -> {
                    Gson gson = new Gson();
                    Recipe[] tmpArray = gson.fromJson(response.toString(), Recipe[].class);
                    // The recipes should be the same reference
                    List<Recipe> bookmarkList = new ArrayList<>(Arrays.asList(tmpArray.clone()));
                    boolean isAdded = false;
                    for (Recipe recipe : bookmarkList) {
                        if (recipe.getId().equals(selectedRecipe.getId())) {
                            isAdded = true;
                            break;
                        }
                    }
                    bookmarkListener.setAdded(isAdded);
                    int textId = isAdded ? R.string.delete_from_bookmark : R.string.add_to_bookmark;
                    bookmarkBtn.setText(textId);
                },
                error -> {
                    Log.e("ViewRecipeActivity", "FetchData failed", error);
                    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
                }
        );
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }

    public void getIngredients() {
        for (Ingredient i : selectedRecipe.getIngredients()) {
            counter++;
            ingredients += counter + ". " + i.toString() + "\n";
        }
        counter = 0;
    }

    public void getSteps() {
        for (String prepStep : selectedRecipe.getPrepSteps()) {
            counter++;
            preparationSteps += counter + ". " + prepStep + "\n \n";
        }
        counter = 0;
    }

    public void PopUpWriteReview() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View myPopUpReview = inflater.inflate(R.layout.pop_up_review, null, false);

        AlertDialog.Builder popUpBuilder = new AlertDialog.Builder(this);
        popUpBuilder.setView(myPopUpReview);
        popUpBuilder.setCancelable(false);
        myPopUpReviewDialog = popUpBuilder.create();

        TextView header = myPopUpReview.findViewById(R.id.writeReviewHead);
        header.setText("Write A Review");

        inputReview = myPopUpReview.findViewById(R.id.writeReviewText);

        Button submitBtn = myPopUpReview.findViewById(R.id.submitBtn);
        if (submitBtn != null) {
            submitBtn.setOnClickListener(this);
        }

        Button cancelBtn = myPopUpReview.findViewById(R.id.cancelBtn);
        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(this);
        }
        myPopUpReviewDialog.show();
        //if rating!=null, submit enable
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.submitBtn) {
            reviewToJson();
            submitReview();
            myPopUpReviewDialog.dismiss();
        }

        if (id == R.id.cancelBtn) {
            myPopUpReviewDialog.dismiss();
        }

        if (id == R.id.bookmark_this) {

            //
        }
        if (id == R.id.write_a_review) {
            //TODO: send an intent to write
            PopUpWriteReview();

        }
    }


    private void navigationBar() {
        BottomNavigationView bottonNavigationView = findViewById(R.id.bottom_nav);
        bottonNavigationView.setOnNavigationItemSelectedListener(this);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(this, MainActivity.class);
        switch (id) {
            case R.id.nav_home:
                intent.putExtra("nav_item", R.id.nav_home);
                break;
            case R.id.nav_bookmarks:
                intent.putExtra("nav_item", R.id.nav_bookmarks);
                break;
            case R.id.nav_advanced_filter:
                intent.putExtra("nav_item", R.id.nav_advanced_filter);
                break;
            case R.id.nav_account:
                intent.putExtra("nav_item", R.id.nav_account);
                break;
            default:
                return false;
        }
        startActivity(intent);
        return true;
    }
}
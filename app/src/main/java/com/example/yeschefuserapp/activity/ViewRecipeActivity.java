package com.example.yeschefuserapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.adapter.ReviewListAdapter;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.listener.BookmarkListener;
import com.example.yeschefuserapp.model.Ingredient;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.model.UserReview;
import com.example.yeschefuserapp.utility.MySingleton;
import com.example.yeschefuserapp.utility.ReviewCommunicationModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewRecipeActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {
    private Recipe selectedRecipe = new Recipe();
    private Integer reviewNo;
    private Double ratingAvg;
    private Integer ratingTotal;
    private String ingredients = "";
    private String preparationSteps = "";
    private int counter = 0;
    private String selectedRecipeId;
    private JSONObject reviewJsonObject;
    private UserContext userContext;
    private String ACCESS_TOKEN;
    private Context context;

    AlertDialog myPopUpReviewDialog;
    EditText inputReview;
    RatingBar submitRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        userContext = new UserContext(this);
        ACCESS_TOKEN = userContext.getToken();
        Intent intent = getIntent();
        selectedRecipeId = (String)((Recipe) intent.getSerializableExtra("recipe")).getId();

        context = this;
        fetchSelectedRecipe(selectedRecipeId);

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

    public void fetchSelectedRecipe(String id) {
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                String.format(context.getString(R.string.domain_name) + "api/user/all_recipes/%s", id),
                null,
                response -> {
                    Gson gson = new Gson();
                    selectedRecipe = gson.fromJson(response.toString(), Recipe.class);

                    ImageView recipeImage = findViewById(R.id.recipe_image);
                    if (selectedRecipe.getResizedImageURL() != null && selectedRecipe.getResizedImageURL().size() != 0) {
                        Glide.with(this)
                                .load(selectedRecipe.getResizedImageURL().get(0))
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(recipeImage);
                    }

                    TextView recipeName = findViewById(R.id.recipe_name);
                    recipeName.setText(selectedRecipe.getName());

                    TextView prepTime = findViewById(R.id.prep_time);
                    prepTime.setText("Prep time : " + String.valueOf(selectedRecipe.getPrepTime() / 60) + "min");

                    TextView rating = findViewById(R.id.rating);
                    RatingBar viewRatingBar = findViewById(R.id.view_rating_bar);
                    if (selectedRecipe.getUserReviews() != null) {
                        getAvgRating();
                        rating.setText(String.valueOf(ratingAvg) + "/5.0 (" + reviewNo.toString() + " reviews)");
                        viewRatingBar.setRating(ratingAvg.floatValue());
                    } else
                        rating.setText("No reviews");

                    TextView calories = findViewById(R.id.calories);
                    if (selectedRecipe.getCalories() != null) {
                        calories.setText(selectedRecipe.getCalories().toString() + " kCal");
                    } else
                        calories.setText("No calories info found");


                    TextView ingredientsHeader = findViewById(R.id.ingredients_header);
                    TextView ingredientsView = findViewById(R.id.ingredients);
                    getIngredients();
                    ingredientsView.setText(ingredients);

                    TextView stepsHeader = findViewById(R.id.steps_header);
                    TextView steps = findViewById(R.id.steps);
                    getSteps();
                    steps.setText(preparationSteps);

                    ReviewListAdapter adapter = new ReviewListAdapter(R.layout.review_item_row, this, selectedRecipe.getUserReviews());
                    RecyclerView reviewRecyclerView = findViewById(R.id.recycler_review);
                    if (reviewRecyclerView != null) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                        reviewRecyclerView.setLayoutManager(layoutManager);
                        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        reviewRecyclerView.setAdapter(adapter);
                    }

                    Button writeAReview = findViewById(R.id.write_a_review);
                    writeAReview.setOnClickListener(this);

                    Button bookmarkBtn = findViewById(R.id.add_bookmark_btn);
                    this.userContext = new UserContext(this);
                    String userEmail = this.userContext.getEmail();
                    BookmarkListener bookmarkListener = new BookmarkListener(this, selectedRecipe.getId(), bookmarkBtn, false);
                    bookmarkBtn.setOnClickListener(bookmarkListener);
                    fetchBookmarkData(bookmarkBtn, bookmarkListener, userEmail);

                    navigationBar();
                },
                error -> {
                    Toast.makeText(this, "You are Logged out", Toast.LENGTH_LONG).show();
                    userContext.clearLoginPreferences();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headerMap;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }

    private void reviewToJson() {
        ReviewCommunicationModel review = new ReviewCommunicationModel();
        review.setRecipeId(selectedRecipe.getId());
        review.setRating(Math.round(submitRatingBar.getRating()));
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
        final String requestBody = reviewJsonObject.toString();
        StringRequest objectRequest = new StringRequest(
                Request.Method.POST,
                String.format(getString(R.string.domain_name) + "api/user/post_review"),
                response -> {
                    //TODO go to review page
//                    Gson gson = new Gson();
//                    selectedRecipe = gson.fromJson(response.toString(), Recipe.class);

                    Intent intent = new Intent(ViewRecipeActivity.this,ViewRecipeActivity.class);
                    intent.putExtra("recipe", selectedRecipe);
                    startActivity(intent);
                },
                error -> {
                    //TODO uncomment the below codes after you are done with this function
                    Toast.makeText(this, "You are Logged out", Toast.LENGTH_LONG).show();
                    userContext.clearLoginPreferences();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(this).addToRequestQueue(objectRequest);
    }

    private void fetchBookmarkData(Button bookmarkBtn, BookmarkListener bookmarkListener, String email) {
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format(getString(R.string.domain_name) + "api/user/bookmarks/%s", email),
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
                    Toast.makeText(this, "You are Logged out", Toast.LENGTH_LONG).show();
                    userContext.clearLoginPreferences();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
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

        submitRatingBar = myPopUpReview.findViewById(R.id.submit_rating_bar);
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
        //TODO if rating!=null, submit enable
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

        //if (id == R.id.bookmark_this) {

        //
        //}
        if (id == R.id.write_a_review) {
            PopUpWriteReview();

        }
    }


    private void navigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(this, MainActivity.class);

        if (id == R.id.nav_home) intent.putExtra("nav_item", R.id.nav_home);
        else if (id == R.id.nav_bookmarks) intent.putExtra("nav_item", R.id.nav_bookmarks);
        else if (id == R.id.nav_advanced_filter)
            intent.putExtra("nav_item", R.id.nav_advanced_filter);
        else if (id == R.id.nav_account) intent.putExtra("nav_item", R.id.nav_account);
        else return false;

        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("nav_item", R.id.nav_home);
        startActivity(intent);
    }
}
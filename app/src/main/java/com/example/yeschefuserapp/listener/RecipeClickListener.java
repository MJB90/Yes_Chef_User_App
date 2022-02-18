package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.LoginActivity;
import com.example.yeschefuserapp.activity.ViewRecipeActivity;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.model.Recipe;
import com.example.yeschefuserapp.utility.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class RecipeClickListener implements ItemClickListener {
    private final Context context;
    private final UserContext userContext;
    private final String ACCESS_TOKEN;

    public RecipeClickListener(Context context) {
        this.context = context;
        userContext = new UserContext(context);
        ACCESS_TOKEN= userContext.getToken();
    }

    @Override
    public void onItemClick(Recipe recipe) {
        //Saving the user view history
        StringRequest objectRequest = new StringRequest(
                Request.Method.POST,
                String.format(context.getString(R.string.domain_name) + "api/user/post_user_activity/%s", recipe.getId()),
                response -> {
                    Intent intent = new Intent(context, ViewRecipeActivity.class);
                    intent.putExtra("recipe", recipe);
                    context.startActivity(intent);
                },
                error -> {
                    Toast.makeText(context, "You are Logged out", Toast.LENGTH_LONG).show();
                    userContext.clearLoginPreferences();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(context).addToRequestQueue(objectRequest);
    }
}

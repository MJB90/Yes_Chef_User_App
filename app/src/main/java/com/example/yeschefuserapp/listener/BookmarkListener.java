package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yeschefuserapp.model.BookmarkCommunicationModel;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class BookmarkListener implements View.OnClickListener {
    private static final String TAG = "BookmarkListener";

    private final Context context;
    private final String userEmail;
    private final String recipeId;

    public BookmarkListener(Context context, String userEmail, String recipeId) {
        this.context = context;
        this.userEmail = userEmail;
        this.recipeId = recipeId;
    }


    @Override
    public void onClick(View view) {
        try {
            BookmarkCommunicationModel model = new BookmarkCommunicationModel(recipeId, userEmail);
            Gson gson = new Gson();
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:8090/api/user/post_bookmark",
                    new JSONObject(gson.toJson(model)),
                    response -> Toast.makeText(context, "Add successfully", Toast.LENGTH_LONG).show(),
                    error -> {
                        Log.e(TAG, "Add bookmark failed", error);
                        Toast.makeText(context, "Add failed", Toast.LENGTH_LONG).show();
                    }
            );
            MySingleton.getInstance(context).addToRequestQueue(objectRequest);
        } catch (JSONException e) {
            Log.e(TAG, "Build json object failed", e);
        }
    }
}

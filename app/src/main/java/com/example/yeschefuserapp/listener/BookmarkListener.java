package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.model.BookmarkCommunicationModel;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookmarkListener implements View.OnClickListener {
    private static final String TAG = "BookmarkListener";

    private final Context context;
    private final UserContext userContext;
    private final String recipeId;
    private final Button btn;
    private boolean isAdded;

    public BookmarkListener(Context context, String recipeId, Button btn, boolean isAdded) {
        this.context = context;
        this.userContext = new UserContext(context);
        this.recipeId = recipeId;
        this.btn = btn;
        this.isAdded = isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    @Override
    public void onClick(View view) {
        try {
            if (isAdded) {
                deleteBookmark();
            } else {
                addBookmark();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Build json object failed", e);
        }
    }

    private void addBookmark() throws JSONException {
        BookmarkCommunicationModel model = new BookmarkCommunicationModel(recipeId, this.userContext.getEmail());
        Gson gson = new Gson();
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8090/api/user/post_bookmark",
                new JSONObject(gson.toJson(model)),
                response -> {
                    Toast.makeText(context, "Add successfully", Toast.LENGTH_LONG).show();
                    isAdded = true;
                    btn.setText(R.string.delete_from_bookmark);
                },
                error -> {
                    Log.e(TAG, "Add bookmark failed", error);
                    Toast.makeText(context, "Add failed", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Authorization", "Bearer " + userContext.getToken());
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(context).addToRequestQueue(objectRequest);
    }

    private void deleteBookmark() throws JSONException {
        StringRequest objectRequest = new StringRequest(
                Request.Method.DELETE,
                String.format("http://10.0.2.2:8090/api/user/delete_bookmark/%s/%s", this.userContext.getEmail(), recipeId),
                response -> {
                    Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show();
                    isAdded = false;
                    btn.setText(R.string.add_to_bookmark);
                },
                error -> {
                    Log.e(TAG, "Delete bookmark failed", error);
                    Toast.makeText(context, "Delete failed", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Authorization", "Bearer " + userContext.getToken());
                return headerMap;
            }
        };
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(context).addToRequestQueue(objectRequest);
    }
}

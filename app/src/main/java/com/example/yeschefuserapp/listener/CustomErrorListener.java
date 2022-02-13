package com.example.yeschefuserapp.listener;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomErrorListener implements Response.ErrorListener {
    private final String tag;
    private final Context context;

    public CustomErrorListener(String tag, Context context) {
        this.tag = tag;
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String errMsg = "Error occurs";
        try {
            JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
            errMsg = jsonObject.getString("message");
        } catch (JSONException e) {
            Log.e(tag, "Parse json exception", e);
        }
        Log.e(tag, errMsg, error);
        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
    }
}

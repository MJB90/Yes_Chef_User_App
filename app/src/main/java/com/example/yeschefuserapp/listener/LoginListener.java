package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.MainActivity;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.model.AppUser;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginListener implements View.OnClickListener {
    private static final String TAG = "LoginListener";
    private final Context context;
    private final UserContext userContext;
    private final TextView emailText;
    private final TextView pwdText;
    private final CheckBox rememberMe;
    private final CustomErrorListener errorListener;

    public LoginListener(Context context, TextView emailText, TextView pwdText, CheckBox rememberMe) {
        this.context = context;
        this.userContext = new UserContext(context);
        this.emailText = emailText;
        this.pwdText = pwdText;
        this.rememberMe=rememberMe;
        this.errorListener = new CustomErrorListener(TAG, context);
    }

    @Override
    public void onClick(View view) {
        login(emailText.getText().toString(), pwdText.getText().toString());
    }

    private void login(String email, String password) {
        AppUser appUser = AppUser.builder().
                email(email).
                password(password).
                build();
        Gson gson = new Gson();

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                context.getString(R.string.domain_name)+ "api/login?email="+email+"&password="+password,
                null,
                response -> {
                    try {
                        String token=response.getString("access_token");
                        userContext.setUserInfo(email, password,token,rememberMe.isChecked());
                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, errorListener
        );
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
        MySingleton.getInstance(context).addToRequestQueue(objectRequest);
    }
}

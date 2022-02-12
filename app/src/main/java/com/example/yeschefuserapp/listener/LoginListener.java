package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.activity.MainActivity;
import com.example.yeschefuserapp.model.AppUser;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

public class LoginListener implements View.OnClickListener {
    private static final String TAG = "LoginListener";
    private final Context context;
    private final TextView emailText;
    private final TextView pwdText;

    public LoginListener(Context context, TextView emailText, TextView pwdText) {
        this.context = context;
        this.emailText = emailText;
        this.pwdText = pwdText;
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

        StringRequest objectRequest = new StringRequest(
                Request.Method.POST,
                "http://10.0.2.2:8090/api/user/login",
                response -> {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                },
                error -> {
                    Log.e(TAG, "Login failed", error);
                    Toast.makeText(context, "Credentials are not valid", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public byte[] getBody() {
                return gson.toJson(appUser).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(objectRequest);
    }
}

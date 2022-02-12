package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.model.AppUser;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

public class SignupListener implements View.OnClickListener {
    private static final String TAG = "SignupListener";

    private final Context context;
    private final TextView emailText;
    private final TextView pwdText;
    private final CustomErrorListener errorListener;

    public SignupListener(Context context, TextView emailText, TextView pwdText) {
        this.context = context;
        this.emailText = emailText;
        this.pwdText = pwdText;
        this.errorListener = new CustomErrorListener(TAG, context);
    }

    @Override
    public void onClick(View view) {
        this.signup(emailText.getText().toString(), pwdText.getText().toString());
    }

    private void signup(String email, String password) {
        AppUser appUser = AppUser.builder().
                email(email).
                password(password).
                build();
        Gson gson = new Gson();

        StringRequest objectRequest = new StringRequest(
                Request.Method.POST,
                "http://10.0.2.2:8090/api/user/signup",
                response -> Toast.makeText(context, "Signup successful!", Toast.LENGTH_SHORT).show(),
                errorListener
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

package com.example.yeschefuserapp.context;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yeschefuserapp.R;

public class UserContext {
    private static final String EMAIL = "email";
    private static final String PWD = "password";

    private final SharedPreferences sharedPreferences;

    public UserContext(Context context) {
        this.sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference), Context.MODE_PRIVATE);
    }

    public void setUserInfo(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.putString(PWD, password);
        editor.apply();
    }

    public String getEmail() {
        return this.sharedPreferences.getString(EMAIL, "wrong");
    }

    public String getPwd() {
        return this.sharedPreferences.getString(PWD, "wrong");
    }
}

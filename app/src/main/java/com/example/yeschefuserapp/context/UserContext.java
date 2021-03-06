package com.example.yeschefuserapp.context;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yeschefuserapp.R;

public class UserContext {
    private static final String EMAIL = "email";
    private static final String OLD_PWD = "old password";
    private static final String PWD = "password";
    private static final String TOKEN = "token";

    private final SharedPreferences sharedPreferences;

    public UserContext(Context context) {
        this.sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference), Context.MODE_PRIVATE);
    }

    public void setUserInfo(String email, String password, String token, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.putString(OLD_PWD, password); //for changing the password
        if (isChecked) editor.putString(PWD, password); //for autologin
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public String getEmail() {
        return this.sharedPreferences.getString(EMAIL, "wrong");
    }

    public String getPwd() {
        return this.sharedPreferences.getString(PWD, "wrong");
    }

    public String getOldPwd() {
        return this.sharedPreferences.getString(OLD_PWD, "wrong");
    }

    public String getToken() {
        return this.sharedPreferences.getString(TOKEN, "token");
    }

    public void clearLoginPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

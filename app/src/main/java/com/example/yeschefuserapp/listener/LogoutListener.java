package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.yeschefuserapp.activity.LoginActivity;
import com.example.yeschefuserapp.context.UserContext;

public class LogoutListener implements View.OnClickListener {
    private Context context;
    private UserContext userContext;

    public LogoutListener(Context context) {
        this.context = context;
        userContext=new UserContext(context);
    }

    @Override
    public void onClick(View view) {
        userContext.clearLoginPreferences();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}

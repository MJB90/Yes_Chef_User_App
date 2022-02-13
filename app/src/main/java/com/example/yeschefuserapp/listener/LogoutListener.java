package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.yeschefuserapp.activity.LoginActivity;

public class LogoutListener implements View.OnClickListener {
    private Context context;

    public LogoutListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}

package com.example.yeschefuserapp.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.LoginActivity;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.model.AppUser;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

public class ForgetPasswordListener implements View.OnClickListener {
    private final Context context;
    private final String email;
    private final String otp;
    private final String newPassword;
    private final String reenterNewPassword;
    private final UserContext userContext;

    public ForgetPasswordListener(Context context, String email, String otp, String newPassword, String reenterNewPassword) {
        this.context = context;
        this.email = email;
        this.otp = otp;
        this.newPassword = newPassword;
        this.reenterNewPassword = reenterNewPassword;
        userContext = new UserContext(context);
    }

    @Override
    public void onClick(View view) {
        if (newPassword.equals(reenterNewPassword)) {
            AppUser appUser = AppUser.builder().
                    email(email).
                    password(newPassword).
                    build();
            Gson gson = new Gson();

            StringRequest objectRequest = new StringRequest(
                    Request.Method.POST,
                    context.getString(R.string.domain_name) + "api/user/forget_password/" + otp,
                    response -> {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        userContext.clearLoginPreferences();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    },
                    error -> Toast.makeText(context, "Unsuccessful", Toast.LENGTH_SHORT).show()
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
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));
            MySingleton.getInstance(context).addToRequestQueue(objectRequest);
        } else {
            Toast.makeText(view.getContext(), "The new password re-entered doesn't match the new password!", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.yeschefuserapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.listener.ChangePasswordListener;
import com.example.yeschefuserapp.listener.ForgetPasswordListener;
import com.example.yeschefuserapp.model.AppUser;
import com.example.yeschefuserapp.utility.MySingleton;
import com.google.gson.Gson;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText forgetEmail;
    private AlertDialog myPopUpChangePasswordDialog;
    private TextView oldPassword;
    private TextView newPassword;
    private TextView reenterNewPassword;
    private UserContext userContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        userContext = new UserContext(this);
        forgetEmail = findViewById(R.id.forget_email);
        Button confirmBtn = findViewById(R.id.confirm_button);

        if (forgetEmail != null && confirmBtn != null) {
            confirmBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.confirm_button) {
            String email = forgetEmail.getText().toString();
            AppUser appUser = AppUser.builder().
                    email(email).
                    password("").
                    build();
            Gson gson = new Gson();

            StringRequest objectRequest = new StringRequest(
                    Request.Method.POST,
                    getString(R.string.domain_name) + "api/user/forget_password",
                    response -> {
                        Toast.makeText(this, response,Toast.LENGTH_SHORT).show();
                        PopUpChangePassword(view);
                    },
                    error -> Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_SHORT).show()
            ){
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
            MySingleton.getInstance(view.getContext()).addToRequestQueue(objectRequest);
        } else if (id == R.id.confirm_btn) {
            ForgetPasswordListener changePasswordListener = new ForgetPasswordListener(view.getContext(),
                    userContext.getEmail(),
                    oldPassword.getText().toString(),
                    newPassword.getText().toString(),
                    reenterNewPassword.getText().toString());
            changePasswordListener.onClick(view);
        } else if (id == R.id.cancel_btn) {
            myPopUpChangePasswordDialog.dismiss();
        }
    }

    public void PopUpChangePassword(View view) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View myPopUpChangePassword = inflater.inflate(R.layout.pop_up_change_password, null, false);

        AlertDialog.Builder popUpBuilder = new AlertDialog.Builder(view.getContext());
        popUpBuilder.setView(myPopUpChangePassword);
        popUpBuilder.setCancelable(false);
        myPopUpChangePasswordDialog = popUpBuilder.create();

        TextView header = myPopUpChangePassword.findViewById(R.id.writeChangePasswordHead);
        header.setText(R.string.check_email);
        oldPassword = myPopUpChangePassword.findViewById(R.id.old_password);
        oldPassword.setHint("Verification code");
        newPassword = myPopUpChangePassword.findViewById(R.id.new_password);
        reenterNewPassword = myPopUpChangePassword.findViewById(R.id.renter_new_password);

        Button confirmBtn = myPopUpChangePassword.findViewById(R.id.confirm_btn);
        if (confirmBtn != null) {
            confirmBtn.setOnClickListener(this);
        }

        Button cancelBtn = myPopUpChangePassword.findViewById(R.id.cancel_btn);
        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(this);
        }
        myPopUpChangePasswordDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
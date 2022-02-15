package com.example.yeschefuserapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.yeschefuserapp.R;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText forgetEmail;
    Button confirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forgetEmail=findViewById(R.id.forget_email);
        confirmBtn=findViewById(R.id.confirm_button);


    }
}
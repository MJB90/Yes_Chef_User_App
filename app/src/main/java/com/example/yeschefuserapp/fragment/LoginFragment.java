package com.example.yeschefuserapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.ForgetPasswordActivity;
import com.example.yeschefuserapp.listener.LoginListener;

public class LoginFragment extends Fragment {
    Button loginBtn;
    EditText emailText;
    EditText passwordText;
    CheckBox rememberMe;
    TextView forgetPassword;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginBtn = view.findViewById(R.id.login_btn);
        emailText = view.findViewById(R.id.email);
        passwordText = view.findViewById(R.id.password);
        rememberMe = view.findViewById(R.id.remember_me);
        forgetPassword = view.findViewById(R.id.forget_password);

        //Auto login

        LoginListener listener = new LoginListener(this.getContext(), emailText, passwordText, rememberMe);
        loginBtn.setOnClickListener(listener);

        forgetPassword.setOnClickListener(view1 -> {
            Intent intent = new Intent(view1.getContext(), ForgetPasswordActivity.class);
            startActivity(intent);
        });
        return view;
    }

}
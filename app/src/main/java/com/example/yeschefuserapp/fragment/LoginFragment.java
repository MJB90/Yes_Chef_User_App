package com.example.yeschefuserapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.ForgetPasswordActivity;
import com.example.yeschefuserapp.activity.MainActivity;
import com.example.yeschefuserapp.context.UserContext;
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
        UserContext userContext = new UserContext(view.getContext());

        //Auto login
        if (userContext.getEmail().contains("EMAIL") && userContext.getPwd().contains("PWD") && userContext.getToken().contains("TOKEN")) {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        }
        LoginListener listener = new LoginListener(this.getContext(), emailText, passwordText, rememberMe);
        loginBtn.setOnClickListener(listener);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
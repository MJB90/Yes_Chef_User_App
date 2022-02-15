package com.example.yeschefuserapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.MainActivity;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.listener.LoginListener;

public class LoginFragment extends Fragment {
    Button loginBtn;
    EditText emailText;
    EditText passwordText;
    CheckBox rememberMe;

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

        // TODO: Add back remember me feature
        UserContext userContext=new UserContext(view.getContext());
        if (userContext.getEmail().contains("EMAIL") && userContext.getPwd().contains("PWD") && userContext.getToken().contains("TOKEN")) {
           boolean loginOk = logIn(userContext.getEmail(), userContext.getPwd(),userContext.getToken());
            if (loginOk) {
                startProtectedActivity(view);
           }
        }

        LoginListener listener = new LoginListener(this.getContext(), emailText, passwordText, rememberMe);
        loginBtn.setOnClickListener(listener);

        return view;
    }

    private boolean logIn(String email, String password, String token ){
        return false;
    }

    private void startProtectedActivity(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
    }

}
package com.example.yeschefuserapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.activity.MainActivity;

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
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        loginBtn=view.findViewById(R.id.login_btn);
        emailText=view.findViewById(R.id.email);
        passwordText=view.findViewById(R.id.password);
        rememberMe=view.findViewById(R.id.remember_me);

        SharedPreferences pref = getActivity().getSharedPreferences("user_credentials",view.getContext().MODE_PRIVATE);
        if (pref.contains("username") && pref.contains("password")) {
            boolean loginOk = logIn(pref.getString("username", "wrong"), pref.getString("password", "wrong"));
            if (loginOk) {
                startProtectedActivity(view);
            }
        }

        loginBtn.setOnClickListener(v -> {
            String username= emailText.getText().toString();
            String password=passwordText.getText().toString();
            if (logIn(username, password)) {
                if (rememberMe.isChecked()){
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("username",username);
                    editor.putString("password",password);
                    editor.commit();
                }
                Toast.makeText(view.getContext(),"Login successful!",Toast.LENGTH_SHORT).show();
                startProtectedActivity(view);
            }
            else{
                Toast.makeText(view.getContext(),"Credentials are not valid",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private boolean logIn(String username, String password) {
        return username.equals("") && password.equals("");
    }
    private void startProtectedActivity(View view){
        Intent intent= new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
    }

}
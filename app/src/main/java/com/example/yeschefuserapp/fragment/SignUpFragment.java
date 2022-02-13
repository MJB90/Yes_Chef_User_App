package com.example.yeschefuserapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.listener.SignupListener;

public class SignUpFragment extends Fragment {

    public SignUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        Button signupBtn = view.findViewById(R.id.signup_btn);
        SignupListener listener = new SignupListener(view.getContext(), view.findViewById(R.id.email), view.findViewById(R.id.password));
        signupBtn.setOnClickListener(listener);

        return view;
    }
}
package com.example.yeschefuserapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.listener.LogoutListener;

public class AccountFragment extends Fragment {
    private TextView userEmail;
    private Button logoutBtn;
    UserContext userContext;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        userContext = new UserContext(this.getContext());

        userEmail = view.findViewById(R.id.user_email);
        userEmail.setText(userContext.getEmail());

        logoutBtn = view.findViewById(R.id.logout_btn);
        LogoutListener logoutListener = new LogoutListener(this.getContext());
        logoutBtn.setOnClickListener(logoutListener);

        return view;
    }
}
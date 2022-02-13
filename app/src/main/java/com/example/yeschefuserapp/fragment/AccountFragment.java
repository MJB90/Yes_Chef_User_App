package com.example.yeschefuserapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.listener.LogoutListener;

public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Button logoutBtn = view.findViewById(R.id.logout_btn);
        LogoutListener logoutListener = new LogoutListener(this.getContext());
        logoutBtn.setOnClickListener(logoutListener);

        return view;
    }
}
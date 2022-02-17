package com.example.yeschefuserapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.yeschefuserapp.fragment.LoginFragment;
import com.example.yeschefuserapp.fragment.SignUpFragment;

public class LoginAdapter extends FragmentPagerAdapter {
    int totalTabs;

    public LoginAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new SignUpFragment();
        }
        return new LoginFragment();
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

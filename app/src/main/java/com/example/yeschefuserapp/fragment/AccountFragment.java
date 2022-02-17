package com.example.yeschefuserapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.yeschefuserapp.R;
import com.example.yeschefuserapp.context.UserContext;
import com.example.yeschefuserapp.listener.ChangePasswordListener;
import com.example.yeschefuserapp.listener.LogoutListener;
import com.google.android.material.card.MaterialCardView;

public class AccountFragment extends Fragment {
    private TextView userEmail;
    private MaterialCardView userPassword;
    private Button logoutBtn;
    private UserContext userContext;
    private AlertDialog myPopUpChangePasswordDialog;
    private TextView oldPassword;
    private TextView newPassword;
    private TextView reenterNewPassword;

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

        userPassword=view.findViewById(R.id.card_password_account);
        userPassword.setOnClickListener(v->{
            PopUpChangePassword(v);
        });


        logoutBtn = view.findViewById(R.id.logout_btn);
        LogoutListener logoutListener = new LogoutListener(this.getContext());
        logoutBtn.setOnClickListener(logoutListener);

        return view;
    }

    public void PopUpChangePassword(View view) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View myPopUpChangePassword = inflater.inflate(R.layout.pop_up_change_password, null, false);

        AlertDialog.Builder popUpBuilder = new AlertDialog.Builder(view.getContext());
        popUpBuilder.setView(myPopUpChangePassword);
        popUpBuilder.setCancelable(false);
        myPopUpChangePasswordDialog = popUpBuilder.create();

        oldPassword = myPopUpChangePassword.findViewById(R.id.old_password);
        newPassword = myPopUpChangePassword.findViewById(R.id.new_password);
        reenterNewPassword = myPopUpChangePassword.findViewById(R.id.renter_new_password);

        Button confirmBtn = myPopUpChangePassword.findViewById(R.id.confirm_btn);
        if (confirmBtn != null) {
            if (newPassword.getText().toString().equals(reenterNewPassword.getText().toString()) &&
                    oldPassword.getText().toString().equals(userContext.getPwd())){
                ChangePasswordListener changePasswordListener = new ChangePasswordListener(view.getContext(),
                        userContext.getEmail(),
                        newPassword.getText().toString());
                confirmBtn.setOnClickListener(changePasswordListener);
            }
            else if (! newPassword.getText().toString().equals(reenterNewPassword.getText().toString())){
                Toast.makeText(view.getContext(), "The new password reentered doesn't match the new password!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(view.getContext(), "The old password is incorrect!", Toast.LENGTH_SHORT).show();
            }
        }

        Button cancelBtn = myPopUpChangePassword.findViewById(R.id.cancel_btn);
        if (cancelBtn != null) {
            cancelBtn.setOnClickListener(v->{myPopUpChangePasswordDialog.dismiss();});
        }
        myPopUpChangePasswordDialog.show();
    }
}
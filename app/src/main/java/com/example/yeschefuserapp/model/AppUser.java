package com.example.yeschefuserapp.model;

import java.util.List;

import lombok.Data;

@Data
public class AppUser {
    public String id;
    public String email;
    public String password;
    public List<String> roles;
}

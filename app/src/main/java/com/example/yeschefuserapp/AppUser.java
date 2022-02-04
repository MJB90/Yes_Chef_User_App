package com.example.yeschefuserapp;

import java.util.List;

import lombok.Data;

@Data
public class AppUser {
    public String id;
    public String email;
    public String password;
    public List<String> roles;
}

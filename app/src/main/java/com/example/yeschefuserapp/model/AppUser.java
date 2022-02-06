package com.example.yeschefuserapp.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class AppUser implements Serializable {
    public String id;
    public String email;
    public String password;
    public List<String> roles;
}

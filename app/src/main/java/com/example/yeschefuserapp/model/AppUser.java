package com.example.yeschefuserapp.model;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUser implements Serializable {
    private String id;
    private String email;
    private String password;
    private List<String> roles;
}

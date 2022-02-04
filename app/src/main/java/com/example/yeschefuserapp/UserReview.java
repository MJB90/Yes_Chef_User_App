package com.example.yeschefuserapp;

import lombok.Data;

@Data
public class UserReview {
    private String email;
    private Integer rating;
    private String description;
}

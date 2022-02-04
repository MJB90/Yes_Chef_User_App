package com.example.yeschefuserapp.model;

import lombok.Data;

@Data
public class UserReview {
    private String email;
    private Integer rating;
    private String description;
}

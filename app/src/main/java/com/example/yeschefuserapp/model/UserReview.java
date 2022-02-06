package com.example.yeschefuserapp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserReview implements Serializable {
    private String email;
    private Integer rating;
    private String description;
}

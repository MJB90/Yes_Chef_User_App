package com.example.yeschefuserapp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserReview implements Serializable {
    private String userEmail;
    private Integer rating;
    private String description;
    //TODO deserialize date and make a utility deserializer
//    private Date reviewDate;

}

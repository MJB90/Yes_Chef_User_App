package com.example.yeschefuserapp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserReview implements Serializable {
    private String userEmail;
    private Integer rating;
    private String description;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

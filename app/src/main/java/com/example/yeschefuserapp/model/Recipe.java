package com.example.yeschefuserapp.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;


@Data
public class Recipe implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<String> imageURL;
    private List<String> cuisineType;
    private String courseType;
    private List<String> difficulty;
    private List<String> technique;
    private List<String> tags;
    private Integer prepTime;
    private Integer noOfServings;
    private Double calories;
    List<Ingredient> ingredients;
    private List<String> prepSteps;
    private List<UserReview> userReviews;

}

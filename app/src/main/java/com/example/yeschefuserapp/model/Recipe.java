package com.example.yeschefuserapp.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;


@Data
public class Recipe implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<String> imageUrl;
    private List<String> cuisineType;
    private String courseType;
    private String difficulty;
    private List<String> tags;
    private Integer prepTime;
    private Integer noOfServings;
    private Double calories;
    private List<Ingredient> ingredients;
    private List<String> prepSteps;
    private List<UserReview> userReviews;

}

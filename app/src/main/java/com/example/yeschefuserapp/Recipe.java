package com.example.yeschefuserapp;

import lombok.Data;

@Data
public class Recipe {
    private String id;
    private String recipeUrl;
    private String recipeName;
    private String recipePhoto;
    private String[] ingredients;
    private double ratings;
    private int reviews;
    private String cookTime;
    private int serve;
}

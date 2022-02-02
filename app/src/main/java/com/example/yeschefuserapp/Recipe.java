package com.example.yeschefuserapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe {
    private String id;
    private String recipeUrl;
    private String recipeName;
    private String recipePhoto;
    private String[] ingredients;
    private int ratings;
    private int reviews;
    private String cookTime;
    private int serve;

    public Recipe(String id, String recipeUrl, String recipeName, String recipePhoto, String[] ingredients, int ratings, int reviews, String cookTime, int serve) {
        this.id = id;
        this.recipeUrl = recipeUrl;
        this.recipeName = recipeName;
        this.recipePhoto = recipePhoto;
        this.ingredients = ingredients;
        this.ratings = ratings;
        this.reviews = reviews;
        this.cookTime = cookTime;
        this.serve = serve;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipePhoto() {
        return recipePhoto;
    }

    public void setRecipePhoto(String recipePhoto) {
        this.recipePhoto = recipePhoto;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public int getServe() {
        return serve;
    }

    public void setServe(int serve) {
        this.serve = serve;
    }
}

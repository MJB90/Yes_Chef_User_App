package com.example.yeschefuserapp;

import java.util.ArrayList;

public class Recipe {
    private String recipeName;
    private Integer recipePhoto;
    private ArrayList<Ingredient> ingredients;
    private int ratings;
    private int reviews;
    private String cookTime;
    private int serve;

    public Recipe(String recipeName, Integer recipePhoto, ArrayList<Ingredient> ingredients, int ratings, int reviews, String cookTime, int serve) {
        this.recipeName = recipeName;
        this.recipePhoto = recipePhoto;
        this.ingredients = ingredients;
        this.ratings = ratings;
        this.reviews = reviews;
        this.cookTime = cookTime;
        this.serve = serve;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Integer getRecipePhoto() {
        return recipePhoto;
    }

    public void setRecipePhoto(Integer recipePhoto) {
        this.recipePhoto = recipePhoto;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
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

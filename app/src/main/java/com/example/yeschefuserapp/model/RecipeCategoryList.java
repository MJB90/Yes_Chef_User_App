package com.example.yeschefuserapp.model;

import java.util.List;

import lombok.Data;

@Data
public class RecipeCategoryList {
    private String name;
    private List<Recipe> recipeList;
    private String url;
    public RecipeCategoryList(String name, List<Recipe> recipeList, String url){
        this.name=name;
        this.recipeList=recipeList;
        this.url=url;
    }
}

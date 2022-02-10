package com.example.yeschefuserapp.utility;

import com.example.yeschefuserapp.model.Recipe;

import java.util.List;

import lombok.Data;

@Data
public class RecommendedRecipesDescription {
    private String name;
    private List<Recipe> recommendedRecipeData;

    public RecommendedRecipesDescription(String name, List<Recipe> recommendedRecipeData){
        this.name=name;
        this.recommendedRecipeData=recommendedRecipeData;
    }
}

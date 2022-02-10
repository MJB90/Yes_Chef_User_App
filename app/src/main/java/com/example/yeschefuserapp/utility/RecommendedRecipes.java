package com.example.yeschefuserapp.utility;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class RecommendedRecipes {
    List<RecommendedRecipesDescription> recommendedRecipes;

    public RecommendedRecipes(List<RecommendedRecipesDescription> recommendedRecipes) {
        this.recommendedRecipes = recommendedRecipes;
    }
}

package com.example.yeschefuserapp.utility;

import com.example.yeschefuserapp.model.Recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AdvancedFilterTags {
    private Double maxCalories;
    private Integer maxPrepTime;
    private Integer maxNoServings;
    private List<String> tags;
    private List<String> difficulty;
    private List<String> courseType;
    private List<String> cuisineType;

    public AdvancedFilterTags(Double maxCalories, Integer maxPrepTime, Integer maxNoServings, List<String> tags, List<String> difficulty, List<String> courseType, List<String> cuisineType) {
        this.maxCalories = maxCalories;
        this.maxPrepTime = maxPrepTime;
        this.maxNoServings = maxNoServings;
        this.tags = tags;
        this.difficulty = difficulty;
        this.courseType = courseType;
        this.cuisineType = cuisineType;
    }

}

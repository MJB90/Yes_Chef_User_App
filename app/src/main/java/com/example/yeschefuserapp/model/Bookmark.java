package com.example.yeschefuserapp.model;

import com.example.yeschefuserapp.utility.LocalDataTimeGsonAdapter;
import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Bookmark {
    private String recipeId;
    @JsonAdapter(LocalDataTimeGsonAdapter.class)
    private LocalDateTime bookmarkDateTime;
}
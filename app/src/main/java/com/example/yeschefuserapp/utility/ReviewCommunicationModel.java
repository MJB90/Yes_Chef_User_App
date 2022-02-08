package com.example.yeschefuserapp.utility;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewCommunicationModel {
    private String recipeId;
    private String userEmail;
    private String description;
    private Integer rating;
}

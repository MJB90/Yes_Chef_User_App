package com.example.yeschefuserapp.model;

import com.example.yeschefuserapp.utility.LocalDataTimeGsonAdapter;
import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserReview implements Serializable {
    private String userEmail;
    private Integer rating;
    private String description;
    //TODO deserialize date and make a utility deserializer
    @JsonAdapter(LocalDataTimeGsonAdapter.class)
    private LocalDateTime reviewDateTime;

}

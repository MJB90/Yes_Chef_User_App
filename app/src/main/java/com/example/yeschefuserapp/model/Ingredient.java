package com.example.yeschefuserapp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Ingredient implements Serializable {
    private String ingredient;
    private Integer amount;
    private String unit;
}

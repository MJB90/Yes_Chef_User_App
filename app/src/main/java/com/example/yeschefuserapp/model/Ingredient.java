package com.example.yeschefuserapp.model;


import androidx.annotation.NonNull;

import java.io.Serializable;

import lombok.Data;

@Data
public class Ingredient implements Serializable {
    private String ingredient;
    private String amount;
    private String unit;
    private String comment;

    @NonNull
    @Override
    public String toString(){
        if(getComment() == null||getComment().trim().isEmpty()) {
            if(getUnit()==null||getUnit().trim().isEmpty())
                return getIngredient();
            else
            return getAmount() + " " + getUnit() + " of " + getIngredient();
        }
        else {
            if(getUnit()==null||getUnit().trim().isEmpty())
                return getIngredient();
            else
            return getAmount() + " " + getUnit() + " of " + getIngredient() + ", " + getComment();
        }
    }
}




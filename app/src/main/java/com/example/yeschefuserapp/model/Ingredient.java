package com.example.yeschefuserapp.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Ingredient implements Serializable {
    private String ingredient;
    private String amount;
    private String unit;
    private String comment;

    @Override
    public String toString(){
        if(getComment() == null||getComment().trim().isEmpty())
            return getAmount()+ " "+getUnit()+" of "+getIngredient();
        else
        return getAmount()+ " "+getUnit()+" of "+getIngredient()+", "+getComment();
    }
}




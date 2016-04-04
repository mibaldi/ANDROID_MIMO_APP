package com.android.mikelpablo.otakucook.Models;

/**
 * Created by mikelbalducieldiaz on 4/4/16.
 */
public class Ingredient {
    private long id;
    private String name;
    private Boolean frozen;
    private String category;
    private String baseType;


    public Ingredient() {}
    public Ingredient(String name, Boolean frozen,String category,String baseType) {
        this.name = name;
        this.frozen = frozen;
        this.category = category;
        this.baseType = baseType;
    }
}

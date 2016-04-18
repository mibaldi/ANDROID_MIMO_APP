package com.android.mikelpablo.otakucook.Ingredients.Models;

import com.android.mikelpablo.otakucook.Models.Ingredient;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by pabji on 17/04/2016.
 */
public class BaseIngredient implements ParentListItem{

    private List<Ingredient> ingredientList;
    private String baseName;

    public BaseIngredient(String baseName,List<Ingredient> ingredientList) {
        this.baseName = baseName;
        this.ingredientList = ingredientList;
    }

    public void addIngredient(Ingredient ingredient){
        ingredientList.add(ingredient);
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public List<Ingredient> getChildItemList() {
        return ingredientList;
    }




    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}

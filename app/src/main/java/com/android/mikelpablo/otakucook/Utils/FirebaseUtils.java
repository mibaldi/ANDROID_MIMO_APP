package com.android.mikelpablo.otakucook.Utils;

import com.android.mikelpablo.otakucook.Models.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikelbalducieldiaz on 21/4/16.
 */
public class FirebaseUtils {
    public static List<Ingredient> getIngredientsAvailablesRecipe(List<String> ids, List<Ingredient>ingredientList){
        List<Ingredient> recipeIngredientStorage = new ArrayList<>();
        for ( Ingredient ingredient: ingredientList){
            if (ids.contains(String.valueOf(ingredient.id))){
                recipeIngredientStorage.add(ingredient);
            }
        }
        return recipeIngredientStorage;
    }
}

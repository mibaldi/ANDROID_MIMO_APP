package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class MeasureFB {
    public String measure;
    public  float quantity;
    public long recipeId;
    public long  id;
    public long ingredientId;
    public MeasureFB (Measure measure,long recipeId){
        this.id = measure.id;
        this.measure = measure.measure;
        this.quantity = measure.quantity;
        this.recipeId = recipeId;
        this.ingredientId = measure.ingredient.id;
    }

}

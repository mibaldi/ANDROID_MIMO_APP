package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikelbalducieldiaz on 4/4/16.
 */
public class RecipeFB {
    public long id;
    public  String name;
    public int portions;
    public Boolean favorite;
    public String author;
    public int score;
    public String photo;
    public List<Long> measures= new ArrayList<>();
    public List<Long> tasks= new ArrayList<>();

    public RecipeFB (Recipe recipe){
        this.id = recipe.id;
        this.name = recipe.name;
        this.portions = recipe.portions;
        this.favorite = recipe.favorite;
        this.author = recipe.author;
        this.score = recipe.score;
        this.photo = recipe.photo;
        for (Measure measure: recipe.measureIngredients){
            measures.add(measure.id);
        }
        for (Task task: recipe.tasks){
            tasks.add(task.id);
        }
    }
}

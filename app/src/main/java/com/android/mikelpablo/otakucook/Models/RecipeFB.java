package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RecipeFB {
    public long id;
    public  String name;
    public int portions;
    public Boolean favorite;
    public String author;
    public int score;
    public String photo;

    public RecipeFB (Recipe recipe){
        this.id = recipe.id;
        this.name = recipe.name;
        this.portions = recipe.portions;
        this.favorite = recipe.favorite;
        this.author = recipe.author;
        this.score = recipe.score;
        this.photo = recipe.photo;
    }
}

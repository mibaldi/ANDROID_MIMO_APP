package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class TaskFB {
    public long id;
    public String name;
    public String photo;
    public int seconds;
    public long recipeId;
    public String description;

    public TaskFB (Task task,long recipeId){
        this.id = task.id;
        this.name = task.name;
        this.photo = task.photo;
        this.seconds = task.seconds;
        this.description = task.description;
        this.recipeId = recipeId;
    }
}

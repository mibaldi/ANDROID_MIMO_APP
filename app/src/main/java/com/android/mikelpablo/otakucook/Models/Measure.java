package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class Measure implements Parcelable {
    public String measure;
    public  int quantity;
    public long recipeId;
    public long  id;
    public Ingredient ingredient;
    public long ingredientId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.measure);
        dest.writeInt(this.quantity);
        dest.writeLong(this.recipeId);
        dest.writeLong(this.ingredientId);
    }

    public Measure() {
    }

    protected Measure(Parcel in) {
        this.measure = in.readString();
        this.quantity = in.readInt();
        this.recipeId = in.readLong();
        this.ingredientId = in.readLong();
    }

    public static final Parcelable.Creator<Measure> CREATOR = new Parcelable.Creator<Measure>() {
        @Override
        public Measure createFromParcel(Parcel source) {
            return new Measure(source);
        }

        @Override
        public Measure[] newArray(int size) {
            return new Measure[size];
        }
    };
}

package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mikelbalducieldiaz on 4/4/16.
 */
public class Ingredient implements Parcelable {
    public long id;
    public String name;
    public Boolean frozen;
    public String category;
    public String baseType;



    public Ingredient() {}
    public Ingredient(Long id,String name, Boolean frozen,String category,String baseType) {
        this.id = id;
        this.name = name;
        this.frozen = frozen;
        this.category = category;
        this.baseType = baseType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.frozen);
        dest.writeString(this.category);
        dest.writeString(this.baseType);
    }

    protected Ingredient(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.frozen = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.category = in.readString();
        this.baseType = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}

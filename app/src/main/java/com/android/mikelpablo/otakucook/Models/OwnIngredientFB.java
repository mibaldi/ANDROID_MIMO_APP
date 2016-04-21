package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mikelbalducieldiaz on 21/4/16.
 */
public class OwnIngredientFB implements Parcelable {
    public String id;
    public String shoppingcart;
    public String storage;

    public OwnIngredientFB(String id, String shoppingcart, String storage) {
        this.id = id;
        this.shoppingcart = shoppingcart;
        this.storage = storage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.shoppingcart);
        dest.writeString(this.storage);
    }

    protected OwnIngredientFB(Parcel in) {
        this.id = in.readString();
        this.shoppingcart = in.readString();
        this.storage = in.readString();
    }

    public static final Parcelable.Creator<OwnIngredientFB> CREATOR = new Parcelable.Creator<OwnIngredientFB>() {
        @Override
        public OwnIngredientFB createFromParcel(Parcel source) {
            return new OwnIngredientFB(source);
        }

        @Override
        public OwnIngredientFB[] newArray(int size) {
            return new OwnIngredientFB[size];
        }
    };
}

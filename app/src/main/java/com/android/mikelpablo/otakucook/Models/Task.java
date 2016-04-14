package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class Task implements Parcelable,Comparable<Task> {
    public long id;
    public long idServer;
    public String name;
    public String photo;
    public int seconds;
    public long recipeId;
    public String description;

    public Task() {
    }

    @Override
    public int compareTo(Task another) {
        return this.name.compareTo(another.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.idServer);
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeInt(this.seconds);
        dest.writeLong(this.recipeId);
        dest.writeString(this.description);
    }

    protected Task(Parcel in) {
        this.id = in.readLong();
        this.idServer = in.readLong();
        this.name = in.readString();
        this.photo = in.readString();
        this.seconds = in.readInt();
        this.recipeId = in.readLong();
        this.description = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}

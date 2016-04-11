package com.android.mikelpablo.otakucook.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mikelbalducieldiaz on 9/4/16.
 */
public class Task implements Parcelable {
    public long id;
    public long idServer;
    public String name;
    public String photo;
    public int seconds;
    public long recipeId;
    public String taskDescription;

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
        dest.writeString(this.taskDescription);
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.id = in.readLong();
        this.idServer = in.readLong();
        this.name = in.readString();
        this.photo = in.readString();
        this.seconds = in.readInt();
        this.recipeId = in.readLong();
        this.taskDescription = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
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

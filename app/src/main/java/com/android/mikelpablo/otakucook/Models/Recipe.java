package com.android.mikelpablo.otakucook.Models;

import java.util.List;

/**
 * Created by mikelbalducieldiaz on 4/4/16.
 */
public class Recipe {
    public long id;
    public long idServer;
    public  String name;
    public int portions;
    public Boolean favorite;
    public String author;
    public int score;
    public String photo;
    private List<Measure> measures;
    private List<Task> tasks;

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}

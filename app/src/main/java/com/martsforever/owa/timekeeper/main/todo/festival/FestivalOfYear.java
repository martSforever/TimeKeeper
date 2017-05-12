package com.martsforever.owa.timekeeper.main.todo.festival;

/**
 * Created by OWA on 2017/5/11.
 */

public class FestivalOfYear {
    private String name;
    private String startTime;

    public FestivalOfYear() {
    }

    public FestivalOfYear(String name, String startTime) {
        this.name = name;
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "FestivalOfYear{" +
                "name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}

package com.geniusmind.malek.clevermind.Model;

public class LatestEventInformation {
    private String title;
    private String days;
    private String date;
    private String time;

    public LatestEventInformation(String title,String days,String date,String time){
        this.title=title;
        this.days=days;
        this.date=date;
        this.time=time;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDays() {
        return days;
    }
}

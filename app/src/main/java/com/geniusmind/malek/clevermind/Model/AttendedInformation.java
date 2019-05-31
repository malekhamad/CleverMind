package com.geniusmind.malek.clevermind.Model;

public class AttendedInformation {
    private String id;
    private String title;
    private String days;
    private String time;
    private String url;
    // create constructor with multiple parameters to pass arguments . . . ;
    public AttendedInformation(String id, String caption, String days, String time, String url) {
        this.id = id;
        this.title = caption;
        this.days = days;
        this.time = time;
        this.url = url;

    }

    // getter methods . . . ;
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDays() {
        return days;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }


}

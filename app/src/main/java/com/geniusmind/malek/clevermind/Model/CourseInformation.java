package com.geniusmind.malek.clevermind.Model;

public class CourseInformation {
    private String id;
    private String title;
    private String days;
    private String time;
    private String url;
    private String courseInfo;
    private int price;
    private boolean isPublic;
    private boolean isAttended;
    // create constructor with multiple parameters to pass arguments . . . ;
    public CourseInformation(String id, String caption, String days, String time, String url, String courseInfo, int price, boolean isPublic, boolean isAttended) {
        this.id = id;
        this.title = caption;
        this.days = days;
        this.time = time;
        this.url = url;
        this.courseInfo=courseInfo;
        this.price=price;
        this.isPublic=isPublic;
        this.isAttended=isAttended;

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

    public String getCourseInfo() {
        return courseInfo;
    }

    public int getPrice() {
        return price;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isAttended() {
        return isAttended;
    }
}

package com.geniusmind.malek.clevermind.Model;

public class NewsInformation {
    private String title ;
    private String subtitle;
    private String description;
    public String img_url;

    public NewsInformation(String title, String subtitle, String description, String img_url){
        this.title=title;
        this.subtitle=subtitle;
        this.description=description;
        this.img_url=img_url;
    }
    public String getTitle(){
        return title;
    }
    public String getSubtitle(){
        return subtitle;
    }
    public String getDescription(){
        return description;
    }
    public String getImg_url(){
        return img_url;
    }
}

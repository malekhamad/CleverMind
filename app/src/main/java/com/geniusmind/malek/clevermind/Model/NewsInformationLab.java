package com.geniusmind.malek.clevermind.Model;

import java.util.ArrayList;
import java.util.List;

public class NewsInformationLab {
    private static NewsInformationLab sNewsInformationLab;
    private List<NewsInformation> list;


    private NewsInformationLab(){
        list=new ArrayList<>();
    }

    public synchronized static NewsInformationLab getInstance(){
        if(sNewsInformationLab==null){
            return sNewsInformationLab=new NewsInformationLab();
        } return sNewsInformationLab;
    }
    // put all news inside list  . . . ;
    public void putNews(List<NewsInformation>list){
        this.list.addAll(list);
    }
    // get all objects . . . . ;
    public List<NewsInformation> get(){
        return this.list;
    }

    // delete all items in list . . . ;
    public void deleteNews(){
        list.clear();
    }





}

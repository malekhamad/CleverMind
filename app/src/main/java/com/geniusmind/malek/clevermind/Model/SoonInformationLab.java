package com.geniusmind.malek.clevermind.Model;

import java.util.ArrayList;
import java.util.List;

public class SoonInformationLab {
    private static SoonInformationLab sInformatoinLab;
    private List<String> list;


    private SoonInformationLab(){
        list=new ArrayList<>();
    }

    public synchronized static SoonInformationLab getInstance(){
        if(sInformatoinLab==null){
            return sInformatoinLab=new SoonInformationLab();
        } return sInformatoinLab;
    }
    // put all news inside list  . . . ;
    public void putSoonInfo(List<String>list){
        this.list.addAll(list);
    }
    // get all objects . . . . ;
    public List<String> get(){
        return this.list;
    }

    // delete all items in list . . . ;
    public void deleteSoonInfo(){
        list.clear();
    }


}

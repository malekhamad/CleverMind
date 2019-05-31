package com.geniusmind.malek.clevermind.Model;

import java.util.ArrayList;
import java.util.List;

public class LatestEventInformationLab {
    private static LatestEventInformationLab sInformationLab;
    private List<LatestEventInformation> eventList;

    private LatestEventInformationLab(){
        eventList=new ArrayList<>();

    }

    public synchronized static LatestEventInformationLab getInstance(){
        if(sInformationLab==null){
            return sInformationLab=new LatestEventInformationLab();
        }else return sInformationLab;
    }

    // get all objects . . . . ;
    public List<LatestEventInformation> get(){
        return this.eventList;
    }

    public void setEventList(List<LatestEventInformation>eventList){
        this.eventList=eventList;
    }

    //delete all element in jobsList . . . ; ;
    public void deleteJobs(){
        eventList.clear();
    }
}

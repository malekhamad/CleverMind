package com.geniusmind.malek.clevermind.Model;

import java.util.ArrayList;
import java.util.List;

public class AttendedInformationLab {
    private static AttendedInformationLab sAttendedInformationLab;
    private List<AttendedInformation> attendedList;

    private AttendedInformationLab(){
        attendedList=new ArrayList<>();
    }

    public synchronized static AttendedInformationLab getInstance(){
        if(sAttendedInformationLab==null){
            return sAttendedInformationLab=new AttendedInformationLab();
        } return sAttendedInformationLab;
    }

    // get all objects . . . . ;
    public List<AttendedInformation> get(){
        return this.attendedList;
    }

    public void setAttendedList(List<AttendedInformation>attendedList){
        this.attendedList.addAll(attendedList);
    }
    //delete all element in jobsList . . . ; ;
    public void deleteAttended(){
        attendedList.clear();
    }

}

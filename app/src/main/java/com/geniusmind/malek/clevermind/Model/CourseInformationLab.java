package com.geniusmind.malek.clevermind.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class CourseInformationLab {
    private static CourseInformationLab courseInfo;
    Context mContext;
   private List<CourseInformation> list;

    private CourseInformationLab(){
         list =new ArrayList<>();
    }
    // to create instance for trainingInfoLab . . . ;
    public synchronized static CourseInformationLab getInstance(){
        if(courseInfo ==null) {
           return courseInfo =new CourseInformationLab();
        }
            return courseInfo;
    }

    // to get all variable from list . . . ;
    public List<CourseInformation> get(){
        return list;
    }
    // put all courses in array list . . . ;
    public void putCourses(List<CourseInformation>list){
        this.list.addAll(list);
    }

    // delete all course from list . . . .;
    public void deleteCourses(){
        this.list.clear();
    }

    // to get list by id . . .  ;
    public CourseInformation get(String id){
        for(CourseInformation information: list)
            if(information.getId().equals(id)){
               return information;}
               return null;


    }

}

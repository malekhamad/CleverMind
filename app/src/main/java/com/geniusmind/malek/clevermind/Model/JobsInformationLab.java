package com.geniusmind.malek.clevermind.Model;

import java.util.ArrayList;
import java.util.List;

public class JobsInformationLab {

    private static JobsInformationLab sJobsInformationLab;
    private List<JobsInformation>jobsList;

    private JobsInformationLab(){
        jobsList=new ArrayList<>();
    }

    public synchronized static JobsInformationLab getInstance(){
        if(sJobsInformationLab==null){
           return sJobsInformationLab=new JobsInformationLab();
        }else return sJobsInformationLab;
    }

    // get all objects . . . . ;
    public List<JobsInformation> get(){
        return this.jobsList;
    }

    public void setJobsList(List<JobsInformation>jobsList){
        this.jobsList=jobsList;
    }
    // get one object . . . ;
    public JobsInformation get(String id){
        for(JobsInformation information:jobsList)
            if(information.getJobsId().equals(id)){
                return information;}
        return null;
    }

    //delete all element in jobsList . . . ; ;
    public void deleteJobs(){
        jobsList.clear();
    }
}

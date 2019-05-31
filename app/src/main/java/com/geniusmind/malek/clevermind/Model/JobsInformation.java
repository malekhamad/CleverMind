package com.geniusmind.malek.clevermind.Model;

public class JobsInformation  {
    private String jobsId;
    private String jobsImageUrl;
    private String jobsTitle;
    private String jobsDescription;

    public JobsInformation(String jobsId, String jobsImageUrl, String jobsTitle, String jobsDescription) {
        this.jobsId = jobsId;
        this.jobsImageUrl = jobsImageUrl;
        this.jobsTitle = jobsTitle;
        this.jobsDescription = jobsDescription;
    }

    public String getJobsId() {
        return jobsId;
    }

    public String getJobsImageUrl() {
        return jobsImageUrl;
    }

    public String getJobsTitle() {
        return jobsTitle;
    }

    public String getJobsDescription() {
        return jobsDescription;
    }
}

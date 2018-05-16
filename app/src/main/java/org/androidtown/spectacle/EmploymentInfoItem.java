package org.androidtown.spectacle;

//채용정보 아이템 자료형
public class EmploymentInfoItem {
    String companyName, endDate, jobType, jobInfo;

    public EmploymentInfoItem() {
    }

    public EmploymentInfoItem(String companyName, String endDate, String jobType, String jobInfo) {
        this.companyName = companyName;
        this.endDate = endDate;
        this.jobType = jobType;
        this.jobInfo = jobInfo;
    }

}

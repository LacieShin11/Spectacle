package org.drawcoding.spectacle;

//채용정보 아이템 자료형
public class EmploymentInfoItem {
    String companyName, endDate, jobCategory, jobInfo, location, link, experience;

    public EmploymentInfoItem() {
        this.companyName = "";
        this.endDate = "";
        this.jobCategory = "";
        this.jobInfo = "";
        this.location = "";
        this.link = "";
        this.experience = "";
    }

    public EmploymentInfoItem(String companyName, String endDate, String jobCategory, String jobInfo, String location, String link, String experience) {
        this.companyName = companyName;
        this.endDate = endDate;
        this.jobCategory = jobCategory;
        this.jobInfo = jobInfo;
        this.location = location;
        this.link = link;
        this.experience = experience;
    }
}

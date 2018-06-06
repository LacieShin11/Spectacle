package org.androidtown.spectacle;

public class ListVO {
    private String category;
    private String projectName;
    private String date;
    private int contentID;

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setContentID(int contentID) {this.contentID = contentID;}
    public int getContentID() {return contentID;}
}

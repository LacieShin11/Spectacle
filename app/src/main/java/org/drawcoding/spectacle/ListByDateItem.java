package org.drawcoding.spectacle;

public class ListByDateItem {
    String categoryName, specName, date;
    int contentID;

    public ListByDateItem() {
    }

    public ListByDateItem(String categoryName, String specName, String date, int contentID) {
        this.categoryName = categoryName;
        this.specName = specName;
        this.date = date;
        this.contentID = contentID;
    }

    public int getContentID() {
        return contentID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }
}


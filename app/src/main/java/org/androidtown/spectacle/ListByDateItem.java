package org.androidtown.spectacle;

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
}


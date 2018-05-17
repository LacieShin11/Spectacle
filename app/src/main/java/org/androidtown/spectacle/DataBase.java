package org.androidtown.spectacle;

import android.provider.BaseColumns;

public final class DataBase {
    public static final class CreateDB implements BaseColumns{
        public static final String CONTENT_ID = "contentId";
        public static final String CATEGORY = "category";
        public static final String ACTIVITYNAME = "activityName";
        public static final String ACTIVITYCONTENT = "activityContent";
        public static final String STARTDATE = "startDate";
        public static final String ENDDATE = "endDate";
        public static final String IMAGE = "image";
        public static final String _TABLENAME = "CONTENTTABLE";
        public static final String _CREATE =
                "create Table "+_TABLENAME + "(" + CONTENT_ID +" Integer primary key autoincrement, "
                        +CATEGORY+" string not null , "
                        +ACTIVITYNAME+" string not null, "
                        +ACTIVITYCONTENT+ " string , "
                        +STARTDATE + " string not null, "
                        +ENDDATE + " string not null, "
                        +IMAGE + " string); ";
    }
}

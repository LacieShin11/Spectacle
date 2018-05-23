package org.androidtown.spectacle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper {
    private static final String DATABASE_NAME = "spectacle.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {
        //생성자
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //최초 DB를 만들 때 한 번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBase.CreateDB._CREATE);
        }

        //버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // db.execSQL("DROP TABLE IF EXIST "+DataBase.CreateDB._TABLENAME);
            //onCreate(db);
        }
    }

    public DbOpenHelper(Context context) {
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDB.close();
    }

    public long insertColumn(String category, String activityName, String activityContent, String startDate, String endDate, String image) {
        ContentValues values = new ContentValues();
        values.put(DataBase.CreateDB.CATEGORY, category);
        values.put(DataBase.CreateDB.ACTIVITYNAME, activityName);
        values.put(DataBase.CreateDB.ACTIVITYCONTENT, activityContent);
        values.put(DataBase.CreateDB.STARTDATE, startDate);
        values.put(DataBase.CreateDB.ENDDATE, endDate);
        values.put(DataBase.CreateDB.IMAGE, image);
        return mDB.insert(DataBase.CreateDB._TABLENAME, null, values);
    }

    public void displayColumn() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ", null);
        while (c.moveToNext()){
            String category = c.getString(c.getColumnIndex("category"));
            String title = c.getString(c.getColumnIndex("activityName"));
            String content = c.getString(c.getColumnIndex("activityContent"));

            Log.i("column 내용 ", category + " / "  + title + " / " + content );
        }
    }
    //listView에 표시할 값 얻기
    public String[] getTitle() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] titles = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            titles[i] = c.getString(c.getColumnIndex("activityName"));
            i++;
        }
        return titles;
    }
    public String[] getDate() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] date = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            date[i] = c.getString(c.getColumnIndex("startDate"));
            i++;
        }
        return date;
    }
    public String[] getCategory() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] categories = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            categories[i] = c.getString(c.getColumnIndex("category"));
            i++;
        }
        return categories;
    }
    //카테고리 탭 리스트에 필요한 메소드
    public String[] getListViewTitle(String selectCategory) {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE", null);
        if(selectCategory == "교내활동") {
             c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '교내활동' ORDER BY STARTDATE", null);
        } else if(selectCategory == "외부활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '대외활동' ORDER BY STARTDATE", null);
        } else if(selectCategory == "어학") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '어학' ORDER BY STARTDATE", null);
        } else if(selectCategory == "자격증") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '자격증' ORDER BY STARTDATE", null);
        } else if(selectCategory == "인턴") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '인턴&알바' ORDER BY STARTDATE", null);
        } else if(selectCategory == "봉사") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '봉사활동' ORDER BY STARTDATE", null);
        }

        String[] titles = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            titles[i] = c.getString(c.getColumnIndex("activityName"));
            i++;
        }
        return titles;
    }
    public String[] getListViewDate(String selectCategory) {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE", null);
        if(selectCategory == "교내활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '교내활동' ORDER BY STARTDATE", null);
        } else if(selectCategory == "외부활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '대외활동' ORDER BY STARTDATE", null);
        } else if(selectCategory == "어학") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '어학' ORDER BY STARTDATE", null);
        } else if(selectCategory == "자격증") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '자격증' ORDER BY STARTDATE", null);
        } else if(selectCategory == "인턴") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '인턴&알바' ORDER BY STARTDATE", null);
        } else if(selectCategory == "봉사") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '봉사활동' ORDER BY STARTDATE", null);
        }
        String[] date = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            date[i] = c.getString(c.getColumnIndex("startDate"));
            i++;
        }
        return date;
    }
    public String[] getListViewcategory(String selectCategory) {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE", null);
        if(selectCategory == "교내활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '교내활동' ORDER BY STARTDATE", null);
        } else if(selectCategory == "외부활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '대외활동' ORDER BY STARTDATE", null);
        } else if(selectCategory == "어학") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '어학' ORDER BY STARTDATE", null);
        } else if(selectCategory == "자격증") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '자격증' ORDER BY STARTDATE", null);
        } else if(selectCategory == "인턴") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '인턴&알바' ORDER BY STARTDATE", null);
        } else if(selectCategory == "봉사") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '봉사활동' ORDER BY STARTDATE", null);
        }
        String[] category = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            category[i] = c.getString(c.getColumnIndex("category"));
            i++;
        }
        return category;
    }

    public void deleteTable() { mDB.delete("CONTENTTABLE", null, null); }
}
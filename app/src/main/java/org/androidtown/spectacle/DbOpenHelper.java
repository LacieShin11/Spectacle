package org.androidtown.spectacle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static org.androidtown.spectacle.DataBase.CreateDB.CONTENT_ID;
import static org.androidtown.spectacle.DataBase.CreateDB._TABLENAME;

public class DbOpenHelper {
    private static final String DATABASE_NAME = "spectacle.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    public static SQLiteDatabase rDB;
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
            db.execSQL(DataBase.CreateDB._CREATE);//테이블 생성
        }

        //버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // db.execSQL("DROP TABLE IF EXIST "+DataBase.CreateDB._TABLENAME);
            //onCreate(db);
        }//초기화할 때 호출한다고 함. 테이블을 삭제하고 새로 생성하는 역할
    }

    public DbOpenHelper(Context context) {
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();//DB를 쓰기버전으로 데려옴
        rDB = mDBHelper.getReadableDatabase();//DB를 읽기버전으로 데려옴
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
        while (c.moveToNext()) {
            String category = c.getString(c.getColumnIndex("category"));
            String title = c.getString(c.getColumnIndex("activityName"));
            String content = c.getString(c.getColumnIndex("activityContent"));

            Log.i("\ncolumn 내용 ", category + " | " + title + " | " + content);
        }//커서 객체 이용해서 테이블의 각 카테코리, 타이틀, 컨텐트 내용 가져오기
        //테이블의 전체 내용 조회하는 코드-한 행씩 읽어나가며 마지막 행 출력후에는 빠져나옴
    }

    public Cursor getTable() {
        // Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        Cursor res = rDB.rawQuery("select * from CONTENTTABLE ORDER BY STARTDATE", null);
        return res;
    }//**엑셀용-테이블 전체 가져오기


    //listView에 표시할 값 얻기
    public String[] getTitle() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] titles = new String[c.getCount()];
        int i = 0;

        while (c.moveToNext()) {
            titles[i] = c.getString(c.getColumnIndex("activityName"));
            i++;
        }
        return titles;
    }//테이블에 제목 데이터는 STARTDATE로 정렬해서 titles배열에 저장

    public String[] getContent() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] contents = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            contents[i] = c.getString(c.getColumnIndex("activityContent"));
            i++;
        }
        return contents;
    }//테이블에 제목 데이터는 STARTDATE로 정렬해서 contents배열에 저장

    public String[] getID() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ", null);
        String[] ids = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            ids[i] = c.getString(c.getColumnIndex("contentId"));
            i++;
        }
        return ids;
    }//테이블에 id 데이터는 STARTDATE로 정렬해서 ids배열에 저장-엑셀용

    public int[] getContentID() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE", null);
        int[] contentID = new int[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            contentID[i] = c.getInt(c.getColumnIndex("contentId"));
            i++;
        }
        return contentID;
    }//테이블 id 데이터 - 예전에는 select * from CONTENTTABLE ORDER BY STARTDATE였고 getID였음

    public String[] getDate() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] date = new String[c.getCount()];
        int i = 0;

        while (c.moveToNext()) {
            String startD = c.getString(c.getColumnIndex("startDate"));
            String endD = c.getString(c.getColumnIndex("endDate"));

            if (startD.equals(endD))
                date[i] = c.getString(c.getColumnIndex("startDate"));
            else
                date[i] = c.getString(c.getColumnIndex("startDate")) + " ~ " + c.getString(c.getColumnIndex("endDate"));
            i++;
        }
        return date;
    } //getDate, getDate2로 나눠서 startDate와 endDate를 받아오려고 했지만 합쳐짐

    public String[] getDate1() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] date = new String[c.getCount()];//테이블 행 개수만큼 배열공간 생성
        int i = 0;
        while(c.moveToNext()) {
            date[i] = c.getString(c.getColumnIndex("startDate"));
            i++;
        }
        return date;
    }//테이블에 날짜 데이터는 STARTDATE로 정렬해서 date배열에 저장

    public String[] getDate2() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] date2 = new String[c.getCount()];//테이블 행 개수만큼 배열공간 생성
        int i = 0;
        while(c.moveToNext()) {
            date2[i] = c.getString(c.getColumnIndex("endDate"));
            i++;
        }
        return date2;
    }//테이블에 날짜 데이터는 STARTDATE로 정렬해서 date2배열에 저장

    public String[] getImage() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] images = new String[c.getCount()];//테이블 행 개수만큼 배열공간 생성
        int i = 0;
        while(c.moveToNext()) {
            images[i] = c.getString(c.getColumnIndex("image"));
            i++;
        }
        return images;
    }//테이블에 날짜 데이터는 STARTDATE로 정렬해서 images배열에 저장

    public String[] getCategory() {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE ", null);
        String[] categories = new String[c.getCount()];
        int i = 0;

        while (c.moveToNext()) {
            categories[i] = c.getString(c.getColumnIndex("category"));
            i++;
        }
        return categories;
    }//테이블에 카테고리 데이터는 STARTDATE로 정렬해서 categories배열에 저장

    public String[] getSelectedRow(int content_id) {
        String selectedTitle = "";
        String selectedCategory = "";
        String selectedStartDate = "";
        String selectedEndDate = "";
        String selectedContent = "";
        String sql = "SELECT * FROM " + _TABLENAME + " WHERE " + CONTENT_ID + "='" + content_id + "';";
        Cursor row = mDB.rawQuery(sql, null);

        if (row.getCount() > 0)
            while (row.moveToNext()) {
                selectedTitle = row.getString(row.getColumnIndex("activityName"));
                selectedCategory = row.getString(row.getColumnIndex("category"));
                selectedStartDate = row.getString(row.getColumnIndex("startDate"));
                selectedEndDate = row.getString(row.getColumnIndex("endDate"));
                selectedContent = row.getString(row.getColumnIndex("activityContent"));
            }

        String[] selectedRow = {selectedTitle, selectedCategory, selectedStartDate, selectedEndDate, selectedContent};

        return selectedRow;
    }//선택된 행의 데이터 전체를 가져오는 메소드-새로 추가된 듯

    //카테고리 탭 리스트에 필요한 메소드
    public String[] getListViewTitle(String selectCategory) {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE", null);
        if (selectCategory == "교내활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '교내활동' ORDER BY STARTDATE", null);
        } else if (selectCategory == "외부활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '대외활동' ORDER BY STARTDATE", null);
        } else if (selectCategory == "어학") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '어학' ORDER BY STARTDATE", null);
        } else if (selectCategory == "자격증") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '자격증' ORDER BY STARTDATE", null);
        } else if (selectCategory == "인턴") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '인턴&알바' ORDER BY STARTDATE", null);
        } else if (selectCategory == "봉사") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '봉사활동' ORDER BY STARTDATE", null);
        }

        String[] titles = new String[c.getCount()];
        int i = 0;
        while (c.moveToNext()) {
            titles[i] = c.getString(c.getColumnIndex("activityName"));
            i++;
        }
        //titles라는 배열에 activityName 컬럼의 데이터를 차곡차곡 저장
        return titles;
    }

    public String[] getListViewDate(String selectCategory) {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE", null);
        if (selectCategory == "교내활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '교내활동' ORDER BY STARTDATE", null);
        } else if (selectCategory == "외부활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '대외활동' ORDER BY STARTDATE", null);
        } else if (selectCategory == "어학") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '어학' ORDER BY STARTDATE", null);
        } else if (selectCategory == "자격증") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '자격증' ORDER BY STARTDATE", null);
        } else if (selectCategory == "인턴") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '인턴&알바' ORDER BY STARTDATE", null);
        } else if (selectCategory == "봉사") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '봉사활동' ORDER BY STARTDATE", null);
        }
        String[] date = new String[c.getCount()];
        int i = 0;
        while (c.moveToNext()) {
            date[i] = c.getString(c.getColumnIndex("startDate"));
            i++;
        }
        return date;
    }

    public String[] getListViewcategory(String selectCategory) {
        Cursor c = mDB.rawQuery("Select * from CONTENTTABLE ORDER BY STARTDATE", null);
        if (selectCategory == "교내활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '교내활동' ORDER BY STARTDATE", null);
        } else if (selectCategory == "외부활동") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '대외활동' ORDER BY STARTDATE", null);
        } else if (selectCategory == "어학") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '어학' ORDER BY STARTDATE", null);
        } else if (selectCategory == "자격증") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '자격증' ORDER BY STARTDATE", null);
        } else if (selectCategory == "인턴") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '인턴&알바' ORDER BY STARTDATE", null);
        } else if (selectCategory == "봉사") {
            c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '봉사활동' ORDER BY STARTDATE", null);
        }
        String[] category = new String[c.getCount()];
        int i = 0;
        while (c.moveToNext()) {
            category[i] = c.getString(c.getColumnIndex("category"));
            i++;
        }
        return category;
    }

    public int[] getListViewID(String selectCategory) {
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

        int[] contentID = new int[c.getCount()];
        int i = 0;
        while(c.moveToNext()) {
            contentID[i] = c.getInt(c.getColumnIndex("contentId"));
            i++;
        }
        return contentID;
    }

    public void delete(int content_id) {
        String sql = "DELETE FROM " + _TABLENAME + " WHERE " + CONTENT_ID + "='" + content_id + "';";
        mDB.execSQL(sql);
    }


    public void deleteTable() {
        mDB.delete("CONTENTTABLE", null, null);
    }//DB테이블 삭제

    //통계에 필요한 메소드
    public int[] getSpecCount() {
        Cursor c;
        c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '교내활동'", null);
        int campusCount = c.getCount();
        c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '대외활동'", null);
        int internationalCount = c.getCount();
        c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '인턴&알바'", null);
        int internCount = c.getCount();
        c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '봉사활동'", null);
        int volunteerCount = c.getCount();
        c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '어학'", null);
        int languageCount = c.getCount();
        c = mDB.rawQuery("Select * from CONTENTTABLE WHERE CATEGORY = '자격증'", null);
        int certificateCount = c.getCount();


        int[] count = {campusCount, internationalCount, internCount, volunteerCount, languageCount, certificateCount};

        c.close();
        return count;
    }

}
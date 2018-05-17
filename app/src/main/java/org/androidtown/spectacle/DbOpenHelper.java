package org.androidtown.spectacle;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {
    private static final String DATABASE_NAME ="spectacle.db";
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
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
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

    public long insertColumn(String category, String activityName, String activityContent, String startDate, String endDate, String image){
        ContentValues values = new ContentValues();
        values.put(DataBase.CreateDB.CATEGORY, category);
        values.put(DataBase.CreateDB.ACTIVITYNAME, activityName);
        values.put(DataBase.CreateDB.ACTIVITYCONTENT, activityContent);
        values.put(DataBase.CreateDB.STARTDATE, startDate);
        values.put(DataBase.CreateDB.ENDDATE, endDate);
        values.put(DataBase.CreateDB.IMAGE, image);
        return mDB.insert(DataBase.CreateDB._TABLENAME, null, values);
    }
}

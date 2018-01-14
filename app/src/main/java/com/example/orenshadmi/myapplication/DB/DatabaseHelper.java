package com.example.orenshadmi.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "LEADERBOARD.db";
    public static final String TABLE_NAME = "LEADER_BOARD_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "SCORE";
    public static final String COL_4 = "GAME_LEVEL";
    public static final String COL_5= "LOCATION";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,SCORE INTEGER,GAME_LEVEL INTEGER, LOCATION TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String score, String gameLevel, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, score);
        contentValues.put(COL_4, gameLevel);
        contentValues.put(COL_5, location);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getDataByGameLevel(String gameLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM LEADER_BOARD_table WHERE GAME_LEVEL = ? ORDER BY SCORE DESC LIMIT 10", new String[] {gameLevel});



        return res;
    }


//    public boolean updateData(String id, String name, String surname, String mark){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_1, id);
//        contentValues.put(COL_2, name);
//        contentValues.put(COL_3, surname);
//        contentValues.put(COL_4, mark);
//        db.update(TABLE_NAME,contentValues,"ID = ?",new String[] { id });
//        return true;
//    }

    public int deleteDate(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
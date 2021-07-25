package com.aosproject.dailyone.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryHelper extends SQLiteOpenHelper {

    public  DiaryHelper(Context context) { super(context, "DiaryHelper.db", null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE diarydata(id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, emoji INT, date TEXT not null DEFAULT (datetime('now', 'localtime')));";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS diarydata";
        db.execSQL(query);
        onCreate(db);
    }
}

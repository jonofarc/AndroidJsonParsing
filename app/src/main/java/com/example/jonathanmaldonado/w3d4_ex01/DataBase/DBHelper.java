package com.example.jonathanmaldonado.w3d4_ex01.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.jonathanmaldonado.w3d4_ex01.DataBase.FeedReaderContract.FeedEntry;


/**
 * Created by Jonathan Maldonado on 7/26/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION =7;
    public static final String DATABASE_NAME="mydatabase.db";

    public static  final String SQL_CREATE_ENTRIES = "CREATE TABLE "+
            FeedEntry.TABLE_NAME+" ("+
            FeedEntry._ID+" INTEGER PRIMARY KEY,"+
            FeedEntry.COLUMN_NAME_ALIAS+" TEXT,"+
            FeedEntry.COLUMN_NAME_FULL_NAME+" TEXT,"+
            FeedEntry.COLUMN_NAME_ADDRESS+" TEXT,"+
            FeedEntry.COLUMN_NAME_EMAIL+" TEXT,"+
            FeedEntry.COLUMN_NAME_PICTURE_IMAGE+" BLOB)";

    public static final String SQL_DELETE_ENTRIES= "DROP TABLE IF EXISTS "+ FeedEntry.TABLE_NAME;

    public DBHelper (Context context){

        super(context, DATABASE_NAME, null,DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}

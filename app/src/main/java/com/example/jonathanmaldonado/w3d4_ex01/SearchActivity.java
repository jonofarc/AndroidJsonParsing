package com.example.jonathanmaldonado.w3d4_ex01;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonathanmaldonado.w3d4_ex01.DataBase.FeedReaderContract;

import com.example.jonathanmaldonado.w3d4_ex01.DataBase.DBHelper;

public class SearchActivity extends AppCompatActivity {

    private DBHelper helper;
    private SQLiteDatabase database;

    private ImageView profilePictureIV;
    private TextView fullNameTV;
    private TextView addressTV;
    private TextView emailTV;
    private TextView alertTV;
    private String message;

    private String userToDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        helper = new DBHelper(this);
        database = helper.getWritableDatabase();

        fullNameTV= (TextView) findViewById(R.id.tv_searchFullName);
        addressTV= (TextView) findViewById(R.id.tv_searchAddress);
        emailTV= (TextView) findViewById(R.id.tv_searchEmail);
        profilePictureIV= (ImageView) findViewById(R.id.iv_searchProfilePicture);
        alertTV= (TextView) findViewById(R.id.tv_searchAlerts);

        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.MAIN_ACTIVITY_EXTRA);
        if(intent != null && !TextUtils.isEmpty(message)){

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }else{
            message="*";
        }

        readUser();
    }

    private void readUser(){


        /*// example

        String[] tableColumns = new String[] {
            "column1",
            "(SELECT max(column1) FROM table2) AS max"
        };
        String whereClause = "column1 = ? OR column1 = ?";
        String[] whereArgs = new String[] {
            "value1",
            "value2"
        };
        String orderBy = "column1";

        Cursor c = sqLiteDatabase.query(
            "table1",
            tableColumns,
            whereClause,
            whereArgs,
            null,
            null,
            orderBy
         );

        // since we have a named column we can do
        int idx = c.getColumnIndex("max");

         */

        String[] projection={
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ALIAS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FULL_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ADDRESS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PICTURE_URL
        };
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ALIAS+"= ?";
        String[] selectionArg = {
                "Record title"
        };
        String sortOtder = FeedReaderContract.FeedEntry.COLUMN_NAME_FULL_NAME+"DESC";

        String whereClause = "Alias = ?";
        String[] whereArgs = new String[] {
                message
        };

        Cursor cursor = database.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   //Table
                projection,             //Projection
                whereClause,                   //Selection (WHERE)
                whereArgs,                   //Values for selection
                null,                   //Group by
                null,                   //Filters
                null                    //Sort order

        );



        String newMessage="";
        while (cursor.moveToNext()){
            long entryID =cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            String entryAlias=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ALIAS));
            String entryFullName=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FULL_NAME));
            String entryAddress=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ADDRESS));
            String entryEmail=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL));
            String entryPictureURL=cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PICTURE_URL));





            newMessage += "User ID: "+ entryID+" \n Alias: "+ entryAlias+ " \n Full Name: "+ entryFullName+ " \n Address "+entryAddress+" \n Email "+entryEmail+" \n Picture URL "+entryPictureURL+"\n";

        }


        alertTV.setText(newMessage);

    }


}

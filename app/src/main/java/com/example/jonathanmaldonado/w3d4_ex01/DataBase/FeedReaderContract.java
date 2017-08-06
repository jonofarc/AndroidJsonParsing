package com.example.jonathanmaldonado.w3d4_ex01.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Jonathan Maldonado on 7/26/2017.
 */

public final class FeedReaderContract {

    private FeedReaderContract(){

    }

    public static class FeedEntry implements BaseColumns{

        /*
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE="subtitle";
        */

        public static final String TABLE_NAME = "entry";
        public static String COLUMN_NAME_ALIAS = "Alias";
        public static String COLUMN_NAME_FULL_NAME = "fullName";
        public static final String COLUMN_NAME_ADDRESS="address";
        public static final String COLUMN_NAME_EMAIL="email";
        public static final String COLUMN_NAME_PICTURE_URL="PictureURL";
    }

}


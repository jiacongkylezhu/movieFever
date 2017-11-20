package com.kylezhudev.moviefever.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;



    public FavoritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + FavoritesContract.FavoritesEntry.TABLE_FAVORITES + " (" +
                FavoritesContract.FavoritesEntry._ID                 + " INTEGER PRIMARY KEY, " +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID     + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_NAME         + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_POSTER_URL + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoritesEntry.TABLE_FAVORITES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + FavoritesContract.FavoritesEntry.TABLE_FAVORITES + "'");

        onCreate(db);

    }
}

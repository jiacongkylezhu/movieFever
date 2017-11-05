package com.kylezhudev.moviefever.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kylezhudev.moviefever.R;

public class FavoritesProvider extends ContentProvider {
    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;


    private FavoritesDBHelper mFavoritesDBHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoritesDBHelper = new FavoritesDBHelper(context);
        return true;
    }



    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mFavoritesDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case FAVORITES:
                long id = db.insert(FavoritesContract.FavoritesEntry.TABLE_FAVORITES, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoritesEntry.CONTENT_URI, id);
                }else{
                    throw new UnsupportedOperationException(getContext().getString(R.string.error_failed_to_insert) + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.error_unknown_uri) + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase db = mFavoritesDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match){
            case FAVORITES:
                returnCursor = db.query(FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return returnCursor;
            case FAVORITES_WITH_ID:
                returnCursor = db.query(FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        projection,
                        FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        null);
                return returnCursor;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.error_unknown_uri) + uri);

        }

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mFavoritesDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match){
            case FAVORITES:
                numDeleted = db.delete(FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        selection,
                        selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + FavoritesContract.FavoritesEntry.TABLE_FAVORITES + "'");
                break;

            case FAVORITES_WITH_ID:
                numDeleted = db.delete(FavoritesContract.FavoritesEntry.TABLE_FAVORITES,
                        FavoritesContract.FavoritesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + FavoritesContract.FavoritesEntry.TABLE_FAVORITES + "'");
                break;

            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.error_unknown_uri));
        }

        return numDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException(getContext().getString(R.string.error_not_yet_implemented));
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException(getContext().getString(R.string.error_not_yet_implemented));
    }


}

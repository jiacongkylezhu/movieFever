package com.kylezhudev.moviefever.data;


import android.net.Uri;
import android.provider.BaseColumns;


public class FavoritesContract {
    public static final String AUTHORITY = "com.kylezhudev.moviefever";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";


    public static final class FavoritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon()
                        .appendPath(PATH_FAVORITES)
                        .build();

        public static final String TABLE_FAVORITES = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_POSTER_URL = "poster_url";


    }


}

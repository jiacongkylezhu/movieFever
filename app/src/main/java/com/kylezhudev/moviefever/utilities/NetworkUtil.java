package com.kylezhudev.moviefever.utilities;


import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtil {
    private final String KEY_API = "api_key";
    private final String API_KEY = "";

    private final String IMG_BASE_URL = "https://image.tmdb.org/t/p";
    private final String SEARCH_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private final String KEY_SORT_BY = "sort_by";
    private final String POPULARITY = "popularity.desc";
    private final String IMG_SIZE = "w185";



    public URL getPopMovieUrl() throws MalformedURLException {
        Uri builtUri = Uri.parse(SEARCH_BASE_URL)
                .buildUpon()
                .appendQueryParameter(KEY_API, API_KEY)
                .appendQueryParameter(KEY_SORT_BY, POPULARITY)
                .build();
        URL popMovieUrl = new URL(builtUri.toString()) ;
        return popMovieUrl;
    }

    public URL getImgUrl(String imgId) throws MalformedURLException {
        Uri uri = Uri.parse(IMG_BASE_URL)
                .buildUpon()
                .appendPath(IMG_SIZE)
                .appendEncodedPath(imgId)
                .build();
        URL imgUrl = new URL(uri.toString());
        return imgUrl;
    }


}

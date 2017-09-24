package com.kylezhudev.moviefever.utilities;


import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtil {
    private static final String KEY_API = "api_key";

    //TODO INSERT API KEY HERE****************************************
    private static final String API_KEY = "f01866a98f14dbeb0c2787741bf73236";

    private static final String IMG_BASE_URL = "https://image.tmdb.org/t/p";
    private static final String MOVIE_SEARCH_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String POPULAR = "popular";
    private static final String TOP_RATE = "top_rated";
    private static final String UPCOMING = "upcoming";

    private static final String IMG_SIZE = "w185";

    private static final String LANGUAGE = "language";

    private static final String EN_US = "en-US";


    private static final String URL_TAG = "URL Checker";


    public static URL getPopMovieUrl() throws MalformedURLException {
        Uri builtUri = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(POPULAR)
                .appendQueryParameter(KEY_API, API_KEY)
                .build();
        URL popMovieUrl = new URL(builtUri.toString());
        Log.i(URL_TAG, "Pop Movie Url: " + popMovieUrl.toString());
        return popMovieUrl;
    }

    public static URL getUpcomingMovieUrl() throws MalformedURLException {
        Uri builtUrl = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(UPCOMING)
                .appendQueryParameter(KEY_API, API_KEY)
                .appendQueryParameter(LANGUAGE, EN_US)
                .build();
        Log.i(URL_TAG, "Upcoming URL: " + builtUrl.toString());
        return new URL(builtUrl.toString());
    }

    public static URL getImgUrl(String imgId) throws MalformedURLException {
        Uri uri = Uri.parse(IMG_BASE_URL)
                .buildUpon()
                .appendPath(IMG_SIZE)
                .appendEncodedPath(imgId)
                .build();
        URL imgUrl = new URL(uri.toString());
        Log.i(URL_TAG, "Img Url: " + imgUrl.toString());
        return imgUrl;
    }

    public JSONObject getPopMovieResults(URL url) throws IOException {
        JSONObject rawMovieResults;
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                rawMovieResults = new JSONObject(scanner.next());
                return rawMovieResults;
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static JSONObject getRawMovieResults(URL url) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        Log.i("JSON_retrieving", "okHttp Executed");

        JSONObject jsonResults = new JSONObject(response.body().string());
        Log.i("JSON_Result", "JSON Result: " + jsonResults.toString());
        return jsonResults;
    }


    public static JSONObject getMovieDetailsById(String id) throws IOException, JSONException {
        Uri builtUri = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendQueryParameter(KEY_API, API_KEY)
                .build();

        Log.i("JSON_Result", "Movie detail URL: " + builtUri.toString());

        URL detailUrl = new URL(builtUri.toString());
        JSONObject movieDetailJson = getRawMovieResults(detailUrl);
        Log.i("JSON_Result", "Movie detail JSON: " + movieDetailJson.toString());
        return movieDetailJson;
    }

    public static URL gethighRateUrl() throws MalformedURLException {
        Uri builtUri = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(TOP_RATE)
                .appendQueryParameter(KEY_API, API_KEY)
                .appendQueryParameter(LANGUAGE, EN_US)
                .build();

        return new URL(builtUri.toString());
    }


}

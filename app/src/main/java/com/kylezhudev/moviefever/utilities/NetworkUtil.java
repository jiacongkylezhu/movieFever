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

public class NetworkUtil {
    private static final String KEY_API = "api_key";
    //TODO API KEY ****************************************
    private static final String API_KEY = "";

    private static final String IMG_BASE_URL = "https://image.tmdb.org/t/p";
    private static final String SEARCH_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String KEY_SORT_BY = "sort_by";
    private static final String POPULARITY = "popularity.desc";
    private static final String IMG_SIZE = "w185";
    private static final String URL_TAG = "URL Checker";



    public static URL getPopMovieUrl() throws MalformedURLException {
        Uri builtUri = Uri.parse(SEARCH_BASE_URL)
                .buildUpon()
                .appendQueryParameter(KEY_API, API_KEY)
                .appendQueryParameter(KEY_SORT_BY, POPULARITY)
                .build();
        URL popMovieUrl = new URL(builtUri.toString()) ;
        Log.i(URL_TAG, "Pop Movie Url: " + popMovieUrl.toString());
        return popMovieUrl;
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

        try{
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput){
                rawMovieResults = new JSONObject(scanner.next());
                return rawMovieResults;
            } else{
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
        return  jsonResults;
    }




}

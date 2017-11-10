package com.kylezhudev.moviefever.utilities;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.kylezhudev.moviefever.APIKeys;
import com.kylezhudev.moviefever.R;

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




    private static final String IMG_BASE_URL = "https://image.tmdb.org/t/p";
    private static final String MOVIE_SEARCH_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String POPULAR = "popular";
    private static final String TOP_RATE = "top_rated";
    private static final String UPCOMING = "upcoming";
    private static final String REVIEW = "reviews";

//    private static final String IMG_SIZE = "w185";
    private static final String IMG_SIZE = "w780";
    private static final String LANGUAGE = "language";
    private static final String EN_US = "en-US";
    private static final String KEY_YOUTUBE = "v";



    private static final String YOUTUBE_TRAILER_URL = "https://www.youtube.com/watch";


    private static final String URL_TAG = "URL Checker";
    private static final String JSON_RETRIEVE_TAG = "JSON_Retrieving";
    private static final String JSON_RESULT_TAG = "JSON_Retrieving";


    public static URL getPopMovieUrl() throws MalformedURLException {
        Uri builtUri = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(POPULAR)
                .appendQueryParameter(KEY_API, APIKeys.MOVIE_API_KEY)
                .build();
        URL popMovieUrl = new URL(builtUri.toString());
        Log.i(URL_TAG, "Pop Movie Url: " + popMovieUrl.toString());
        return popMovieUrl;
    }

    public static URL getUpcomingMovieUrl() throws MalformedURLException {
        Uri builtUrl = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(UPCOMING)
                .appendQueryParameter(KEY_API, APIKeys.MOVIE_API_KEY)
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

    public static URL getTrailerUrl(Context context, String movieId) throws MalformedURLException {
        Uri builtUrl = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
//                .appendEncodedPath(movieId)
                .appendPath(movieId)
                .appendPath(context.getString(R.string.videos))
                .appendQueryParameter(KEY_API, APIKeys.MOVIE_API_KEY)
                .appendQueryParameter(LANGUAGE, EN_US)
                .build();
        URL videoUrl = new URL(builtUrl.toString());
        Log.i(URL_TAG, "Trailer Url: " + videoUrl.toString());
        return videoUrl;
    }

    public static URL getYoutubeUrl(String key) throws MalformedURLException {
        Uri uri = Uri.parse(YOUTUBE_TRAILER_URL)
                .buildUpon()
                .appendQueryParameter(KEY_YOUTUBE, key)
                .build();
        URL youtubeVideoUrl = new URL(uri.toString());
        return  youtubeVideoUrl;

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

    /**
     * Using OkHttpClient to fetch raw JSON objects with urls
     *
     */



    public static JSONObject getRawMovieResults(URL url) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        Log.i(JSON_RETRIEVE_TAG, "okHttp retrieved raw movie Json");

        JSONObject jsonResults = new JSONObject(response.body().string());
        Log.i(JSON_RESULT_TAG, "JSON Result: " + jsonResults.toString());
        return jsonResults;
    }



    public static JSONObject getRawVideoJson(URL videoUrl) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request videoJsonRequest = new Request.Builder().url(videoUrl).build();
        Response videoJsonResponse = okHttpClient.newCall(videoJsonRequest).execute();
        Log.i(JSON_RETRIEVE_TAG, "okHttp retrieved raw video Json");

        JSONObject rawVideoJson = new JSONObject(videoJsonResponse.body().string());
        Log.i(JSON_RESULT_TAG, "Video JSON Result: " + rawVideoJson.toString());
        return rawVideoJson;
    }


    public static JSONObject getMovieDetailsById(String id) throws IOException, JSONException {
        Uri builtUri = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendQueryParameter(KEY_API, APIKeys.MOVIE_API_KEY)
                .build();

        Log.i("JSON_Result", "Movie detail URL: " + builtUri.toString());

        URL detailUrl = new URL(builtUri.toString());
        JSONObject movieDetailJson = getRawMovieResults(detailUrl);
        Log.i("JSON_Result", "Movie detail JSON: " + movieDetailJson.toString());
        return movieDetailJson;
    }



    public static URL getHighRateUrl() throws MalformedURLException {
        Uri builtUri = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(TOP_RATE)
                .appendQueryParameter(KEY_API, APIKeys.MOVIE_API_KEY)
                .appendQueryParameter(LANGUAGE, EN_US)
                .build();

        return new URL(builtUri.toString());
    }

    public static URL getReviewUrl(String id) throws MalformedURLException {
        Uri builtUri = Uri.parse(MOVIE_SEARCH_BASE_URL)
                .buildUpon()
                .appendPath(id)
                .appendPath(REVIEW)
                .appendQueryParameter(KEY_API, APIKeys.MOVIE_API_KEY)
                .appendQueryParameter(LANGUAGE, EN_US)
                .build();
        return new URL(builtUri.toString());
    }

    public static JSONObject getReviewJson(URL url) throws IOException, JSONException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request reviewJsonRequest = new Request.Builder().url(url).build();
        Response reviewJsonResponse = okHttpClient.newCall(reviewJsonRequest).execute();

        JSONObject reviewJson = new JSONObject(reviewJsonResponse.body().string());
        return reviewJson;
    }




}

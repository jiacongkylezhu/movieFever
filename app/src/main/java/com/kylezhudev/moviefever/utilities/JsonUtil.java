package com.kylezhudev.moviefever.utilities;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsonUtil {
    private static final String MF_RESULT = "results";
    private static final String MF_POSTER_PATH = "poster_path";
    private static final String MF_TITLE = "original_title";
    private static final String MF_VOTE_AVERAGE = "vote_average";
    private static final String MF_RELEASED_DATE = "release_date";
    private static final String MF_MOVIE_ID = "id";
    private static final String MF_RUN_TIME = "runtime";
    private static final String MF_VIDEO = "video";
    private static final String MF_OVERVIEW = "overview";


    public static String[] getPosterPathFromJson(String movieResults) throws JSONException {
        return getResultFromJson(movieResults, MF_POSTER_PATH);
    }

    public static String[] getTitleFromJson(String movieResults) throws JSONException {
        return getResultFromJson(movieResults, MF_TITLE);
    }


    public static String[] getMovieIdFromJson(String movieResults) throws JSONException {
        return getResultFromJson(movieResults, MF_MOVIE_ID);
    }


    private static String[] getResultFromJson(String movieResults, String path) throws JSONException {
        JSONObject movieRawJson = new JSONObject(movieResults);
        JSONArray movieJsonArray = movieRawJson.getJSONArray(MF_RESULT);
        String[] resultString = new String[movieJsonArray.length()];

        for (int i = 0; i < movieJsonArray.length(); i++) {
            JSONObject pathJson = movieJsonArray.getJSONObject(i);
            resultString[i] = pathJson.getString(path);
            Log.i("JsonResults", "String " + resultString[i]);
        }
        return resultString;
    }

    public static String getDetailRunTime(String detailResult) throws JSONException {
        return getDetailFromJson(detailResult, MF_RUN_TIME);
    }

    public static String getDetailReleaseDate(String detailResult) throws JSONException {
        return getDetailFromJson(detailResult, MF_RELEASED_DATE);
    }

    public static String getDetailTitle(String detailResult) throws JSONException {
        return getDetailFromJson(detailResult, MF_TITLE);
    }

    public static String getDetailVoteAverage(String detailResult) throws JSONException {
        return getDetailFromJson(detailResult, MF_VOTE_AVERAGE);
    }

    public static String getDetailVideo(String detailResult) throws JSONException {
        return getDetailFromJson(detailResult, MF_VIDEO);
    }

    public static String getDetailOverview(String detailResult) throws JSONException {
        return getDetailFromJson(detailResult, MF_OVERVIEW);
    }

    private static String getDetailFromJson(String detailResult, String path) throws JSONException {
        JSONObject detailRawJson = new JSONObject(detailResult);
        String resultString = detailRawJson.getString(path);
        Log.i("DetailJsonResults", "String " + resultString);
        return resultString;
    }


}

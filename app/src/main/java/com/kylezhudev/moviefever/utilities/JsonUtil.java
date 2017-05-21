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


    public static String[] getPosterPathFromJson(String movieResults) throws JSONException {
        JSONObject movieRawJson = new JSONObject(movieResults);
        JSONArray movieJsonArray = movieRawJson.getJSONArray(MF_RESULT);
        String[] resultString = new String[movieJsonArray.length()];

        for (int i = 0; i < movieJsonArray.length(); i++) {
            JSONObject pathJson = movieJsonArray.getJSONObject(i);
            resultString[i] = pathJson.getString(MF_POSTER_PATH);
            Log.i("JsonResults", "String " + resultString[i]);
        }
        return resultString;
    }

    public static String[] getTitleFromJson(String movieResults) throws JSONException {
        JSONObject movieRawJson = new JSONObject(movieResults);
        JSONArray movieJsonArray = movieRawJson.getJSONArray(MF_RESULT);
        String[] resultString = new String[movieJsonArray.length()];

        for (int i = 0; i < movieJsonArray.length(); i++) {
            JSONObject pathJson = movieJsonArray.getJSONObject(i);
            resultString[i] = pathJson.getString(MF_TITLE);
            Log.i("JsonResults", "String " + resultString[i]);
        }
        return resultString;
    }

    public static String[] getVoteAverageFromJson(String movieResults) throws JSONException {
        JSONObject movieRawJson = new JSONObject(movieResults);
        JSONArray movieJsonArray = movieRawJson.getJSONArray(MF_RESULT);
        String[] resultString = new String[movieJsonArray.length()];

        for (int i = 0; i < movieJsonArray.length(); i++) {
            JSONObject pathJson = movieJsonArray.getJSONObject(i);
            resultString[i] = pathJson.getString(MF_VOTE_AVERAGE);
            Log.i("JsonResults", "String " + resultString[i]);
        }
        return resultString;
    }
}

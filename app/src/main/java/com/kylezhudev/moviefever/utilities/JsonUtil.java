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
//    private static final String result = "{\"page\":1,\"results\":[{\"poster_path\":\"\\/y4MBh0EjBlMuOzv9axM4qJlmhzz.jpg\",\"adult\":false,\"overview\":\"The Guardians must fight to keep their newfound family together as they unravel the mysteries of Peter Quill's true parentage.\",\"release_date\":\"2017-04-24\",\"genre_ids\":[35,28,12,878],\"id\":283995,\"original_title\":\"Guardians of the Galaxy Vol. 2\",\"original_language\":\"en\",\"title\":\"Guardians of the Galaxy Vol. 2\",\"backdrop_path\":\"\\/aJn9XeesqsrSLKcHfHP4u5985hn.jpg\",\"popularity\":125.757946,\"vote_count\":1489,\"video\":false,\"vote_average\":7.6},{\"poster_path\":\"\\/2HjngGzVK3NTzptEtsT8E0Hi3ZB.jpg\",\"adult\":false,\"overview\":\"A live-action adaptation of Disney's version of the classic 'Beauty and the Beast' tale of a cursed prince and a beautiful young woman who helps him break the spell.\",\"release_date\":\"2017-03-16\",\"genre_ids\":[14,10749],\"id\":321612,\"original_title\":\"Beauty and the Beast\",\"original_language\":\"en\",\"title\":\"Beauty and the Beast\",\"backdrop_path\":\"\\/7QshG75xKCmClghQDU1ta2BTaja.jpg\",\"popularity\":97.735611,\"vote_count\":2422,\"video\":false,\"vote_average\":6.8}],\"total_results\":311205,\"total_pages\":15561}";

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

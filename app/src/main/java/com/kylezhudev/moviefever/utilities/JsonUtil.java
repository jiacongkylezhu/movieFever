package com.kylezhudev.moviefever.utilities;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsonUtil {

    public static String[] getMovieResultsFromJson(String movieResults) throws JSONException {

        final String MF_RESULT = "results";
        final String MF_POSTER_PATH= "poster_path";
        final String MF_TITLE= "original_title";
        final String MF_VOTE_AVERAGE= "vote_average";

        JSONObject movieJson = new JSONObject(movieResults);
        JSONArray movieJsonArray = movieJson.getJSONArray(MF_RESULT);
        String[] resultString = new String[movieJsonArray.length()];

        Log.i("JsonArray_result","JsonArray: " + movieJsonArray);




        return resultString;
    }

}

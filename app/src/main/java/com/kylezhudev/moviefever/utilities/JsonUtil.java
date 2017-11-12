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
    private static final String VIDEO_KEY = "key";
    private static final String VIDEO_NAME = "name";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";


    public static String[] getPosterPathFromJson(String movieResults) throws JSONException {
        return getResultFromJson(movieResults, MF_POSTER_PATH);
    }

    public static String[] getTitleFromJson(String movieResults) throws JSONException {
        return getResultFromJson(movieResults, MF_TITLE);
    }


    public static String[] getMovieIdFromJson(String movieResults) throws JSONException {
        return getResultFromJson(movieResults, MF_MOVIE_ID);
    }

    /**
     * Get movie array from raw movie video JSON
     *
     */

    public static String[] getVideoKeyFromJson(String videoResults) throws JSONException {
        return getResultFromJson(videoResults, VIDEO_KEY);
    }

    public static String[] getTrailerName(String videoResults) throws JSONException {
        return getResultFromJson(videoResults, VIDEO_NAME);
    }


    /**
     * Reuse to get all Json arrays from raw Json objects.
     * @param movieResults
     * @param path
     * @return
     * @throws JSONException
     */


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

    public static String getDetailPosterPath(String detailResult) throws JSONException {
        return getDetailFromJson(detailResult, MF_POSTER_PATH);
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






    /**
     * getDetailFromJson
     * reuse for getting movie details
     */

    private static String getDetailFromJson(String detailResult, String path) throws JSONException {
        JSONObject detailRawJson = new JSONObject(detailResult);
        String resultString = detailRawJson.getString(path);
        Log.i("DetailJsonResults", "String " + resultString);
        return resultString;
    }

    public static String[] getAuthorFromJson(String reviewResult) throws JSONException {
        return getReviewsFromJson(reviewResult, REVIEW_AUTHOR);
    }

    public static String[] getReviewFromJson(String reviewResult) throws JSONException {
        return getReviewsFromJson(reviewResult, REVIEW_CONTENT);
    }






    /**
     *
     * Reuse for getting review authors and contents
     * @param reviewResult
     * @param path
     * @return
     * @throws JSONException
     */



    private static String[] getReviewsFromJson(String reviewResult, String path) throws JSONException {
        JSONObject reviewRawJson = new JSONObject(reviewResult);
        JSONArray reviewJsonArray = reviewRawJson.getJSONArray(MF_RESULT);
        String[] resultString = new String[reviewJsonArray.length()];

        for(int i = 0; i < resultString.length; i++){
            JSONObject pathJson = reviewJsonArray.getJSONObject(i);
            resultString[i] = pathJson.getString(path);
        }

        return resultString;
    }



}

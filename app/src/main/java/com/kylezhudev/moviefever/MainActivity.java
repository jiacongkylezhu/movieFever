package com.kylezhudev.moviefever;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kylezhudev.moviefever.utilities.JsonUtil;
import com.kylezhudev.moviefever.utilities.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>{
    TextView mTextView;
    ProgressBar mProgressBar;
    TextView mTvError;
    String mJsonString;
    RecyclerView mRvMovie;
    RecyclerView.Adapter mRvMovieAdapter;
    final String result = "{\"page\":1,\"results\":[{\"poster_path\":\"\\/y4MBh0EjBlMuOzv9axM4qJlmhzz.jpg\",\"adult\":false,\"overview\":\"The Guardians must fight to keep their newfound family together as they unravel the mysteries of Peter Quill's true parentage.\",\"release_date\":\"2017-04-24\",\"genre_ids\":[35,28,12,878],\"id\":283995,\"original_title\":\"Guardians of the Galaxy Vol. 2\",\"original_language\":\"en\",\"title\":\"Guardians of the Galaxy Vol. 2\",\"backdrop_path\":\"\\/aJn9XeesqsrSLKcHfHP4u5985hn.jpg\",\"popularity\":125.757946,\"vote_count\":1489,\"video\":false,\"vote_average\":7.6},{\"poster_path\":\"\\/2HjngGzVK3NTzptEtsT8E0Hi3ZB.jpg\",\"adult\":false,\"overview\":\"A live-action adaptation of Disney's version of the classic 'Beauty and the Beast' tale of a cursed prince and a beautiful young woman who helps him break the spell.\",\"release_date\":\"2017-03-16\",\"genre_ids\":[14,10749],\"id\":321612,\"original_title\":\"Beauty and the Beast\",\"original_language\":\"en\",\"title\":\"Beauty and the Beast\",\"backdrop_path\":\"\\/7QshG75xKCmClghQDU1ta2BTaja.jpg\",\"popularity\":97.735611,\"vote_count\":2422,\"video\":false,\"vote_average\":6.8}],\"total_results\":311205,\"total_pages\":15561}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv_json_results);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mTvError = (TextView) findViewById(R.id.tv_error);



        if(mJsonString != null){

        }



        String[] results = new String[result.length()];
        try {
           results = JsonUtil.getPosterPathFromJson(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i< results.length; i++){
                mTextView.append(results[i] + "\n");
        }


    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                if(args == null){
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public String loadInBackground() {
                URL url =  null;
                JSONObject rawJson = null;
                try {
                    url = NetworkUtil.getPopMovieUrl();
                    rawJson = NetworkUtil.getRawMovieResults(url);
                    mJsonString = rawJson.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return mJsonString;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(data == null){
            mTvError.setVisibility(View.VISIBLE);
     //TODO notify adapter data change
        }else{

        }


    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }




}

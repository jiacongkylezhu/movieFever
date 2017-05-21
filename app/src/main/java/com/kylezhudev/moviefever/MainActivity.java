package com.kylezhudev.moviefever;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kylezhudev.moviefever.utilities.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>{
    private static final int MOVIE_LOADER_ID = 1;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private TextView mTvError;
    private String mJsonString;
    private RecyclerView mRvMovie;
    private MovieViewAdapter mRvMovieAdapter;
    private GridLayoutManager gridLayoutManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRvMovie = (RecyclerView) findViewById(R.id.rv_movie_main);
        mTextView = (TextView) findViewById(R.id.tv_json_results);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        mTvError = (TextView) findViewById(R.id.tv_error);
        gridLayoutManager = new GridLayoutManager(this, 2);
        mRvMovie.setLayoutManager(gridLayoutManager);
        mRvMovie.setHasFixedSize(true);
        mRvMovieAdapter = new MovieViewAdapter(this);
        mRvMovie.setAdapter(mRvMovieAdapter);

        //TODO 5/19 set RecyclerView and view holder
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);



//        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);




//        String[] results = new String[result.length()];
//        try {
//           results = JsonUtil.getPosterPathFromJson(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        for(int i=0; i< results.length; i++){
//                mTextView.append(results[i] + "\n");
//        }
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
//                if(args == null){
//                    return;
//                }
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                URL url;
                JSONObject rawJson;
                try {
                    url = NetworkUtil.getPopMovieUrl();
                    rawJson = NetworkUtil.getRawMovieResults(url);
                    mJsonString = rawJson.toString();
                    Log.i("Main_JSON_Checker", "Raw JSON:" + mJsonString);
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
            try {
                mRvMovieAdapter.saveMovieResultsData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }




}

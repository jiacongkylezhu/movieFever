package com.kylezhudev.moviefever;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        implements LoaderManager.LoaderCallbacks<String> {
    private static final int MOVIE_LOADER_ID = 1;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private TextView mTvError;
    private Menu mMenu;
    private RecyclerView mRvMovie;
    private MovieViewAdapter mRvMovieAdapter;
    private GridLayoutManager gridLayoutManager;


    private static int SORT_BY_FLAG = 1;


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



        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_upcoming:
                SORT_BY_FLAG = 0;
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                return true;
            case R.id.item_popularity:
                SORT_BY_FLAG = 1;
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                return true;
            case R.id.item_high_rate:
                SORT_BY_FLAG = 2;
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mJsonString = null;

            @Override
            protected void onStartLoading() {
                if (mJsonString != null) {
                    deliverResult(mJsonString);
                }
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                URL url;
                JSONObject rawJson;
                try {
                    switch (SORT_BY_FLAG) {
                        default: {
                            url = NetworkUtil.getPopMovieUrl();
                        }
                        break;
                        case 0: {
                            url = NetworkUtil.getUpcomingMovieUrl();
                        }
                        break;
                        case 1: {
                            url = NetworkUtil.getPopMovieUrl();
                        }
                        break;
                        case 2: {
                            url = NetworkUtil.gethighRateUrl();
                        }
                        break;



                    }
//                    url = NetworkUtil.getPopMovieUrl();
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

            @Override
            public void deliverResult(String data) {
                mJsonString = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (data == null) {
            mTvError.setVisibility(View.VISIBLE);

        } else {
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

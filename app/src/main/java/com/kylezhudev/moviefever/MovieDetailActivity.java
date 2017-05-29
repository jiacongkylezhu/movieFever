package com.kylezhudev.moviefever;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kylezhudev.moviefever.utilities.JsonUtil;
import com.kylezhudev.moviefever.utilities.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;


public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private TextView mTvMovieTitle;
    private TextView mTvReleasedYear;
    private TextView mTvRunTime;
    private TextView mTvRating;
    private TextView mTvOverView;
    private TextView mTvTrailer1;
    private TextView mTvNotrailer;
    private TextView mTvErrorMessage;
    private ImageView mImgPlay1;
    private ImageView mImgPlay2;
    private ImageView mImgNoTrailer;
    private ImageView mPosterThumbnail;
    private ImageView mDivider0, mDivider1, mDivider2;
    private Button mMarkAsFavorite;
    private RelativeLayout mRlTrailer1;
    private RelativeLayout mRlTrailer2;


    private ProgressBar mProgressBar;
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_POSTER_URL = "posterUrl";
    private static final int LOADER_ID = 2;
    private static String mMovieId;
    private String mPosterUrlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mTvMovieTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
        mTvReleasedYear = (TextView) findViewById(R.id.tv_released_year);
        mTvRunTime = (TextView) findViewById(R.id.tv_movie_runtime);
        mTvRating = (TextView) findViewById(R.id.tv_rating);
        mTvOverView = (TextView) findViewById(R.id.tv_movie_overview);
        mTvNotrailer = (TextView) findViewById(R.id.tv_no_trailer);
        mTvTrailer1 = (TextView) findViewById(R.id.tv_trailer_1);
        mTvErrorMessage = (TextView) findViewById(R.id.tv_detail_error);
        mImgPlay1 = (ImageView) findViewById(R.id.img_play_arrow_1);
        mImgPlay2 = (ImageView) findViewById(R.id.img_play_arrow_2);
        mPosterThumbnail = (ImageView) findViewById(R.id.img_detail_movie_thumbnail);
        mImgNoTrailer = (ImageView) findViewById(R.id.img_detail_not_available);
        mDivider0 = (ImageView) findViewById(R.id.img_divider_0);
        mDivider1 = (ImageView) findViewById(R.id.img_divider_1);
        mDivider2 = (ImageView) findViewById(R.id.img_divider_2);
        mMarkAsFavorite = (Button) findViewById(R.id.btn_mark_as_favorite);
        mRlTrailer1 = (RelativeLayout) findViewById(R.id.relative_layout_detail_trailer_1);
        mRlTrailer2 = (RelativeLayout) findViewById(R.id.relative_layout_detail_trailer_2);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_detail_load_indicator);

        Intent intent = getIntent();
        mMovieId = intent.getStringExtra(KEY_MOVIE_ID);
        mPosterUrlString = intent.getStringExtra(KEY_POSTER_URL);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);


    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String mDetailJson = null;

            @Override
            protected void onStartLoading() {

                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();

            }

            @Override
            public String loadInBackground() {
                JSONObject detailJsonObject;


                try {
                    detailJsonObject = NetworkUtil.getMovieDetailsById(mMovieId);
                    mDetailJson = detailJsonObject.toString();
                    return mDetailJson;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mDetailJson;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (data == null) {
            showError();
        } else {
            try {
                showDetailResults(data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void showError() {
        mTvMovieTitle.setText("");
        mPosterThumbnail.setVisibility(View.INVISIBLE);
        mTvReleasedYear.setVisibility(View.INVISIBLE);
        mTvRunTime.setVisibility(View.INVISIBLE);
        mTvRating.setVisibility(View.INVISIBLE);
        mTvOverView.setVisibility(View.INVISIBLE);
        mDivider0.setVisibility(View.INVISIBLE);
        mDivider1.setVisibility(View.INVISIBLE);
        mDivider2.setVisibility(View.INVISIBLE);
        mRlTrailer1.setVisibility(View.INVISIBLE);
        mRlTrailer2.setVisibility(View.INVISIBLE);
        mTvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void noTrailer() {
        mImgNoTrailer.setVisibility(View.VISIBLE);
        mTvNotrailer.setVisibility(View.VISIBLE);
        mRlTrailer2.setVisibility(View.INVISIBLE);
        mTvTrailer1.setVisibility(View.INVISIBLE);

    }

    private void showDetailResults(String loadedData) throws MalformedURLException, JSONException {
        mTvErrorMessage.setVisibility(View.INVISIBLE);
        if (mPosterUrlString != null) {
            Picasso
                    .with(this)
                    .load(mPosterUrlString)
                    .fit()
                    .into(mPosterThumbnail);

            String releaseDate = JsonUtil.getDetailReleaseDate(loadedData);
            String releaseYear = releaseDate.substring(0, 4);
            mTvReleasedYear.setText(releaseYear);
            mTvMovieTitle.setText(JsonUtil.getDetailTitle(loadedData));
            mTvRunTime.setText(JsonUtil.getDetailRunTime(loadedData));
            mTvRunTime.append("min");
            mTvRating.setText(JsonUtil.getDetailVoteAverage(loadedData));
            mTvRating.append("/10");
            mTvOverView.setText(JsonUtil.getDetailOverview(loadedData));
            if (JsonUtil.getDetailVideo(loadedData) == "false") {
                noTrailer();
            }
        }


    }


}

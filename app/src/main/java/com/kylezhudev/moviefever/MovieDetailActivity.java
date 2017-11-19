package com.kylezhudev.moviefever;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.kylezhudev.moviefever.data.FavoritesContract;
import com.kylezhudev.moviefever.utilities.JsonUtil;
import com.kylezhudev.moviefever.utilities.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;


public class MovieDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {
    private TextView mTvMovieTitle;
    private TextView mTvReleasedYear;
    private TextView mTvRunTime;
    private TextView mTvRating;
    private TextView mTvOverView;
    private TextView mTvTrailerName;
    private TextView mTvTrailerTitle;
    private TextView mTvErrorMessage;
    private ImageView mIcReleaseDate;
    private ImageView mIcRunTime;
    private ImageView mIcRating;
    private ImageView mPosterThumbnail;
    private ImageView mDivider0;
    private ImageButton mMarkAsFavorite;
    private RelativeLayout mRlMoreTrailer;
    private YouTubeThumbnailView mTrailerThumbnail;
    private FrameLayout mFlYouTube;
    private ProgressBar mProgressBar;
    private ImageView mErrorIcon;

    public static final String KEY_MOVIE_ID = "id";
    public static final String KEY_POSTER_URL = "posterUrl";
    private static final int MOVIE_DETAIL_LOADER_ID = 2;
    private static final int MOVIE_TRAILER_LOADER_ID = 3;
    private static final int FAVORITES_LOADER_ID = 4;
    private static String mMovieId;
    private String mPosterUrlString;
    private static String mTrailer1Id;
    private static String mTrailer2Id;
    private ThumbnailListener mThumbnailListener;
    private YouTubePlayer mYouTubePlayer;
    private YouTubePlayerSupportFragment mYouTube1stTrailerFragment;
    private Cursor mFavoritesResults;
    private LoaderManager.LoaderCallbacks mThumbnailLoaderListener;
    private YouTubeThumbnailLoader mYouTubeThumbnailLoader;
    private LoaderManager.LoaderCallbacks<Cursor> mFavoritesResultsLoaderListener;


    private static String mName;
    public static final String TRAILER_INTENT_KEY = "trailer_key";
    private static final String TRAILER_PRE_KEY = "trailer_id";
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private static boolean isFavorite = false;

    private ReviewFragment mReviewFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTvMovieTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
        mTvReleasedYear = (TextView) findViewById(R.id.tv_released_date);
        mTvRunTime = (TextView) findViewById(R.id.tv_movie_runtime);
        mTvRating = (TextView) findViewById(R.id.tv_rating);
        mTvOverView = (TextView) findViewById(R.id.tv_movie_overview);
        mTvTrailerName = (TextView) findViewById(R.id.tv_trailer_1);
        mTvTrailerTitle = (TextView) findViewById(R.id.tv_trailers_title);
        mTvErrorMessage = (TextView) findViewById(R.id.tv_detail_error);

        mPosterThumbnail = (ImageView) findViewById(R.id.img_detail_movie_thumbnail);
        mIcReleaseDate = (ImageView) findViewById(R.id.img_released_date);
        mIcRunTime = (ImageView) findViewById(R.id.img_movie_runtime);
        mIcRating = (ImageView) findViewById(R.id.img_rating);
        mDivider0 = (ImageView) findViewById(R.id.img_divider_0);
        mMarkAsFavorite = (ImageButton) findViewById(R.id.btn_mark_as_favorite);
        mRlMoreTrailer = (RelativeLayout) findViewById(R.id.relative_layout_detail_trailer_1);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_detail_load_indicator);
        mErrorIcon = (ImageView) findViewById(R.id.img_info_icon);
        mTrailerThumbnail = (YouTubeThumbnailView) findViewById(R.id.trailer1_thumbnail);
        mFlYouTube = (FrameLayout) findViewById(R.id.fl_youtube);


        Intent intent = getIntent();
        mMovieId = intent.getStringExtra(KEY_MOVIE_ID);
        mPosterUrlString = intent.getStringExtra(KEY_POSTER_URL);
        mThumbnailListener = new ThumbnailListener();


        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);
//        getSupportLoaderManager().restartLoader(MOVIE_TRAILER_LOADER_ID, null, this);
        setThumbnailLoader();
        getSupportLoaderManager().initLoader(MOVIE_TRAILER_LOADER_ID, null, mThumbnailLoaderListener);
        isInFavorites();

        mTrailerThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trailer1Id = v.getTag().toString();
                startTrailerActivity(getApplicationContext(), trailer1Id);
            }
        });



        setFragment();
    }

    private void setFragment() {
        FragmentTransaction reviewFragmentTran = getSupportFragmentManager().beginTransaction();
        mReviewFragment = new ReviewFragment().newInstance(KEY_MOVIE_ID, mMovieId);
        reviewFragmentTran.replace(R.id.frag_detail_placeholder, mReviewFragment);
        reviewFragmentTran.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
        }

        if(mYouTubeThumbnailLoader != null){
            mYouTubeThumbnailLoader.release();
        }
        super.onPause();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        if (id == MOVIE_DETAIL_LOADER_ID) {
            return new AsyncTaskLoader<String>(this) {
                String mDetailJsonString = null;


                @Override
                protected void onStartLoading() {
                    hideDetailUI();
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();

                }

                @Override
                public String loadInBackground() {
                    JSONObject detailJsonObject;


                    try {
                        detailJsonObject = NetworkUtil.getMovieDetailsById(mMovieId);
                        mDetailJsonString = detailJsonObject.toString();
                        return mDetailJsonString;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return mDetailJsonString;
                }
            };
        }

//        if (id == MOVIE_TRAILER_LOADER_ID) {
//            return new AsyncTaskLoader<String>(this) {
//                String mTrailerJsonString;
//
//
//                @Override
//                protected void onStartLoading() {
//                    forceLoad();
//                }
//
//                @Override
//                public String loadInBackground() {
//                    JSONObject videoJsonObject;
//
//                    try {
//                        videoJsonObject = NetworkUtil.getRawVideoJson(
//                                NetworkUtil.getTrailerUrl(getContext(), mMovieId));
//                        mTrailerJsonString = videoJsonObject.toString();
//                        return mTrailerJsonString;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    return mTrailerJsonString;
//                }
//            };
//        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (loader.getId() == MOVIE_DETAIL_LOADER_ID) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (data == null) {
                showError();
            } else {
                try {
                    setDetailResults(data);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

//        if (loader.getId() == MOVIE_TRAILER_LOADER_ID) {
//            if (data == null) {
//                noTrailer();
//            } else {
//                try {
//                    showTrailer(data);
//                    saveVideoID(data);
//                    int initAttempt = 0;
//                    loadTrailer(initAttempt);
//                    loadThumbnail(mTrailerThumbnail, mTrailer2Id);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void setThumbnailLoader() {
        mThumbnailLoaderListener = new LoaderManager.LoaderCallbacks<String>() {

            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<String>(getBaseContext()) {
                    String mTrailerJsonString;


                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public String loadInBackground() {
                        JSONObject videoJsonObject;

                        try {
                            videoJsonObject = NetworkUtil.getRawVideoJson(
                                    NetworkUtil.getTrailerUrl(getContext(), mMovieId));
                            mTrailerJsonString = videoJsonObject.toString();
                            return mTrailerJsonString;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return mTrailerJsonString;
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                if (data == null) {
                    noTrailer();
                } else {
                    try {
                        showTrailer(data);
                        saveVideoID(data);
                        int initAttempt = 0;
                        loadTrailer(initAttempt);
                        loadThumbnail(mTrailerThumbnail, mTrailer2Id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {

            }
        };
    }

    private void showError() {
        mTvMovieTitle.setText("");
        hideDetailUI();
        mTvErrorMessage.setVisibility(View.VISIBLE);
        mErrorIcon.setVisibility(View.VISIBLE);
    }

    private void hideDetailUI() {
        mTvMovieTitle.setVisibility(View.INVISIBLE);
        mPosterThumbnail.setVisibility(View.INVISIBLE);
        mTvReleasedYear.setVisibility(View.INVISIBLE);
        mTvRunTime.setVisibility(View.INVISIBLE);
        mTvRating.setVisibility(View.INVISIBLE);
        mTvOverView.setVisibility(View.INVISIBLE);
        mDivider0.setVisibility(View.INVISIBLE);
        mRlMoreTrailer.setVisibility(View.INVISIBLE);
        mMarkAsFavorite.setVisibility(View.INVISIBLE);
        mTvTrailerTitle.setVisibility(View.INVISIBLE);
        mIcReleaseDate.setVisibility(View.INVISIBLE);
        mIcRunTime.setVisibility(View.INVISIBLE);
        mIcRating.setVisibility(View.INVISIBLE);

    }

    private void showDetailUI() {
        mTvMovieTitle.setVisibility(View.VISIBLE);
        mPosterThumbnail.setVisibility(View.VISIBLE);
        mTvReleasedYear.setVisibility(View.VISIBLE);
        mTvRunTime.setVisibility(View.VISIBLE);
        mTvRating.setVisibility(View.VISIBLE);
        mTvOverView.setVisibility(View.VISIBLE);
        mDivider0.setVisibility(View.VISIBLE);
        mRlMoreTrailer.setVisibility(View.VISIBLE);
        mMarkAsFavorite.setVisibility(View.VISIBLE);
        mTvTrailerTitle.setVisibility(View.VISIBLE);
        mIcReleaseDate.setVisibility(View.VISIBLE);
        mIcRunTime.setVisibility(View.VISIBLE);
        mIcRating.setVisibility(View.VISIBLE);
    }


    private void setDetailResults(String loadedData) throws MalformedURLException, JSONException {
        mTvErrorMessage.setVisibility(View.INVISIBLE);
        mErrorIcon.setVisibility(View.INVISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        double heightDouble = width * 1.5;
        int height = (int) heightDouble;

        if (mPosterUrlString != null) {
            showDetailUI();
            Picasso
                    .with(this)
                    .load(mPosterUrlString)
//                    .resize(width, height)
//                    .centerCrop()
                    .placeholder(R.drawable.poster_placeholder)
                    .into(mPosterThumbnail);

            String releaseDate = JsonUtil.getDetailReleaseDate(loadedData);
//            String releaseYear = releaseDate.substring(0, 4);
//            mTvReleasedYear.setText(releaseYear);
            mTvReleasedYear.setText(releaseDate);
            mName = JsonUtil.getDetailTitle(loadedData);
            mTvMovieTitle.setText(mName);
            mTvRunTime.setText(JsonUtil.getDetailRunTime(loadedData));
            mTvRunTime.append(getBaseContext().getString(R.string.minutes));
            mTvRating.setText(JsonUtil.getDetailVoteAverage(loadedData));
            mTvRating.append(getBaseContext().getString(R.string.max_score));
            mTvOverView.setText(JsonUtil.getDetailOverview(loadedData));
        }
    }


    private void noTrailer() {
        ((ViewGroup) mFlYouTube.getParent()).removeView(mFlYouTube);
    }

    private void noMoreTrailer() {
        ((ViewGroup) mRlMoreTrailer.getParent()).removeView(mRlMoreTrailer);
        ((ViewGroup) mTvTrailerTitle.getParent()).removeView(mTvTrailerTitle);
    }

    private void showTrailer(String loadedVideoJsonString) throws JSONException {
        mFlYouTube.setVisibility(View.VISIBLE);

        String[] trailerNames = JsonUtil.getTrailerName(loadedVideoJsonString);

        if (trailerNames.length > 0) {
            mTvTrailerName.setText(trailerNames[0]);
        }


    }

    private void saveVideoID(String videoJsonString) throws JSONException {
        String[] videoKeys = JsonUtil.getVideoKeyFromJson(videoJsonString);
        if (videoKeys.length > 0) {
            mTrailer1Id = videoKeys[0];
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit()
                    .putString(TRAILER_PRE_KEY, mTrailer1Id)
                    .apply();
            Log.i("Video ID 1", "Trailer1 ID: " + videoKeys[0]);
        }


        if (videoKeys.length > 1) {
            mTrailer2Id = videoKeys[1];
            mTrailerThumbnail.setTag(videoKeys[1]);
            Log.i("Video ID 2", "Trailer2 ID: " + videoKeys[1]);
        }

    }

    private void loadThumbnail(YouTubeThumbnailView view, String videoKey) {
        view.setTag(videoKey);
        view.initialize(APIKeys.YOUTUBE_API_KEY, mThumbnailListener);
    }

    private void startTrailerActivity(Context context, String videoId) {
        Intent playerIntent = new Intent(context, TrailerActivity.class);
        playerIntent.putExtra(TRAILER_INTENT_KEY, videoId);
        startActivity(playerIntent);
    }


    private void loadTrailer(final int attempts) {
        mYouTube1stTrailerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youTube_fragment);
        mYouTube1stTrailerFragment.initialize(APIKeys.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
                mYouTubePlayer = player;
                if (!wasRestored && mTrailer1Id != null) {
                    mYouTubePlayer.cueVideo(mTrailer1Id);
                }
            }


            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                int maxAttempts = 3;
                if (attempts <= maxAttempts) {
                    loadTrailer(attempts + 1);
                    Log.i("Attempts counter", "Attempt: " + attempts);
                } else {
                    mYouTubePlayer = null;
                    Toast.makeText(getApplicationContext(), "Failed to Initialized video", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });
    }

    /***
     * onClick: query database check if the movie is already added to Favorites
     * if not, add movie id, name, and poster url to favorites table
     * @param view
     */


    public void onClickAddFavorite(View view) {

        final LoaderManager.LoaderCallbacks<Cursor> favoritesResultsLoaderListener
                = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Cursor>(getBaseContext()) {
                    Cursor favoritesCursor = null;

                    @Override
                    protected void onStartLoading() {
//                        if (favoritesCursor != null) {
//                            deliverResult(favoritesCursor);
//                        } else {
                            forceLoad();
//                        }
                    }

                    @Override
                    public Cursor loadInBackground() {

                        try {
                            Uri uri = Uri.withAppendedPath(FavoritesContract.FavoritesEntry.CONTENT_URI, mMovieId);
                            favoritesCursor = getContentResolver().query(uri,
                                    null,
                                    null,
                                    null,
                                    null);
                            return favoritesCursor;
                        } catch (Exception e) {
                            Log.i(LOG_TAG, "Data is not in database or failed to asynchronously load data");
                            e.printStackTrace();
                        }

                        return favoritesCursor;
                    }

//                    @Override
//                    public void deliverResult(Cursor data) {
//                        favoritesCursor = data;
//                        super.deliverResult(data);
//                    }
                };
            }



            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mFavoritesResults = data;
                mFavoritesResults.moveToFirst();

                if (data.getCount() != 0) {
                    Log.i(LOG_TAG, "Cursor loaded data: " + data.toString());
                    int movieIdIndex = mFavoritesResults.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
                    String savedMovieId = mFavoritesResults.getString(movieIdIndex);
                    if (savedMovieId.equals(mMovieId)) {

                        Uri uri = Uri.withAppendedPath(FavoritesContract.FavoritesEntry.CONTENT_URI, mMovieId);
                        int numDeleted = getContentResolver().delete(uri,
                                null,
                                null);

                        Log.i(LOG_TAG, "Deleted " + numDeleted + " rows");
                        isFavorite = false;
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    ContentValues newFavoriteValue = new ContentValues();
                    newFavoriteValue.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovieId);
                    newFavoriteValue.put(FavoritesContract.FavoritesEntry.COLUMN_NAME, mName);
                    newFavoriteValue.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_URL, mPosterUrlString);
                    getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, newFavoriteValue);
                    isFavorite = true;
                    Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();
                }

                setFavoriteIcon(isFavorite);


            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mFavoritesResults = null;
            }
        };


            getSupportLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, favoritesResultsLoaderListener);


    }

    private boolean isInFavorites(){

        mFavoritesResultsLoaderListener
                = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Cursor>(getBaseContext()) {
                    Cursor favoritesCursor = null;

                    @Override
                    protected void onStartLoading() {

                        forceLoad();

                    }

                    @Override
                    public Cursor loadInBackground() {

                        try {

                            Uri uri = Uri.withAppendedPath(FavoritesContract.FavoritesEntry.CONTENT_URI, mMovieId);
                            favoritesCursor = getContentResolver().query(uri,
                                    null,
                                    null,
                                    null,
                                    null);
                            return favoritesCursor;

                        } catch (Exception e) {
                            Log.i(LOG_TAG, "Data is not in database or failed to asynchronously load data");
                            e.printStackTrace();
                        }
                        return favoritesCursor;
                    }

                };
            }


            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                mFavoritesResults = data;
                mFavoritesResults.moveToFirst();

                if (data.getCount() != 0) {
                    Log.i(LOG_TAG, "Cursor loaded data: " + data.toString());
                    int movieIdIndex = mFavoritesResults.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
                    String savedMovieId = mFavoritesResults.getString(movieIdIndex);
                    if (savedMovieId.equals(mMovieId)) {
                        isFavorite = true;
                    }
                } else {
                    isFavorite =false;
                }
                setFavoriteIcon(isFavorite);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                mFavoritesResults = null;
            }
        };

        getSupportLoaderManager().initLoader(FAVORITES_LOADER_ID, null, mFavoritesResultsLoaderListener);
        return isFavorite;
    }

    private void setFavoriteIcon(boolean isFavorite){
        if(!isFavorite){
            mMarkAsFavorite.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
        }else{
            mMarkAsFavorite.setImageResource(R.drawable.ic_bookmark_black_24dp);
        }

    }




    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
            thumbnailView.setImageResource(R.drawable.no_thumbnail);
        }

        @Override
        public void onInitializationSuccess(YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader thumbnailLoader) {
            thumbnailLoader.setOnThumbnailLoadedListener(this);
            thumbnailView.setImageResource(R.drawable.loading_thumbnail);
            mYouTubeThumbnailLoader = thumbnailLoader;

            if (thumbnailView.getTag() != null) {
                String videoKey = thumbnailView.getTag().toString();
                thumbnailLoader.setVideo(videoKey);
            } else {
                noMoreTrailer();
            }
        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult initializationResult) {
            thumbnailView.setImageResource(R.drawable.no_thumbnail);
        }
    }


}

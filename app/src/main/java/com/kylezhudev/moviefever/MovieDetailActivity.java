package com.kylezhudev.moviefever;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
    private TextView mTvTrailerName;
    private TextView mTvNoTrailer;
    private TextView mTvTrailerTitle;
    private TextView mTvErrorMessage;
    private ImageView mImgNoTrailer;
    private ImageView mPosterThumbnail;
    private ImageView mDivider0;
    private Button mMarkAsFavorite;
    private RelativeLayout mRlMoreTrailer;
    private YouTubeThumbnailView mTrailerThumbnail;
    private FrameLayout mFlYouTube;


    private ProgressBar mProgressBar;
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_POSTER_URL = "posterUrl";
    private static final int MOVIE_DETAIL_LOADER_ID = 2;
    private static final int MOVIE_TRAILER_LOADER_ID = 3;
    private static String mMovieId;
    private String mPosterUrlString;
    private static String mTrailer1Id;
    private static String mTrailer2Id;
    private ThumbnailListener thumbnailListener;
    private YouTubePlayer mYouTubePlayer;
    private YouTubePlayerSupportFragment mYouTube1stTrailerFragment;


    public static final String VIDEO_INTENT_KEY = "video_intent";
    private static final int TRAILER_1_KEY = 101;
    private static final int TRAILER_2_KEY = 102;
    public static final String TRAILER_INTENT_KEY = "trailer_key";
    private static final String TRAILER_PRE_KEY = "trailer_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mTvMovieTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
        mTvReleasedYear = (TextView) findViewById(R.id.tv_released_year);
        mTvRunTime = (TextView) findViewById(R.id.tv_movie_runtime);
        mTvRating = (TextView) findViewById(R.id.tv_rating);
        mTvOverView = (TextView) findViewById(R.id.tv_movie_overview);
        mTvNoTrailer = (TextView) findViewById(R.id.tv_no_trailer);
        mTvTrailerName = (TextView) findViewById(R.id.tv_trailer_1);
        mTvTrailerTitle = (TextView) findViewById(R.id.tv_trailers_title);
        mTvErrorMessage = (TextView) findViewById(R.id.tv_detail_error);

        mPosterThumbnail = (ImageView) findViewById(R.id.img_detail_movie_thumbnail);
        mImgNoTrailer = (ImageView) findViewById(R.id.img_detail_not_available);
        mDivider0 = (ImageView) findViewById(R.id.img_divider_0);
        mMarkAsFavorite = (Button) findViewById(R.id.btn_mark_as_favorite);
        mRlMoreTrailer = (RelativeLayout) findViewById(R.id.relative_layout_detail_trailer_1);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_detail_load_indicator);
        mTrailerThumbnail = (YouTubeThumbnailView) findViewById(R.id.trailer1_thumbnail);
        mFlYouTube = (FrameLayout) findViewById(R.id.fl_youtube);


        Intent intent = getIntent();
        mMovieId = intent.getStringExtra(KEY_MOVIE_ID);
        mPosterUrlString = intent.getStringExtra(KEY_POSTER_URL);
        thumbnailListener = new ThumbnailListener();

        getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER_ID, null, this);
        getSupportLoaderManager().restartLoader(MOVIE_TRAILER_LOADER_ID, null, this);

        mTrailerThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trailer1Id = v.getTag().toString();
                startTrailerActivity(getApplicationContext(), trailer1Id);
            }
        });


    }


    @Override
    protected void onPause() {
        if(mYouTubePlayer != null){
            mYouTubePlayer.release();
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

        if (id == MOVIE_TRAILER_LOADER_ID) {
            return new AsyncTaskLoader<String>(this) {
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
                                NetworkUtil.getTailerUrl(getContext(), mMovieId));
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
                    showDetailResults(data);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (loader.getId() == MOVIE_TRAILER_LOADER_ID) {
            //TODO(1) adjust thumbnail position and get rid of play arrow drawables
            //TODO(2) Set up onClick thumnail and play trailer on fullscreen
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
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private void showError() {
        mTvMovieTitle.setText("");
        hideDetailUI();
        mTvErrorMessage.setVisibility(View.VISIBLE);
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
    }


    private void showDetailResults(String loadedData) throws MalformedURLException, JSONException {
        mTvErrorMessage.setVisibility(View.INVISIBLE);
        if (mPosterUrlString != null) {
            showDetailUI();
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
        }
    }


    private void noTrailer() {
        mImgNoTrailer.setVisibility(View.VISIBLE);
        mTvNoTrailer.setVisibility(View.VISIBLE);
        mTvTrailerName.setVisibility(View.INVISIBLE);
        mFlYouTube.setVisibility(View.INVISIBLE);
    }

    private void showTrailer(String loadedVideoJsonString) throws JSONException {
        mImgNoTrailer.setVisibility(View.INVISIBLE);
        mTvNoTrailer.setVisibility(View.INVISIBLE);
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
//            mTrailerThumbnail.setTag(videoKeys[0]);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit()
                    .putString(TRAILER_PRE_KEY, mTrailer1Id)
                    .apply();
            Log.i("Video ID 1", "Trailer1 ID: " + videoKeys[0]);
        }

        if (videoKeys.length > 0) {
            mTrailer2Id = videoKeys[1];
            mTrailerThumbnail.setTag(videoKeys[1]);
            Log.i("Video ID 2", "Trailer2 ID: " + videoKeys[1]);
        }

    }


    private void loadThumbnail(YouTubeThumbnailView view, String videoKey) {
        view.setTag(videoKey);
        view.initialize(APIKeys.YOUTUBE_API_KEY, thumbnailListener);
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
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                mYouTubePlayer = player;
                if (!wasRestored && mTrailer1Id != null) {
                    player.cueVideo(mTrailer1Id);
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                int maxAttempts = 3;
                if(attempts <= maxAttempts){
                    loadTrailer(attempts + 1);
                    Log.i("Attempts counter", "Attempt: " + attempts);
                }else{
                    mYouTubePlayer = null;
                    Toast.makeText(getApplicationContext(), "Failed to Initialized video", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });

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
            String videoKey = thumbnailView.getTag().toString();
            thumbnailLoader.setVideo(videoKey);
        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult initializationResult) {
            thumbnailView.setImageResource(R.drawable.no_thumbnail);
        }
    }



}

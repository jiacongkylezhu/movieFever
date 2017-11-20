package com.kylezhudev.moviefever;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kylezhudev.moviefever.utilities.JsonUtil;
import com.kylezhudev.moviefever.utilities.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private static final String LOG_TAG = ReviewFragment.class.getSimpleName();
    private RecyclerView mRvReviewList;
    private TextView mTvReviewTitle;
    private ReviewListAdapter reviewListAdapter;
    private RecyclerView.LayoutManager mLayoutManger;
    private TextView mTvNoReview;
    private ImageView mIvInfoIcon;
    private Context mContext;
    private ProgressBar mProgressBar;
    private static String mMovieId;
    private static String mReviewRawString = null;
    private static final int REVIEW_LOADER_ID = 1;
    String[] mReviewResult;


    public ReviewFragment() {
        // Required empty public constructor
        mContext = getActivity();
    }

    public ReviewFragment newInstance(String key, String movieId) {
        ReviewFragment reviewFragment = new ReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(key, movieId);
        reviewFragment.setArguments(bundle);
        return reviewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mMovieId = bundle.getString(MovieDetailActivity.KEY_MOVIE_ID);
            Log.i(LOG_TAG, "Movie ID " + mMovieId);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_review_progress_bar);
        mTvNoReview = (TextView) rootView.findViewById(R.id.tv_no_review);
        mIvInfoIcon = (ImageView) rootView.findViewById(R.id.img_review_info);
        mTvReviewTitle = (TextView) rootView.findViewById(R.id.tv_review_title);
        mRvReviewList = (RecyclerView) rootView.findViewById(R.id.rv_review_list);
        mLayoutManger = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        reviewListAdapter = new ReviewListAdapter();
        getLoaderManager().initLoader(REVIEW_LOADER_ID, null, this);
        return rootView;
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(getActivity()) {


            @Override
            protected void onStartLoading() {
                mProgressBar.setVisibility(View.VISIBLE);
                if (mReviewRawString != null) {
                    deliverResult(mReviewRawString);
                }

                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    URL url = NetworkUtil.getReviewUrl(mMovieId);
                    mReviewRawString = NetworkUtil.getReviewJson(url).toString();
                    return mReviewRawString;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mReviewRawString;
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (data != null) {
            try {
                mReviewResult = new String[JsonUtil.getReviewResultJson(data).length];
                Log.i(LOG_TAG, "Review: " + data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mReviewResult.length != 0) {
                try {
                    reviewListAdapter.saveReviewResults(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRvReviewList.setLayoutManager(mLayoutManger);
                mRvReviewList.setAdapter(reviewListAdapter);
                hideNoReview();
            } else {
                data = null;
                try {
                    reviewListAdapter.saveReviewResults(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRvReviewList.setLayoutManager(mLayoutManger);
                mRvReviewList.setAdapter(reviewListAdapter);
                showNoReview();
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        loader = null;
    }

    private void showNoReview() {
        mTvNoReview.setVisibility(View.VISIBLE);
        mIvInfoIcon.setVisibility(View.VISIBLE);
        mTvReviewTitle.setVisibility(View.INVISIBLE);
    }

    private void hideNoReview() {
        mTvNoReview.setVisibility(View.INVISIBLE);
        mIvInfoIcon.setVisibility(View.INVISIBLE);
        mTvReviewTitle.setVisibility(View.VISIBLE);
    }
}

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kylezhudev.moviefever.utilities.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{
    private RecyclerView mRvReviewList;
    private TextView tvReviewTitle;
    private ReviewListAdapter mReviewAdatper;
    private RecyclerView.LayoutManager mLayoutManger;
    private Context mContext;
    private ProgressBar mProgressBar;
    private String movieId;



    public ReviewFragment() {
        // Required empty public constructor
        mContext = getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_review_progress_bar);
        mRvReviewList = (RecyclerView) rootView.findViewById(R.id.rv_review_list);
        mLayoutManger = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mReviewAdatper = new ReviewListAdapter();
        mRvReviewList.setLayoutManager(mLayoutManger);
        mRvReviewList.setAdapter(mReviewAdatper);



        return rootView;
    }



    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(mContext) {
            String jsonString;
            @Override
            protected void onStartLoading() {
                if (jsonString != null){
                    deliverResult(jsonString);
                }
                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    URL url = NetworkUtil.getReviewUrl(movieId);
                    jsonString = NetworkUtil.getReviewJson(url).toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    //TODO complete onLoadFinished and figure out how to pass movie id from detail activity to this fragment.

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}

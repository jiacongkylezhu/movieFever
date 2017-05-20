package com.kylezhudev.moviefever;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kylezhudev.moviefever.utilities.JsonUtil;
import com.kylezhudev.moviefever.utilities.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

public class MovieViewAdapter extends Adapter<MovieViewAdapter.MovieViewHolder> {
    private static int mViewHolderCount;
    private int mNumberItems;
    private String[] mPosterPaths;
    private String[] mMovieTitle;
    private URL[] mPosterPathUrls;
    private Context mContext;

    public MovieViewAdapter(Context context, int numberItems) {
        mContext = context;
        mNumberItems = numberItems;
        mViewHolderCount = 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIVMovieThumbnail;
        private TextView mTVMovieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mIVMovieThumbnail = (ImageView) itemView.findViewById(R.id.iv_movie_image);
            mTVMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (mPosterPathUrls != null) {
            Picasso
                    .with(mContext)
                    .load(mPosterPathUrls[position].toString())
                    .into(holder.mIVMovieThumbnail);


        }


        holder.mTVMovieTitle.setText(mMovieTitle[position]);
    }




    @Override
    public int getItemCount() {
        if (mNumberItems == 0) {
            return 0;
        } else {
            mNumberItems = mPosterPathUrls.length;
        }
        return mNumberItems;
    }

    public void setData(String movieData) throws JSONException, MalformedURLException {
        if (movieData != null) {

            mPosterPaths = JsonUtil.getPosterPathFromJson(movieData);
            mMovieTitle = JsonUtil.getTitleFromJson(movieData);

            for (int i = 0; i < mPosterPaths.length; i++) {
                mPosterPathUrls[i] = NetworkUtil.getImgUrl(mPosterPaths[i]);

            }

        } else {
            Log.e("SetDataError", "No data is set in adapter");
        }
    }

}

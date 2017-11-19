package com.kylezhudev.moviefever;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder> {

    private String[] mPosterPaths;
    private String[] mMovieTitle;
    private String[] mMovieIds;
    private URL[] mPosterPathUrls;
    private  Context mContext;
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_POSTER_URL = "posterUrl";
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();


    public MovieViewAdapter(Context context) {
        mContext = context;

    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mIVMovieThumbnail;
        private TextView mTVMovieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mIVMovieThumbnail = (ImageView) itemView.findViewById(R.id.iv_movie_image);
            mTVMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
        }

        @Override
        public void onClick(View v) {
        int position = getAdapterPosition();
            String movieId = mMovieIds[position];
            String posterUrlString = mPosterPathUrls[position].toString();
            Intent intent = new Intent(v.getContext(), MovieDetailActivity.class);
            intent.putExtra(KEY_MOVIE_ID, movieId);
            intent.putExtra(KEY_POSTER_URL, posterUrlString);
            v.getContext().startActivity(intent);




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
//        int width = holder.mIVMovieThumbnail.getWidth();
//        Log.i(LOG_TAG, "width: " + width);
//        double heightDouble = width * 1.5;
//        int height = (int) heightDouble;
        if (mPosterPathUrls != null) {
            Picasso
                    .with(mContext)
                    .load(mPosterPathUrls[position].toString())
//                    .resize(width, height)
                    .into(holder.mIVMovieThumbnail);
        }
        holder.mTVMovieTitle.setText(mMovieTitle[position]);
    }


    @Override
    public int getItemCount() {
        if (mPosterPathUrls == null) {
            return 0;

        }
        return mPosterPathUrls.length;
    }



    public void saveMovieResultsData(String movieData) throws JSONException, MalformedURLException, URISyntaxException {
        if (movieData != null) {
            mMovieIds = JsonUtil.getMovieIdFromJson(movieData);
            mPosterPaths = JsonUtil.getPosterPathFromJson(movieData);
            mMovieTitle = JsonUtil.getTitleFromJson(movieData);
            List<URL> tmpArrayList = new ArrayList<>();

            for (int i = 0; i < mPosterPaths.length; i++) {
                 tmpArrayList.add(i,NetworkUtil.getImgUrl(mPosterPaths[i]));
            }
            mPosterPathUrls = new URL[tmpArrayList.size()];
            tmpArrayList.toArray(mPosterPathUrls);
            notifyDataSetChanged();
        } else {
            Log.e("SetDataError", "No data is set in adapter");
        }
    }
}

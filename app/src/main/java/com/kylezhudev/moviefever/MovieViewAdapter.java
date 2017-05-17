package com.kylezhudev.moviefever;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieViewAdapter extends Adapter<MovieViewAdapter.MovieViewHolder>{
    private static int mViewHolderCount;
    private int mNumberItems;

    public MovieViewAdapter(int numberItems){
        mNumberItems = numberItems;
        mViewHolderCount = 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        if(mNumberItems == 0){
            return 0;
        }
        return mNumberItems;
    }

}

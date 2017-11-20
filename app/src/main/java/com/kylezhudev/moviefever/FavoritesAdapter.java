package com.kylezhudev.moviefever;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kylezhudev.moviefever.data.FavoritesContract;
import com.squareup.picasso.Picasso;

import static com.kylezhudev.moviefever.MovieDetailActivity.KEY_MOVIE_ID;
import static com.kylezhudev.moviefever.MovieDetailActivity.KEY_POSTER_URL;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder> {

    private Cursor mCursor;
    private Context mContext;


    public FavoritesAdapter(Context context) {
        mContext = context;
    }


    public class FavoritesHolder extends RecyclerView.ViewHolder {
        private ImageView mIvMovieThumbnail;
        private TextView mTvMovieTile;

        public FavoritesHolder(View itemView) {
            super(itemView);

            mIvMovieThumbnail = (ImageView) itemView.findViewById(R.id.iv_movie_image);
            mTvMovieTile = (TextView) itemView.findViewById(R.id.tv_movie_title);
        }

    }


    @Override
    public FavoritesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new FavoritesHolder(view);
    }


    @Override
    public void onBindViewHolder(FavoritesHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
        int nameIndex = mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_NAME);
        int urlIndex = mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_URL);


        mCursor.moveToPosition(position);

        final String movieId = mCursor.getString(idIndex);
        String movieName = mCursor.getString(nameIndex);
        final String movieUrl = mCursor.getString(urlIndex);

        holder.mTvMovieTile.setText(movieName);
        Picasso
                .with(mContext)
                .load(movieUrl)
                .into(holder.mIvMovieThumbnail);

        holder.mIvMovieThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                intent.putExtra(KEY_MOVIE_ID, movieId);
                intent.putExtra(KEY_POSTER_URL, movieUrl);
                v.getContext().startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }

        Cursor tmpCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return tmpCursor;
    }


}

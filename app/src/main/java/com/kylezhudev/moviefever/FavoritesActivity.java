package com.kylezhudev.moviefever;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.kylezhudev.moviefever.data.FavoritesContract;

public class FavoritesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRlMovie;
    private LinearLayout mLlError;
    private GridLayoutManager mGridManager;
    private final int GRID_LAYOUT_COLUMNS = 2;
    private FavoritesAdapter mFavoritesAdapter;
    private static final int CURSOR_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        mLlError = (LinearLayout) findViewById(R.id.ll_no_favorite);
        mRlMovie = (RecyclerView) findViewById(R.id.rl_favorite);
        mGridManager = new GridLayoutManager(this, GRID_LAYOUT_COLUMNS);
        mRlMovie.setLayoutManager(mGridManager);
        mRlMovie.setHasFixedSize(true);
        mFavoritesAdapter = new FavoritesAdapter(this);
        mRlMovie.setAdapter(mFavoritesAdapter);

    }

    private void showError() {
        mRlMovie.setVisibility(View.INVISIBLE);
        mLlError.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor movieCursor;

            @Override
            protected void onStartLoading() {
                if(movieCursor != null){
                    deliverResult(movieCursor);
                }else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    movieCursor = getContentResolver().query(
                            FavoritesContract.FavoritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavoritesContract.FavoritesEntry._ID);
                    return movieCursor;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return movieCursor;
            }

            @Override
            public void deliverResult(Cursor data) {
                movieCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() != 0){
            mFavoritesAdapter.swapCursor(data);
        }else{
            showError();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            mFavoritesAdapter.swapCursor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }
}

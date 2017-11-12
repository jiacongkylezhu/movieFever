package com.kylezhudev.moviefever;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kylezhudev.moviefever.utilities.JsonUtil;

import org.json.JSONException;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewHolder> {
    private String[] mAuthors, mReviews;


    public class ReviewHolder extends RecyclerView.ViewHolder {
        private TextView mTvAuthor, mTvReview;

        public ReviewHolder(View itemView) {
            super(itemView);
            mTvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            mTvReview = (TextView) itemView.findViewById(R.id.tv_review);
        }
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        if(mAuthors != null && mAuthors.length != 0 ){
            holder.mTvAuthor.setText(mAuthors[position] + " :");
        }

        if ( mReviews != null && mReviews.length != 0){
            holder.mTvReview.setText(mReviews[position]);
        }
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        } else {
            return mReviews.length;
        }

    }

    public void saveReviewResults(String reviewJsonString) throws JSONException {
        if (reviewJsonString != null) {
            mAuthors = JsonUtil.getAuthorFromJson(reviewJsonString);
            mReviews = JsonUtil.getReviewFromJson(reviewJsonString);
        }
    }


}

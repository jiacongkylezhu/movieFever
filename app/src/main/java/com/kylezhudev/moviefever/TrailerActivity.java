package com.kylezhudev.moviefever;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TrailerActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{

    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        Intent intent = getIntent();
        videoId = intent.getStringExtra(MovieDetailActivity.TRAILER_INTENT_KEY);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youTube_activity_playerView);
        youTubePlayerView.initialize(APIKeys.YOUTUBE_API_KEY, this);
        youTubePlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(youTubePlayer != null){
                    youTubePlayer.cueVideo(videoId);
                }
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.youTubePlayer = player;
        if(!wasRestored && videoId != null){
            player.loadVideo(videoId, 1);
            player.setFullscreen(true);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        this.youTubePlayer = null;
    }
}

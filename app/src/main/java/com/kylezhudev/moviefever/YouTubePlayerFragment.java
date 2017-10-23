package com.kylezhudev.moviefever;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class YouTubePlayerFragment extends Fragment {

    private YouTubePlayer youTubePlayer;
    private String videoId;


    public YouTubePlayerFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        YouTubePlayerSupportFragment youTubePlayerSupportFragment =
                YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youTube_playerView, youTubePlayerSupportFragment).commit();

        youTubePlayerSupportFragment.initialize(APIKeys.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if(!wasRestored && youTubePlayer != null){
                    youTubePlayer = player;
                    videoId = getTag();
                    youTubePlayer.cueVideo(videoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                youTubePlayer = null;

            }

        });
        return inflater.inflate(R.layout.fragment_you_tube_player, container, false);
    }

}

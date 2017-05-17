package com.mahmoudtarrasse.baking.ui;


import android.database.Cursor;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.mahmoudtarrasse.baking.R;
import com.mahmoudtarrasse.baking.Utility;
import com.mahmoudtarrasse.baking.data.RecipesContract;
import com.mahmoudtarrasse.baking.modules.Ingredients;
import com.mahmoudtarrasse.baking.modules.Steps;

import org.json.JSONArray;
import org.json.JSONException;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {

    private int stepId;
    private int recipeId;
    private int numberOfSteps;

    private OnNavigation navigator;


//    private SimpleExoPlayerView playerView;
    private TextView titleTextView;
    private TextView descTextView;
    private VideoView videoView;
    private Button nextButton;
    private Button prevButton;
    private SimpleExoPlayer player;

    public StepFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        stepId = args.getInt(Utility.ARG_STEP_ID);
        recipeId = args.getInt(Utility.ARG_RECIPE_ID);

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        titleTextView = (TextView) rootView.findViewById(R.id.step_name);
        descTextView = (TextView) rootView.findViewById(R.id.step_description);
        videoView = (VideoView) rootView.findViewById(R.id.video_view);
        prevButton = (Button) rootView.findViewById(R.id.prev_button);
        if (prevButton != null)
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previous();
                }
            });
        nextButton = (Button) rootView.findViewById(R.id.next_button);

        if (nextButton != null)
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    next();
                }
            });


        Timber.d(recipeId + " " + stepId);

        getLoaderManager().initLoader(recipeId, null, loader);

        return rootView;
    }


    LoaderManager.LoaderCallbacks<Cursor> loader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), RecipesContract.RecipeTable.buildRecipeId(recipeId),
                    null, null,
                    null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            data.moveToFirst();
            String stepsJson = data.getString(data.getColumnIndex(RecipesContract.RecipeTable.STEPS_JSON));
            Gson gson = new Gson();
            try {
                JSONArray stepList = new JSONArray(stepsJson);
                Steps step = gson.fromJson(stepList.get(stepId).toString(), Steps.class);
                titleTextView.setText(step.getShortDescription());
                descTextView.setText(step.getDescription());
                numberOfSteps = stepList.length();

                if(step.getVideoURL() != null && !(step.getVideoURL().isEmpty())){
                    Timber.d(step.getVideoURL());
                    playVideo(step.getVideoURL());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Timber.d(stepsJson);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void playVideo(String url)
    {
        try
        {
            final MediaController mediaController = new MediaController(getActivity());
            mediaController.setAnchorView(videoView);

            Uri video = Uri.parse(url);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                public void onPrepared(MediaPlayer mp)
                {
                    videoView.start();
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            mediaController.setAnchorView(videoView);
                        }
                    });
                }
            });

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    public void next(){
        if (navigator != null){
            if (stepId >= (numberOfSteps-1))
                navigator.OnSwitch(0);
            else
                navigator.OnSwitch((stepId + 1));
        }
    }

    public void previous(){
        if (navigator != null){
            if (stepId == 0)
                navigator.OnSwitch((numberOfSteps - 1));
            else
                navigator.OnSwitch((stepId - 1));
        }
    }


    public OnNavigation getNavigator() {
        return navigator;
    }

    public void setNavigator(OnNavigation navigator) {
        this.navigator = navigator;
    }

    public interface OnNavigation{
        public void OnSwitch (int position);
    }

}

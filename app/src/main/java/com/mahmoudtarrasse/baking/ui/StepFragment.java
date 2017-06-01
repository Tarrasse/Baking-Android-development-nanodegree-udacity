package com.mahmoudtarrasse.baking.ui;


import android.database.Cursor;
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
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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


    private SimpleExoPlayerView playerView;
    private TextView titleTextView;
    private TextView descTextView;
    private TextView noVideoView;

    private SimpleExoPlayer mPlayer;

    private long resumePosition = C.POSITION_UNSET;


    public StepFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        stepId = args.getInt(Utility.ARG_STEP_ID);
        recipeId = args.getInt(Utility.ARG_RECIPE_ID);

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        playerView = (SimpleExoPlayerView) rootView.findViewById(R.id.exo_player);
        titleTextView = (TextView) rootView.findViewById(R.id.step_name);
        descTextView = (TextView) rootView.findViewById(R.id.step_description);
        noVideoView = (TextView) rootView.findViewById(R.id.no_video_available);
        Button prevButton = (Button) rootView.findViewById(R.id.prev_button);
        if (prevButton != null)
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previous();
                }
            });
        Button nextButton = (Button) rootView.findViewById(R.id.next_button);

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
                }else{
                    noVideoView.setVisibility(View.VISIBLE);

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

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), getActivity().getString(R.string.app_name)));
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            playerView.setPlayer(mPlayer);

            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(url),
                    dataSourceFactory, extractorsFactory, null, null);

            mPlayer.prepare(videoSource);
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

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null){
            resumePosition = mPlayer.isCurrentWindowSeekable() ? Math.max(0, mPlayer.getCurrentPosition()) : C.TIME_UNSET;
            mPlayer.release();
        }
    }
}

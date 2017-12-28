package youtubeapidemo.examples.com.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static youtubeapidemo.examples.com.bakingapp.R.id.playerView;

public class DescriptionActivityFragment extends Fragment {
    public static final String STEP_NO = "step_no";
    private static final String CURRENT_WINDOW = "current_window";
    private static final String PLAY_BACK_POSITION = "playback_position";
    private static final String PLAY_WHEN_READY = "play_when_ready";
    private static final String SCROLL_POS_X = "scroll_view_position_x";
    private static final String SCROLL_POS_Y = "scroll_view_position_y";
    private SimpleExoPlayerView mPlayerView;
    private Button mPreviousButton, mNextButton;
    private ArrayList<Description> mDescription;
    public static int mCurrentPosition = 0;
    private TextView mStep, mDescriptionPosition;
    private int pos = 0;
    private TextView mNoVideoText;
    private ImageView mImageView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private static boolean playWhenReady;
    private int currentWindow=0;
    private long playbackPosition=0;
    private ScrollView mScrollView;
    private Bundle bundle;

    public DescriptionActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_description_activity, container, false);
        mPreviousButton = (Button) rootView.findViewById(R.id.previous_button);
        mNextButton = (Button) rootView.findViewById(R.id.next_button);
        mStep = (TextView) rootView.findViewById(R.id.step_description);
        mDescriptionPosition = (TextView) rootView.findViewById(R.id.description_position);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(playerView);
        mImageView = (ImageView) rootView.findViewById(R.id.no_video_image);
        mDescription = new ArrayList<>();
        mScrollView = (ScrollView) rootView.findViewById(R.id.scrollview);
        mPreviousButton.setVisibility(View.INVISIBLE);
        mNextButton.setVisibility(View.INVISIBLE);
        bundle = savedInstanceState;
        Bundle args = getArguments();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
                getResources().getBoolean(R.bool.is_mobile)) {
            if (args != null) {
                pos = args.getInt(DescriptionActivity.ARG_DES, 0);
            }
        } else {
            if (args != null) {
                pos = args.getInt(IngredientActivity.ARG, 0);
            }
        }
        if (savedInstanceState == null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                    && getResources().getBoolean(R.bool.is_tablet))
                mCurrentPosition = 0;
        } else if (savedInstanceState.containsKey(STEP_NO)) {
            mCurrentPosition = savedInstanceState.getInt(STEP_NO, 0);
        //    if (getResources().getBoolean(R.bool.is_mobile )) {
                playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY, false);
                playbackPosition = savedInstanceState.getLong(PLAY_BACK_POSITION, 0);
                currentWindow = savedInstanceState.getInt(CURRENT_WINDOW, 0);
        //    }
        }
        mNoVideoText = (TextView) rootView.findViewById(R.id.no_video_text);
        return rootView;
    }


    private void display() {

        SetUpPreviousButton();
        SetUpNextButton();
    }

    private void SetUpPreviousButton() {
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentPosition--;
                if (mCurrentPosition == 0) {
                    mPreviousButton.setVisibility(View.INVISIBLE);
                }
                mImageView.setVisibility(View.INVISIBLE);
                if (mNoVideoText != null)
                    mNoVideoText.setVisibility(View.INVISIBLE);
                mStep.setText(mDescription.get(mCurrentPosition).getDescription());
                mNextButton.setVisibility(View.VISIBLE);
                mDescriptionPosition.setText("" + (mCurrentPosition) + "/" + (mDescription.size() - 1));
                mStep.setText(mDescription.get(mCurrentPosition).getDescription());
                releaseVideoPlayer();
                showCurrentStep();
            }
        });
    }

    void releaseVideoPlayer() {
        if (mSimpleExoPlayer != null) {
            playbackPosition = mSimpleExoPlayer.getCurrentPosition();
            currentWindow = mSimpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mSimpleExoPlayer.release();
        }
        mSimpleExoPlayer = null;
    }


    private void SetUpNextButton() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentPosition++;
                if (mCurrentPosition == mDescription.size() - 1) {
                    mNextButton.setVisibility(View.INVISIBLE);
                }
                if (mNoVideoText != null)
                    mNoVideoText.setVisibility(View.INVISIBLE);
                mImageView.setVisibility(View.INVISIBLE);
                mStep.setText(mDescription.get(mCurrentPosition).getDescription());
                mPreviousButton.setVisibility(View.VISIBLE);
                mDescriptionPosition.setText("" + (mCurrentPosition) + "/" + (mDescription.size() - 1));
                releaseVideoPlayer();
                showCurrentStep();
            }
        });
    }

    private void makeJsonArrayRequest() {
        String mUrlBaking = BakingUtility.BAKING_URL;
        JsonArrayRequest req = new JsonArrayRequest(mUrlBaking,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject recipe = (JSONObject) response.get(pos);
                            JSONArray steps = recipe.getJSONArray(BakingUtility.STEPS);
                            for (int i = 0; i < steps.length(); i++) {
                                JSONObject step = (JSONObject) steps.get(i);
                                int id = step.getInt(BakingUtility.ID);
                                String shortDescription = step.getString(BakingUtility
                                        .SHORT_DESCRIPTION);
                                String describe = step.getString(BakingUtility.DESCRIPTION);
                                String videoURL = step.getString(BakingUtility.VIDEO_URL);
                                String thumbnailURL = step.getString(BakingUtility.THUMBNAIL_URL);
                                mDescription.add(new Description(id, shortDescription,
                                        describe, videoURL, thumbnailURL));
                            }

                            showCurrentStep();
                            display();
                            mStep.setText(mDescription.get(mCurrentPosition).getDescription());
                            mDescriptionPosition.setVisibility(View.VISIBLE);
                            mDescriptionPosition.setText("" + (mCurrentPosition) + "/"
                                    + (mDescription.size() - 1));
                            mNextButton.setVisibility(View.VISIBLE);
                            mPreviousButton.setVisibility(View.VISIBLE);
                            if (mCurrentPosition == mDescription.size() - 1)
                                mNextButton.setVisibility(View.INVISIBLE);
                            else if (mCurrentPosition == 0)
                                mPreviousButton.setVisibility(View.INVISIBLE);
                            if (bundle != null &&
                                    getResources().getConfiguration().orientation ==
                                            Configuration.ORIENTATION_LANDSCAPE) {
                                mScrollView.scrollTo(bundle.getInt(SCROLL_POS_X),
                                        bundle.getInt(SCROLL_POS_Y));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "Error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);
    }


    public void showCurrentStep() {
        if (mPlayerView != null)
            mPlayerView.setVisibility(View.VISIBLE);
        final String url = mDescription.get(mCurrentPosition).getVideoURL();
        if (url == null || url.isEmpty()) {
            if (mPlayerView != null)
                mPlayerView.setVisibility(View.GONE);
            String video_img_url = mDescription.get(mCurrentPosition).getThumbnailURL();
            if (video_img_url != null && !video_img_url.isEmpty()) {
                mImageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(video_img_url)
                        .placeholder(R.drawable.covered_food_tray_on_a_hand_of_hotel_room_service)
                        .error(R.drawable.covered_food_tray_on_a_hand_of_hotel_room_service)
                        .into(mImageView);
            } else if (mNoVideoText != null)
                mNoVideoText.setVisibility(View.VISIBLE);
            return;
        } else if (mPlayerView != null) {
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(mSimpleExoPlayer);
            Uri uri = Uri.parse(url);
            initializePlayer(uri);
        }
    }

    private void initializePlayer(final Uri uri) {
        MediaSource mediaSource = buildMediaSource(uri);
        //  mSimpleExoPlayer.prepare(mediaSource, true, false);
        mSimpleExoPlayer.setPlayWhenReady(playWhenReady);
        mSimpleExoPlayer.seekTo(currentWindow, playbackPosition);
        mSimpleExoPlayer.prepare(mediaSource);
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseVideoPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseVideoPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseVideoPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        makeJsonArrayRequest();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_NO, mCurrentPosition);
        //  outState.putLong(SEEK_POSITION,ExoPlayerHandler.mSimpleExoPlayer.getCurrentPosition());
        outState.putInt(CURRENT_WINDOW, currentWindow);
        outState.putLong(PLAY_BACK_POSITION, playbackPosition);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        if (mScrollView != null) {
            outState.putInt(SCROLL_POS_X, mScrollView.getScrollX());
            outState.putInt(SCROLL_POS_Y, mScrollView.getScrollY());
        }
    }


    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }


}

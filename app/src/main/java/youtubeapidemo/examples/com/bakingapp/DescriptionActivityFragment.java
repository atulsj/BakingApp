package youtubeapidemo.examples.com.bakingapp;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static youtubeapidemo.examples.com.bakingapp.R.id.playerView;

public class DescriptionActivityFragment extends Fragment {
    public static final String VIDEO_URL = "video_url";
    public static final String STEP_NO = "step_no";

    private SimpleExoPlayerView mPlayerView;
    private Button mPreviousButton, mNextButton;
    private ArrayList<Description> mDescription;
    public static int mCurrentPosition = 0;
    private TextView mStep, mDescriptionPosition;
    private int pos = 0;
    private View rootView;
    private TextView mNoVideoText;

    private ImageView mImageView;

    public DescriptionActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_description_activity, container, false);
        mPreviousButton = (Button) rootView.findViewById(R.id.previous_button);
        mNextButton = (Button) rootView.findViewById(R.id.next_button);
        mStep = (TextView) rootView.findViewById(R.id.step_description);
        mDescriptionPosition = (TextView) rootView.findViewById(R.id.description_position);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(playerView);
        mImageView = (ImageView) rootView.findViewById(R.id.no_video_image);
        mDescription = new ArrayList<>();
        Configuration configuration = getActivity().getResources().getConfiguration();

        mPreviousButton.setVisibility(View.INVISIBLE);
        mNextButton.setVisibility(View.INVISIBLE);

        Bundle args = getArguments();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (args != null) {
                pos = args.getInt(DescriptionActivity.ARG_DES, 0);
            }
        } else {
            if (args != null) {
                pos = args.getInt(IngredientActivity.ARG, 0);
            }
        }
        if (savedInstanceState == null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                mCurrentPosition = 0;

        } else if (savedInstanceState.containsKey(STEP_NO)) {
            mCurrentPosition = savedInstanceState.getInt(STEP_NO);
        }
        makeJsonArrayRequest();

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
                if (mNoVideoText != null)
                    mNoVideoText.setVisibility(View.INVISIBLE);
                mStep.setText(mDescription.get(mCurrentPosition).getDescription());
                mNextButton.setVisibility(View.VISIBLE);
                mDescriptionPosition.setText("" + (mCurrentPosition) + "/" + (mDescription.size() - 1));
                mStep.setText(mDescription.get(mCurrentPosition).getDescription());
                ExoPlayerHandler.getInstance().goToBackground();
                ExoPlayerHandler.getInstance().releaseVideoPlayer();
                initializePlayer();
            }
        });
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
                mStep.setText(mDescription.get(mCurrentPosition).getDescription());
                mPreviousButton.setVisibility(View.VISIBLE);
                mDescriptionPosition.setText("" + (mCurrentPosition) + "/" + (mDescription.size() - 1));

                ExoPlayerHandler.getInstance().goToBackground();
                ExoPlayerHandler.getInstance().releaseVideoPlayer();
                initializePlayer();
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
                                String shortDescription = step.getString(BakingUtility.SHORT_DESCRIPTION);
                                String describe = step.getString(BakingUtility.DESCRIPTION);
                                String videoURL = step.getString(BakingUtility.VIDEO_URL);
                                String thumbnailURL = step.getString(BakingUtility.THUMBNAIL_URL);
                                mDescription.add(new Description(id, shortDescription,
                                        describe, videoURL, thumbnailURL));
                            }

                            display();
                            initializePlayer();
                            mStep.setText(mDescription.get(mCurrentPosition).getDescription());
                            mDescriptionPosition.setVisibility(View.VISIBLE);
                            mDescriptionPosition.setText("" + (mCurrentPosition) + "/" + (mDescription.size() - 1));
                            mNextButton.setVisibility(View.VISIBLE);
                            mPreviousButton.setVisibility(View.VISIBLE);
                            if (mCurrentPosition == mDescription.size()-1)
                                mNextButton.setVisibility(View.INVISIBLE);
                            else
                            if (mCurrentPosition == 0)
                                mPreviousButton.setVisibility(View.INVISIBLE);


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

    private void initializePlayer() {
        if (mPlayerView != null)
            mPlayerView.setVisibility(View.VISIBLE);
        final String url = mDescription.get(mCurrentPosition).getVideoURL();
        if (url == null || url.isEmpty()) {
            if (mPlayerView != null)
                mPlayerView.setVisibility(View.GONE);
            if (mNoVideoText != null)
                mNoVideoText.setVisibility(View.VISIBLE);
            return;
        }

        if (mPlayerView != null) {
            ExoPlayerHandler.getInstance()
                    .prepareExoPlayerForUri(rootView.getContext(),
                            Uri.parse(url), mPlayerView);
            ExoPlayerHandler.getInstance().goToForeground();
            rootView.findViewById(R.id.exo_fullscreen_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context context = rootView.getContext();
                            Intent intent = new Intent(context,
                                    FullScreenVideoActivity.class);
                            intent.putExtra(VIDEO_URL, url);
                            context.startActivity(intent);
                        }
                    });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ExoPlayerHandler.getInstance().goToBackground();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ExoPlayerHandler.getInstance().releaseVideoPlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        ExoPlayerHandler.getInstance().releaseVideoPlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_NO, mCurrentPosition);
        //  outState.putLong(SEEK_POSITION,ExoPlayerHandler.mSimpleExoPlayer.getCurrentPosition());
    }

}

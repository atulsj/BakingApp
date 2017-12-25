package youtubeapidemo.examples.com.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceView;

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


public class ExoPlayerHandler {
    private static ExoPlayerHandler instance;
    public  SimpleExoPlayer mSimpleExoPlayer;
    private Uri playerUri;
    private boolean isPlayerPlaying=true;

    static ExoPlayerHandler getInstance() {
        if (instance == null) {
            instance = new ExoPlayerHandler();
        }
        return instance;
    }

    private ExoPlayerHandler() {
    }

    void prepareExoPlayerForUri(Context context, Uri uri,
                                SimpleExoPlayerView exoPlayerView) {
        if (context != null && uri != null && exoPlayerView != null) {
            if (!uri.equals(playerUri) || mSimpleExoPlayer == null) {
                // Create a new mSimpleExoPlayer if the mSimpleExoPlayer is null or
                // we want to play a new video
                mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(context),
                        new DefaultTrackSelector(), new DefaultLoadControl());
                exoPlayerView.setPlayer(mSimpleExoPlayer);
                playerUri = uri;
                // Do all the standard ExoPlayer code here...
                // Prepare the mSimpleExoPlayer with the source.
                MediaSource mediaSource = buildMediaSource(uri);
                mSimpleExoPlayer.prepare(mediaSource, true, false);
                mSimpleExoPlayer.setPlayWhenReady(isPlayerPlaying);
            }
            mSimpleExoPlayer.clearVideoSurface();
            mSimpleExoPlayer.setVideoSurfaceView(
                    (SurfaceView) exoPlayerView.getVideoSurfaceView());
            mSimpleExoPlayer.seekTo(mSimpleExoPlayer.getCurrentPosition()+1);
            exoPlayerView.setPlayer(mSimpleExoPlayer);
        }
    }


    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    void releaseVideoPlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.release();
        }
        mSimpleExoPlayer = null;
    }

    void goToBackground() {
        if (mSimpleExoPlayer != null) {
            isPlayerPlaying = mSimpleExoPlayer.getPlayWhenReady();
            mSimpleExoPlayer.setPlayWhenReady(false);
        }
    }

    void goToForeground() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.setPlayWhenReady(isPlayerPlaying);
        }
    }


}

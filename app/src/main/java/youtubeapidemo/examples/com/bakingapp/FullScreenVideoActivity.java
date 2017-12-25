package youtubeapidemo.examples.com.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class FullScreenVideoActivity extends AppCompatActivity {

    private boolean destroyVideo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent=getIntent();
        String url = null;
        if(intent!=null && intent.hasExtra(DescriptionActivityFragment.VIDEO_URL)){
            url=intent.getStringExtra(DescriptionActivityFragment.VIDEO_URL);
        }
        SimpleExoPlayerView exoPlayerView =
                (SimpleExoPlayerView)findViewById(R.id.playerView);
        ExoPlayerHandler exoPlayerHandler=ExoPlayerHandler.getInstance();
                exoPlayerHandler.prepareExoPlayerForUri(getApplicationContext(),
                        Uri.parse(url),exoPlayerView);
        exoPlayerHandler.goToForeground();

        findViewById(R.id.exo_fullscreen_button).setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        destroyVideo = false;
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed(){
        destroyVideo = false;
        super.onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
        ExoPlayerHandler.getInstance().goToBackground();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(destroyVideo){
            ExoPlayerHandler.getInstance().releaseVideoPlayer();
        }
    }
}

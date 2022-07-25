package com.dunkeydev.rootstreaming;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fullscreen extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView playerView;
    TextView textView;
    boolean fullscreen = false;
    ImageView fullscreenButton;
    private String url;
    private boolean playwhenready = false;
    private  int currentWindow = 0;
    private  long playbackposition = 0;
    private CustomDialog customDialog;
    private DefaultTrackSelector trackSelector;
    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector.Parameters trackSelectorParameters;
    private ImageButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Fullscreen");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        playerView = findViewById(R.id.exoplayer_fullscreen);
        textView = findViewById(R.id.tv_fullscreen);

        customDialog = new CustomDialog(Fullscreen.this);

        fullscreenButton = playerView.findViewById(R.id.exoplayer_fullscreen_icon);
        button = findViewById(R.id.exo_track_selection_view_btn);
        trackSelector = new DefaultTrackSelector();


        Intent intent = getIntent();
        url = intent.getExtras().getString("ur");
        String title = intent.getExtras().getString("nam");

        textView.setText(title);

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fullscreen){
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(Fullscreen.this,R.drawable.ic_fullscreen_expand));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if (getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                }else {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(Fullscreen.this,R.drawable.ic_fullscreen_skrink));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if (getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;

                    textView.setVisibility(View.GONE);


                }
            }
        });
        player = ExoPlayerFactory.newSimpleInstance(this);
        playerView.setPlayer(player);
        Uri uri = Uri.parse(url);
        MediaSource mediaSource = buildMediaSource(uri);
        player.seekTo(currentWindow,playbackposition);
        player.prepare(mediaSource,false,false);
        player.setPlayWhenReady(false);
        playerView.setRewindIncrementMs(10000);
        playerView.setFastForwardIncrementMs(10000);
        playerView.setKeepScreenOn(true);



        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);

                if (playbackState == Player.STATE_READY)
                {
                    customDialog.DismissDialog();

                }
                else if (playbackState == Player.STATE_BUFFERING)
                {
                    customDialog.ShowDialog();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (!isShowingTrackSelectionDialog && TrackSelectionDialog.willHaveContent(trackSelector)) {
                        isShowingTrackSelectionDialog = true;
                        TrackSelectionDialog trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(trackSelector,/* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
                        trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
                    }
                }
            }
        });


    }


    private MediaSource buildMediaSource(Uri uri){


        DataSource.Factory datasourcefactory =
                new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "player"));
        return  new ProgressiveMediaSource.Factory(datasourcefactory)
                .createMediaSource(uri);
    }


    @Override
    protected void onStart() {
        super.onStart();
        customDialog.ShowDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    private void releasePlayer(){
        if (player != null){
            playwhenready = player.getPlayWhenReady();
            playbackposition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        player.stop();

        final Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

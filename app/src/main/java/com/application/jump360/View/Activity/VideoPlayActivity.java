package com.application.jump360.View.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.application.jump360.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayActivity extends AppCompatActivity {

    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.ivPrev)
    ImageView ivPrev;
    @BindView(R.id.ivPause)
    ImageView ivPause;
    @BindView(R.id.ivNext)
    ImageView ivNext;

    int video_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setVideo();
    }

    public void setVideo() {
        video_index = getIntent().getIntExtra("position", 0);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video_index++;
                if (video_index < (MainActivity.videoArrayList.size())) {
                    playVideo(video_index);
                } else {
                    video_index = 0;
                    playVideo(video_index);
                }
            }
        });
        playVideo(video_index);
    }

    // play video file
    public void playVideo(int pos) {
        try {
            videoView.setVideoURI(MainActivity.videoArrayList.get(pos).getPath());
            videoView.start();
            ivPause.setImageResource(R.drawable.ic_pause);
            video_index = pos;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //play previous video
    @OnClick(R.id.ivPrev)
    public void previousVideo() {
        if (video_index > 0) {
            video_index--;
            playVideo(video_index);
        } else {
            video_index = MainActivity.videoArrayList.size() - 1;
            playVideo(video_index);
        }
    }

    //play next video
    @OnClick(R.id.ivNext)
    public void nextVideo() {
        if (video_index < (MainActivity.videoArrayList.size() - 1)) {
            video_index++;
            playVideo(video_index);
        } else {
            video_index = 0;
            playVideo(video_index);
        }

    }

    //pause video
    @OnClick(R.id.ivPause)
    public void pauseVideo() {
        if (videoView.isPlaying()) {
            videoView.pause();
            ivPause.setImageResource(R.drawable.ic_play);
        } else {
            videoView.start();
            ivPause.setImageResource(R.drawable.ic_pause);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}
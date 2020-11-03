package com.application.jump360.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.VideoView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.application.jump360.Models.AVIModel;
import com.application.jump360.R;
import com.application.jump360.Utils.AppEnum;
import com.application.jump360.View.Activity.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

import static com.application.jump360.Utils.FilePickManager.getSelectedAVI;

public class AVIViewModel extends ViewModel {

    private MutableLiveData<ArrayList<AVIModel>> listOfData = new MutableLiveData<ArrayList<AVIModel>>();
    public ArrayList<AVIModel> list = new ArrayList<AVIModel>();

    public AVIViewModel() {
    }

    public MutableLiveData<ArrayList<AVIModel>> getAVIData(Activity activity, AppEnum.AVIType type, Intent data) {
        list.addAll(getSelectedAVI(activity, data, type));
        listOfData.setValue(list);
        return listOfData;
    }

    public void setAudioData(MediaPlayer mp, Activity context, Uri path, int position) {

        try {
            mp.reset();
            mp.setDataSource(context, path);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlay(MediaPlayer mp, Activity context, Uri path, int position) {
        if (mp.isPlaying()) {
            mp.stop();
        }
        setAudioData(mp, context, path, position);
    }

    public void setPause(MediaPlayer mp) {
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

    // play video file
    public void playVideo(int pos, VideoView videoView) {
        try {
            videoView.setVideoURI(MainActivity.videoArrayList.get(pos).getPath());

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("JAY", "playVideo: " + e);
        }
    }

    public void pauseVideo(VideoView videoView) {
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }
}


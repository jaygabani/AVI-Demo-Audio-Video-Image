package com.application.jump360.Models;

import android.net.Uri;

import com.application.jump360.Utils.AppEnum;

public class AVIModel {

    public Uri path;
    public String title;
    public AppEnum.AVIType type;
    public boolean isPlaying = false;

    public AVIModel(Uri path, String title, AppEnum.AVIType type) {
        this.path = path;
        this.title = title;
        this.type = type;
    }

    public Uri getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }


    public AppEnum.AVIType getType() {
        return type;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
package com.application.jump360.Utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.application.jump360.Models.AVIModel;

import java.util.ArrayList;
import java.util.List;

import static com.application.jump360.Utils.Util.REQUEST_AUDIO_CODE;
import static com.application.jump360.Utils.Util.REQUEST_IMAGE_CODE;
import static com.application.jump360.Utils.Util.REQUEST_VIDEO_CODE;

public class FilePickManager {


    public static void pickFile(Activity activity, AppEnum.AVIType type) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        switch (type) {
            case VIDEO:
                intent.setType("video/*");
                activity.startActivityForResult(intent, REQUEST_VIDEO_CODE);
                break;
            case AUDIO:
                intent.setType("audio/*");
                activity.startActivityForResult(intent, REQUEST_AUDIO_CODE);
                break;
            case IMAGE:
                intent.setType("image/*");
                activity.startActivityForResult(intent, REQUEST_IMAGE_CODE);
                break;
            default:
                break;
        }
    }

    // start-> get selected file based on it's type
    public static List<AVIModel> getSelectedAVI(Activity activity, Intent data, AppEnum.AVIType type) {

        List<AVIModel> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                result.add(splitData(uri, type));
            }
        } else {
            Uri uri = data.getData();
            result.add(splitData(uri, type));
        }

        return result;
    }
    // end


    public static AVIModel splitData(Uri uri, AppEnum.AVIType type) {
        String path = uri.getPath();
        String splitName = path.substring(path.lastIndexOf("/") + 1, path.length());
        String[] name = splitName.split("\\.");
        Log.i("JAY", "getSelectedAVI: " + path + "\n name" + splitName + " " + name[0]);
        AVIModel model = new AVIModel(uri, name[0], type);

        return model;
    }
}

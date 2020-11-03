package com.application.jump360.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.jump360.Models.AVIModel;
import com.application.jump360.R;
import com.application.jump360.Utils.AppEnum;
import com.application.jump360.Utils.Util;
import com.application.jump360.View.Adapters.AVIAdapter;
import com.application.jump360.ViewModel.AVIViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.application.jump360.Utils.FilePickManager.pickFile;
import static com.application.jump360.Utils.Util.REQUEST_AUDIO_CODE;
import static com.application.jump360.Utils.Util.REQUEST_IMAGE_CODE;
import static com.application.jump360.Utils.Util.REQUEST_PERMISSION_CODE;
import static com.application.jump360.Utils.Util.REQUEST_VIDEO_CODE;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rcvAVI)
    RecyclerView rcvAVI;
    @BindView(R.id.txtNoData)
    TextView txtNoData;

    private Activity activity = MainActivity.this;
    AVIViewModel aviViewModel;
    AVIAdapter adapter;

    public static ArrayList<AVIModel> videoArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        videoArrayList = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content_type, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuImage:
                if (Util.getInstance().checkPermission(activity))
                    pickFile(activity, AppEnum.AVIType.IMAGE);
                else
                    requestPermission();
                return true;
            case R.id.menuAudio:
                if (Util.getInstance().checkPermission(activity))
                    pickFile(activity, AppEnum.AVIType.AUDIO);
                else
                    requestPermission();
                return true;
            case R.id.menuVideo:
                if (Util.getInstance().checkPermission(activity))
                    pickFile(activity, AppEnum.AVIType.VIDEO);
                else
                    requestPermission();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void requestPermission() {
        Util.getInstance().requestPermission(activity);
    }

    private void setDataIntoView(Intent data, AppEnum.AVIType type) {
        aviViewModel = new ViewModelProvider(this).get(AVIViewModel.class);
        aviViewModel.getAVIData(activity, type, data).observe(this, new Observer<ArrayList<AVIModel>>() {
            @Override
            public void onChanged(ArrayList<AVIModel> aviList) {
                if (aviList != null && aviList.size() > 0) {
                    txtNoData.setVisibility(View.GONE);
                    rcvAVI.setVisibility(View.VISIBLE);
                    videoArrayList.clear();
                    videoArrayList.addAll(aviList);
                    adapter = new AVIAdapter(activity, aviList, aviViewModel);
                    rcvAVI.setHasFixedSize(true);
                    rcvAVI.setAdapter(adapter);
                } else {
                    rcvAVI.setVisibility(View.GONE);
                    txtNoData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CODE) {
                setDataIntoView(data, AppEnum.AVIType.VIDEO);
            } else if (requestCode == REQUEST_AUDIO_CODE) {
                setDataIntoView(data, AppEnum.AVIType.AUDIO);
            } else if (requestCode == REQUEST_IMAGE_CODE) {
                setDataIntoView(data, AppEnum.AVIType.IMAGE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!Util.getInstance().checkPermission(activity)) {
                        Util.getInstance().comnToast(activity, getResources().getString(R.string.permission_txt));
                    }
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
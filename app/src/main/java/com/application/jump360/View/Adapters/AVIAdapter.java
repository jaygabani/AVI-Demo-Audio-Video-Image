package com.application.jump360.View.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.jump360.Models.AVIModel;
import com.application.jump360.R;
import com.application.jump360.Utils.AppEnum;
import com.application.jump360.View.Activity.ImageViewActivity;
import com.application.jump360.View.Activity.VideoPlayActivity;
import com.application.jump360.ViewModel.AVIViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AVIAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AVIModel> items;
    private Activity context;
    private static final int VIEW_IMAGE = 1;
    private static final int VIEW_AV = 2;
    private MediaPlayer mediaPlayer;
    private AVIViewModel aviViewModel;
    private int lastPosition = -1;

    public AVIAdapter(Activity context, List<AVIModel> items, AVIViewModel aviViewModel) {
        this.items = items;
        this.context = context;
        this.aviViewModel = aviViewModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_IMAGE) {
            final View v = inflater.inflate(R.layout.raw_image_item, parent, false);
            viewHolder = new ImageViewHolder(v);
        } else {
            final View v = inflater.inflate(R.layout.raw_av_item, parent, false);
            viewHolder = new AVViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("JAY", "onBindViewHolder: " + position);

        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).ivImage.setImageURI(items.get(position).getPath());

            ((ImageViewHolder) holder).ivImage.setOnClickListener(view -> {
                context.startActivity(new Intent(context, ImageViewActivity.class)
                        .putExtra("path", items.get(position).getPath().toString()));
                context.overridePendingTransition(R.anim.from_right, R.anim.to_left);
            });

        } else if (holder instanceof AVViewHolder) {
            AVViewHolder viewHolder = ((AVViewHolder) holder);
            viewHolder.txtTitle.setText(items.get(position).getTitle());

            switch (items.get(position).getType()) {
                case VIDEO:

                    viewHolder.ivPlay.setVisibility(View.GONE);
                    viewHolder.ivStop.setVisibility(View.GONE);
                    viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video));

                    viewHolder.cvAV.setOnClickListener(view -> {
                        context.startActivity(new Intent(context, VideoPlayActivity.class)
                                .putExtra("position", position));
                        context.overridePendingTransition(R.anim.from_right, R.anim.to_left);

                    });
                    break;
                case AUDIO:
                    viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio));

                    viewHolder.ivPlay.setOnClickListener(view -> {
                        lastPosition = position;
                        viewHolder.ivPlay.setColorFilter(context.getResources().getColor(R.color.colorAccent));
                        notifyDataSetChanged();
                        aviViewModel.setPlay(mediaPlayer, context, items.get(position).getPath(), position);
                    });

                    viewHolder.ivStop.setOnClickListener(view -> {
                        if (lastPosition == position) {
                            viewHolder.ivPlay.setColorFilter(context.getResources().getColor(R.color.black));
                            aviViewModel.setStop(mediaPlayer);
                        }
                    });
                    break;
            }
            if (lastPosition != position) {
                viewHolder.ivPlay.setColorFilter(context.getResources().getColor(R.color.black));
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getType().equals(AppEnum.AVIType.IMAGE)) {
            return VIEW_IMAGE;
        } else {
            return VIEW_AV;
        }
    }

    public class AVViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvAV)
        MaterialCardView cvAV;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.ivPlay)
        ImageView ivPlay;
        @BindView(R.id.ivStop)
        ImageView ivStop;

        public AVViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mediaPlayer = new MediaPlayer();
        }

    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.cvImage)
        MaterialCardView cvImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
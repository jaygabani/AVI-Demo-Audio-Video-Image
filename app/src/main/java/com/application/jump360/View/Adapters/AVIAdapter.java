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
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.jump360.Models.AVIModel;
import com.application.jump360.R;
import com.application.jump360.Utils.AppEnum;
import com.application.jump360.View.Activity.ImageViewActivity;
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

        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).ivImage.setImageURI(items.get(position).getPath());

            ((ImageViewHolder) holder).ivImage.setOnClickListener(view -> {
                lastPosition = -1;
                aviViewModel.setPause(mediaPlayer);
                notifyDataSetChanged();

                context.startActivity(new Intent(context, ImageViewActivity.class)
                        .putExtra("path", items.get(position).getPath().toString()));
                context.overridePendingTransition(R.anim.from_right, R.anim.to_left);
            });

        } else if (holder instanceof AVViewHolder) {
            AVViewHolder viewHolder = ((AVViewHolder) holder);
            viewHolder.txtTitle.setText(items.get(position).getTitle());

            switch (items.get(position).getType()) {
                case VIDEO:

                    viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video));

                    viewHolder.ivPlay.setOnClickListener(view -> {
                        aviViewModel.setPause(mediaPlayer);

                        if (lastPosition == position && items.get(position).isPlaying()) {
                            lastPosition = -1;
                            viewHolder.ivPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            notifyDataSetChanged();

                            viewHolder.videoView.setVisibility(View.GONE);
                            viewHolder.ivImage.setVisibility(View.VISIBLE);
                            aviViewModel.pauseVideo(viewHolder.videoView);

                        } else {
                            lastPosition = position;
                            viewHolder.ivPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
                            notifyDataSetChanged();
                            viewHolder.ivImage.setVisibility(View.GONE);
                            viewHolder.videoView.setVisibility(View.VISIBLE);
                            items.get(position).setPlaying(true);
                            aviViewModel.playVideo(position, viewHolder.videoView);
                        }
                    });

                    break;
                case AUDIO:
                    viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio));
                    viewHolder.ivPlay.setOnClickListener(view -> {
                        if (lastPosition == position && items.get(position).isPlaying()) {
                            lastPosition = -1;
                            viewHolder.ivPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            notifyDataSetChanged();
                            viewHolder.ivPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                            aviViewModel.setPause(mediaPlayer);
                        } else {
                            lastPosition = position;
                            viewHolder.ivPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
                            notifyDataSetChanged();
                            items.get(position).setPlaying(true);
                            aviViewModel.setPlay(mediaPlayer, context, items.get(position).getPath(), position);
                        }
                    });
                    break;
            }

            if (lastPosition != position) {
                Log.i("JAY first ", "onBindViewHolder: " + position);
                viewHolder.videoView.setVisibility(View.GONE);
                viewHolder.ivImage.setVisibility(View.VISIBLE);
                viewHolder.ivPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                items.get(position).setPlaying(false);
                switch (items.get(position).getType()) {
                    case VIDEO:
                        viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_video));
                        aviViewModel.pauseVideo(viewHolder.videoView);
                        break;
                    case AUDIO:
                        viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio));
                        break;
                }
            }
        }
        System.gc();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
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
        @BindView(R.id.videoView)
        VideoView videoView;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.ivPlay)
        ImageView ivPlay;
        @BindView(R.id.ivPause)
        ImageView ivPause;

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
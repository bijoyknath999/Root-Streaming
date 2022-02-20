package com.dunkeydev.rootstreaming;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class ViewHolder extends RecyclerView.ViewHolder {


    public ViewHolder(@NonNull View itemView) {

        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mClickListener.onItemClick(view,getAdapterPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mClickListener.onItemLongClick(view,getAdapterPosition());
                return false;
            }
        });



    }

    public void setExoplayer(Context context, String imgurl , String name, String Videourl){

        TextView textView = itemView.findViewById(R.id.tv_item_name);
        textView.setText(name);

        ImageView imageView = itemView.findViewById(R.id.tv_item_image);
        Glide.with(context)
                .load(""+imgurl)
                .error(R.drawable.logo)
                .into(imageView);
    }
    private ViewHolder.Clicklistener mClickListener;
    public interface Clicklistener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view ,int position);
    }

    public void setOnClicklistener(ViewHolder.Clicklistener clicklistener){
        mClickListener = clicklistener;
    }

}

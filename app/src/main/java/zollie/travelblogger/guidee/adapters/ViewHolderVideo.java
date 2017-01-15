package zollie.travelblogger.guidee.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.youtube.player.YouTubePlayerView;

import zollie.travelblogger.guidee.R;

/**
 * Created by FuszeneckerZ on 2017.01.15..
 */

public class ViewHolderVideo extends RecyclerView.ViewHolder{

    public YouTubePlayerView mVideo;
    public FrameLayout mCarousel;

    public YouTubePlayerView getmVideo() {
        return mVideo;
    }

    public void setmVideo(YouTubePlayerView mVideo) {
        this.mVideo = mVideo;
    }

    public FrameLayout getmCarousel() {
        return mCarousel;
    }

    public void setmCarousel(FrameLayout mCarousel) {
        this.mCarousel = mCarousel;
    }

    public ViewHolderVideo(View itemView){
        super(itemView);

        mVideo = (YouTubePlayerView) itemView.findViewById(R.id.mCarouselVideo);
        mCarousel = (FrameLayout) itemView.findViewById(R.id.mCarouselVideoCard);
    }
}
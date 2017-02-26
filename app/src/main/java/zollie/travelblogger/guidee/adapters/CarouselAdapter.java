package zollie.travelblogger.guidee.adapters;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.api.services.youtube.YouTube;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.fragments.ContentViewerDialogFragment;
import zollie.travelblogger.guidee.models.CarouselModel;

/**
 * Created by FuszeneckerZ on 2017.01.14..
 */

public class CarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CarouselModel> allCarousels = new ArrayList<CarouselModel>();
    private Context mContext;
    private final int VIDEO = 0, IMAGE = 1;
    private YouTubePlayer player;
    private YouTubeThumbnailLoader mThumbnailLoader;
    public CarouselAdapter(Context context, ArrayList<CarouselModel> allCarouselList) {
        allCarousels = allCarouselList;
        mContext = context;
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (allCarousels.get(position).carouselType == CarouselModel.CarouselType.IMAGE) {
            return IMAGE;
        } else if (allCarousels.get(position).carouselType == CarouselModel.CarouselType.VIDEO) {
            return VIDEO;
        }
        return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch(viewType) {
            case IMAGE:
                View v1 = inflater.inflate(R.layout.carousel_card_image, parent, false);
                viewHolder = new ViewHolderImage(v1);
                break;
            case VIDEO:
                View v2 = inflater.inflate(R.layout.carousel_card_video, parent, false);
                viewHolder = new ViewHolderVideo(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.journey_card_placeholder, parent, false);
                viewHolder = new JourneyAdapter.ViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CarouselModel mCarousel = allCarousels.get(position);
        switch(holder.getItemViewType()) {
            case IMAGE:
                ImageView mCarouselImage = ((ViewHolderImage) holder).getmImage();
                //===================== Adding Image to to Horizontal Slide via Glide =========
                Glide
                        .with(mContext)
                        .load(mCarousel.imageUrl)
                        .centerCrop()
                        .override(160, 160)
                        .crossFade()
                        .into(mCarouselImage);
                //=============================================================================
                ((ViewHolderImage) holder).mCarousel.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        // Go to new activity and display image
                        FragmentTransaction ft = ((Activity)mContext).getFragmentManager().beginTransaction();
                        Fragment prev = ((Activity)mContext).getFragmentManager().findFragmentByTag("imageViewer");
                        if (prev != null) {
                            ft.remove(prev);
                        }

                        DialogFragment newFragment = ContentViewerDialogFragment.newInstance(mCarousel.imageUrl);
                        newFragment.show(ft, "imageViewer");
                    }
                });

                ((ViewHolderImage) holder).mCarousel.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(mCarousel.toDelete == 1){  // Long click happened in EditEventView, therefore Carousel model is deletable
                            mCarousel.toDelete = 2; // Prepare Carousel model for deletion

                        }
                        return true;
                    }
                });

                break;
            case VIDEO:

                final YouTubePlayerView mCarouselVideo = ((ViewHolderVideo) holder).getmVideo();
                final YouTubeThumbnailView mCarouselThumbnail = ((ViewHolderVideo) holder).getmThumbnail();
             //   mCarouselVideo.setSoundEffectsEnabled(true);
                final YouTubePlayer.OnInitializedListener onInitializedListener;
                onInitializedListener = new YouTubePlayer.OnInitializedListener(){

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
              //          youTubePlayer.loadVideo(mCarousel.videoUrl);
                        player = youTubePlayer;
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        int b = 1;
                    }
                };
                mCarouselVideo.initialize(mContext.getString(R.string.Youtube_API), onInitializedListener);
                final YouTubeThumbnailView.OnInitializedListener onThumbnailInitialize;
                onThumbnailInitialize = new YouTubeThumbnailView.OnInitializedListener(){

                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        mThumbnailLoader = youTubeThumbnailLoader;
                        mThumbnailLoader.setOnThumbnailLoadedListener(new ThumbnailLoadedListener());
                        youTubeThumbnailLoader.setVideo(mCarousel.videoUrl);
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };
                mCarouselThumbnail.initialize(mContext.getString(R.string.Youtube_API), onThumbnailInitialize);
                mCarouselThumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCarouselThumbnail.setVisibility(View.INVISIBLE);
                        player.loadVideo(mCarousel.videoUrl);
                        // Go to new activity and display video
                    }
                });



                /*((ViewHolderVideo) holder).mCarousel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        mCarouselVideo.initialize(mContext.getString(R.string.Youtube_API), onInitializedListener);
                    }
                });*/
                break;
            default:
                break;
        }
    }
    private final class ThumbnailLoadedListener implements YouTubeThumbnailLoader.OnThumbnailLoadedListener {


        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {

        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

        }
    }

    @Override
    public int getItemCount() {
        return allCarousels.size();
    }
}
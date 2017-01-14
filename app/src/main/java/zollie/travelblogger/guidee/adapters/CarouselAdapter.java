package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.CarouselModel;

/**
 * Created by FuszeneckerZ on 2017.01.14..
 */

public class CarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<CarouselModel> allCarousels = new ArrayList<CarouselModel>();
    private Context mContext;
    private final int VIDEO = 0, IMAGE = 1;

    public static class ViewHolderImage extends RecyclerView.ViewHolder{

        public ImageView mImage;
        public TextView mTitle;
        public RelativeLayout mCarousel;

        public ViewHolderImage(View itemView){
            super(itemView);

            mImage = (ImageView) itemView.findViewById();
            mTitle = (TextView) itemView.findViewById();
            mCarousel = (RelativeLayout) itemView.findViewById();
        }
    }

    public static class ViewHolderVideo extends RecyclerView.ViewHolder{

        public YouTubePlayerView mVideo;
        public TextView mTitle;
        public RelativeLayout mCarousel;

        public ViewHolderVideo(View itemView){
            super(itemView);

            mVideo = (YouTubePlayerView) itemView.findViewById();
            mTitle = (TextView) itemView.findViewById();
            mCarousel = (RelativeLayout) itemView.findViewById();
        }
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
                View v1 = inflater.inflate(R.layout.layout_carousel_image, parent, false);
                viewHolder = new ViewHolderImage(v1);
                break;
            case VIDEO:
                View v2 = inflater.inflate(R.layout.layout_carousel_video, parent, false);
                viewHolder = new ViewHolderVideo(v2);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

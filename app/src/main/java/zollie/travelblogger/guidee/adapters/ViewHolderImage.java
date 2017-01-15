package zollie.travelblogger.guidee.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import zollie.travelblogger.guidee.R;

/**
 * Created by FuszeneckerZ on 2017.01.15..
 */

public class ViewHolderImage extends RecyclerView.ViewHolder{

    public ImageView mImage;
    public FrameLayout mCarousel;

    public ImageView getmImage() {
        return mImage;
    }

    public void setmImage(ImageView mImage) {
        this.mImage = mImage;
    }

    public FrameLayout getmCarousel() {
        return mCarousel;
    }

    public void setmCarousel(FrameLayout mCarousel) {
        this.mCarousel = mCarousel;
    }

    public ViewHolderImage(View itemView){
        super(itemView);

        mImage = (ImageView) itemView.findViewById(R.id.mCarouselImage);
        mCarousel = (FrameLayout) itemView.findViewById(R.id.mCarouselImageCard);
    }
}
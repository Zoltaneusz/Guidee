package zollie.travelblogger.guidee.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import zollie.travelblogger.guidee.R;

/**
 * Created by FuszeneckerZ on 2017.01.15..
 */

public class DialogViewHolderImage extends RecyclerView.ViewHolder{

    public PhotoView mImage;
    public FrameLayout mCarousel;

    public PhotoView getmImage() {
        return mImage;
    }

    public void setmImage(PhotoView mImage) {
        this.mImage = mImage;
    }

    public FrameLayout getmCarousel() {
        return mCarousel;
    }

    public void setmCarousel(FrameLayout mCarousel) {
        this.mCarousel = mCarousel;
    }

    public DialogViewHolderImage(View itemView){
        super(itemView);

        mImage = (PhotoView) itemView.findViewById(R.id.mDialogImage);
        mCarousel = (FrameLayout) itemView.findViewById(R.id.mDialogImageCard);
    }
}
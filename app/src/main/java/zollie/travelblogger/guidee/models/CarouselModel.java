package zollie.travelblogger.guidee.models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.25..
 */

public class CarouselModel {
    public enum CarouselType{
        IMAGE,
        VIDEO
    }

    public CarouselType carouselType;
    public String imageUrl;
    public String videoUrl;


    public CarouselModel(Map<String, Object> rawCarouselModel) {
        String imageUrl = null;
        String videoUrl = null;

        try {
            imageUrl = (String) rawCarouselModel.get("imageURL");
        } catch (Exception e) {

        }
        try {
            videoUrl = (String) rawCarouselModel.get("videoYoutubeId");
        } catch (Exception e1) {

        }
        if(imageUrl != null) {
            this.imageUrl = imageUrl;
            this.carouselType = CarouselType.IMAGE;
        }
        if(videoUrl != null) {
            this.videoUrl = videoUrl;
            this.carouselType = CarouselType.VIDEO;
        }

    }


}

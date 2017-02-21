package zollie.travelblogger.guidee.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.25..
 */

public class CarouselModel implements Parcelable{


    public String imageUrl;
    public String videoUrl;
    public int FIRNumber;
    public enum CarouselType{
        IMAGE,
        VIDEO
    }
    public CarouselType carouselType;

    public CarouselModel(){

    }

    public CarouselModel(Map<String, Object> rawCarouselModel, int number) {

        try {
            this.imageUrl = (String) rawCarouselModel.get("imageURL");
        } catch (Exception e) {

        }
        try {
            this.videoUrl = (String) rawCarouselModel.get("videoYoutubeId");
        } catch (Exception e1) {

        }
        if(imageUrl != null) {
            this.carouselType = CarouselType.IMAGE;
        }
        if(videoUrl != null) {
            this.carouselType = CarouselType.VIDEO;
        }
        try {
            this.FIRNumber = number;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public CarouselModel(Parcel in){

        try {
            this.imageUrl = in.readString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.videoUrl = in.readString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.FIRNumber = in.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(imageUrl != null) {
            this.carouselType = CarouselType.IMAGE;
        }
        if(videoUrl != null) {
            this.carouselType = CarouselType.VIDEO;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeString(imageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            parcel.writeString(videoUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            parcel.writeInt(FIRNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final Parcelable.Creator<CarouselModel> CREATOR = new Parcelable.Creator<CarouselModel>() {
        public CarouselModel createFromParcel(Parcel in){
            return new CarouselModel(in);
        }
        public CarouselModel[] newArray(int size){
            return new CarouselModel[size];
        }
    };

}

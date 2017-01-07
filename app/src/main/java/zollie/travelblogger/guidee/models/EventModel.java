package zollie.travelblogger.guidee.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.24..
 */

public class EventModel implements Parcelable{
    public String summary;
    public String title;
    public ArrayList<CarouselModel> carouselModels;

    public EventModel(Map<String, Object> rawEventModel) {
        ArrayList<Map<String, Object>> rawCarouselModels = null;
        try {
            this.summary = (String) rawEventModel.get("summary");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.title = (String) rawEventModel.get("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            rawCarouselModels = (ArrayList<Map<String, Object>>) rawEventModel.get("carouselModels");
            for(Map<String, Object> rawCarouselModel : rawCarouselModels ){
                CarouselModel carouselModel = new CarouselModel(rawCarouselModel);
                this.carouselModels= new ArrayList<CarouselModel>();
                this.carouselModels.add(carouselModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventModel(Parcel in){
        this.summary = in.readString();
        this.title = in.readString();
        carouselModels= new ArrayList<CarouselModel>();
        in.readTypedList(carouselModels, CarouselModel.CREATOR);
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<CarouselModel> getCarouselModels() {
        return carouselModels;
    }

    public void setCarouselModels(ArrayList<CarouselModel> carouselModels) {
        this.carouselModels = carouselModels;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(summary);
        parcel.writeString(title);
        parcel.writeTypedList(carouselModels);
    }
    public static final Parcelable.Creator<EventModel> CREATOR = new Parcelable.Creator<EventModel>(){

        @Override
        public EventModel createFromParcel(Parcel parcel) {
            return new EventModel(parcel);
        }

        @Override
        public EventModel[] newArray(int i) {
            return new EventModel[0];
        }
    };
}
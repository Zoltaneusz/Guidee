package zollie.travelblogger.guidee.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.24..
 */

public class EventModel implements Parcelable{
    public String summary;
    public String title;
    public LatLng eventLatLng;
    public ArrayList<CarouselModel> carouselModels;

    public EventModel(Map<String, Object> rawEventModel) {
        Map<String, Object> locationData = (Map<String, Object>) rawEventModel.get("location");
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
            this.eventLatLng = new LatLng((double) locationData.get("latitude"), (double) locationData.get("longitude"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.carouselModels = new ArrayList<CarouselModel>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            rawCarouselModels = (ArrayList<Map<String, Object>>) rawEventModel.get("carouselModels");
            for(Map<String, Object> rawCarouselModel : rawCarouselModels ){
                CarouselModel carouselModel = new CarouselModel(rawCarouselModel);
                this.carouselModels.add(carouselModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventModel(Parcel in){
        try {
            this.summary = in.readString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.title = in.readString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LatLng mLatLng = null;
        try {
            mLatLng = new LatLng(in.readDouble(), in.readDouble());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.eventLatLng = mLatLng;
        } catch (Exception e) {
            e.printStackTrace();
        }
        carouselModels= new ArrayList<CarouselModel>();
        try {
            in.readTypedList(carouselModels, CarouselModel.CREATOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            parcel.writeString(summary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            parcel.writeString(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            parcel.writeDouble(eventLatLng.latitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            parcel.writeDouble(eventLatLng.longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            parcel.writeTypedList(carouselModels);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
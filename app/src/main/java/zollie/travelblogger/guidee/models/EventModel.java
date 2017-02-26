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
    public String journeyID;
    public int FIRNumber;
    public int deletedIndexes = 0;
    public LatLng eventLatLng;
    public boolean userEligible; // deprecated, remove in future !
    public ArrayList<CarouselModel> carouselModels;

    public EventModel(JourneyModel journeyModel){

        this.title = "Your events title";
        this.summary = "Your events summary";
        this.eventLatLng = new LatLng(42.993976403334706,16.40115687746871);
        this.journeyID = journeyModel.ID;
        this.FIRNumber = journeyModel.eventModels.size();
        this.userEligible = journeyModel.eventModels.get(0).userEligible;
        CarouselModel emptyCarouselModel = new CarouselModel();
        emptyCarouselModel.imageUrl = "https://firebasestorage.googleapis.com/v0/b/guidee-f0453.appspot.com/o/images%2F9F4DD9C8-5465-464B-9249-9127AD09E729.jpg?alt=media&token=32a14ec9-a298-4c68-89b3-211eb0f86e7e";
        emptyCarouselModel.carouselType = CarouselModel.CarouselType.IMAGE;
        this.carouselModels = new ArrayList<CarouselModel>();
        this.carouselModels.add(emptyCarouselModel);
        this.deletedIndexes = 0;
    }

    public EventModel(Map<String, Object> rawEventModel, boolean journeyEligible, String ID, int number) {
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
            this.journeyID = ID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.FIRNumber = number;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int i = 0;
            rawCarouselModels = (ArrayList<Map<String, Object>>) rawEventModel.get("carouselModels");
            for(Map<String, Object> rawCarouselModel : rawCarouselModels ){
                if(rawCarouselModel != null) {
                    CarouselModel carouselModel = new CarouselModel(rawCarouselModel, i);
                    this.carouselModels.add(carouselModel);
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.userEligible = journeyEligible;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventModel(EventModel event){
        this.title = event.title;
        this.summary = event.summary;
        this.eventLatLng = event.eventLatLng;
        this.journeyID = event.journeyID;
        this.FIRNumber = event.FIRNumber;
        this.deletedIndexes = event.deletedIndexes;
        this.userEligible = event.userEligible;
        this.carouselModels = event.carouselModels;

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
        try {
            this.journeyID = in.readString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.FIRNumber = in.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.deletedIndexes = in.readInt();
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
        boolean[] data2 = new boolean[1];
        try {
            in.readBooleanArray(data2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.userEligible = data2[0];
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
            parcel.writeString(journeyID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            parcel.writeInt(FIRNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            parcel.writeInt(deletedIndexes);
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
            parcel.writeBooleanArray(new boolean[]{this.userEligible});
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

    public int getHighestCarouselID(EventModel eventModel){
        int Nr = 0;
        int i = 0;
        for(CarouselModel mCarouselModel : this.carouselModels){
            if(mCarouselModel.FIRNumber > Nr) {
                Nr = mCarouselModel.FIRNumber;
            }
            i++;
        }
        return Nr;
    }

    public void addItemToCarousels(ArrayList<String> images, String video){
        int i = 0;
        for(String image : images) {
            CarouselModel mCarouselModel = new CarouselModel();
            mCarouselModel.carouselType = CarouselModel.CarouselType.IMAGE;
            mCarouselModel.imageUrl = image;
            mCarouselModel.FIRNumber = this.getHighestCarouselID(this) + 1;
            this.carouselModels.add(mCarouselModel);
        }
        if(video.equals("ytQ5CYE1VZw")){}
        else {
            CarouselModel mCarouselModel = new CarouselModel();
            mCarouselModel.carouselType = CarouselModel.CarouselType.VIDEO;
            mCarouselModel.videoUrl = video;
            mCarouselModel.FIRNumber = this.getHighestCarouselID(this) + 1;
            this.carouselModels.add(mCarouselModel);
        }
    }
}
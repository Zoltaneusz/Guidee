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
    public int toDelete = 0; // 0: NO; 1: opened in edit mode, don't delete yet; 2: YES
    public LatLng eventLatLng;
    public boolean userEligible; // deprecated, remove in future !
    public ArrayList<CarouselModel> carouselModels = new ArrayList<CarouselModel>();

    public EventModel(JourneyModel journeyModel){

        this.title = "Your events title";
        this.summary = "Your events summary";
        this.eventLatLng = new LatLng(42.993976403334706,16.40115687746871);
        this.journeyID = journeyModel.ID;
        this.FIRNumber = journeyModel.eventModels.size();
        this.userEligible = journeyModel.userEligible;
        addEmptyCarousel();

        this.deletedIndexes = 0;
    }

    public EventModel(String ID, Boolean eligible){

        this.title = "Your events title";
        this.summary = "Your events summary";
        this.eventLatLng = new LatLng(42.993976403334706,16.40115687746871);
        this.journeyID = ID;
        this.FIRNumber = 0;
        this.userEligible = eligible;
        addEmptyCarousel();

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
        this.toDelete = event.toDelete;

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
        try {
            this.toDelete = in.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LatLng mLatLng = null;
        double lati = in.readDouble();
        double longi = in.readDouble();
        try {
            mLatLng = new LatLng(lati, longi);
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
            parcel.writeInt(toDelete);
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
            if(carouselModels.size() == 0) {
                addEmptyCarousel();
                parcel.writeTypedList(carouselModels);
            }
            else {parcel.writeTypedList(carouselModels);}
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

    public boolean addItemToCarousels(ArrayList<String> images, String video){
        int i = 0;
        boolean retVal = false;
        for(String image : images) {
            CarouselModel mCarouselModel = new CarouselModel();
            mCarouselModel.carouselType = CarouselModel.CarouselType.IMAGE;
            mCarouselModel.imageUrl = image;
            if(this.carouselModels.get(0).imageUrl.equals("empty")){
                this.carouselModels.get(0).imageUrl = image;
                retVal = true;
            }
            else {
                mCarouselModel.FIRNumber = this.getHighestCarouselID(this) + 1;
                this.carouselModels.add(mCarouselModel);
            }

        }
        if(video.equals("")){}  // példa: ytQ5CYE1VZw
        else {
            if(this.carouselModels.get(0).imageUrl.equals("empty")){
                this.carouselModels.get(0).videoUrl = video;
                this.carouselModels.get(0).imageUrl = "";
                this.carouselModels.get(0).carouselType = CarouselModel.CarouselType.VIDEO;
                retVal = true;
            }else {
                CarouselModel mCarouselModel = new CarouselModel();
                mCarouselModel.carouselType = CarouselModel.CarouselType.VIDEO;
                mCarouselModel.videoUrl = video;
                mCarouselModel.FIRNumber = this.getHighestCarouselID(this) + 1;
                this.carouselModels.add(mCarouselModel);
            }
        }
        return retVal;
    }
    private void addEmptyCarousel(){
        CarouselModel emptyCarouselModel = new CarouselModel();
        emptyCarouselModel.imageUrl = "empty";
        emptyCarouselModel.carouselType = CarouselModel.CarouselType.IMAGE;
        this.carouselModels.add(emptyCarouselModel);
    }
}
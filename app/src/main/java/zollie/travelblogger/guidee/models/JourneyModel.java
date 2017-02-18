package zollie.travelblogger.guidee.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.internal.ParcelableSparseArray;

import com.google.android.gms.games.event.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.24..
 */

public class JourneyModel implements Parcelable {

    public String title;
    public String summary;
    public String coverImageUrl;
    public String userAvatarUrl;
    public String identifier;
    public String ID;
    public boolean userEligible;
    public AnnotationModel annotationModel;
    public ArrayList<EventModel> eventModels= new ArrayList<EventModel>();

    public JourneyModel(Map<String, Object> rawJourneyModel, String journeyReference, boolean eligibile) {
        try {
            this.title = (String) rawJourneyModel.get("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.summary = (String) rawJourneyModel.get("summary");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.coverImageUrl = (String) rawJourneyModel.get("coverImageUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.userAvatarUrl = (String) rawJourneyModel.get("userAvatarUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.identifier = (String) rawJourneyModel.get("identifier");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.annotationModel = new AnnotationModel((Map<String, Object>)rawJourneyModel.get("annotationModel"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.eventModels = new ArrayList<EventModel>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.userEligible = eligibile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.ID = journeyReference;
        } catch (Exception e) {
            e.printStackTrace();
        }


  /*      try { // THIS SHOULD BE CHANGED TO USE THE VALUE OF journeyReference with Value Listener to make it more ROBUST
            this.ID = journeyReference.toString().substring(45,65);
        } catch (Exception e) {
            e.printStackTrace();
            this.ID = journeyReference.toString().substring(43,44);
        }*/
        ArrayList<Map<String, Object>> rawEventModels = null;
        try {
            rawEventModels = (ArrayList<Map<String, Object>>) rawJourneyModel.get("eventModels");
            for(Map<String, Object> rawEventModel : rawEventModels ){
                EventModel eventModel = new EventModel(rawEventModel, this.userEligible);
                this.eventModels.add(eventModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public JourneyModel(Parcel in){
        String[] data = new String[6];
        in.readStringArray(data);
        this.title = data[0];
        this.summary =  data[1];
        this.coverImageUrl = data[2];
        this.userAvatarUrl = data[3];
        this.identifier = data[4];
        this.ID = data[5];
        boolean[] data2 = new boolean[1];
        in.readBooleanArray(data2);
        this.userEligible = data2[0];
        annotationModel = in.readParcelable(AnnotationModel.class.getClassLoader());
        in.readTypedList(eventModels, EventModel.CREATOR);
    }
    public boolean setEventsEligibility(){
        for(EventModel event : eventModels){
            event.userEligible = this.userEligible;
        }
        return this.userEligible;
    }

    public JourneyModel(JourneyModel journeyModel){
        this.title = journeyModel.title;
        this.summary = journeyModel.summary;
        this.coverImageUrl = journeyModel.coverImageUrl;
        this.userAvatarUrl = journeyModel.userAvatarUrl;
        this.identifier = journeyModel.identifier;
        this.ID = journeyModel.ID;
        this.annotationModel = journeyModel.annotationModel;
        this.eventModels = journeyModel.eventModels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.title,
                this.summary,
                this.coverImageUrl,
                this.userAvatarUrl,
                this.identifier,
                this.ID});
        parcel.writeBooleanArray(new boolean[]{this.userEligible});
        parcel.writeParcelable(annotationModel, i);
        parcel.writeTypedList(eventModels);
    }
    public static final Parcelable.Creator<JourneyModel> CREATOR = new Parcelable.Creator<JourneyModel>(){

        @Override
        public JourneyModel createFromParcel(Parcel parcel) {
            return new JourneyModel(parcel);
        }

        @Override
        public JourneyModel[] newArray(int i) {
            return new JourneyModel[0];
        }
    };
}
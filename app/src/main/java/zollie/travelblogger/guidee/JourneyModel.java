package zollie.travelblogger.guidee;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.24..
 */

class JourneyModel {

    public AnnotationModel annotationModel;
    public ArrayList<EventModel> eventModels;

    public String title;
    public String summary;
    public String coverImageUrl;
    public String userAvatarUrl;
    public String identifier;


    public JourneyModel(Map<String, Object> rawJourneyModel) {
        this.title = (String) rawJourneyModel.get("title");
        this.summary = (String) rawJourneyModel.get("summary");
        this.coverImageUrl = (String) rawJourneyModel.get("coverImageUrl");
        this.userAvatarUrl = (String) rawJourneyModel.get("userAvatarUrl");
        this.identifier = (String) rawJourneyModel.get("identifier");
        this.annotationModel = new AnnotationModel((Map<String, Object>)rawJourneyModel.get("annotationModel"));
        this.eventModels = new ArrayList<EventModel>();
        ArrayList<Map<String, Object>> rawEventModels = null;
        try {
            rawEventModels = (ArrayList<Map<String, Object>>) rawJourneyModel.get("eventModels");
            for(Map<String, Object> rawEventModel : rawEventModels ){
                EventModel eventModel = new EventModel(rawEventModel);
                this.eventModels.add(eventModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
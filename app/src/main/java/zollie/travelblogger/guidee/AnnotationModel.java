package zollie.travelblogger.guidee;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.24..
 */

class AnnotationModel {
    public String markerID;
    public Bitmap markerIcon;
    public String markerIconURL;
    public long markerLikes;
    public LatLng markerLatLng;
    public String markerSubtitle;
    public String markerTitle;

    public AnnotationModel(Map<String, Object> rawAnnotationModel) {
        Map<String, Object> locationData = (Map<String, Object>) rawAnnotationModel.get("location");
        this.markerID = null;
        this.markerIcon = null;
        this.markerIconURL = (String) rawAnnotationModel.get("imageURL");
        this.markerLikes = (long) rawAnnotationModel.get("likes");
        this.markerLatLng = new LatLng((double) locationData.get("latitude"), (double) locationData.get("longitude"));
        this.markerSubtitle = (String) rawAnnotationModel.get("subtitle");;
        this.markerTitle = (String) rawAnnotationModel.get("title");
    }

    public String getMarkerIconURL() {
        return markerIconURL;
    }

    public long getMarkerLikes() {
        return markerLikes;
    }

    public LatLng getMarkerLatLng() {
        return markerLatLng;
    }

    public String getMarkerSubtitle() {
        return markerSubtitle;
    }

    public String getMarkerTitle() {
        return markerTitle;
    }

    public void setMarkerIconURL(String markerIconURL) {
        this.markerIconURL = markerIconURL;
    }

    public void setMarkerLikes(int markerLikes) {
        this.markerLikes = markerLikes;
    }

    public void setMarkerLatLng(LatLng markerLatLng) {
        this.markerLatLng = markerLatLng;
    }

    public void setMarkerSubtitle(String markerSubtitle) {
        this.markerSubtitle = markerSubtitle;
    }

    public void setMarkerTitle(String markerTitle) {
        this.markerTitle = markerTitle;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public void setMarkerIcon(Bitmap markerIcon) {
        this.markerIcon = markerIcon;
    }

    public String getMarkerID() {
        return markerID;
    }

    public Bitmap getMarkerIcon() {
        return markerIcon;
    }
}
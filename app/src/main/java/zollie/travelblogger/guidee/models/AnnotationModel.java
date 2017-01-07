package zollie.travelblogger.guidee.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created by FuszeneckerZ on 2016.12.24..
 */

public class AnnotationModel implements Parcelable {
    public String markerID;
    public String markerIconURL;
    public long markerLikes;
    public String markerSubtitle;
    public String markerTitle;
    public Marker marker;
    public LatLng markerLatLng;
    public Bitmap markerIcon;

    public AnnotationModel(Map<String, Object> rawAnnotationModel) {
        Map<String, Object> locationData = (Map<String, Object>) rawAnnotationModel.get("location");
        this.markerID = null;
        this.markerIcon = null;
        try {
            this.markerIconURL = (String) rawAnnotationModel.get("imageURL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.markerLikes = (long) rawAnnotationModel.get("likes");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.markerLatLng = new LatLng((double) locationData.get("latitude"), (double) locationData.get("longitude"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.markerSubtitle = (String) rawAnnotationModel.get("subtitle");
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.markerTitle = (String) rawAnnotationModel.get("title");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AnnotationModel(Parcel in){
        this.markerID = in.readString();
        this.markerIconURL = in.readString();
        this.markerLikes = in.readLong();
        this.markerSubtitle = in.readString();
        this.markerTitle = in.readString();
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
    public Marker getMarker() {
            return marker;
            }

    public void setMarker(Marker marker) {
            this.marker = marker;
            }

    @Override
    public int describeContents() {
            return 0;
            }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(markerID);
            parcel.writeString(markerIconURL);
            parcel.writeLong(markerLikes);
            parcel.writeString(markerSubtitle);
            parcel.writeString(markerTitle);
            }
    public static final Parcelable.Creator<AnnotationModel> CREATOR = new Parcelable.Creator<AnnotationModel>() {
        public AnnotationModel createFromParcel(Parcel in){
            return new AnnotationModel(in);
        }
        public AnnotationModel[] newArray(int size){
            return new AnnotationModel[size];
        }
    };

}

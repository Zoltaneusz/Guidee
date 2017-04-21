package zollie.travelblogger.guidee.models;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by FuszeneckerZ on 2017. 04. 13..
 */

public class MarkerItem implements ClusterItem {
    private final LatLng mPosition;
    private  String mTitle;
    private  String mSnippet;
    private Bitmap mIcon;
    private String mID;

    public MarkerItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MarkerItem(double lat, double lng, String title, String snippet, Bitmap icon) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mIcon = icon;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public Bitmap getmIcon() {
        return mIcon;
    }

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

}

package zollie.travelblogger.guidee.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by FuszeneckerZ on 2017. 04. 13..
 */

public class MarkerItem implements ClusterItem {
    private final LatLng mPosition;
    private  String mTitle;
    private  String mSnippet;

    public MarkerItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MarkerItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
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
}

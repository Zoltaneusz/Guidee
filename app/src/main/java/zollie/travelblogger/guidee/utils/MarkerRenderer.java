package zollie.travelblogger.guidee.utils;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.models.MarkerItem;

/**
 * Created by FuszeneckerZ on 2017. 04. 21..
 */

public class MarkerRenderer extends DefaultClusterRenderer<MarkerItem> {

    public JourneyModel journeyModel;
    public MarkerInterface markerInterface;
    public MarkerRenderer(Context context, GoogleMap map, ClusterManager<MarkerItem> clusterManager, MarkerInterface mInterface) {
        super(context, map, clusterManager);
        this.markerInterface = mInterface;
    }

    @Override
    protected void onBeforeClusterItemRendered(MarkerItem item, MarkerOptions markerOptions) {
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.getmIcon()));
        markerOptions.snippet(item.getSnippet());
        markerOptions.title(item.getTitle());

        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    protected void onClusterItemRendered(MarkerItem clusterItem, Marker marker) {
        clusterItem.setID(marker.getId());
        markerInterface.setMarkerId(marker);
        super.onClusterItemRendered(clusterItem, marker);

    }
}

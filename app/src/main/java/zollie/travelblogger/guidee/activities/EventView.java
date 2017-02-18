package zollie.travelblogger.guidee.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;

import java.util.ArrayList;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.CarouselAdapter;
import zollie.travelblogger.guidee.models.CarouselModel;
import zollie.travelblogger.guidee.models.EventModel;

/**
 * Created by FuszeneckerZ on 2017.01.09..
 */

public class EventView extends YouTubeBaseActivity {
    final int locationPermission = 0;
    MapView mMapView;
    public GoogleMap googleMap;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_event_view);

        Bundle intentData = getIntent().getExtras();
        final EventModel mEvent = (EventModel) intentData.getParcelable("ser_event");
        if(mEvent.carouselModels.get(0).imageUrl != null) {
            if (mEvent.carouselModels.get(0).carouselType == CarouselModel.CarouselType.IMAGE) {
                ImageView coverImage = (ImageView) findViewById(R.id.event_imgFirst);
                //===================== Adding Image to to Horizontal Slide via Glide =========
                Glide
                        .with(this)
                        .load(mEvent.carouselModels.get(0).imageUrl)
                        .crossFade()
                        .into(coverImage);
                //=============================================================================
            }
        }
        if(mEvent.title != null) {
            TextView eventTitle = (TextView) findViewById(R.id.event_name);
            eventTitle.setText(mEvent.title);
        }
        TextView mEventSummary = (TextView) findViewById(R.id.event_summary_content);
        try {
            mEventSummary.setText(mEvent.summary);
        } catch (Exception e) {
            e.printStackTrace();
        }


        fillRecyclerView(R.id.event_pictures_recycle_test, R.id.event_pictures_recycle_placeholder, mEvent.carouselModels);

        //======= Check user edit right for this event and add FAB for editing Event ===============
        boolean userEdit = false;
        userEdit = mEvent.userEligible;
        if(userEdit) {
            FloatingActionButton editEventFAB = (FloatingActionButton) findViewById(R.id.event_edit_FAB);
            editEventFAB.setVisibility(View.VISIBLE);
            editEventFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toEventIntent = new Intent(mContext, EditEventView.class);
                    toEventIntent.putExtra("ser_event", mEvent);
                    mContext.startActivity(toEventIntent);
                }
            });


        }

        //==========================================================================================

        mMapView = (MapView) findViewById(R.id.event_Map);
        mMapView.onCreate(savedInstanceState);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.event_scroll_view);
        ImageView transparent = (ImageView)findViewById(R.id.imagetrans);

        // Method to deprecate touch events of ScrollView in case the user touches the map
        transparent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        mMapView.onResume(); // needed to get the map to display immediately
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.addMarker(new MarkerOptions()
                        .position(mEvent.eventLatLng)
                        .title(mEvent.title)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

 /*               CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(mEvent.eventLatLng)      // Sets the center of the map to location user
                        .zoom(19f)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/
     /*           CameraUpdate center=
                        CameraUpdateFactory.newLatLng(mEvent.eventLatLng);
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
*/
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mEvent.eventLatLng, 10);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(mEvent.eventLatLng ));
                googleMap.animateCamera(cameraUpdate);
            }
        });


        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, locationPermission);

            }
        }
    }

    public void fillRecyclerView(int primaryResource, int emptyResource, ArrayList<CarouselModel> carouselModels){

        RecyclerView rvCarousels = (RecyclerView) findViewById(primaryResource);

        CarouselAdapter adapter = new CarouselAdapter(this, carouselModels);
        rvCarousels.setAdapter(adapter);
        rvCarousels.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, zollie.travelblogger.guidee.utils.DividerItemDecoration.HORIZONTAL_LIST);
        rvCarousels.addItemDecoration(itemDecoration);
        //      if(eventModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case locationPermission: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

package zollie.travelblogger.guidee;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by zoltanfuszenecker on 10/29/16.
 */



public class ExploreFragment extends Fragment {


    MapView mMapView;
    private GoogleMap googleMap;

    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    Bitmap bmp = Bitmap.createBitmap(90, 90, conf);
    Canvas canvas1 = new Canvas(bmp);

    // paint defines the text color, stroke width and size
    Paint color = new Paint();
    Bitmap circleBitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);




        mMapView = (MapView) rootView.findViewById(R.id.exploreMap);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);


                // For dropping a marker at a point on the Map
                final LatLng sydney = new LatLng(-34, 151);

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        marker.remove();

                        // Animating marker implementation =================================================
                        pulseMarker(1);
                        Marker myMarker2 = mMap.addMarker(new MarkerOptions().position(sydney)
                                .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                                .title("Zollie")
                                .snippet("Journey to Sydney")
                                // Specifies the anchor to be at a particular point in the marker image.
                                .anchor(0.4f, 1));

                        //==================================================================================

                        return false;
                    }
                });

         //       googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_friends)));

               /*Creating custom map markers here */
                // =================================================================================

                pulseMarker(0);

                // add marker to Map
                Marker myMarker = mMap.addMarker(new MarkerOptions().position(sydney)
                        .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                        .title("Zollie")
                        .snippet("Journey to Sydney")
                        // Specifies the anchor to be at a particular point in the marker image.
                        .anchor(0.4f, 1));

                //==================================================================================




                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }

    //================================================


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        //MarkerAnimation.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //MarkerAnimation.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    public void pulseMarker(int step){


        color.setTextSize(35);
        color.setColor(Color.GRAY);

        // modify canvas
        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.profile_pic), 0,0, color);
        //canvas1.drawText("Zollie", 30, 40, color);


        Bitmap bitmap = bmp;
        circleBitmap = Bitmap.createBitmap(bitmap.getWidth()+5, bitmap.getHeight()+5, Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint profilePic = new Paint();
        profilePic.setShader(shader);
        profilePic.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        Paint circleFrame = new Paint();
        circleFrame.setColor(Color.GRAY);

        c.drawCircle((int)(bitmap.getWidth()/2.5), (int)(bitmap.getHeight()/2.5),(int) (bitmap.getWidth()/2.5)-1+step*10, circleFrame);
        circleFrame.setColor(Color.LTGRAY);
        circleFrame.setStyle(Paint.Style.STROKE);
        c.drawCircle((int)(bitmap.getWidth()/2.5), (int)(bitmap.getHeight()/2.5),(int) (bitmap.getWidth()/2.5)-4+step*10, circleFrame);
        c.drawCircle((int)(bitmap.getWidth()/2.5), (int)(bitmap.getHeight()/2.5),(int) (bitmap.getWidth()/2.5)+step*10, circleFrame);
        c.drawCircle((int)(bitmap.getWidth()/2.5), (int)(bitmap.getHeight()/2.5), (int)(bitmap.getWidth()/2.5)-5+step*10, profilePic);

    }

}


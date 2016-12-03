package zollie.travelblogger.guidee;

import android.*;
import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by zoltanfuszenecker on 10/29/16.
 */


public class ExploreFragment extends Fragment {


    final int locationPermission = 0;
    Handler h = new Handler();
    private ProfileFragment _profileFrag = new ProfileFragment();
    Marker myMarker2;
    Marker myMarker3;
    Marker myMarker4;
    Marker myMarker5;
    Marker myMarker6;
    MapView mMapView;

    private GoogleMap googleMap;

    // paint defines the text color, stroke width and size
    Paint color = new Paint();
    Bitmap circleBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        final float scale = getResources().getDisplayMetrics().density;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final Bitmap bmp = Bitmap.createBitmap((int)(60*scale),(int) (60*scale), conf);
        final Canvas canvas1 = new Canvas(bmp);
        mMapView = (MapView) rootView.findViewById(R.id.exploreMap);
        mMapView.onCreate(savedInstanceState);



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, locationPermission);

            }
        }
            mMapView.onResume(); // needed to get the map to display immediately
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap mMap) {
                    googleMap = mMap;

                    DataHandler.getInstance().getJourneys(new DataHandlerListener() {
                        @Override
                        public void onJourneyData(Map<String, Object> rawJourneyData) {
                            Map<String, Object> annotationModel = (Map<String, Object>) (rawJourneyData.get("annotationModel"));
                            addMapMarker(annotationModel);
                        }
                    });

                    // For showing a move to my location button

                    //           googleMap.setMyLocationEnabled(true);

                    // For dropping a marker at a point on the Map
                    final LatLng sydney = new LatLng(-34, 151);

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            marker.remove();

                            // Animating marker implementation =================================================
                            pulseMarker(4, bmp, canvas1, scale);
                            myMarker2 = mMap.addMarker(new MarkerOptions().position(sydney)
                                    .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                                    .title("Zollie")
                                    .snippet("Journey to Sydney")
                                    // Specifies the anchor to be at a particular point in the marker image.
                                    .anchor(0.4f, 1));

                            h.postDelayed(new Runnable(){  // Csak a legutolsót törli ki...a többi marker korábbról ott marad
                                @Override
                                public void run() {
                                    try{
                                        pulseMarker(6, bmp, canvas1, scale);
                                        myMarker4 = mMap.addMarker(new MarkerOptions().position(sydney)
                                                .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                                                .title("Zollie")
                                                .snippet("Journey to Sydney")
                                                // Specifies the anchor to be at a particular point in the marker image.
                                                .anchor(0.4f, 1));
                                    }
                                    catch(Exception e)
                                    {
                                        //  break;
                                    }
                                }
                            }, 100);
                            h.postDelayed(new Runnable(){  // Csak a legutolsót törli ki...a többi marker korábbról ott marad
                                @Override
                                public void run() {

                                    try{
                                        myMarker4.remove();

                                    }
                                    catch(Exception e)
                                    {
                                        //  break;
                                    }
                                }
                            }, 300);
                            h.postDelayed(new Runnable(){  // Csak a legutolsót törli ki...a többi marker korábbról ott marad
                                @Override
                                public void run() {

                                    try{

                                        pulseMarker(6, bmp, canvas1, scale);
                                        myMarker5 = mMap.addMarker(new MarkerOptions().position(sydney)
                                                .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                                                .title("Zollie")
                                                .snippet("Journey to Sydney")
                                                // Specifies the anchor to be at a particular point in the marker image.
                                                .anchor(0.4f, 1));
                                    }
                                    catch(Exception e)
                                    {
                                        //  break;
                                    }
                                }
                            }, 400);

                            h.postDelayed(new Runnable(){  // Csak a legutolsót törli ki...a többi marker korábbról ott marad
                                @Override
                                public void run() {

                                    try{
                                        myMarker5.remove();

                                    }
                                    catch(Exception e)
                                    {
                                        //  break;
                                    }
                                }
                            },500);
                            //==================================================================================
                            myMarker2.showInfoWindow();
                            return false;
                        }
                    });

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.contentContainer, _profileFrag).commit();
                        }
                    });

                    //       googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_friends)));

               /*Creating custom map markers here */
                    // =================================================================================
                    pulseMarker(4, bmp, canvas1, scale);
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

    private void addMapMarker(Map<String, Object> mapMarkerData) {

        Map<String, Object> locationData = (Map<String, Object>) mapMarkerData.get("location");
        double markerLat = (double) locationData.get("latitude");
        double markerLng = (double) locationData.get("longitude");
        String markerImageSource = (String) mapMarkerData.get("imageURL");
        String markerTitle = (String) mapMarkerData.get("title");
//        String markerSubtitle = (String) mapMarkerData.get("subtitle");
        long markerLikes = (long) mapMarkerData.get("likes");

        googleMap.addMarker(new MarkerOptions().position(new LatLng(markerLat, markerLng))
                .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                .title(markerTitle)
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.4f, 1));
    }

    //================================================



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

    @Override
    public void onResume() {
        super.onResume();
        // Change statusbar color ===============================
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = getActivity().getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.pastellRed));
        }
        //========================================================
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
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

    public void pulseMarker(int step, Bitmap bitm, Canvas canv, float scale){


        color.setTextSize(35);
        color.setColor(Color.BLACK);

        // modify canvas
        canv.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.profile_pic), 10,10, color);
        //canvas1.drawText("Zollie", 30, 40, color);


        Bitmap bitmap = bitm;
        circleBitmap = Bitmap.createBitmap(bitmap.getWidth()+5, bitmap.getHeight()+5, Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint profilePic = new Paint();
        profilePic.setFlags(Paint.ANTI_ALIAS_FLAG);
        profilePic.setShader(shader);
        profilePic.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        Paint circleFrame = new Paint();
        circleFrame.setFlags(Paint.ANTI_ALIAS_FLAG);
        circleFrame.setColor(Color.BLACK);

        c.drawCircle((int)(bitmap.getWidth()/2.5+10), (int)(bitmap.getHeight()/2.5+10),(int) (bitmap.getWidth()/2.5)-3+step*1, circleFrame);
        circleFrame.setColor(Color.GRAY);
        circleFrame.setStyle(Paint.Style.STROKE);
        c.drawCircle((int)(bitmap.getWidth()/2.5+10), (int)(bitmap.getHeight()/2.5+10),(int) (bitmap.getWidth()/2.5)-7+step*1, circleFrame);
        c.drawCircle((int)(bitmap.getWidth()/2.5+10), (int)(bitmap.getHeight()/2.5+10),(int) (bitmap.getWidth()/2.5)-3+step*1, circleFrame);
        c.drawCircle((int)(bitmap.getWidth()/2.5+10), (int)(bitmap.getHeight()/2.5+10), (int)(bitmap.getWidth()/2.5)-8+step*1, profilePic);

    }

}


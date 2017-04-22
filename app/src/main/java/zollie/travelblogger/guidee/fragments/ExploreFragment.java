package zollie.travelblogger.guidee.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.clustering.ClusterManager;


import java.util.ArrayList;
import java.util.Map;

import zollie.travelblogger.guidee.activities.JourneyView;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.models.CommentModel;
import zollie.travelblogger.guidee.models.MarkerItem;
import zollie.travelblogger.guidee.utils.ImageProcessor;
import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.utils.MarkerInterface;
import zollie.travelblogger.guidee.utils.MarkerRenderer;

import static zollie.travelblogger.guidee.R.id.mJourney;

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
    Bitmap markerImageGlob = null;
    ArrayList<JourneyModel> allJourneys = new ArrayList<JourneyModel>();
    public GoogleMap googleMap;
    //MarkerCache[] markerCache = new MarkerCache[100];
    private ClusterManager<MarkerItem> mClusterManager;
    MarkerItem clickedMarker;
    // paint defines the text color, stroke width and size

    Bitmap circleBitmap;
    ImageProcessor imageProcessor = new ImageProcessor(getActivity());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        final float scale = getResources().getDisplayMetrics().density;
        final Bitmap.Config conf = Bitmap.Config.ARGB_8888;

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
                    initializeClusterer();
                    Bitmap bmp = Bitmap.createBitmap((int)(60*scale),(int) (60*scale), conf);
                    Canvas canvas1 = new Canvas(bmp);
                    circleBitmap = imageProcessor.pulseMarker(4, bmp, canvas1, scale, circleBitmap);
                    DataHandler.getInstance().getJourneys(new DataHandlerListener() {
                        @Override
                        public void onJourneyData(final Map<String, Object> rawJourneyData, String journeyReference) {
                            //addMapMarker(journeyModel, mMap);
                            JourneyModel journeyModel = new JourneyModel(rawJourneyData, journeyReference, false);
                            allJourneys.add(journeyModel);
                            setUpClusterer(journeyModel, mMap);

                        }

                        @Override
                        public void onUserData(Map<String, Object> rawUserData, String userID) {

                        }

                        @Override
                        public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {
                            CommentModel commentModel = new CommentModel(rawCommentData, commentReference, journeyIdent);
                        }
                    });

                    // For showing a move to my location button

                    //           googleMap.setMyLocationEnabled(true);

                    // For dropping a ,,marker at a point on the Map
                    final LatLng sydney = new LatLng(-34, 151);
                    //==================== Old code without clustering =============================
                    /*googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                         //   Bitmap bmp = Bitmap.createBitmap((int)(60*scale),(int) (60*scale), conf);
                            Bitmap markerImage = null;

                            final String markerID = marker.getId();
                            JourneyModel mJourney = null;
                            int j = 0;
                            for(int i=0; i<allJourneys.size(); i++)
                            {
                                 mJourney = allJourneys.get(i);

                                if(mJourney.annotationModel.getMarkerID().equals(markerID))
                                {
                                    //markerImage = markerImageGlob;
//                                    markerImage = mJourney.annotationModel.markerIcon;
                                    mJourney.annotationModel.setMarker(marker);
                                    j=i;
                                    break;
                                }
                            }
                            new AsyncAnimation().execute(allJourneys.get(j));


                            return false;
                        }
                    });*/
                    //==============================================================================
                    //==================== New code with clustering ================================
                    mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {
                        @Override
                        public boolean onClusterItemClick(MarkerItem markerItem) {
                            //   Bitmap bmp = Bitmap.createBitmap((int)(60*scale),(int) (60*scale), conf);
                            clickedMarker = markerItem;
                            Bitmap markerImage = null;
                            final String markerID = markerItem.getID();
                            JourneyModel mJourney = null;
                            int j = 0;
                            for(int i=0; i<allJourneys.size(); i++)
                            {
                                mJourney = allJourneys.get(i);

                                    if (mJourney.annotationModel.getMarkerID().equals(markerID)) {
                                        //markerImage = markerImageGlob;
//                                    markerImage = mJourney.annotationModel.markerIcon;
                                        mJourney.annotationModel.setMarkerItem(markerItem);
                                        j = i;
                                        break;
                                    }

                            }
                            new AsyncAnimation().execute(allJourneys.get(j));
                            return false;
                        }
                    });
                    //==============================================================================
                    //==================== Old code without clustering =============================
                /*    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            // ======================== Go to selected journey =====================
                            final String markerID = marker.getId();
                            JourneyModel mJourney = null;
                            int j = 0;
                            for(int i=0; i<allJourneys.size(); i++)
                            {
                                mJourney = allJourneys.get(i);

                                if(mJourney.annotationModel.getMarkerID().equals(markerID))
                                {
                                    //markerImage = markerImageGlob;
//                                    markerImage = mJourney.annotationModel.markerIcon;
                                    j=i;
                                    break;
                                }
                            }
                            Intent toJourneyIntent = new Intent(getActivity(), JourneyView.class);
                            toJourneyIntent.putExtra("ser_journey", allJourneys.get(j));
                            getActivity().startActivity(toJourneyIntent);

                        }
                    });*/
                    //==============================================================================
                    //==================== New code with clustering ================================
                    mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem>() {
                        @Override
                        public void onClusterItemInfoWindowClick(MarkerItem markerItem) {
                            // ======================== Go to selected journey =====================
                            final String markerID = markerItem.getID();
                            JourneyModel mJourney = null;
                            int j = 0;
                            for(int i=0; i<allJourneys.size(); i++)
                            {
                                mJourney = allJourneys.get(i);

                                if(mJourney.annotationModel.getMarkerID().equals(markerID))
                                {
                                    //markerImage = markerImageGlob;
//                                    markerImage = mJourney.annotationModel.markerIcon;
                                    j=i;
                                    break;
                                }
                            }
                            Intent toJourneyIntent = new Intent(getActivity(), JourneyView.class);
                            toJourneyIntent.putExtra("ser_journey", allJourneys.get(j));
                            getActivity().startActivity(toJourneyIntent);
                        }
                    });
                    /*mClusterManager.getMarkerCollection().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            // ======================== Go to selected journey =====================
                            final String markerID = marker.getId();
                            JourneyModel mJourney = null;
                            int j = 0;
                            for(int i=0; i<allJourneys.size(); i++)
                            {
                                mJourney = allJourneys.get(i);

                                if(mJourney.annotationModel.getMarkerID().equals(markerID))
                                {
                                    //markerImage = markerImageGlob;
//                                    markerImage = mJourney.annotationModel.markerIcon;
                                    j=i;
                                    break;
                                }
                            }
                            Intent toJourneyIntent = new Intent(getActivity(), JourneyView.class);
                            toJourneyIntent.putExtra("ser_journey", allJourneys.get(j));
                            getActivity().startActivity(toJourneyIntent);

                        }
                    });*/

                    //==============================================================================
                    //       googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_friends)));

               /*Creating custom map markers here */

                    // For zooming automatically to the location of the marker
                    //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                   // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(39.43681513892361, 3.224011088360298), 5);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(39.43681513892361, 3.224011088360298)));
                    googleMap.animateCamera(cameraUpdate);
                    googleMap.setOnInfoWindowClickListener(mClusterManager);
                    googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
                    mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new InfoWindowAdapter());
                }
            });


        return rootView;
    }

    private void addMapMarker(final JourneyModel journeyModel, GoogleMap mMap){

        LatLng locationData = null;
        try {
            locationData = journeyModel.annotationModel.markerLatLng;
        } catch (Exception e) {
            e.printStackTrace();
        }

            final double markerLat = locationData.latitude;
            final double markerLng = locationData.longitude;

        final String markerTitle = journeyModel.annotationModel.markerTitle;
        long markerLikes = journeyModel.annotationModel.markerLikes;
        String journeySummary = journeyModel.summary;

            try {
                Canvas myCanvas = new Canvas();
                final float scale = getResources().getDisplayMetrics().density;


                if(markerImageGlob != null)
                    circleBitmap = imageProcessor.pulseMarker(1, markerImageGlob, myCanvas, scale, circleBitmap);

          /*
          // ===================== Old code snippet to add marker (WORKING)=========================
          Marker mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(markerLat, markerLng))
                        .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                        .title(markerTitle)
                        .anchor(0.4f, 1));
                String markerID = mMarker.getId();
                journeyModel.annotationModel.markerID = markerID;
            */
          //========================================================================================
            //==================== New code snippet to add marker item to clustermanager ===========
                MarkerItem mMarkerItem = new MarkerItem(markerLat,markerLng,markerTitle,markerLikes, journeySummary,circleBitmap);
                mClusterManager.addItem(mMarkerItem);
                mClusterManager.setRenderer(new MarkerRenderer(getActivity(), googleMap, mClusterManager, new MarkerInterface(){
                    @Override
                    public void setMarkerId(Marker marker) {
                        journeyModel.annotationModel.markerID = marker.getId();
                    }
                })
                );
            //======================================================================================
            }
            catch (Exception e) {
                e.printStackTrace();
            }

    }
     private Bitmap getMarkerPicture(JourneyModel journeyModel){
         final String markerImageSource = journeyModel.annotationModel.getMarkerIconURL();
         Bitmap markerImage= null;
         try{
             //============= Downloadding marker image via Glide ================
             markerImage = Glide
                     .with(getActivity())
                     .load(markerImageSource)
                     .asBitmap()
                     .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();

             //==================================================================
 //            URL markerImageUrl = new URL(markerImageSource);
 //            markerImage = BitmapFactory.decodeStream(markerImageUrl.openConnection().getInputStream());
             float scale = getResources().getDisplayMetrics().density;
             markerImage = imageProcessor.resizeMarkerImage(markerImage, scale*2/3);
             markerImageGlob = markerImage;
             //journeyModel.annotationModel.setMarkerIcon(markerImage);
          //   mapMarkerData.put("imgBitmap", markerImage);
             //markerImageGlob = BitmapFactory.decodeStream(markerImageUrl.openConnection().getInputStream());
            // markerImageGlob = resizeMarkerImage(markerImageGlob);
          /*   Canvas myCanvas = new Canvas();
             final float scale = getResources().getDisplayMetrics().density;
             pulseMarker(1, markerImageGlob, myCanvas, scale);*/
         }
         catch(Exception e) {
             e.printStackTrace();
         }
         return markerImage;
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
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
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

    public void animateMarker(Marker marker, final JourneyModel mJourney, final Bitmap bmp, final Canvas canvas1, final GoogleMap mMap){

        final LatLng markerLatLng = marker.getPosition();
        final String markerTitle = marker.getTitle();
        final float scale = getResources().getDisplayMetrics().density;
      //  String markerImage = marker.get
        try {
            marker.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
        circleBitmap = null;
        // Animating marker implementation =================================================
        circleBitmap = imageProcessor.pulseMarker(4, bmp, canvas1, scale, circleBitmap);
        myMarker2 = mMap.addMarker(new MarkerOptions().position(markerLatLng)
                .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                .title(markerTitle)
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.4f, 1));

        h.postDelayed(new Runnable(){  // Csak a legutolsót törli ki...a többi marker korábbról ott marad
            @Override
            public void run() {
                try{
                    circleBitmap = imageProcessor.pulseMarker(6, bmp, canvas1, scale, circleBitmap);
                    myMarker4 = mMap.addMarker(new MarkerOptions().position(markerLatLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                            .title(markerTitle)
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

                    circleBitmap = imageProcessor.pulseMarker(6, bmp, canvas1, scale, circleBitmap);
                    myMarker5 = mMap.addMarker(new MarkerOptions().position(markerLatLng)
                            .icon(BitmapDescriptorFactory.fromBitmap(circleBitmap))
                            .title(markerTitle)
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
        mJourney.annotationModel.markerID = myMarker2.getId();
        circleBitmap = null;
        markerImageGlob = null;
       /* for(int i=0; i<100; i++)
        {   if (markerCache[i] == null){
            markerCache[i] = new MarkerCache();
            markerCache[i].setMarkerID(markerID);
            markerCache[i].setMarkerIcon(bmp);
            i=100;
        }
        }*/
    }

    class AsyncMarkerLoader extends AsyncTask <Object,Void, JourneyModel>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected JourneyModel doInBackground(Object... params) {
            try {
                getMarkerPicture((JourneyModel) params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (JourneyModel) params[0];
        }
        @Override
        protected void onPostExecute(JourneyModel journeyModel) {
         //   super.onPostExecute(result);
                addMapMarker(journeyModel, googleMap);
  //          googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(circleBitmap)));
        }
    }

    class AsyncAnimation extends AsyncTask <Object, Void, JourneyModel>{
        @Override
        protected JourneyModel doInBackground(Object... params) {
            try {
                getMarkerPicture((JourneyModel) params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (JourneyModel) params[0];
        }

        @Override
        protected void onPostExecute(JourneyModel journeyModel) {

                if(markerImageGlob != null) {
                    Canvas canvas1 = new Canvas(markerImageGlob);
                    //animateMarker(journeyModel.annotationModel.getMarker(), journeyModel, markerImageGlob, canvas1, googleMap);
                }

        }
    }
    private void initializeClusterer(){
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MarkerItem>(getActivity(), googleMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

    }
    private void setUpClusterer(JourneyModel journeyModel, GoogleMap mMap) {


        // Add cluster items (markers) to the cluster manager.
        new AsyncMarkerLoader().execute(journeyModel, mMap);
    }
    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
           // MarkerItem offsetItem = new MarkerItem(lat, lng,"Title" ,"Snippet" , BitmapFactory.decodeResource(getResources(),R.mipmap.maxresdefault));
           // mClusterManager.addItem(offsetItem);
        }
    }
    public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {

        private View mContentView;

        InfoWindowAdapter() {
            this.mContentView = getActivity().getLayoutInflater().inflate(R.layout.infow_window, null);

        }

        @Override
        public View getInfoWindow(Marker marker) {

            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            TextView infoTitle = (TextView) mContentView.findViewById(R.id.info_window_title);
            TextView infoSummary = (TextView) mContentView.findViewById(R.id.info_window_summary);
            TextView infoLikes = (TextView) mContentView.findViewById(R.id.info_window_likes);

            infoTitle.setText(clickedMarker.getTitle());
            infoSummary.setText(clickedMarker.getSnippet());
            infoLikes.setText(String.valueOf(clickedMarker.getLikes()));

            return mContentView;
        }
    }

 }

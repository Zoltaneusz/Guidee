package zollie.travelblogger.guidee.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.api.client.util.Data;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.CarouselAdapter;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.models.CarouselModel;
import zollie.travelblogger.guidee.models.EventModel;
import zollie.travelblogger.guidee.models.JourneyModel;

/**
 * Created by FuszeneckerZ on 2017.01.09..
 */

public class EditEventView extends YouTubeBaseActivity {
    final int locationPermission = 0;
    MapView mMapView;
    public GoogleMap googleMap;
    Context mContext = this;
    LatLng updatedLatLng = new LatLng(19,46);
    ArrayList<String> photoPaths = new ArrayList<String>();
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 1;
    boolean storagePermission = false;
    ArrayList<String> imageUrls = new ArrayList<String>();
    int saveVisible = 0;
    String parentActivity = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_view);

        Bundle intentData = getIntent().getExtras();
        final EventModel mEvent = (EventModel) intentData.getParcelable("ser_event");

        parentActivity = intentData.getString("parent");
        if(mEvent.carouselModels != null) {
            if (mEvent.carouselModels.get(0).imageUrl != null) {
                if (mEvent.carouselModels.get(0).carouselType == CarouselModel.CarouselType.IMAGE) {
                    ImageView coverImage = (ImageView) findViewById(R.id.edit_event_imgFirst);
                    //===================== Adding Image to to Horizontal Slide via Glide =========
                    Glide
                            .with(this)
                            .load(mEvent.carouselModels.get(0).imageUrl)
                            .crossFade()
                            .into(coverImage);
                    //=============================================================================
                }
            }
        }
        // Get initial LatLng value in case user edits the journey but leaves the marker as is
        updatedLatLng = new LatLng(mEvent.eventLatLng.latitude, mEvent.eventLatLng.longitude);

        // Change all carousel models to be deletable
        for(CarouselModel carouselM : mEvent.carouselModels){
            carouselM.toDelete = 1;
        }

        final EditText mEventSummary = (EditText) findViewById(R.id.edit_event_summary_content);
        try {
            mEventSummary.setText(mEvent.summary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final EditText mEventTitle = (EditText) findViewById((R.id.edit_event_name));
        try {
            mEventTitle.setText(mEvent.title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final EditText mEventVideoUrl = (EditText) findViewById((R.id.edit_event_video_content));
        mEventVideoUrl.setText("ytQ5CYE1VZw");
        // ========================= Method for uploading pictures =================================
        checkPermission();
        TextView highlightTitle = (TextView) findViewById(R.id.edit_event_pictures_title);
        highlightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storagePermission) {
                    FilePickerBuilder.getInstance().setMaxCount(5)
                            .setSelectedFiles(photoPaths)
                            .setActivityTheme(R.style.AppTheme)
                            .pickPhoto((Activity) mContext);
                }
            }
        });

        //================================== Writes data to FIR database and navigates user back ===
        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.edit_event_save_FAB);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<CarouselModel> iterator = mEvent.carouselModels.iterator();

                while(iterator.hasNext()){
                    CarouselModel carouselM = iterator.next();
                    if(carouselM.toDelete == 2) {
                        iterator.remove();
                        DataHandler.getInstance().deleteCarouselInFIR(carouselM.FIRNumber, mEvent);
                        mEvent.deletedIndexes++;
                    }
                }
                int i = 0;
                for(CarouselModel carouselM : mEvent.carouselModels){
                    mEvent.carouselModels.get(i).toDelete = 0;
                    i++;
                }

                EventModel updatedEvent = new EventModel(mEvent); // only copies reference, nor real clone!!!
                updatedEvent.summary = mEventSummary.getText().toString();
                updatedEvent.title = mEventTitle.getText().toString();
                updatedEvent.eventLatLng = new LatLng(updatedLatLng.latitude, updatedLatLng.longitude);
                String eventVideoURL = mEventVideoUrl.getText().toString();

                int carousels = mEvent.carouselModels.size() + mEvent.deletedIndexes;
                updatedEvent.addItemToCarousels(imageUrls, eventVideoURL);
                DataHandler.getInstance().setEventInFIR(carousels, updatedEvent);

                if(parentActivity.equals("EditJourneyView")) {
                    Intent toJourneyIntent = new Intent(mContext, EditJourneyView.class);
                    Bundle intentData = getIntent().getExtras();
                    JourneyModel parentJourney = (JourneyModel) intentData.getParcelable("ser_journey");
                    parentJourney.eventModels.add(updatedEvent);
                    toJourneyIntent.putExtra("ser_journey", parentJourney);
                    mContext.startActivity(toJourneyIntent);

                }else {
                    Intent toEventIntent = new Intent(mContext, EventView.class);
                    toEventIntent.putExtra("ser_event", updatedEvent);
                    mContext.startActivity(toEventIntent);
                }
            }
        });
        //==========================================================================================
        FloatingActionButton cancelButton = (FloatingActionButton) findViewById(R.id.edit_event_cancel_FAB);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toEventIntent = new Intent(mContext, EventView.class);
                toEventIntent.putExtra("ser_event", mEvent);
                mContext.startActivity(toEventIntent);
            }
        });


//        FloatingActionButton editEventFAB = (FloatingActionButton) findViewById(R.id.event_edit_FAB);
//        editEventFAB.setVisibility(View.INVISIBLE);

        fillRecyclerView(R.id.edit_event_pictures_recycle_test, R.id.edit_event_pictures_recycle_placeholder, mEvent.carouselModels);

        mMapView = (MapView) findViewById(R.id.edit_event_Map);
        mMapView.onCreate(savedInstanceState);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.edit_event_scroll_view);

        // Method to deprecate touch events of ScrollView in case the user touches the map =========
        ImageView transparent = (ImageView)findViewById(R.id.imagetrans);
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
        // =========================================================================================

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

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mEvent.eventLatLng, 9);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(mEvent.eventLatLng ));
                googleMap.animateCamera(cameraUpdate);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        updatedLatLng = latLng;
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    }
                });
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
            case locationPermission:
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


            case READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //  Method for uploading pictures
                    storagePermission = true;

                } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
        }

            // other 'case' lines to check for other
            // permissions this app might request
    }


    /* Problem with uploading more pictures: when the user selects more pictures, they will be uploaded after selection.
    After that, when he opens the upload dialog box again, he may choose up to another 5 pictures. These will be uploaded aswell after closing the dialog.
    Clicking the save button and navigating back to EventView will however only save the last 5 uploaded pictures to Firebase! <--- BAROMSÁG!
    */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    //photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS));
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    ArrayList<String> docPaths  = new ArrayList<>();
                    docPaths .addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                }
                break;
        }
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://guidee-f0453.appspot.com");
        int i = 0;
        final FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.edit_event_save_FAB);
        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.edit_event_progressbar);
        for(String photoPath : photoPaths) {

            saveButton.setVisibility(View.INVISIBLE);
            progressbar.setVisibility(View.VISIBLE);
            final Uri file = Uri.fromFile(new File(photoPath));
            StorageReference pictureRef = storageRef.child("images/" + file.getLastPathSegment());
            UploadTask uploadTask = pictureRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    // Upload download URL to Firebase database
                    imageUrls.add(downloadUrl.toString());
                    saveVisible++;
                    if(saveVisible == photoPaths.size()) {
                        saveButton.setVisibility(View.VISIBLE);
                        progressbar.setVisibility(View.INVISIBLE);
                    }
                }
            });
            i++;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Reading the storage is needed to choose files for uploading.");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);
                }
                return false;
            } else {
                storagePermission = true;
                return true;
            }
        } else {
            return true;
        }
    }
}

package zollie.travelblogger.guidee.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.CommentAdapter;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.adapters.EventAdapter;
import zollie.travelblogger.guidee.adapters.JourneyAdapter;
import zollie.travelblogger.guidee.models.CommentModel;
import zollie.travelblogger.guidee.models.EventModel;
import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.models.UserModel;
import zollie.travelblogger.guidee.utils.ProfileHandlerUtility;

/**
 * Created by FuszeneckerZ on 2016.12.31..
 */

public class EditJourneyView extends Activity{

    final int locationPermission = 0;
    MapView mMapView;
    public GoogleMap googleMap;
    LatLng updatedLatLng = new LatLng(19,46);
    ArrayList<String> photoPaths = new ArrayList<String>();
    ArrayList<CommentModel> allComments = new ArrayList<CommentModel>();
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 1;
    boolean storagePermission = false;
    ArrayList<String> imageUrls = new ArrayList<String>();
    int saveVisible = 0;
    boolean userEditEight = false;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_edit_journey_view);
        Bundle intentData = getIntent().getExtras();
        final JourneyModel mJourney = (JourneyModel) intentData.getParcelable("ser_journey");
        if(mJourney.coverImageUrl != null){
            ImageView coverImage = (ImageView ) findViewById(R.id.edit_journey_imgFirst);
            //===================== Adding Image to to Horizontal Slide via Glide =========
            Glide
                    .with(this)
                    .load(mJourney.coverImageUrl)
                    .crossFade()
                    .into(coverImage);
            //=============================================================================

        }

        // Get initial LatLng value in case user edits the journey but leaves the marker as is
        updatedLatLng = new LatLng(mJourney.annotationModel.markerLatLng.latitude, mJourney.annotationModel.markerLatLng.longitude);

        // Change all event models to be deletable
        for(EventModel eventM : mJourney.eventModels){
            eventM.toDelete = 1;
        }

        final EditText  mJourneySummary = (EditText) findViewById(R.id.edit_journey_summary_content);
        try {
            mJourneySummary.setText(mJourney.summary);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final EditText mJourneyTitle = (EditText) findViewById(R.id.edit_journey_name);
        try {
            mJourneyTitle.setText(mJourney.title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ============================== Upload data to FIR and navigate back =====================
        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.edit_journey_save_FAB);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<EventModel> iterator = mJourney.eventModels.iterator();
                while(iterator.hasNext()){
                    EventModel eventM = iterator.next();
                    if(eventM.toDelete == 2) {
                        iterator.remove();
                        DataHandler.getInstance().deleteEventInFIR(eventM.FIRNumber, mJourney);
                        mJourney.deletedIndexes++;
                    }
                }
                int i = 0;
                for(EventModel eventM : mJourney.eventModels){
                    mJourney.eventModels.get(i).toDelete = 0;
                    i++;
                }


                mJourney.summary = mJourneySummary.getText().toString();
                mJourney.title = mJourneyTitle.getText().toString();
                mJourney.annotationModel.markerLatLng = new LatLng(updatedLatLng.latitude, updatedLatLng.longitude);
                if(!imageUrls.isEmpty()) {
                    mJourney.coverImageUrl = imageUrls.get(0);
                }
                int eventsCount = mJourney.eventModels.size();
                DataHandler.getInstance().setJourneyInFIR(eventsCount, mJourney);
                // Change comments, profile picture and events needed

                Intent toJourneyIntent = new Intent(mContext, JourneyView.class);
                toJourneyIntent.putExtra("ser_journey", mJourney);
                mContext.startActivity(toJourneyIntent);

            }
        });
        //==========================================================================================
        // ============================== Navigate back ============================================
        FloatingActionButton cancelButton = (FloatingActionButton) findViewById(R.id.edit_journey_cancel_FAB);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If new empty event was created, then delete it.

                Intent toJourneyIntent = new Intent(mContext, JourneyView.class);
                toJourneyIntent.putExtra("ser_journey", mJourney);
                mContext.startActivity(toJourneyIntent);

            }
        });
        //==========================================================================================
        //=============================== Method for adding new event ==============================
        TextView highlightTitle = (TextView) findViewById(R.id.edit_journey_events_title);
        highlightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataHandler.getInstance().createEventInFIR(9, mJourney);
                EventModel emptyEvent = new EventModel(mJourney);
                mJourney.eventModels.add(emptyEvent);

                Intent toEventIntent = new Intent(mContext, EditEventView.class);
                toEventIntent.putExtra("ser_event", emptyEvent);
                toEventIntent.putExtra("ser_journey", mJourney);
                toEventIntent.putExtra("parent", "EditJourneyView");
                mContext.startActivity(toEventIntent);
            }
        });
        //==========================================================================================
        //=============================== Upload cover image =======================================
        checkPermission();
        ImageView coverImage = (ImageView) findViewById(R.id.edit_journey_imgFirst);
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storagePermission) {
                    FilePickerBuilder.getInstance().setMaxCount(1)
                            .setSelectedFiles(photoPaths)
                            .setActivityTheme(R.style.AppTheme)
                            .pickPhoto((Activity) mContext);
                }
            }
        });
        //==========================================================================================
        //================== Get user eligibility for writing this journey =========================
        new AsyncEditRightCheck().execute(mJourney);
        //==========================================================================================
        fillRecyclerView(R.id.edit_journey_events_recycle, R.id.edit_journey_events_recycle_placeholder, mJourney.eventModels);

        DataHandler.getInstance().getCommentsWithID(mJourney.ID, new DataHandlerListener() {
            @Override
            public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {

            }

            @Override
            public void onUserData(Map<String, Object> rawUserData) {

            }

            @Override
            public void onCommentData(Map<String, Object> rawCommentData) {
                CommentModel commentModel = new CommentModel(rawCommentData);
                allComments.add(commentModel);
                fillCommentsRecyclerView(R.id.edit_journey_comments_recycle, R.id.edit_journey_comments_recycle_placeholder, allComments);

            }
        });
        mMapView = (MapView) findViewById(R.id.edit_journey_Map);
        mMapView.onCreate(savedInstanceState);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.edit_journey_scroll_view);
        ImageView transparent = (ImageView)findViewById(R.id.edit_journey_imagetrans);

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
                        .position(mJourney.annotationModel.markerLatLng)
                        .title(mJourney.title)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        updatedLatLng = latLng;
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    }
                });
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mJourney.annotationModel.markerLatLng, 10);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(mJourney.annotationModel.markerLatLng));
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
            case READ_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //  Method for uploading pictures
                    storagePermission = true;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void fillRecyclerView(int primaryResource, int emptyResource, ArrayList<EventModel> eventModels){

        RecyclerView rvEvents = (RecyclerView) findViewById(primaryResource);

        EventAdapter adapter = new EventAdapter(this, eventModels);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, zollie.travelblogger.guidee.utils.DividerItemDecoration.VERTICAL_LIST);
        rvEvents.addItemDecoration(itemDecoration);
        //      if(eventModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }
    public void fillCommentsRecyclerView(int primaryResource, int emptyResource, ArrayList<CommentModel> commentModels){

        RecyclerView rvEvents = (RecyclerView) findViewById(primaryResource);

        CommentAdapter adapter = new CommentAdapter(this, commentModels);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, zollie.travelblogger.guidee.utils.DividerItemDecoration.VERTICAL_LIST);
        rvEvents.addItemDecoration(itemDecoration);
        //      if(eventModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }

    class AsyncEditRightCheck extends AsyncTask<Object, Void, JourneyModel>{
        @Override
        protected JourneyModel doInBackground(Object... params) {
            final JourneyModel mJourney =  (JourneyModel) params[0];
            ProfileHandlerUtility mUtility = new ProfileHandlerUtility();
            boolean editRight = false;

            final ArrayList<String> allJourneys = new ArrayList<String>();
            FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser();
            String firUserID = firUser.getUid();
            DataHandler.getInstance().getUserStringWithId(new String(firUserID), new DataHandlerListener() {
                @Override
                public void onJourneyData(final Map<String, Object> rawJourneyData, String journeyReference) {
                }

                @Override
                public void onUserData(Map<String, Object> rawUserData) {
                    UserModel mInstance = new UserModel(rawUserData);
                    for (Map.Entry<String, Object> map : mInstance.userJourneys.entrySet()) {
                        String journeyModel = (String) map.getValue();
                        allJourneys.add(journeyModel);
                        for(String string : allJourneys){
                            if(string.matches(mJourney.ID)) {
                                mJourney.userEligible = true;
                                mJourney.setEventsEligibility();
                            }
                        }
                    }
                }

                @Override
                public void onCommentData(Map<String, Object> rawCommentData) {

                }

            });

            return mJourney;
        }

        @Override
        protected void onPostExecute(JourneyModel mJourney) {
            userEditEight = mJourney.userEligible;
        }
    }

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
        final FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.edit_journey_save_FAB);
        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.edit_journey_progressbar);
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

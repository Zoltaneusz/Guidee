package zollie.travelblogger.guidee.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.EnvironmentCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.adapters.CommentAdapter;
import zollie.travelblogger.guidee.adapters.DataHandler;
import zollie.travelblogger.guidee.adapters.DataHandlerListener;
import zollie.travelblogger.guidee.adapters.EventAdapter;
import zollie.travelblogger.guidee.adapters.JourneyAdapter;
import zollie.travelblogger.guidee.adapters.LovedListener;
import zollie.travelblogger.guidee.fragments.LikeListDialogFragment;
import zollie.travelblogger.guidee.models.CommentModel;
import zollie.travelblogger.guidee.models.EventModel;
import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.models.UserModel;
import zollie.travelblogger.guidee.utils.ImageProcessor;
import zollie.travelblogger.guidee.utils.ProfileHandlerUtility;

/**
 * Created by FuszeneckerZ on 2016.12.31..
 */

public class JourneyView extends AppCompatActivity{

    final int locationPermission = 0;
    final int writeStoragePermission = 1;
    int storagePermission = 0;
    MapView mMapView;
    public GoogleMap googleMap;
    ArrayList<CommentModel> allComments = new ArrayList<CommentModel>(30);
    boolean userEditRight = false;
    public Context mContext;
    Bitmap userAvatarGlobal = null;
    ImageProcessor imageProcessor = new ImageProcessor(this);
    FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser(); // Should be in onResume() with almost every other method.
    JourneyModel mJourney = null;
    RecyclerView.ItemDecoration itemDecoration = null;
    Profile faceUser = null;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        itemDecoration = new
                DividerItemDecoration(this, zollie.travelblogger.guidee.utils.DividerItemDecoration.VERTICAL_LIST);
        setContentView(R.layout.activity_journey_view);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
                faceUser = currentProfile;
            }
        };

        mMapView = (MapView) findViewById(R.id.journey_Map);
        mMapView.onCreate(savedInstanceState);
        final NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.journey_scroll_view);
        ImageView transparent = (ImageView)findViewById(R.id.journey_imagetrans);

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
        if(Build.VERSION.SDK_INT>22) {
            if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("Write storage", "Permission is granted");
                storagePermission = 1;

            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, writeStoragePermission);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
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
            case writeStoragePermission: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    storagePermission = 1;
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
    protected void onResume() {
        super.onResume();

        faceUser = Profile.getCurrentProfile();
        Bundle intentData = getIntent().getExtras();
        mJourney  = (JourneyModel) intentData.getParcelable("ser_journey");

        // Change App Toolbar
        changeAppBar(mJourney);

        //============================= Intent to journey owner ===========================
        ImageView journeyOwnerView = (ImageView) findViewById(R.id.journey_owner_icon);
        journeyOwnerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfileIntent = new Intent(mContext, ProfileView.class);
                toProfileIntent.putExtra("owner_ID", mJourney.ownerID);
                mContext.startActivity(toProfileIntent);
            }
        });
        //=================================================================================
        //Get user eligibility for writing this journey
        new AsyncEditRightCheck().execute(mJourney);

        TextView mJourneySummary = (TextView) findViewById(R.id.journey_summary_content);
        try {
            mJourneySummary.setText(mJourney.summary);
        } catch (Exception e) {
            e.printStackTrace();
        }



        //===================== Journey LIKE method ================================================
        final FloatingActionButton likeButton = (FloatingActionButton) findViewById(R.id.journey_love_icon);
        //  Static  imageview change
        DataHandler.getInstance().setUserLoved(mJourney, firUser, likeButton);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataHandler.getInstance().loveJourneyInFIR(mJourney, firUser, (FloatingActionButton) view);
            }
        });


        //==========================================================================================
        //===================== Journey SHARE method ===============================================
        final FloatingActionButton shareButton = (FloatingActionButton) findViewById(R.id.journey_share_FAB);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (storagePermission == 1) {
                    imageProcessor.shareContent(mJourney);
                }
            }
        });


        //==========================================================================================
        TextView addComment = (TextView) findViewById(R.id.journey_comments_add);
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCommentPopup(mJourney);
            }
        });
        //====================== Get journey owner picture and display it ==========================
        new AsyncOwnerPic().execute(mJourney);
        //==========================================================================================

        fillRecyclerView(R.id.journey_events_recycle, R.id.journey_events_recycle_placeholder, mJourney.eventModels);

        DataHandler.getInstance().getCommentsWithID(mJourney.ID, new DataHandlerListener() {
            @Override
            public void onJourneyData(Map<String, Object> rawJourneyData, String journeyReference) {

            }

            @Override
            public void onUserData(Map<String, Object> rawUserData, String userID) {

            }

            @Override
            public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {
                CommentModel commentModel = new CommentModel(rawCommentData, commentReference, journeyIdent);
                allComments.add(commentModel);
                fillCommentsRecyclerView(R.id.journey_comments_recycle, R.id.journey_comments_recycle_placeholder, allComments);

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
        // ===================================== Get Facebook User Friends List ====================
        final TextView journeyLoveList = (TextView) findViewById(R.id.journey_love_list);
        /* make the API call */
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/" + faceUser.getId() + "/friends",
//            //    "/{friend-list-id}",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//            /* handle the result */
//            // TODO: compare friends array from FIR with Facebook friends
//                JSONObject friendsJSON = response.getJSONObject();
//                        try {
//                       //     JSONObject friends = friendsJSON.getJSONObject("friends");
//                            JSONArray friendsArray = (JSONArray) friendsJSON.get("data");
//                            JSONObject friendObject = (JSONObject) friendsArray.get(0);
//                            String friendName = (String) friendObject.get("name");
//                            journeyLoveList.setText(friendName + " loves this");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//        ).executeAsync();
        // Alternative
   /*     AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest graphRequest = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                try {
                    JSONArray jsonArrayFriends = jsonObject.getJSONObject("friendlist").getJSONArray("name");
                  //  JSONArray jsonArrayFriends = jsonObject.getJSONArray("name");
                    JSONObject friendlistObject = jsonArrayFriends.getJSONObject(0);
                    String friendListID = friendlistObject.getString("id");
                    myNewGraphReq(friendListID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
    //    param.putString("fields", "friendlist", "members");
        graphRequest.setParameters(param);
        graphRequest.executeAsync();

*/
        // Alternative  2
        GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(AccessToken.getCurrentAccessToken(),"/me/friends",null, HttpMethod.GET,new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {

                try {
                    JSONArray rawName = response.getJSONObject().getJSONArray("data");

                    String friendList = "{\"friendlist\":" + rawName.getJSONObject(0).get("name") + "}";
                    //String friendlist =  rawName.toString() ;
                    Log.d("TAG","response of friendlist is : " + friendList);
                    if(rawName.length()>1)
                        journeyLoveList.setText(rawName.getJSONObject(0).get("name") + " " + Integer.toString(rawName.length()) + "love this");
                    else
                        journeyLoveList.setText(rawName.getJSONObject(0).get("name") + "loves this");
                       /* //coding for insert data in db.

                        String result = JSONUtils.insertUserprofile(imagePath, name, fbid, friendList);

                        Log.d("TAG", "Result of fb is : " + result);

                        if (result.toLowerCase().contains("success")) {

                            myPreferences.setFBUserId(Constant.PREFERENCE_LOGIN_FB, fbid);
                            //LoginManager.getInstance().logOut();
                        }*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).executeAsync();

        // =========================================================================================

        journeyLoveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHandler.getInstance().getJourneyLovers(mJourney, new LovedListener() {
                    @Override
                    public void onLoved(final Map<String, Object> rawUserData) {

                        // Go to Fragment that shows likers list
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Fragment prev = getFragmentManager().findFragmentByTag("likersViewer");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        DialogFragment newFragment = LikeListDialogFragment.newInstance(rawUserData);
                        newFragment.show(ft, "likersViewer");
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void myNewGraphReq(String friendlistId) {
        final String graphPath = "/"+friendlistId+"/members/";
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                JSONObject object = graphResponse.getJSONObject();
                try {
                    JSONArray arrayOfUsersInFriendList= object.getJSONArray("data");
                /* Do something with the user list */
                /* ex: get first user in list, "name" */
                    JSONObject user = arrayOfUsersInFriendList.getJSONObject(0);
                    String usersName = user.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle param = new Bundle();
        param.putString("fields", "name");
        request.setParameters(param);
        request.executeAsync();
    }

    public void fillRecyclerView(int primaryResource, int emptyResource, ArrayList<EventModel> eventModels){

        RecyclerView rvEvents = (RecyclerView) findViewById(primaryResource);
        rvEvents.setNestedScrollingEnabled(false);
        EventAdapter adapter = new EventAdapter(this, eventModels);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvEvents.addItemDecoration(itemDecoration);
  //      if(eventModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }
    public void fillCommentsRecyclerView(int primaryResource, int emptyResource, ArrayList<CommentModel> commentModels){

        RecyclerView rvEvents = (RecyclerView) findViewById(primaryResource);
        rvEvents.setNestedScrollingEnabled(false);
        CommentAdapter adapter = new CommentAdapter(this, commentModels);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvEvents.addItemDecoration(itemDecoration);
        //      if(eventModels.isEmpty() == true ) showPlaceholderCards(emptyResource);
        //     rvJourneys.setVisibility(View.INVISIBLE);
    }

    class AsyncEditRightCheck extends AsyncTask<Object, Void, JourneyModel>{ // This can be made far far easier with Firebase key: "userId"
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
                public void onUserData(Map<String, Object> rawUserData, String userID) {
                    UserModel mInstance = new UserModel(rawUserData, userID);
                    for (Map.Entry<String, Object> map : mInstance.userJourneys.entrySet()) {
                        String journeyModel = (String) map.getValue();
                        allJourneys.add(journeyModel);
                        for(String string : allJourneys){
                            if(string.matches(mJourney.ID)) {
                                mJourney.userEligible = true;
                                mJourney.setEventsEligibility();
                                if(mJourney.userEligible) {
                                    FloatingActionButton editJourneyFAB = (FloatingActionButton) findViewById(R.id.journey_edit_FAB);
                                    editJourneyFAB.setVisibility(View.VISIBLE);
    //                                ImageView ownerIcon = (ImageView) findViewById(R.id.journey_owner_icon);
    //                                ownerIcon.setVisibility(View.INVISIBLE);
                                    editJourneyFAB.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent toJourneyIntent = new Intent(mContext, EditJourneyView.class);
                                            toJourneyIntent.putExtra("ser_journey", mJourney);
                                            toJourneyIntent.putExtra("parent", "JourneyView");
                                            mContext.startActivity(toJourneyIntent);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCommentData(Map<String, Object> rawCommentData, String commentReference, String journeyIdent) {

                }

            });

            return mJourney;
        }

        @Override
        protected void onPostExecute(final JourneyModel mJourney) {

        }
    }





    public void addCommentPopup(final JourneyModel journeyModel) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View popupView = li.inflate(R.layout.comment_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(popupView);

        TextView message = (TextView) popupView.findViewById(R.id.c_popup_info);
        message.setText("Your comment");
        final EditText userInput = (EditText) popupView.findViewById(R.id.c_popup_input);
        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Comment")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseUser firUser = FirebaseAuth.getInstance().getCurrentUser();
                        String firUserID = firUser.getUid();
                        DataHandler.getInstance().getUserWithId(firUserID, new DataHandlerListener() {
                            @Override
                            public void onJourneyData(Map<String, Object> rawJourneyData, String journeyID) {

                            }

                            @Override
                            public void onUserData(Map<String, Object> rawUserData, String userID) {
                                String author = null;
                                String avatarURL = null;
                                try {
                                    author = (String) rawUserData.get("name");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    avatarURL = (String) rawUserData.get("avatarUrl");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String comment = userInput.getText().toString();
                                CommentModel commentModel = new CommentModel(author, avatarURL, comment, journeyModel.ID);
                                DataHandler.getInstance().createCommentInFIR(commentModel, journeyModel); // Problem: JourneyView still needs to be refreshed to show the new comment...
                            }

                            @Override
                            public void onCommentData(Map<String, Object> rawCommentData, String commentID, String journeyIdent) {

                            }
                        });

                    }
                });
        alertDialogBuilder
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }

                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    class AsyncOwnerPic extends AsyncTask<Object, Void, JourneyModel>{


        @Override
        protected JourneyModel doInBackground(Object... params) {
            JourneyModel mJourney = new JourneyModel((JourneyModel)(params[0]));
            //===================== Adding Image to to Horizontal Slide via Glide =========
            try {
                userAvatarGlobal= Glide.with(mContext)
                        .asBitmap()
                        .load(mJourney.userAvatarUrl)
                        .into(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            finally{
                float scale = getResources().getDisplayMetrics().density;
                if(userAvatarGlobal != null)
                userAvatarGlobal = imageProcessor.resizeMarkerImage(userAvatarGlobal, scale*3/3);
            }
            //=============================================================================

            return mJourney;
        }

        @Override
        protected void onPostExecute(JourneyModel mJourney) {
            ImageView ownerPic = (ImageView) findViewById(R.id.journey_owner_icon);
            final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            float scale = getResources().getDisplayMetrics().density;
            Bitmap bmp = Bitmap.createBitmap((int)(100*scale),(int) (100*scale), conf);
            Canvas canvas1 = new Canvas(bmp);
            Bitmap circleBitmap = imageProcessor.pulseMarker(4, bmp, canvas1, scale*2, userAvatarGlobal, false);
            if(userAvatarGlobal != null) {
                circleBitmap = imageProcessor.pulseMarker(4, userAvatarGlobal, canvas1, scale * 2, circleBitmap, false);
                ownerPic.setImageBitmap(circleBitmap);
            }

        }
    }
    public void changeAppBar(JourneyModel journeyModel){

        // ================== Change Scrolling Toolbar ========================================
        Toolbar toolbar = (Toolbar) findViewById(R.id.journey_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.backButton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.journey_collapse_toolbar);
        collapsingToolbar.setTitle(journeyModel.title);
        if(journeyModel.coverImageUrl != null){
            ImageView coverImage = (ImageView ) findViewById(R.id.journey_imgFirst);
            //===================== Adding Image to to Horizontal Slide via Glide =========
            Glide
                    .with(this)
                    .load(journeyModel.coverImageUrl)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(coverImage);
            //=============================================================================

        }
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

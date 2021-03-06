package zollie.travelblogger.guidee.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zollie.travelblogger.guidee.R;
import zollie.travelblogger.guidee.models.CarouselModel;
import zollie.travelblogger.guidee.models.CommentModel;
import zollie.travelblogger.guidee.models.EventModel;
import zollie.travelblogger.guidee.models.JourneyModel;
import zollie.travelblogger.guidee.models.UserModel;

/**
 * Created by FuszeneckerZ on 2016.12.03..
 */

public class DataHandler {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();



    private static DataHandler myInstance = null;

    private String myProfPic;

    private DataHandler(){
        myProfPic = "Default";
    }

    public static DataHandler getInstance(){
        if(myInstance == null){
            myInstance = new DataHandler();
        }
        return myInstance;
    }

    public String getProfPic(){
        return this.myProfPic;
    }

    public void getJourneys(final DataHandlerListener dataHandlerListener) {
        mRootRef.child("Journeys").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> kutya = (Map<String, Object>) dataSnapshot.getValue();
                String mJourneyRef = dataSnapshot.getKey();
                dataHandlerListener.onJourneyData(kutya, mJourneyRef);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserStringWithId(String userId, final DataHandlerListener dataHandlerListener){
        DatabaseReference mUserReference = mRootRef.child("Users");
        mUserReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                dataHandlerListener.onUserData(userInfo, dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getUserWithId(String userId, final DataHandlerListener dataHandlerListener) {
        DatabaseReference mUserReference = mRootRef.child("Users");
       // Map<String, Object> mUser = (Map<String, Object>) mUserReference.child(userId);
        mUserReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> userInfo = (Map<String, Object>) dataSnapshot.getValue();
                dataHandlerListener.onUserData(userInfo, dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getJourneyWithIds(ArrayList<String> journeyIds, final DataHandlerListener dataHandlerListener)
    {
        if(journeyIds == null) return;
        for (String journeyId : journeyIds) {
            DatabaseReference mJourneyReference = mRootRef.child("Journeys");
            mJourneyReference.child(journeyId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> mJourneyReference = (Map<String, Object>) dataSnapshot.getValue();
                    String journeyRef = dataSnapshot.getKey();
                    dataHandlerListener.onJourneyData(mJourneyReference, journeyRef);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    public void getCommentsWithID(final String journeyID, final DataHandlerListener dataHandlerListener) {
        DatabaseReference mCommentsReference = mRootRef.child("Comments");
        mCommentsReference.child(journeyID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> commentInfo = (Map<String, Object>) dataSnapshot.getValue();
                String commentRef = dataSnapshot.getKey();
                dataHandlerListener.onCommentData(commentInfo, commentRef, journeyID);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setEventInFIR(int originalCarouselNr, EventModel updatedEvent, boolean emptyFilled){

        DatabaseReference mDatabaseReference  =  mRootRef.child("Journeys");
        ArrayList<Map<String, Object>> rawEventModels = null;
        DatabaseReference eventReference = mDatabaseReference.child(updatedEvent.journeyID).child("eventModels").child(String.valueOf(updatedEvent.FIRNumber));
        // From here we can modify the data in FIR Database.

        Map<String, Object> eventUpdates = new HashMap<String, Object>();
        eventUpdates.put("title", updatedEvent.title);
        eventUpdates.put("summary", updatedEvent.summary);
        eventUpdates.put("location/latitude", updatedEvent.eventLatLng.latitude);
        eventUpdates.put("location/longitude", updatedEvent.eventLatLng.longitude);
        int j = 0;
        if(emptyFilled){
            eventUpdates.put("carouselModels/0/imageURL", updatedEvent.carouselModels.get(0).imageUrl);
            j = 1;
        }
        //int updatedCarouselNr = updatedEvent.getHighestCarouselID(updatedEvent);
        int delCount =  + updatedEvent.deletedIndexes;
        int updatedCarouselNr = updatedEvent.carouselModels.size();
        if(originalCarouselNr != updatedCarouselNr) {
            for (int i = originalCarouselNr; i < updatedCarouselNr - j ; i++) {
                if (updatedEvent.carouselModels.get(i+j).carouselType == CarouselModel.CarouselType.IMAGE) {
                    eventUpdates.put("carouselModels/" + String.valueOf(i+delCount+j) + "/imageURL", updatedEvent.carouselModels.get(i+j).imageUrl);
                } else {
                    if (updatedEvent.carouselModels.get(i+j).videoUrl.length() == 11)  // Only 11 characted long videos will be allowed (yet)
                        eventUpdates.put("carouselModels/" + String.valueOf(i+delCount+j) + "/videoYoutubeId", updatedEvent.carouselModels.get(i+j).videoUrl);
                }
            }
        }
        eventReference.updateChildren(eventUpdates);

    }

    public void deleteCarouselInFIR(int index, EventModel updatedEvent){
        DatabaseReference mDatabaseReference  =  mRootRef.child("Journeys");
        ArrayList<Map<String, Object>> rawEventModels = null;
        DatabaseReference eventReference = mDatabaseReference.child(updatedEvent.journeyID).child("eventModels").child(String.valueOf(updatedEvent.FIRNumber));
        // From here we can modify the data in FIR Database.

        Map<String, Object> eventUpdates = new HashMap<String, Object>();
        if(updatedEvent.carouselModels.size() == 0) {
            eventUpdates.put("carouselModels/" + index, null);
            eventUpdates.put("location", null);
            eventUpdates.put("summary", null);
            eventUpdates.put("title", null);
        }
        else
            eventUpdates.put("carouselModels/" + index, null);
        eventReference.updateChildren(eventUpdates);
    }

    public void setJourneyInFIR(int originalEventNr, JourneyModel updatedJourney){

        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");
        DatabaseReference journeyReference = mDatabaseReference.child(updatedJourney.ID);
        // From here we can modify the data in FIR Database

        Map<String, Object> journeyUpdates = new HashMap<String, Object>();
        journeyUpdates.put("title", updatedJourney.title);
        journeyUpdates.put("summary", updatedJourney.summary);
        journeyUpdates.put("annotationModel/location/latitude", updatedJourney.annotationModel.markerLatLng.latitude);
        journeyUpdates.put("annotationModel/location/longitude", updatedJourney.annotationModel.markerLatLng.longitude);
        journeyUpdates.put("annotationModel/imageURL", updatedJourney.userAvatarUrl);
        journeyUpdates.put("annotationModel/title", updatedJourney.title);
        journeyUpdates.put("userAvatarUrl", updatedJourney.userAvatarUrl);
        journeyUpdates.put("coverImageUrl", updatedJourney.coverImageUrl);
        // Events will be uploaded in EditEventView after
        /*int updatedEventNr = updatedJourney.eventModels.size() + updatedJourney.deletedIndexes;
        if(originalEventNr != updatedEventNr){
            for(int i=originalEventNr; i<updatedEventNr + 1; i++){
                journeyUpdates.put("eventModels/" + String.valueOf(i) + )
            }

        }*/
        journeyReference.updateChildren(journeyUpdates);
    }

    public String createJourneyInFIR(String FIRUser){
        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");
        String journeyKey = mDatabaseReference.push().getKey();

        Map<String, Object> journeyUpdates = new HashMap<String, Object>();
        journeyUpdates.put(journeyKey + "/title", "Your journey title");           // These are not even needed...
        journeyUpdates.put(journeyKey + "/summary", "Your journey summary");
        journeyUpdates.put(journeyKey + "/userAvatarUrl", "Your avatar Url");
        journeyUpdates.put(journeyKey + "/coverImageUrl", "https://firebasestorage.googleapis.com/v0/b/guidee-f0453.appspot.com/o/images%2F9F4DD9C8-5465-464B-9249-9127AD09E729.jpg?alt=media&token=32a14ec9-a298-4c68-89b3-211eb0f86e7e");
        journeyUpdates.put(journeyKey + "/annotationModel/location/latitude", 42.993976403334706);
        journeyUpdates.put(journeyKey + "/annotationModel/location/longitude", 16.40115687746871);
        journeyUpdates.put(journeyKey + "/annotationModel/imageURL", "Your image URL");
        journeyUpdates.put(journeyKey + "/annotationModel/title", "Your journey title");

        mDatabaseReference.updateChildren(journeyUpdates);      // These are not even needed...

        DatabaseReference mUserReference = mRootRef.child("Users").child(FIRUser);
        DatabaseReference mJourneyReference = mUserReference.child("journeys");
        String userJourneyKey = mJourneyReference.push().getKey();

        Map<String, Object> userJourneyUpdates = new HashMap<String, Object>();
        userJourneyUpdates.put(userJourneyKey, journeyKey);
        mJourneyReference.updateChildren(userJourneyUpdates);

        return journeyKey;
    }

    public void deleteJourneyInFIR(final String journeyID, String userID){
        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");

        Map<String, Object> journeyUpdates = new HashMap<String, Object>();
        journeyUpdates.put(journeyID, null);

        mDatabaseReference.updateChildren(journeyUpdates);

        DatabaseReference mUserReference = mRootRef.child("Users").child(userID);
        final DatabaseReference userJourneysRef = mUserReference.child("journeys");
        userJourneysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String journeyKey = dataSnapshot.getKey();
                if (journeyKey.contains(journeyID.substring(0,4))) {
                    Map<String, Object> userJourneyUpdates = new HashMap<String, Object>();
                    userJourneyUpdates.put(journeyKey, null);
                    userJourneysRef.updateChildren(userJourneyUpdates);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void deleteEventInFIR(int index, JourneyModel updatedJourney){
        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");
        DatabaseReference journeyReference = mDatabaseReference.child(updatedJourney.ID);
        // From here we can modify the data in FIR Database

        Map<String, Object> journeyUpdates = new HashMap<String, Object>();
        journeyUpdates.put("eventModels/" + index, null);
        journeyReference.updateChildren(journeyUpdates);

    }

    public void createEventInFIR(int originalEventNr, JourneyModel updatedJourney){
        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");
        DatabaseReference journeyReference = mDatabaseReference.child(updatedJourney.ID);
        //DatabaseReference eventReference = mDatabaseReference.child(updatedJourney.ID).child("eventModels").child(String.valueOf(updatedJourney.eventModels.size()));
        // From here we can modify the data in FIR Database

        String eventIndex = String.valueOf(updatedJourney.eventModels.size() + updatedJourney.deletedIndexes);
        Map<String, Object> journeyUpdates = new HashMap<String, Object>();
        journeyUpdates.put("eventModels/" + eventIndex + "/title", "Your events title");
        journeyUpdates.put("eventModels/" + eventIndex + "/summary", "Your events summary");
        journeyUpdates.put("eventModels/" + eventIndex + "/location/latitude", 43);
        journeyUpdates.put("eventModels/" + eventIndex + "/location/longitude", 16);

        journeyReference.updateChildren(journeyUpdates);

    }

    public void deleteCommentInFIR(CommentModel updatedComment){
        DatabaseReference mDatabaseReference = mRootRef.child("Comments");
        // From here we can modify the data in FIR Database

        Map<String, Object> commentUpdates = new HashMap<String, Object>();
        commentUpdates.put("/" + updatedComment.journeyID + "/" + updatedComment.ID, null);
        mDatabaseReference.updateChildren(commentUpdates);

    }

    public void editCommentInFIR(CommentModel updatedComment){
        DatabaseReference mDatabaseReference = mRootRef.child("Comments");
        DatabaseReference commentReferenceOuter = mDatabaseReference.child(updatedComment.journeyID);
        DatabaseReference commentReference = commentReferenceOuter.child(updatedComment.ID);
        // From here we can modify the data in FIR Database

        Map<String, Object> commentUpdates = new HashMap<String, Object>();
        commentUpdates.put("author", updatedComment.author);
        commentUpdates.put("avatarURL", updatedComment.avatarURL);
        commentUpdates.put("comment", updatedComment.comment);

        commentReference.updateChildren(commentUpdates);

    }

    public void createCommentInFIR(CommentModel commentModel, JourneyModel updatedJourney){
        DatabaseReference mDatabaseReference = mRootRef.child("Comments");
        DatabaseReference commentReferenceOuter = mDatabaseReference.child(updatedJourney.ID);
        String key = commentReferenceOuter.push().getKey();

        Map<String, Object> commentUpdates = new HashMap<String, Object>();
        commentUpdates.put(key + "/author", commentModel.author);
        commentUpdates.put(key + "/avatarURL", commentModel.avatarURL);
        commentUpdates.put(key + "/comment", commentModel.comment);

        commentReferenceOuter.updateChildren(commentUpdates);
    }

    public void createUserInFIR(FirebaseUser firUser, AccessToken facebookToken)
    {
        DatabaseReference mUserReference = mRootRef.child("Users");
        String firUserId = firUser.getUid();

        Map<String, Object> userUpdates = new HashMap<String, Object>();
        userUpdates.put(firUserId + "summary", "Descroption about user");
        userUpdates.put(firUserId + "/followedByCount", 0);
        userUpdates.put(firUserId + "/following/0", "0");
        userUpdates.put(firUserId + "/name", firUser.getDisplayName());
        userUpdates.put(firUserId + "/avatarUrl", "https://graph.facebook.com/" + facebookToken.getUserId() + "/picture?type=square");

    }

    public void loveJourneyInFIR(final JourneyModel journeyModel, final FirebaseUser FIRUser, final FloatingActionButton FABImage){
        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");
        final DatabaseReference journeyReference = mDatabaseReference.child(journeyModel.ID);
        final String FIRUID = FIRUser.getUid();
        final DatabaseReference userReference = journeyReference.child("loved").child(FIRUID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean likeValue = false;
                try {
                    likeValue = (boolean) dataSnapshot.getValue();
                } catch (Exception e) { // First time like this journey
                    Map<String, Object> journeyUpdates = new HashMap<String, Object>();
                    journeyUpdates.put("annotationModel/likes", journeyModel.annotationModel.markerLikes+1);
                    journeyUpdates.put("loved/" + FIRUID, true);
                    journeyUpdates.put("lovedCount", journeyModel.annotationModel.markerLikes+1);
                    journeyReference.updateChildren(journeyUpdates);

                    DatabaseReference mUserRef = mRootRef.child("Users").child(FIRUID);

                    Map<String, Object> userUpdates = new HashMap<String, Object>();
                    userUpdates.put("loved/" + journeyModel.ID, journeyModel.ID);
                    mUserRef.updateChildren(userUpdates);
                    FABImage.setImageResource(R.drawable.hearth_filled);
                    e.printStackTrace();
                }
                finally {
                    if (likeValue) {
                        // User is removing his like from this journey
                        Map<String, Object> journeyUpdates = new HashMap<String, Object>();
                        journeyUpdates.put("annotationModel/likes", journeyModel.annotationModel.markerLikes-1);
                        userReference.removeValue();
                        journeyUpdates.put("lovedCount", journeyModel.annotationModel.markerLikes-1);
                        journeyReference.updateChildren(journeyUpdates);

                        DatabaseReference mUserRef = mRootRef.child("Users").child(FIRUID);

                        Map<String, Object> userUpdates = new HashMap<String, Object>();
                        mUserRef.child("loved").child(journeyModel.ID).removeValue();
                        mUserRef.updateChildren(userUpdates);
                        FABImage.setImageResource(R.drawable.hearth_stroke);

                    } else { // User like this journey again, after clearing the like once // OBSOLETE
                        Map<String, Object> journeyUpdates = new HashMap<String, Object>();
                        journeyUpdates.put("annotationModel/likes", journeyModel.annotationModel.markerLikes + 1);
                        journeyUpdates.put("loved/" + FIRUID, true);
                        journeyUpdates.put("lovedCount", journeyModel.annotationModel.markerLikes+1);
                        journeyReference.updateChildren(journeyUpdates);

                        DatabaseReference mUserRef = mRootRef.child("Users").child(FIRUID);

                        Map<String, Object> userUpdates = new HashMap<String, Object>();
                        userUpdates.put("loved/" + journeyModel.ID, journeyModel.ID);
                        mUserRef.updateChildren(userUpdates);
                        FABImage.setImageResource(R.drawable.hearth_filled);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    //    userReference.removeEventListener(valueEventListener);
    }

 /*    public  void getEvents(final DataHandlerListener dataHandlerListener){

     }*/
    public void setUserLoved(final JourneyModel journeyModel, final FirebaseUser FIRUser, final FloatingActionButton FABImage) {
        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");
        final DatabaseReference journeyReference = mDatabaseReference.child(journeyModel.ID);
        final String FIRUID = FIRUser.getUid();
        DatabaseReference userReference = journeyReference.child("loved").child(FIRUID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean likeValue = false;
                try {
                    likeValue = (boolean) dataSnapshot.getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(likeValue) {
                        FABImage.setImageResource(R.drawable.hearth_filled);
                    }
                    else {
                        FABImage.setImageResource(R.drawable.hearth_stroke);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

                // public  void getEventWithIds........

    public void getUserFollowing(String userID, final FollowingListener followingListener){
        DatabaseReference mUserReference = mRootRef.child("Users");
        DatabaseReference followingReference = null;
        try {
            followingReference = mUserReference.child(userID).child("following");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            followingReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        ArrayList<String> rawUserIDs = null;
                        rawUserIDs = (ArrayList<String>) dataSnapshot.getValue();
                        for(String rawUserID : rawUserIDs) {
                            DataHandler.getInstance().getUserWithId(rawUserID, new DataHandlerListener() {
                                @Override
                                public void onJourneyData(Map<String, Object> rawJourneyData, String journeyID) {

                                }

                                @Override
                                public void onUserData(Map<String, Object> rawUserData, String userId) {
                                    UserModel userModel = new UserModel(rawUserData, userId);
                                    followingListener.onFollowing(userModel);
                                }

                                @Override
                                public void onCommentData(Map<String, Object> rawCommentData, String commentID, String journeyIdent) {

                                }
                            });
                        }
                    } catch (Exception e){
                        Map<String, Object> rawUserIDs = null;
                        if(dataSnapshot.getValue() != null) {
                            rawUserIDs = (Map<String, Object>) dataSnapshot.getValue();
                            for (Map.Entry<String, Object> rawUserID : rawUserIDs.entrySet()) {
                                DataHandler.getInstance().getUserWithId((String) rawUserID.getValue(), new DataHandlerListener() {
                                    @Override
                                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyID) {

                                    }

                                    @Override
                                    public void onUserData(Map<String, Object> rawUserData, String userId) {
                                        UserModel userModel = new UserModel(rawUserData, userId);
                                        followingListener.onFollowing(userModel);
                                    }

                                    @Override
                                    public void onCommentData(Map<String, Object> rawCommentData, String commentID, String journeyIdent) {

                                    }
                                });

                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    public void followUserInFIR(final UserModel followedUser, FirebaseUser loggedInUser, final FloatingActionButton fabImage, final Context context){
        final DatabaseReference mDatabaseReference = mRootRef.child("Users");
        final DatabaseReference followedUserRef = mDatabaseReference.child(followedUser.userFIRId);
        final String FIRUID = loggedInUser.getUid();
        final DatabaseReference userReference = followedUserRef.child("followedBy").child(FIRUID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean followedValue = false;
                try {
                    followedValue = (boolean) dataSnapshot.getValue();
                } catch (Exception e) { // Selected user is not yet followed by logged in user
                    Map<String, Object> userUpdates = new HashMap<String, Object>();
                    userUpdates.put("followedByCount", followedUser.followedByCount + 1);
                    userUpdates.put("followedBy/" + FIRUID, true);
                    followedUserRef.updateChildren(userUpdates);

                    DatabaseReference mUserRef = mDatabaseReference.child(FIRUID);

                    Map<String, Object> loggedUserUpdates = new HashMap<String, Object>();
                    loggedUserUpdates.put("following/" + followedUser.userFIRId, followedUser.userFIRId);
                    mUserRef.updateChildren(loggedUserUpdates);
                    fabImage.setImageResource(R.drawable.ic_person_black_36px);
                    Toast.makeText(context, "Followed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                finally{
                    if(followedValue){ // Logged in user already following the selected one and is removing follow now
                        Map<String, Object> userUpdates = new HashMap<String, Object>();
                        userUpdates.put("followedByCount", followedUser.followedByCount-1);
                        //userUpdates.put("followedBy/" + FIRUID, false);
                        followedUserRef.child("followedBy").child(FIRUID).removeValue(); // userReference-en is meg lehetne hívni?
                        followedUserRef.updateChildren(userUpdates);

                        DatabaseReference mUserRef = mDatabaseReference.child(FIRUID);

                        Map<String, Object> loggedUserUpdates = new HashMap<String, Object>();
                        //loggedUserUpdates.put("following/" + followedUser.userFIRId, null);
                        mUserRef.child("following").child(followedUser.userFIRId).removeValue();
                        mUserRef.updateChildren(loggedUserUpdates);
                        fabImage.setImageResource(R.drawable.ic_person_outline_black_36px);
                        Toast.makeText(context, "Unfollowed", Toast.LENGTH_SHORT).show();
                    }
                    else{ // Logged in user is following the selected user again
                        Map<String, Object> userUpdates = new HashMap<String, Object>();
                        userUpdates.put("followedByCount", followedUser.followedByCount + 1);
                        userUpdates.put("followedBy/" + FIRUID, true);
                        followedUserRef.updateChildren(userUpdates);

                        DatabaseReference mUserRef = mDatabaseReference.child(FIRUID);

                        Map<String, Object> loggedUserUpdates = new HashMap<String, Object>();
                        loggedUserUpdates.put("following/" + followedUser.userFIRId, followedUser.userFIRId);
                        mUserRef.updateChildren(loggedUserUpdates);
                        fabImage.setImageResource(R.drawable.ic_person_black_36px);
                        Toast.makeText(context, "Followed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void followUserInFIR(final UserModel followedUser, FirebaseUser loggedInUser){
        final DatabaseReference mDatabaseReference = mRootRef.child("Users");
        final DatabaseReference followedUserRef = mDatabaseReference.child(followedUser.userFIRId);
        final String FIRUID = loggedInUser.getUid();
        final DatabaseReference userReference = followedUserRef.child("followedBy").child(FIRUID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean followedValue = false;
                try {
                    followedValue = (boolean) dataSnapshot.getValue();
                } catch (Exception e) { // Selected user is not yet followed by logged in user
                    Map<String, Object> userUpdates = new HashMap<String, Object>();
                    userUpdates.put("followedByCount", followedUser.followedByCount + 1);
                    userUpdates.put("followedBy/" + FIRUID, true);
                    followedUserRef.updateChildren(userUpdates);

                    DatabaseReference mUserRef = mDatabaseReference.child(FIRUID);

                    Map<String, Object> loggedUserUpdates = new HashMap<String, Object>();
                    loggedUserUpdates.put("following/" + followedUser.userFIRId, followedUser.userFIRId);
                    mUserRef.updateChildren(loggedUserUpdates);
                    e.printStackTrace();
                }
                finally{
                    if(followedValue){ // Logged in user already following the selected one and is removing follow now
                        Map<String, Object> userUpdates = new HashMap<String, Object>();
                        userUpdates.put("followedByCount", followedUser.followedByCount-1);
                        //userUpdates.put("followedBy/" + FIRUID, false);
                        followedUserRef.child("followedBy").child(FIRUID).removeValue(); // userReference-en is meg lehetne hívni?
                        followedUserRef.updateChildren(userUpdates);

                        DatabaseReference mUserRef = mDatabaseReference.child(FIRUID);

                        Map<String, Object> loggedUserUpdates = new HashMap<String, Object>();
                        //loggedUserUpdates.put("following/" + followedUser.userFIRId, null);
                        mUserRef.child("following").child(followedUser.userFIRId).removeValue();
                        mUserRef.updateChildren(loggedUserUpdates);
                    }
                    else{ // Logged in user is following the selected user again
                        Map<String, Object> userUpdates = new HashMap<String, Object>();
                        userUpdates.put("followedByCount", followedUser.followedByCount + 1);
                        userUpdates.put("followedBy/" + FIRUID, true);
                        followedUserRef.updateChildren(userUpdates);

                        DatabaseReference mUserRef = mDatabaseReference.child(FIRUID);

                        Map<String, Object> loggedUserUpdates = new HashMap<String, Object>();
                        loggedUserUpdates.put("following/" + followedUser.userFIRId, followedUser.userFIRId);
                        mUserRef.updateChildren(loggedUserUpdates);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setUserFollowed(final UserModel followedUser, FirebaseUser loggedInUser,final FloatingActionButton fabImage){
        final DatabaseReference mDatabaseReference = mRootRef.child("Users");
        DatabaseReference followedUserRef = mDatabaseReference.child(followedUser.userFIRId);
        final String FIRUID = loggedInUser.getUid();
        final DatabaseReference userReference = followedUserRef.child("followedBy").child(FIRUID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean followedValue = false;
                try {
                    followedValue = (boolean) dataSnapshot.getValue();
                } catch (Exception e) { // Selected user is not yet followed by logged in user
                    e.printStackTrace();
                }
                finally{
                    if(followedValue){
                        fabImage.setImageResource(R.drawable.ic_person_black_36px);
                    }
                    else{
                        fabImage.setImageResource(R.drawable.ic_person_outline_black_36px);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
        public void setProfPic(String value){
        myProfPic = value;
    }

        public void getJourneyLovers(JourneyModel mJourney, final LovedListener lovedListener) {
            DatabaseReference mJourneyReference = mRootRef.child("Journeys").child(mJourney.ID);
            DatabaseReference mLovedReference = null;
            try {
                mLovedReference = mJourneyReference.child("loved");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mLovedReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> rawLoverIDs = null;
                        rawLoverIDs = (Map<String, Object>) dataSnapshot.getValue();
                        lovedListener.onLoved(rawLoverIDs);
                        /*for(Map.Entry<String, Object> rawLoverID : rawLoverIDs.entrySet())
                        {
                            if((boolean) rawLoverID.getValue()){
                                DataHandler.getInstance().getUserWithId((String) rawLoverID.getKey(), new DataHandlerListener() {
                                    @Override
                                    public void onJourneyData(Map<String, Object> rawJourneyData, String journeyID) {

                                    }

                                    @Override
                                    public void onUserData(Map<String, Object> rawUserData, String userID) {
                                        lovedListener.onLoved(rawUserData);
                                    }

                                    @Override
                                    public void onCommentData(Map<String, Object> rawCommentData, String commentID, String journeyIdent) {

                                    }
                                });
                            }
                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }


}

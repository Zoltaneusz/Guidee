package zollie.travelblogger.guidee.adapters;

import android.widget.ImageView;

import com.google.android.gms.fitness.data.Value;
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
                dataHandlerListener.onUserData(userInfo);
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
                dataHandlerListener.onUserData(userInfo);
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

    public void setEventInFIR(int originalCarouselNr, EventModel updatedEvent){

        DatabaseReference mDatabaseReference  =  mRootRef.child("Journeys");
        ArrayList<Map<String, Object>> rawEventModels = null;
        DatabaseReference eventReference = mDatabaseReference.child(updatedEvent.journeyID).child("eventModels").child(String.valueOf(updatedEvent.FIRNumber));
        // From here we can modify the data in FIR Database.

        Map<String, Object> eventUpdates = new HashMap<String, Object>();
        eventUpdates.put("title", updatedEvent.title);
        eventUpdates.put("summary", updatedEvent.summary);
        eventUpdates.put("location/latitude", updatedEvent.eventLatLng.latitude);
        eventUpdates.put("location/longitude", updatedEvent.eventLatLng.longitude);
        //int updatedCarouselNr = updatedEvent.getHighestCarouselID(updatedEvent);
        int updatedCarouselNr = updatedEvent.carouselModels.size() + updatedEvent.deletedIndexes;
        if(originalCarouselNr != updatedCarouselNr) {
            for (int i = originalCarouselNr; i < updatedCarouselNr + 1; i++) {
                if (updatedEvent.carouselModels.get(i).carouselType == CarouselModel.CarouselType.IMAGE) {
                    eventUpdates.put("carouselModels/" + String.valueOf(i) + "/imageURL", updatedEvent.carouselModels.get(i).imageUrl);
                } else {
                    if (updatedEvent.carouselModels.get(i).videoUrl.length() == 11)  // Only 11 characted long videos will be allowed (yet)
                        eventUpdates.put("carouselModels/" + String.valueOf(i) + "/videoYoutubeId", updatedEvent.carouselModels.get(i).videoUrl);
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
        journeyUpdates.put(eventIndex + "/title", "Your events title");
        journeyUpdates.put(eventIndex + "/summary", "Your events summary");
        journeyUpdates.put(eventIndex + "/location/latitude", 43);
        journeyUpdates.put(eventIndex + "/location/longitude", 16);

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

    public void createUserInFIR(FirebaseUser firUser)
    {
        DatabaseReference mUserReference = mRootRef.child("Users");
        String firUserId = firUser.getUid();

        Map<String, Object> userUpdates = new HashMap<String, Object>();
        userUpdates.put(firUserId + "summary", "Descroption about user");
        userUpdates.put(firUserId + "/followedByCount", 0);
        userUpdates.put(firUserId + "/following/0", "0");
        userUpdates.put(firUserId + "/name", firUser.getDisplayName());
        userUpdates.put(firUserId + "/avatarUrl", firUser.getPhotoUrl());
    }

    public void loveJourneyInFIR(final JourneyModel journeyModel, final FirebaseUser FIRUser, final ImageView imageView){
        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");
        final DatabaseReference journeyReference = mDatabaseReference.child(journeyModel.ID);
        final String FIRUID = FIRUser.getUid();
        DatabaseReference userReference = journeyReference.child("loved").child(FIRUID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean likeValue = (boolean) dataSnapshot.getValue();
                if(likeValue){
                    // User is removing his like from this journey
                    Map<String, Object> journeyUpdates = new HashMap<String, Object>();
                    journeyUpdates.put("annotationModel/likes", String.valueOf(journeyModel.annotationModel.markerLikes));
                    journeyUpdates.put("loved/" + FIRUID, false);
                    journeyReference.updateChildren(journeyUpdates);

                    DatabaseReference mUserRef = mRootRef.child("Users").child(FIRUID);

                    Map<String, Object> userUpdates = new HashMap<String, Object>();
                    userUpdates.put("loved/" + journeyModel.ID, null);
                    mUserRef.updateChildren(userUpdates);
                    imageView.setImageResource(R.drawable.ic_favorites);

                }
                else if(likeValue == false){ // User like this journey again, after clearing the like once
                    Map<String, Object> journeyUpdates = new HashMap<String, Object>();
                    journeyUpdates.put("annotationModel/likes", String.valueOf(journeyModel.annotationModel.markerLikes+1));
                    journeyUpdates.put("loved/" + FIRUID, true);
                    journeyReference.updateChildren(journeyUpdates);

                    DatabaseReference mUserRef = mRootRef.child("Users").child(FIRUID);

                    Map<String, Object> userUpdates = new HashMap<String, Object>();
                    userUpdates.put("loved/" + journeyModel.ID, journeyModel.ID);
                    mUserRef.updateChildren(userUpdates);
                    imageView.setImageResource(R.drawable.heart_filled);
                }
                else{ // First time like this journey
                    Map<String, Object> journeyUpdates = new HashMap<String, Object>();
                    journeyUpdates.put("annotationModel/likes", String.valueOf(journeyModel.annotationModel.markerLikes+1));
                    journeyUpdates.put("loved/" + FIRUID, true);
                    journeyReference.updateChildren(journeyUpdates);

                    DatabaseReference mUserRef = mRootRef.child("Users").child(FIRUID);

                    Map<String, Object> userUpdates = new HashMap<String, Object>();
                    userUpdates.put("loved/" + journeyModel.ID, journeyModel.ID);
                    mUserRef.updateChildren(userUpdates);
                    imageView.setImageResource(R.drawable.heart_filled);

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
    public void setUserLoved(final JourneyModel journeyModel, final FirebaseUser FIRUser, final ImageView imageView) {
        DatabaseReference mDatabaseReference = mRootRef.child("Journeys");
        final DatabaseReference journeyReference = mDatabaseReference.child(journeyModel.ID);
        final String FIRUID = FIRUser.getUid();
        DatabaseReference userReference = journeyReference.child("loved").child(FIRUID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean likeValue = (boolean) dataSnapshot.getValue();
                if(likeValue) {
                    imageView.setImageResource(R.drawable.heart_filled);
                }
                else {
                    imageView.setImageResource(R.drawable.ic_favorites);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

                // public  void getEventWithIds........

        public void setProfPic(String value){
        myProfPic = value;
    }

}

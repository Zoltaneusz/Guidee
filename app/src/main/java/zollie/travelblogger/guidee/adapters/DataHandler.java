package zollie.travelblogger.guidee.adapters;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

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
    public void getCommentsWithID(String journeyID, final DataHandlerListener dataHandlerListener) {
        DatabaseReference mCommentsReference = mRootRef.child("Comments");
        mCommentsReference.child(journeyID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> commentInfo = (Map<String, Object>) dataSnapshot.getValue();
                dataHandlerListener.onCommentData(commentInfo);
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
    public void setEventInFIR(EventModel mEvent){

        DatabaseReference mDatabaseReference  =  mRootRef.child("Journeys");
        ArrayList<Map<String, Object>> rawEventModels = null;
        DatabaseReference eventReference = mDatabaseReference.child(mEvent.journeyID).child("eventModels").child(String.valueOf(mEvent.FIRNumber));
        // From here we can modify the data in FIR Database.
    }



 /*    public  void getEvents(final DataHandlerListener dataHandlerListener){

     }*/

                // public  void getEventWithIds........

        public void setProfPic(String value){
        myProfPic = value;
    }

}
